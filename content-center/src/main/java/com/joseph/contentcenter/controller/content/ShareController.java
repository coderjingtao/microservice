package com.joseph.contentcenter.controller.content;

import com.joseph.contentcenter.aspect.CheckLogin;
import com.joseph.contentcenter.domain.dto.content.ShareDTO;
import com.joseph.contentcenter.domain.entity.content.Share;
import com.joseph.contentcenter.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @author Joseph.Liu
 */
@RestController
@RequestMapping("/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {

    private final ShareService shareService;

    /**
     * 根据id,查看分享文章的内容
     * @param id 分享文章的id
     * //@param @RequestHeader("X-Token") String token ：利用spring mvc 的 requestHeader注解，接收请求头部变量为 X-Token的值放入到token string中
     * @return 分享文章的DTO
     */
    @GetMapping("/{id}")
    @CheckLogin
    public ShareDTO findById(@PathVariable Integer id) {
        return this.shareService.findById(id);
    }

    @GetMapping("/create")
    public Share createShare(){
        Share share = new Share();
        share.setUserId(2);
        share.setTitle("Spring Boot入门");
        share.setBuyCount(20);
        share.setCreateTime(new Date());
        share.setUpdateTime(new Date());
        this.shareService.saveShare(share);
        return share;
    }
}
