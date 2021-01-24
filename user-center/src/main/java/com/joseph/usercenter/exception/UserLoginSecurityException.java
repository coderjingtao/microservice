package com.joseph.usercenter.exception;

/**
 * @author Joseph.Liu
 */
public class UserLoginSecurityException extends RuntimeException {
    public UserLoginSecurityException(){
        super();
    }
    public UserLoginSecurityException(String error){
        super(error);
    }
}
