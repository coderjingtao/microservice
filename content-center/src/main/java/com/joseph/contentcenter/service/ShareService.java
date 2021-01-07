package com.joseph.contentcenter.service;

import com.joseph.contentcenter.dao.content.ShareMapper;
import com.joseph.contentcenter.domain.dto.content.ShareDTO;
import com.joseph.contentcenter.domain.dto.user.UserDTO;
import com.joseph.contentcenter.domain.entity.content.Share;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ShareService {
    private final ShareMapper shareMapper;

    private final RestTemplate restTemplate;

//    private final DiscoveryClient discoveryClient;

    public ShareDTO findById(Integer id) throws Exception {
        //获取分享详情
        Share share = this.shareMapper.selectByPrimaryKey(id);
        //获取分享发布人的id
        Integer userId = share.getUserId();
        //通过微服务通信，获取分布人的用户信息

        //ribbon自动会把user-center转换成用户中心在nacos上面注册的地址
        UserDTO userDto = this.restTemplate.getForObject("http://user-center/users/{userId}",UserDTO.class,userId);

        //消息的装配
        ShareDTO shareDTO = new ShareDTO();
        BeanUtils.copyProperties(share, shareDTO);
        shareDTO.setWxNickname(userDto.getWxNickname());
        return shareDTO;
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();

        //用Http GET方法去请求，并返回一个对象
        String object = restTemplate.getForObject("http://localhost:8080/users/{id}",String.class,1);
        System.out.println(object);
        //entity 比 object 多了一些http status code等一些信息
        ResponseEntity<String> entity = restTemplate.getForEntity("http://localhost:8080/users/{id}",String.class,2);
        System.out.println(entity.getBody());
        System.out.println(entity.getStatusCode());
    }

    public void saveShare(Share share){
        this.shareMapper.insertSelective(share);
    }
}
