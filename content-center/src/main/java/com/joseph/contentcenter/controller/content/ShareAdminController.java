package com.joseph.contentcenter.controller.content;

import com.joseph.contentcenter.domain.dto.content.ShareAuditDTO;
import com.joseph.contentcenter.domain.entity.content.Share;
import com.joseph.contentcenter.service.ShareAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 管理员对分享内容进行审核的controller
 * @author Joseph.Liu
 */
@RestController
@RequestMapping("/admin/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareAdminController {

    private final ShareAdminService shareAdminService;

    @PutMapping("/audit/{shareId}")
    public Share auditById(@PathVariable Integer shareId, @RequestBody ShareAuditDTO shareAuditDTO){
        // TODO 认证与授权
        return shareAdminService.auditById(shareId,shareAuditDTO);
    }
}
