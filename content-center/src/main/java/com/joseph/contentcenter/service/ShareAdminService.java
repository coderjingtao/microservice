package com.joseph.contentcenter.service;

import com.joseph.contentcenter.dao.content.ShareMapper;
import com.joseph.contentcenter.domain.dto.content.ShareAuditDTO;
import com.joseph.contentcenter.domain.dto.mq.UserAddBonusMessageDTO;
import com.joseph.contentcenter.domain.entity.content.Share;
import com.joseph.contentcenter.domain.enums.AuditStatusEnum;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.ForkJoinPool;

/**
 * @author Joseph.Liu
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareAdminService {

    private final ShareMapper shareMapper;

    private final RocketMQTemplate rocketMQTemplate;

    public Share auditById(Integer shareId, ShareAuditDTO shareAuditDTO) {
        //1.查询分享是否存在 且状态为待审核，不是则抛出异常
        Share share = this.shareMapper.selectByPrimaryKey(shareId);
        if(share == null){
            throw new IllegalArgumentException("参数非法,该分享不存在");
        }
        if(!Objects.equals(share.getAuditStatus(), AuditStatusEnum.NOT_YET.name())){
            throw new IllegalArgumentException("参数非法,该分享已审核通过或被拒绝");
        }
        //2.审核资源，将状态设为 PASS / REJECT
        share.setAuditStatus(shareAuditDTO.getAuditStatusEnum().toString());
        this.shareMapper.updateByPrimaryKey(share);
        //3.为提示用户体验，进行异步执行：如果是PASS,发送消息给RocketMQ,让用户中心（微服务）去消费，并为该分享的发布人添加积分
        this.rocketMQTemplate.convertAndSend("add-bonus",
                UserAddBonusMessageDTO.builder()
                .userId(share.getUserId())
                .bonus(50)
                .build()
        );
        return share;
    }
}
