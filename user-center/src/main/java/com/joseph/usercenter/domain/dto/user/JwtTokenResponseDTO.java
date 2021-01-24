package com.joseph.usercenter.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录时，JWT信息响应DTO
 * @author Joseph.Liu
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class JwtTokenResponseDTO {
    /**
     * Token
     */
    private String token;
    /**
     * 过期时间
     */
    private Long expirationTime;
}
