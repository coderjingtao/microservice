package com.joseph.usercenter.service;

import com.joseph.usercenter.dao.user.BonusEventLogMapper;
import com.joseph.usercenter.dao.user.UserMapper;
import com.joseph.usercenter.domain.dto.mq.UserAddBonusMessageDTO;
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
                        .event("投稿")
                        .description("投稿审核通过后加积分")
                        .createTime(new Date())
                        .build()
        );
    }
}
