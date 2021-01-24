package com.joseph.contentcenter.controller.content;

import com.joseph.contentcenter.domain.dto.user.UserDTO;
import com.joseph.contentcenter.feignclient.StandAloneFeignClient;
import com.joseph.contentcenter.feignclient.UserCenterFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    /**
     * 测试：服务发现，证明内容中心通过nacos，总能找到用户中心
     * @return 用户中心所有实例的地址信息
     */
    @GetMapping("/queryusercenter")
    public List<ServiceInstance> queryServiceInstanceById(){
        //查询用户微服务所有实例的信息
        return this.discoveryClient.getInstances("user-center");
    }

    /**
     * 测试：服务发现组件都注册了哪些微服务
     * @return 所有微服务的id
     */
    @GetMapping("/queryAll")
    public List<String> queryAllRegisteredService(){
        return this.discoveryClient.getServices();
    }

    @Autowired
    UserCenterFeignClient userCenterFeignClient;
    @GetMapping("/test-query")
    public UserDTO query(UserDTO userDTO){
        return userCenterFeignClient.query(userDTO);
    }

    @Autowired
    StandAloneFeignClient standAloneFeignClient;
    @GetMapping("/google")
    public String googleIndex(){
        return standAloneFeignClient.index();
    }

//    @Autowired
//    private Source source;
//    @GetMapping("/test-stream")
//    public String testSpringCloudStream(){
//        this.source.output()
//                .send(
//                        MessageBuilder
//                                .withPayload("消息体")
//                                .build()
//                );
//        return "success";
//    }
//
//    @Autowired
//    private MySource mySource;
//    @GetMapping("/test-stream2")
//    public String testCustomSpringCloudStream(){
//        this.mySource.output()
//                .send(
//                        MessageBuilder
//                                .withPayload("自定义stream接口发送的消息")
//                                .build()
//                );
//        return "success";
//    }
}
