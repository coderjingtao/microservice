package com.joseph.gateway;

import lombok.Data;

import java.time.LocalTime;

/**
 * 自定义路由谓词的配置类
 * @author Joseph.Liu
 */
@Data
public class TimeBetweenConfig {
    private LocalTime startTime;
    private LocalTime endTime;
}
