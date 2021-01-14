package com.joseph.contentcenter;

import com.joseph.contentcenter.rocketmq.MySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 内容中心启动类
 * MapperScan: 使用mybatis,并扫描所有的mapper接口
 * EnableFeignClients: 开启FeignClient
 * EnableBinding(Source.class): 使用 spring-cloud-stream 来生产消息
 * @author Joseph.Liu
 */
@MapperScan("com.joseph.contentcenter.dao")
@SpringBootApplication
@EnableFeignClients
@EnableBinding({Source.class, MySource.class})
public class ContentCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class, args);
    }

    //在spring容器中，创建一个对象,类型为RestTemplate,名称/ID为方法名:restTemplate
    //对应传统的spring xml配置：<bean id="restTemplate" class="xxx.RestTemplate"/>

    /**
     * /@Bean
     * 在spring容器中，创建一个对象,类型为RestTemplate,名称/ID为方法名:restTemplate
     * 对应传统的spring xml配置：<bean id="restTemplate" class="xxx.RestTemplate"/>
     * /@LoadBalanced
     * 整合ribbon负载均衡,默认是轮询算法，依次请求每个微服务
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
