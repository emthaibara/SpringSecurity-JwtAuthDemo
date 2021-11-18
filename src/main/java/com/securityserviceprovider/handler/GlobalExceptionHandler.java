package com.securityserviceprovider.handler;

import com.securityserviceprovider.exception.BaseException;
import com.securityserviceprovider.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/17
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseBody
    @ExceptionHandler(value = BaseException.class)
    public Result<String> remoteExceptionHandler(BaseException e) {
        log.error(e.getMessage());
        return Result.error(e.getMessage());
    }

    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public Result<String> validatedExceptionHandler(BindException e) {
        String message = e.getBindingResult().getAllErrors().stream().
                map(DefaultMessageSourceResolvable::getDefaultMessage).
                collect(Collectors.joining());
        log.error(message);
        return Result.error(message);
    }

}
