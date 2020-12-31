package com.joseph.contentcenter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.joseph")
@SpringBootApplication
public class ContentCenterApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentCenterApplication.class, args);
    }

    //在spring容器中，创建一个对象,类型为RestTemplate,名称/ID为方法名:restTemplate
    //对应传统的spring xml配置：<bean id="restTemplate" class="xxx.RestTemplate"/>
    @Bean
    @LoadBalanced //整合ribbon负载均衡
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
