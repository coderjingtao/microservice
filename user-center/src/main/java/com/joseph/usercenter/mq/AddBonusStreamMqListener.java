package com.joseph.usercenter.mq;

import com.joseph.usercenter.dao.user.BonusEventLogMapper;
import com.joseph.usercenter.dao.user.UserMapper;
import com.joseph.usercenter.domain.dto.mq.UserAddBonusMessageDTO;
import com.joseph.usercenter.domain.entity.user.BonusEventLog;
import com.joseph.usercenter.domain.entity.user.User;
import com.joseph.usercenter.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 整合spring cloud stream后，接收由stream发来的消息的监听器
 * @author Joseph.Liu
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class AddBonusStreamMqListener {

    private final UserService userService;


    @StreamListener(Sink.INPUT)
    public void receiveMessage(UserAddBonusMessageDTO userAddBonusMessageDTO){
        this.userService.addBonus(userAddBonusMessageDTO);
        log.info("积分添加完毕...");
    }
}
