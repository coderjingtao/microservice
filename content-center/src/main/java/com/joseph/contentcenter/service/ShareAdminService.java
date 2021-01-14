package com.joseph.contentcenter.service;

import com.joseph.contentcenter.dao.content.RocketmqTransactionLogMapper;
import com.joseph.contentcenter.dao.content.ShareMapper;
import com.joseph.contentcenter.domain.dto.content.ShareAuditDTO;
import com.joseph.contentcenter.domain.dto.mq.UserAddBonusMessageDTO;
import com.joseph.contentcenter.domain.entity.content.RocketmqTransactionLog;
import com.joseph.contentcenter.domain.entity.content.Share;
import com.joseph.contentcenter.domain.enums.AuditStatusEnum;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;

/**
 * 管理员审核用户提交的分享文章
 * @author Joseph.Liu
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareAdminService {

    private final ShareMapper shareMapper;

    private final RocketMQTemplate rocketMQTemplate;

    private final RocketmqTransactionLogMapper rocketmqTransactionLogMapper;

    /**
     * 审核分享的文章，审核通过后使用RocketMQ的RocketMQTemplate进行分布式的事务消息来更新用户中心的积分
     * @param shareId 分享的文章id
     * @param shareAuditDTO 审核的状态：PASS or REJECT
     * @return 返回分享的文章
     */
    public Share auditById(Integer shareId, ShareAuditDTO shareAuditDTO) {
        //1.查询分享是否存在 且状态为待审核，不是则抛出异常
        Share share = this.shareMapper.selectByPrimaryKey(shareId);
        if(share == null){
            throw new IllegalArgumentException("参数非法,该分享不存在");
        }
        if(!Objects.equals(share.getAuditStatus(), AuditStatusEnum.NOT_YET.name())){
            throw new IllegalArgumentException("参数非法,该分享已审核通过或被拒绝");
        }
        //2.如果是PASS,发送消息给RocketMQ,让用户中心（微服务）去消费，并为该分享的发布人添加积分
        if(shareAuditDTO.getAuditStatusEnum().equals(AuditStatusEnum.PASS)){
            //2.1 发送半消息给RocketMQ
            String transactionId = UUID.randomUUID().toString();
            this.rocketMQTemplate.sendMessageInTransaction(
                    //随便起个名字即可,但需要和AddBonusTransactionListener中的保持一致
                    "tx-add-bonus-group",
                    //message topic
                    "add-bonus",
                    // message body
                    MessageBuilder
                            .withPayload(
                                    UserAddBonusMessageDTO.builder()
                                            .userId(share.getUserId())
                                            .bonus(50)
                                            .build()
                            )
                            //header 也有妙用
                            .setHeader(RocketMQHeaders.TRANSACTION_ID, transactionId)
                            .setHeader("share_id", shareId)
                            .build()
                    ,
                    //arg有大用处
                    shareAuditDTO
            );
        }
        //3.如果是 REJECT, 不需要发消息给用户中心来更新用户的积分，只需要把数据库中的审批状态设为Reject
        else{
            auditByShareIdInDb(shareId, shareAuditDTO);
        }


        //        直接发送完整消息给MQ
//        this.rocketMQTemplate.convertAndSend("add-bonus",
//                UserAddBonusMessageDTO.builder()
//                .userId(share.getUserId())
//                .bonus(50)
//                .build()
//        );

        return share;
    }

    /**
     * 审核分享文章，根据审批结果的不同，在数据库中将状态设为 PASS / REJECT
     * @param shareId 分享文章的id
     * @param shareAuditDTO 审批结果DTO
     */
    @Transactional(rollbackFor = Exception.class)
    public void auditByShareIdInDb(Integer shareId, ShareAuditDTO shareAuditDTO) {
        Share share = Share.builder()
                .id(shareId)
                .auditStatus(shareAuditDTO.getAuditStatusEnum().toString())
                .reason(shareAuditDTO.getReason())
                .build();
        this.shareMapper.updateByPrimaryKeySelective(share);
        // todo 把Share写到缓存
    }

    /**
     * 审核分享文章并将本次交易Id计入日志
     * @param shareId 分享文章Id
     * @param shareAuditDTO 审批结果DTO
     * @param transactionId 本次交易Id
     */
    @Transactional(rollbackFor = Exception.class)
    public void auditByShareIdWithMqTransactionLog(Integer shareId, ShareAuditDTO shareAuditDTO, String transactionId){
        this.auditByShareIdInDb(shareId,shareAuditDTO);
        this.rocketmqTransactionLogMapper.insertSelective(
                RocketmqTransactionLog.builder()
                        .transactionId(transactionId)
                        .log("审核分享文章")
                        .build()
        );
    }
}
