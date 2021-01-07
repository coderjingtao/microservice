package com.joseph.contentcenter.controller.content;

import com.joseph.contentcenter.domain.dto.content.ShareDTO;
import com.joseph.contentcenter.domain.entity.content.Share;
import com.joseph.contentcenter.service.ShareService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/shares")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShareController {

    private final ShareService shareService;

    @GetMapping("/{id}")
    public ShareDTO findById(@PathVariable Integer id) throws Exception {
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
