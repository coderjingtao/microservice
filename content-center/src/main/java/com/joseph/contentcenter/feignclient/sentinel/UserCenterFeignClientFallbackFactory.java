package com.joseph.contentcenter.feignclient.sentinel;


import com.joseph.contentcenter.domain.dto.user.UserDTO;
import com.joseph.contentcenter.feignclient.UserCenterFeignClient;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Feign整合Sentinel之后，【限流或降级】等异常发生后，定制自己的处理逻辑
 * @author Joseph.Liu
 */
@Component
@Slf4j
public class UserCenterFeignClientFallbackFactory implements FallbackFactory<UserCenterFeignClient> {

    /**
     * 当sentinel对该资源: http://user-center/users/{userId} 设置流控降级规则，并触发，处理逻辑会转到该方法
     * @param throwable
     * @return
     */
    @Override
    public UserCenterFeignClient create(Throwable throwable) {
        return new UserCenterFeignClient() {
            @Override
            public UserDTO findByUserId(Integer userId) {
                log.warn("内容中心远程调用【用户中心】被限流/降级了",throwable);
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
        };
    }
}
