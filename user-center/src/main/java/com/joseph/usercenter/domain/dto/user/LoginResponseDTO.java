package com.joseph.usercenter.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应所有信息的DTO
 * @author Joseph.Liu
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class LoginResponseDTO {
    /**
     * Token信息
     */
    private JwtTokenResponseDTO token;
    /**
     * 用户信息
     */
    private UserLoginResponseDTO user;
}
