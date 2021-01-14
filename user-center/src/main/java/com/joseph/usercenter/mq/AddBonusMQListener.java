package com.joseph.usercenter.mq;

import com.joseph.usercenter.dao.user.BonusEventLogMapper;
import com.joseph.usercenter.dao.user.UserMapper;
import com.joseph.usercenter.domain.dto.mq.UserAddBonusMessageDTO;
import com.joseph.usercenter.domain.entity.user.BonusEventLog;
import com.joseph.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 为用户增加积分的MQ监听器，当收到从消息生产者（内容中心）发来的消息后，执行相应的业务
 * @author Joseph.Liu
 */
@Service
@RocketMQMessageListener(consumerGroup = "consumer-group", topic = "add-bonus")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AddBonusMQListener implements RocketMQListener<UserAddBonusMessageDTO> {

    private final UserMapper userMapper;
    private final BonusEventLogMapper bonusEventLogMapper;

    @Override
    public void onMessage(UserAddBonusMessageDTO userAddBonusMessageDTO) {
        //1.为用户加积分
        Integer userId = userAddBonusMessageDTO.getUserId();
        User user = this.userMapper.selectByPrimaryKey(userId);
        Integer bonus = userAddBonusMessageDTO.getBonus();
        user.setBonus(user.getBonus()+ bonus);
        this.userMapper.updateByPrimaryKeySelective(user);
        //2.记录日志到bonus_event_log表中
        this.bonusEventLogMapper.insert(
                BonusEventLog.builder()
                        .userId(userId)
                        .value(bonus)
                        .event("投稿")
                        .description("投稿审核通过后加积分")
                        .createTime(new Date())
                        .build()
        );
        log.info("积分添加完毕...");
    }
}
