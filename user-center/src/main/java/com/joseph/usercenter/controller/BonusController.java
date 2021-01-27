package com.joseph.usercenter.controller;

import com.joseph.usercenter.domain.dto.mq.UserAddBonusMessageDTO;
import com.joseph.usercenter.domain.dto.user.UserAddBonusDTO;
import com.joseph.usercenter.domain.entity.user.User;
import com.joseph.usercenter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 对用户积分的操作
 * @author Joseph.Liu
 */
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BonusController {

    private final UserService userService;
    @PutMapping("/add-bonus")
    public User addBonus(@RequestBody UserAddBonusDTO userAddBonusDTO){
        this.userService.addBonus(
                UserAddBonusMessageDTO.builder()
                        .userId(userAddBonusDTO.getUserId())
                        .bonus(userAddBonusDTO.getBonus())
                        .event(userAddBonusDTO.getEvent())
                        .description(userAddBonusDTO.getDescription())
                        .build()
        );
        return this.userService.findById(userAddBonusDTO.getUserId());
    }

}
