package com.securityserviceprovider.exception;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/17
 */


public class BaseException extends RuntimeException{

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
