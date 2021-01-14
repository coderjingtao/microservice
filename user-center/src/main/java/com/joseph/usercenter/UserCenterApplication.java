package com.joseph.usercenter;

import com.joseph.usercenter.mq.MySink;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 用户中心启动类
 * MapperScan: 使用mybatis
 * EnableBinding(Sink.class): 使用 spring-cloud-stream 来消费消息
 * @author Joseph.Liu
 */
@MapperScan("com.joseph.usercenter.dao")
@SpringBootApplication
@EnableBinding({Sink.class, MySink.class})
public class UserCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserCenterApplication.class, args);
    }

}
