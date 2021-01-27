package com.joseph.contentcenter.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joseph.contentcenter.dao.content.MidUserShareMapper;
import com.joseph.contentcenter.dao.content.ShareMapper;
import com.joseph.contentcenter.domain.dto.content.ShareDTO;
import com.joseph.contentcenter.domain.dto.user.UserAddBonusDTO;
import com.joseph.contentcenter.domain.dto.user.UserDTO;
import com.joseph.contentcenter.domain.entity.content.MidUserShare;
import com.joseph.contentcenter.domain.entity.content.Share;
import com.joseph.contentcenter.feignclient.UserCenterFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Objects;

/**
 * @author Joseph.Liu
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class ShareService {
    private final ShareMapper shareMapper;

    private final UserCenterFeignClient userCenterFeignClient;

    private final MidUserShareMapper midUserShareMapper;

    public ShareDTO findById(Integer id) {
        //获取分享详情
        Share share = this.shareMapper.selectByPrimaryKey(id);
        //获取分享发布人的id
        Integer userId = share.getUserId();
        //通过微服务通信，获取分布人的用户信息

        //通过feign client调用微服务
        UserDTO userDto = this.userCenterFeignClient.findByUserId(userId);

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

    public PageInfo<Share> q(String title, Integer pageNo, Integer pageSize, Integer userId) {
        //开始分页：它会切入下面这条不分页的SQL,自动拼接出分页的SQL
        //它的本质就是利用了Mybatis的拦截器，自动在原始的不分页的SQL上加上了limit语句
        PageHelper.startPage(pageNo,pageSize);
        //不分页的SQL
        List<Share> shares = this.shareMapper.selectByParam(title);

        //1.如果用户未登录，那么downloadUrl全部设为null
        if(userId == null){
            shares.forEach(share -> share.setDownloadUrl(null));
        }
        //2.如果用户已登录，查询mid_user_share表，如果没有值，则表示用户还未兑换过该文章，那么downloadUrl也设为null
        else{
            shares.forEach(share -> {
                MidUserShare midUserShare = this.midUserShareMapper.selectOne(
                        MidUserShare.builder()
                                .userId(userId)
                                .shareId(share.getId())
                                .build()
                );
                if(midUserShare == null){
                    share.setDownloadUrl(null);
                }
            });
        }
        return new PageInfo<>(shares);
    }

    public Share exchangeById(Integer shareId, HttpServletRequest request) {
        //0.从Request中拿到用户的id
        Integer userId = (Integer) request.getAttribute("id");

        //1.根据id查询分享文章,校验文章是否存在
        Share share = this.shareMapper.selectByPrimaryKey(shareId);
        if(Objects.isNull(share)){
            throw new IllegalArgumentException("该分享文章不存在");
        }
        //2.如果用户已经兑换过该篇文章，则直接返回
        MidUserShare midUserShare = this.midUserShareMapper.selectOne(
                MidUserShare.builder()
                        .userId(userId)
                        .shareId(shareId)
                        .build()
        );
        if(midUserShare != null){
            return share;
        }
        //3.根据当前登录的用户id,查询积分是否足够兑换文章
        UserDTO userDTO = this.userCenterFeignClient.findByUserId(userId);
        Integer sharePrice = share.getPrice();
        if(sharePrice > userDTO.getBonus()){
            throw new IllegalArgumentException("用户没有足够的积分兑换");
        }
        //4.如果积分足够，去用户中心扣减用户积分，并给内容中心的mid_user_share表中插入一条数据
        this.userCenterFeignClient.addBonus(
                UserAddBonusDTO.builder()
                        .userId(userId)
                        .bonus(-sharePrice)
                        .event("redeem")
                        .description("积分兑换文章")
                        .build()
        );
        this.midUserShareMapper.insert(
                MidUserShare.builder()
                        .userId(userId)
                        .shareId(shareId)
                        .build()
        );
        return share;
    }
}
