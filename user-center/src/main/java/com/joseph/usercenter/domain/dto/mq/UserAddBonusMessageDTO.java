package com.joseph.usercenter.domain.dto.mq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 使用MQ进行用户积分操作的DTO
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
    /**
     * 加积分的事件
     */
    private String event;
    /**
     * 因什么加积分
     */
    private String description;
}
