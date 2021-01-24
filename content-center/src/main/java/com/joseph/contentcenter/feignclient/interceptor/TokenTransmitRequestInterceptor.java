package com.joseph.contentcenter.feignclient.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 用OpenFeign的拦截器的方式来传递请求头部的Token
 * @author Joseph.Liu
 */
public class TokenTransmitRequestInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        //1.通过Spring MVC的静态方法，从request的header中获取token
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        HttpServletRequest request = servletRequestAttributes.getRequest();
        String token = request.getHeader("X-Token");
        //2.将Token传递
        if(StringUtils.isNotBlank(token)){
            requestTemplate.header("X-Token",token);
        }

    }
}
