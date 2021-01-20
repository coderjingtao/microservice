package com.joseph.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * 自定义过滤器工厂：必须以GatewayFilterFactory结尾
 * @author Joseph.Liu
 */
@Slf4j
@Component
public class PreLogGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {

    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return (exchange,chain) -> {
            log.info("已进入PreLog Gateway Filter, 传入的参数为：{},{}",config.getName(),config.getValue());
            ServerHttpRequest modifiedRequest = exchange.getRequest()
                    .mutate()//在这里可以修改请求,此时未修改
                    .build();
            ServerWebExchange modifiedExchange = exchange.mutate()
                    .request(modifiedRequest)//把修改后的请求传进去
                    .build();
            return chain.filter(modifiedExchange);
        };
    }
}
