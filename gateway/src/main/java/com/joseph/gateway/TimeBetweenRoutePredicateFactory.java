package com.joseph.gateway;

import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义路由谓词工厂: 类名的后缀suffix必须以RoutePredicateFactory结尾
 * 来实现只有在某个时间区间内，才会转发请求到微服务
 * @author Joseph Liu
 */
@Component
public class TimeBetweenRoutePredicateFactory extends AbstractRoutePredicateFactory<TimeBetweenConfig> {

    public TimeBetweenRoutePredicateFactory() {
        super(TimeBetweenConfig.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(TimeBetweenConfig config) {
        LocalTime startTime = config.getStartTime();
        LocalTime endTime = config.getEndTime();
        return exchange -> {
            LocalTime now = LocalTime.now();
            return now.isAfter(startTime) && now.isBefore(endTime);
        };
    }

    /**
     * 映射配置文件与配置类TimeBetweenConfig.java的关系
     * @return 时间的list
     */
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("startTime","endTime");
    }

}
