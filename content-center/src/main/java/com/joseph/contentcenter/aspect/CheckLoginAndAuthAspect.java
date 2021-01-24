package com.joseph.contentcenter.aspect;


import com.joseph.contentcenter.exception.UserLoginSecurityException;
import com.joseph.contentcenter.utils.JwtOperator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 检查用户是否登录的切面
 * @author Joseph.Liu
 */
@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class CheckLoginAndAuthAspect {

    private final JwtOperator jwtOperator;
    /**
     * 只要加了@CheckLogin 注解的方法，都会进入该检查用户是否登录的方法
     * @param joinPoint
     * @return
     */
    @Around("@annotation(com.joseph.contentcenter.aspect.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable {
        checkToken();
        return joinPoint.proceed();
    }

    private void checkToken() {
        try {
            //1.从request的header中获取token
            HttpServletRequest request = getHttpServletRequest();
            String token = request.getHeader("X-Token");
            //2.校验token是否合法&是否过期，如果不合法则直接抛异常
            Boolean isValid = jwtOperator.validateToken(token);
            if (BooleanUtils.isFalse(isValid)) {
                throw new UserLoginSecurityException("Token不合法");
            }
            //3.如果校验合法，则把用户的信息放到request的attributes里面，并放行
            Claims claims = jwtOperator.getClaimsFromToken(token);
            request.setAttribute("id", claims.get("id"));
            request.setAttribute("wxNickname", claims.get("wxNickname"));
            request.setAttribute("role", claims.get("role"));
        }catch(Throwable throwable){
            log.error("CheckLoginAspect报错",throwable);
            throw new UserLoginSecurityException("Token不合法");
        }
    }

    /**
     * 通过Spring MVC的静态方法获取HttpServletRequest对象
     * @return HttpServletRequest对象
     */
    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        return Objects.requireNonNull(servletRequestAttributes).getRequest();
    }

    @Around("@annotation(com.joseph.contentcenter.aspect.CheckAuthorization)")
    public Object checkAuth(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            //1.验证用户token是否合法
            checkToken();
            //2.验证用户角色是否匹配
            HttpServletRequest request = getHttpServletRequest();
            String role = (String) request.getAttribute("role");
            //用这个role值与注解CheckAuthorization中传入的值做对比
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            CheckAuthorization annotation = method.getAnnotation(CheckAuthorization.class);
            String value = annotation.value();
            if (!Objects.equals(role, value)) {
                throw new UserLoginSecurityException("用户不是Admin,无权访问！");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new UserLoginSecurityException("用户不是Admin,无权访问！");
        }
        return joinPoint.proceed();
    }
}
