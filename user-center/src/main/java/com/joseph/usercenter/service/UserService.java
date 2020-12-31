package com.joseph.usercenter.service;

import com.joseph.usercenter.dao.user.UserMapper;
import com.joseph.usercenter.domain.entity.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {
    private final UserMapper userMapper;

    public User findById(Integer id){
        return this.userMapper.selectByPrimaryKey(id);
    }
}
