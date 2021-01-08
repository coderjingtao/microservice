package com.joseph.usercenter.controller;

import com.joseph.usercenter.domain.entity.user.User;
import com.joseph.usercenter.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public User findById(@PathVariable Integer id){
        log.info("我被其他微服务请求了 ... ");
        return this.userService.findById(id);
    }

    /**
     * url: q?id=123&wxId=456&....
     * @param user
     * @return
     */
    @GetMapping("/q")
    public User query(User user){
        return user;
    }

    @GetMapping("/create")
    public User createUser(){
        User user = new User();
        user.setRoles("admin");
        user.setAvatarUrl("xxx");
        user.setBonus(200);
        user.setWxId("wx2");
        user.setWxNickname("我是小涛涛");
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userService.saveUser(user);
        return user;
    }
}
