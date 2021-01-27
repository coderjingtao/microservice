package com.joseph.usercenter.service;

import com.joseph.usercenter.dao.user.BonusEventLogMapper;
import com.joseph.usercenter.dao.user.UserMapper;
import com.joseph.usercenter.domain.dto.mq.UserAddBonusMessageDTO;
import com.joseph.usercenter.domain.dto.user.UserLoginRequestDTO;
import com.joseph.usercenter.domain.entity.user.BonusEventLog;
import com.joseph.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 用户相关的业务逻辑类
 * @author Joseph.Liu
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserMapper userMapper;

    private final BonusEventLogMapper bonusEventLogMapper;

    public User findById(Integer id){
        return this.userMapper.selectByPrimaryKey(id);
    }

    public void saveUser(User user){
        this.userMapper.insertSelective(user);
    }

    @Transactional(rollbackFor = Exception.class)
    public void addBonus(UserAddBonusMessageDTO userAddBonusMessageDTO){
        //1.为用户加积分
        Integer userId = userAddBonusMessageDTO.getUserId();
        User user = this.userMapper.selectByPrimaryKey(userId);
        Integer bonus = userAddBonusMessageDTO.getBonus();
        user.setBonus(user.getBonus()+ bonus);
        this.userMapper.updateByPrimaryKeySelective(user);
        //2.记录日志到bonus_event_log表中
        this.bonusEventLogMapper.insert(
                BonusEventLog.builder()
                        .userId(userId)
                        .value(bonus)
                        .event(userAddBonusMessageDTO.getEvent())
                        .description(userAddBonusMessageDTO.getDescription())
                        .createTime(new Date())
                        .build()
        );
    }

    /**
     * 用户登录的业务逻辑：
     * 使用用户微信唯一id去数据库查看用户是否已经注册，如果没有注册就插入数据表返回，已经注册则直接返回
     * @param userLoginRequestDTO 用户微信登录的请求信息
     * @param wxOpenId 用户的微信唯一id
     * @return 注册的用户信息
     */
    public User login(UserLoginRequestDTO userLoginRequestDTO, String wxOpenId){
        User user = this.userMapper.selectOne(
                User.builder()
                        .wxId(wxOpenId)
                        .build()
        );
        if(user == null){
            User newUser = User.builder()
                    .wxId(wxOpenId)
                    .avatarUrl(userLoginRequestDTO.getAvatarUrl())
                    .wxNickname(userLoginRequestDTO.getWxNickname())
                    .bonus(300)
                    .roles("user")
                    .createTime(new Date())
                    .updateTime(new Date())
                    .build();
            this.userMapper.insertSelective(newUser);
            return newUser;
        }
        return user;
    }
}
