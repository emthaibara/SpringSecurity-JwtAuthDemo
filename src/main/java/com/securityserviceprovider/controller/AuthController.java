package com.securityserviceprovider.controller;

import com.securityserviceprovider.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/1
 */
@RestController
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/sharedkitchen/login")
    public Result<String> login(){
        log.info("login verify success");
        return Result.loginVerifySuccess();
    }

    @GetMapping("/sharedkitchen/login/member")
    public Result<String> member(){
        log.info("member");
        return Result.success("member hello");
    }

    @GetMapping("/sharedkitchen/login/nonmember")
    public Result<String> nonMember(){
        log.info("nonmember");
        return Result.success("nonMember hello");
    }

}
