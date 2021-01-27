package com.joseph.usercenter.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.joseph.usercenter.aspect.CheckLogin;
import com.joseph.usercenter.domain.dto.user.JwtTokenResponseDTO;
import com.joseph.usercenter.domain.dto.user.LoginResponseDTO;
import com.joseph.usercenter.domain.dto.user.UserLoginRequestDTO;
import com.joseph.usercenter.domain.dto.user.UserLoginResponseDTO;
import com.joseph.usercenter.domain.entity.user.User;
import com.joseph.usercenter.service.UserService;
import com.joseph.usercenter.utils.JwtOperator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 对用户的相关操作
 * @author Joseph.Liu
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserController {

    private final UserService userService;
    private final WxMaService wxMaService;
    private final JwtOperator jwtOperator;

    @GetMapping("/{id}")
    @CheckLogin
    public User findById(@PathVariable Integer id){
        log.info("我被其他微服务请求了 ... ");
        return this.userService.findById(id);
    }

    /**
     * 根据用户登录微信小程序的code,去请求微信API,验证是否已登录微信小程序
     * @param userLoginRequestDTO 用户登录后的信息DTO
     * @return 用户登录的响应信息：
     * 如果用户已注册：颁发token,并返回用户信息
     * 如果用户未注册：注册并颁发token,并返回用户信息
     */
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody UserLoginRequestDTO userLoginRequestDTO) throws WxErrorException {
        //通过微信小程序API,校验微信小程序是否已经在微信平台登录
        WxMaJscode2SessionResult loginResult = this.wxMaService.getUserService().getSessionInfo(userLoginRequestDTO.getCode());
        //微信的openId : 用户在微信的唯一标识
        String openId = loginResult.getOpenid();
        //查看用户是否注册，如果没有注册就插入数据表
        //如果已经注册，就直接颁发token
        User user = this.userService.login(userLoginRequestDTO, openId);
        Map<String,Object> userInfo = new HashMap<>(3);
        userInfo.put("id",user.getId());
        userInfo.put("wxNickname",user.getWxNickname());
        userInfo.put("role",user.getRoles());
        String token = jwtOperator.generateToken(userInfo);
        log.info("用户{}登录成功，生成的token = {}, 有效期到：{}",
                userLoginRequestDTO.getWxNickname(),
                token,
                jwtOperator.getExpirationTime()
        );
        //构建响应
        return LoginResponseDTO.builder()
                .user(
                        UserLoginResponseDTO.builder()
                                .id(user.getId())
                                .wxNickname(user.getWxNickname())
                                .avatarUrl(user.getAvatarUrl())
                                .bonus(user.getBonus())
                                .build()
                )
                .token(
                        JwtTokenResponseDTO.builder()
                                .token(token)
                                .expirationTime(jwtOperator.getExpirationTime().getTime())
                                .build()
                )
                .build();
    }

    /**
     * 模拟生成一个token,必须和数据库user表中的数据一致
     * @return fake token
     */
    @GetMapping("/gen-token")
    public String generateToken(){
        Map<String,Object> userInfo = new HashMap<>(3);
        userInfo.put("id",2);
        userInfo.put("wxNickname","我是小涛涛");
        userInfo.put("role","admin");
        return jwtOperator.generateToken(userInfo);
    }

    /**
     * url: q?id=123&wxId=456&....
     * @param user
     * @return
     */
    @GetMapping("/q")
    public User query(User user){
        return user;
    }
}
