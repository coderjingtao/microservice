package com.joseph.contentcenter.aspect;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 检查是否为管理员
 * @author Joseph.Liu
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckAuthorization {
    String value();
}
