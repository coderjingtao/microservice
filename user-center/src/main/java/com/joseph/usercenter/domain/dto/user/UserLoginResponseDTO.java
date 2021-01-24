package com.joseph.usercenter.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录时，用户信息响应DTO
 * @author Joseph.Liu
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserLoginResponseDTO {
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 用户积分
     */
    private Integer bonus;
    /**
     * 微信昵称
     */
    private String wxNickname;
    /**
     * 头像地址
     */
    private String avatarUrl;
}
