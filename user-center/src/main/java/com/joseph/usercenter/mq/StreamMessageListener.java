package com.joseph.usercenter.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Service;

/**
 * 整合spring cloud stream后，接收由stream发来的消息的监听器
 * @author Joseph.Liu
 */
@Service
@Slf4j
public class StreamMessageListener {

    @StreamListener(Sink.INPUT)
    public void receiveMessage(String messageBody){
        log.info("通过stream收到了消息：messageBody = {}",messageBody);
    }
}
