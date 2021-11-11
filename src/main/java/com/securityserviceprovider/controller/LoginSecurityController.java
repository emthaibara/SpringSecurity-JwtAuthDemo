package com.securityserviceprovider.controller;

import com.securityserviceprovider.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/1
 */
@RestController
public class LoginSecurityController {

    private static final Logger log = LoggerFactory.getLogger(LoginSecurityController.class);

    @PostMapping("/login")
    public Result<String> login(){
        log.info("login verify success");
        return Result.loginVerifySuccess();
    }

    /**
     *
     * @return return
     */
    @RequestMapping(method = RequestMethod.POST,value = "/login/test")
    public Result<String> login_Test(){
        log.info("login verify Test");
        return Result.loginVerifySuccess();
    }


}
