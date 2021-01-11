package com.joseph.contentcenter.feignclient.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Sentinel 区分来源
 * 为了不影响后续的测试，先把Component注释掉
 * @author Joseph.Liu
 */
//@Component
public class SentinelRequestOriginParser implements RequestOriginParser {

    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        //从请求参数中获取名为 origin的参数并返回
        //如果获取不到origin参数，那么就抛出异常（必须提供该参数）
        String origin = httpServletRequest.getParameter("origin");
        if(StringUtils.isBlank(origin)){
            throw new IllegalArgumentException("[origin must be specified.]");
        }
        return origin;
    }
}
