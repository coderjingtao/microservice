package com.joseph.contentcenter.feignclient.sentinel;

import com.joseph.contentcenter.domain.dto.user.UserDTO;
import com.joseph.contentcenter.feignclient.UserCenterFeignClient;
import org.springframework.stereotype.Component;

/**
 * Feign整合Sentinel之后，【限流或降级】发生后，定制自己的处理逻辑
 * @author Joseph.Liu
 */
@Component
public class UserCenterFeignClientFallback implements UserCenterFeignClient {
    /**
     * 当sentinel对该资源: http://user-center/users/{userId} 设置流控降级规则，并触发，处理逻辑会转到该方法
     * @param userId
     * @return
     */
    @Override
    public UserDTO findByUserId(Integer userId) {
        UserDTO userDTO = new UserDTO();
        userDTO.setWxNickname("服务降级后，会显示该属性");
        return userDTO;
    }

    @Override
    public UserDTO query(UserDTO userDTO) {
        return null;
    }

    @Override
    public UserDTO postQuery(UserDTO userDTO) {
        return null;
    }
}
