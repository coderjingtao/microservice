package com.joseph.contentcenter.domain.dto.content;

import com.joseph.contentcenter.domain.enums.AuditStatusEnum;
import lombok.Data;

/**
 * @author Joseph.Liu
 */
@Data
public class ShareAuditDTO {
    /**
     * 分享的审核状态
     */
    private AuditStatusEnum auditStatusEnum;
    /**
     * 审核通过/不通过原因
     */
    private String reason;
}
