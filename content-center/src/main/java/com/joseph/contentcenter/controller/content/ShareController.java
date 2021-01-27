package com.joseph.contentcenter.controller.content;

import com.github.pagehelper.PageInfo;
import com.joseph.contentcenter.aspect.CheckLogin;
import com.joseph.contentcenter.domain.dto.content.ShareDTO;
import com.joseph.contentcenter.domain.entity.content.Share;
import com.joseph.contentcenter.service.ShareService;
import com.joseph.contentcenter.utils.JwtOperator;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author Joseph.Liu
 */
@RestController
@RequestMapping("/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {

    private final ShareService shareService;
    private final JwtOperator jwtOperator;

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

    /**
     * 分页查询分享文章的接口
     * @param title 文章的标题，可为空
     * @param pageNo 文章的页码，可为空，默认为1
     * @param pageSize 每页的文章数，可为空，默认为10
     * @return 分页后的文章列表
     */
    @GetMapping("/q")
    public PageInfo<Share> q(
            @RequestParam(required = false) String title,
            @RequestParam(required = false,defaultValue = "1") Integer pageNo,
            @RequestParam(required = false,defaultValue = "10") Integer pageSize,
            @RequestHeader(value = "X-Token",required = false) String token){
        //注意点：对page size要做控制，否则内存和数据库容易崩溃
        if(pageSize > 100){
            pageSize = 100;
        }
        Integer userId = null;
        if(StringUtils.isNotBlank(token)){
            Claims claims = this.jwtOperator.getClaimsFromToken(token);
            userId = Integer.valueOf(claims.getId());
        }
        return this.shareService.q(title,pageNo,pageSize,userId);
    }

    @GetMapping("/exchange/{id}")
    @CheckLogin
    public Share exchangeById(@PathVariable Integer id, HttpServletRequest request){
        return this.shareService.exchangeById(id,request);
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
