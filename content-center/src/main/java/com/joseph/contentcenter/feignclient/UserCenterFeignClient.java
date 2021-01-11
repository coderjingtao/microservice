package com.joseph.contentcenter.feignclient;

import com.joseph.contentcenter.domain.dto.user.UserDTO;
import com.joseph.contentcenter.feignclient.sentinel.UserCenterFeignClientFallback;
import com.joseph.contentcenter.feignclient.sentinel.UserCenterFeignClientFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

/**
 * name属性表示调用已经注册到nacos中的微服务的名称
 * fallback是指服务降级后，系统自动调用的类，但拿不到异常
 * fallbackFactory可以拿到异常，但2者不可同时使用
 * @author Joseph.Liu
 */
@FeignClient(name = "user-center"
//        ,fallback = UserCenterFeignClientFallback.class
        ,fallbackFactory = UserCenterFeignClientFallbackFactory.class
)
public interface UserCenterFeignClient {
    /**
     * feign会自动构造出url: http://user-center/users/{userId}
     * @param userId
     * @return
     */
    @GetMapping("/users/{userId}")
    UserDTO findByUserId(@PathVariable Integer userId);

    /**
     * Get多参数请求
     * @param userDTO
     * @return
     */
    @GetMapping("/users/q")
    UserDTO query(@SpringQueryMap UserDTO userDTO);

    /**
     * Post多参数请求
     * @param userDTO
     * @return
     */
    @RequestMapping(value = "/users/post", method = RequestMethod.POST)
    UserDTO postQuery(@RequestBody UserDTO userDTO);
}
