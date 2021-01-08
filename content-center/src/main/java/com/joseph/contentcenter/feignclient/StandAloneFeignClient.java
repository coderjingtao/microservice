package com.joseph.contentcenter.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 独立的不依赖nacos的FeignClient
 * name属性可以随便写
 * @author Joseph.Liu
 */
@FeignClient(name = "google",url = "https://www.google.com/")
public interface StandAloneFeignClient {
    @GetMapping("")
    public String index();
}
