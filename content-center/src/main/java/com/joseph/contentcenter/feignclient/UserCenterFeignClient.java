package com.joseph.contentcenter.feignclient;

import com.joseph.contentcenter.domain.dto.user.UserAddBonusDTO;
import com.joseph.contentcenter.domain.dto.user.UserDTO;
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
     * 根据用户id,去用户中心查询用户的信息
     * feign会自动构造出url: http://user-center/users/{userId}
     * @param userId 用户id
     * // @RequestHeader("X-Token") String token : spring mvc方式保存token到变量X-Token中，并放入请求的头部，这样就可以发送给其他微服务了
     * @return 用户的基本信息
     */
    @GetMapping("/users/{userId}")
    UserDTO findByUserId(@PathVariable Integer userId);

    /**
     * Get多参数查询请求
     * @param userDTO
     * @return
     */
    @GetMapping("/users/q")
    UserDTO query(@SpringQueryMap UserDTO userDTO);

    /**
     * Post多参数查询请求
     * @param userDTO
     * @return
     */
    @RequestMapping(value = "/users/post", method = RequestMethod.POST)
    UserDTO postQuery(@RequestBody UserDTO userDTO);

    /**
     * 给用户添加/扣减积分
     * @param userAddBonusDTO
     * @return
     */
    @PutMapping("/users/add-bonus")
    UserDTO addBonus(@RequestBody UserAddBonusDTO userAddBonusDTO);
}
