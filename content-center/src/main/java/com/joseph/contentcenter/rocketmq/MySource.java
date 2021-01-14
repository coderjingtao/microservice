package com.joseph.contentcenter.rocketmq;


import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * Spring Cloud Stream 自定义接口发送消息
 * @author Joseph.Liu
 */
public interface MySource {

    String MY_OUTPUT = "my-output";

    @Output(MY_OUTPUT)
    MessageChannel output();
}
