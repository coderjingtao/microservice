package com.joseph.usercenter.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.stereotype.Service;

/**
 * 整合spring cloud stream后，接收由stream发来的消息的自定义监听器
 * @author Joseph.Liu
 */
@Service
@Slf4j
public class MyStreamMessageListener {

    @StreamListener(MySink.MY_INPUT)
    public void receiveMessage(String messageBody){
        log.info("自定义接口消费：通过stream收到了消息：messageBody = {}",messageBody);
        //假装发生异常
//        throw new IllegalArgumentException("假装发生异常");
    }

    /**
     * stream 的全局异常处理
     * @param message 发生异常的消息
     */
    @StreamListener("errorChannel")
    public void error(Message<?> message){
        ErrorMessage errorMessage = (ErrorMessage) message;
        log.error("stream发生异常, errorMessage = {}",errorMessage);
        //错误处理的逻辑
    }
}
