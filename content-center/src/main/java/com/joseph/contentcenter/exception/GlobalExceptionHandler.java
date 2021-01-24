package com.joseph.contentcenter.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * spring mvc 的全局异常处理
 * @author Joseph.Liu
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 一定要加上：捕获所有异常：用来阻断默认异常处理器的传递
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorBody> error(Exception e){
        log.warn("全局异常处理器捕获了Exception异常",e);
        return new ResponseEntity<>(
                ErrorBody.builder()
                        .body(e.getMessage())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler({UserLoginSecurityException.class})
    public ResponseEntity<ErrorBody> error(UserLoginSecurityException e){
        log.warn("全局异常处理器捕获了UserLoginSecurityException异常",e);
        return new ResponseEntity<>(
                ErrorBody.builder()
                        .body(e.getMessage())
                        .status(HttpStatus.UNAUTHORIZED.value())
                        .build(),
                HttpStatus.UNAUTHORIZED
        );
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class ErrorBody{
    private String body;
    private int status;
}
