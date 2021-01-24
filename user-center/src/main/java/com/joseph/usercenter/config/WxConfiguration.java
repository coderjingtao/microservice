package com.joseph.usercenter.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.WxMaConfig;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信小程序配置类 WxMa = Wx Mini App
 * @author Joseph.Liu
 */
@Configuration
public class WxConfiguration {

    @Bean
    public WxMaConfig wxMaConfig(){
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid("wxc58d4556b7c1b55e");
        config.setSecret("0eadac3f041cb1f29408fd3b4bc29349");
        return config;
    }

    @Bean
    public WxMaService wxMaService(WxMaConfig wxMaConfig){
        WxMaServiceImpl wxMaService = new WxMaServiceImpl();
        wxMaService.setWxMaConfig(wxMaConfig);
        return wxMaService;
    }
}
