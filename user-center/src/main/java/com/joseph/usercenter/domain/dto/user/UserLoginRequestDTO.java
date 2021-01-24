package com.joseph.usercenter.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录的请求信息
 * @author Joseph.Liu
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserLoginRequestDTO {
    /**
     * 微信登录后返回的code
     */
    private String code;
    /**
     * 微信登录后返回的微信昵称
     */
    private String wxNickname;
    /**
     * 微信登录后返回的头像地址
     */
    private String avatarUrl;
}
