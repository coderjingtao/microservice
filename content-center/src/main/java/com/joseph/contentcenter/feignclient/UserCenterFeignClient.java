package com.joseph.contentcenter.feignclient;

import com.joseph.contentcenter.domain.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

/**
 * name属性表示调用已经注册到nacos中的微服务的名称
 * @author Joseph.Liu
 */
@FeignClient(name = "user-center")
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
