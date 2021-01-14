package com.joseph.contentcenter.rocketmq;

import com.alibaba.fastjson.JSON;
import com.joseph.contentcenter.dao.content.RocketmqTransactionLogMapper;
import com.joseph.contentcenter.domain.dto.content.ShareAuditDTO;
import com.joseph.contentcenter.domain.entity.content.RocketmqTransactionLog;
import com.joseph.contentcenter.service.ShareAdminService;
import lombok.RequiredArgsConstructor;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

/**
 * 监听MQ,得到半消息是否发送成功
 * txProducerGroup中的名称需要与Application.yml文件中的stream.rocketmq.bindings.output.producer.group的名称一致
 * @author Joseph.Liu
 */
@RocketMQTransactionListener(txProducerGroup = "tx-add-bonus-group")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AddBonusTransactionListener implements RocketMQLocalTransactionListener {

    private final ShareAdminService shareAdminService;
    private final RocketmqTransactionLogMapper rocketmqTransactionLogMapper;
    /**
     * 执行本地事务，返回执行本地事务的状态：成功或失败，对应流程图中的第3步和第4步
     * @param message 发送的消息
     * @param o  该参数用来传递其他有用的参数，一般来说都会组装成一个DTO对象，本方法中是null,因为已经把dto放到消息体的header中
     * @return 返回本地事务的执行结果 Commit or Rollback
     */
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);
        Integer shareId = Integer.valueOf((String) headers.get("share_id"));
        String dtoJsonString = (String) headers.get("shareAuditDTO");
        ShareAuditDTO shareAuditDTO = JSON.parseObject(dtoJsonString, ShareAuditDTO.class);
        try {
            //执行本地事务
            this.shareAdminService.auditByShareIdWithMqTransactionLog(shareId,shareAuditDTO,transactionId);
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    /**
     * 检查本地事务是否执行成功，对应流程图中的第6步和第7步
     * 在RocketMQLocalTransactionState.COMMIT之前，如果断网或停机等因素，MQ并没有收到本地事务的执行结果
     * 所以MQ会定时回查状态未知的半消息的本地事务的执行结果
     * 但我们根据什么来检查本地事务的执行结果呢？
     * 那就是在本地事务执行完毕后，在表[rocketmq_transaction_log]中插入一条日志来记录当前的transactionId
     * 如果该表中有transactionId交易记录，则代表本地事务执行成功；如果该表中没有transactionId交易记录，则代表本地事务执行失败
     * @param message
     * @return 返回本地事务的执行结果 Commit or Rollback
     */
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        MessageHeaders headers = message.getHeaders();
        String transactionId = (String) headers.get(RocketMQHeaders.TRANSACTION_ID);

        //相当于查询语句 select * from rocketmq_transaction_log where transactionId = XXX
        RocketmqTransactionLog rocketmqTransactionLog = this.rocketmqTransactionLogMapper.selectOne(
                RocketmqTransactionLog.builder()
                        .transactionId(transactionId)
                        .build()
        );
        if(rocketmqTransactionLog != null){
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.ROLLBACK;
    }
}
