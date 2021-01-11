package com.joseph.contentcenter.feignclient.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sentinel 区分错误信息的类型
 * @author Joseph.Liu
 */
@Component
public class SentinelUrlBlockHandler implements BlockExceptionHandler {
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, BlockException e) throws Exception {
        ErrorMsg msg = null;
        if (e instanceof FlowException) {
            msg = ErrorMsg.builder()
                    .status(100)
                    .msg("被限流了")
                    .build();
        }else if(e instanceof DegradeException){
            msg = ErrorMsg.builder()
                    .status(101)
                    .msg("被降级了")
                    .build();
        }else if(e instanceof ParamFlowException){
            msg = ErrorMsg.builder()
                    .status(102)
                    .msg("热点参数限流")
                    .build();
        }else if(e instanceof SystemBlockException){
            msg = ErrorMsg.builder()
                    .status(103)
                    .msg("系统规则/负载不满足要求")
                    .build();
        }else if(e instanceof AuthorityException){
            msg = ErrorMsg.builder()
                    .status(104)
                    .msg("授权规则不通过")
                    .build();
        }
        //http状态码
        httpServletResponse.setStatus(500);
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setHeader("Content-Type","application/json;charset=utf-8");
        httpServletResponse.setContentType("application/json; charset=UTF-8");
        //spring mvc 自带的json操作工作 ： jackson
        new ObjectMapper().writeValue(httpServletResponse.getWriter(),msg);
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class ErrorMsg{
    private Integer status;
    private String msg;
}
