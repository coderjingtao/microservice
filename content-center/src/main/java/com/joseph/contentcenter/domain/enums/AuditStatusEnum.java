package com.joseph.contentcenter.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 分享的审核状态 Enum
 * @author Joseph.Liu
 */

@Getter
@AllArgsConstructor
public enum AuditStatusEnum {
    //待审核
    NOT_YET,
    //审核通过
    PASS,
    //审核不通过
    REJECT,
    ;
}
