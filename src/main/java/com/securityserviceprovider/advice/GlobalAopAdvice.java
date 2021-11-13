package com.securityserviceprovider.advice;

import com.securityserviceprovider.util.RedisUtil;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/12
 */

@Aspect
@Component
public class GlobalAopAdvice {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @After("com.securityserviceprovider.aspect.SystemArchitecture.redisUtilInitialPointcut()")
    public void redisUtilInitialAdvice(){
        if (Objects.isNull(RedisUtil.getRedisTemplate())){
            RedisUtil.setRedisTemplate(redisTemplate);
        }
    }
}
