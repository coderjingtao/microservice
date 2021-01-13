package com.joseph.usercenter.domain.dto.mq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Joseph.Liu
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAddBonusMessageDTO {
    /**
     * 为谁加积分
     */
    private Integer userId;
    /**
     * 加多少积分
     */
    private Integer bonus;
}
