package com.securityserviceprovider;

import com.securityserviceprovider.dao.AuthUserMapper;
import com.securityserviceprovider.util.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest
class SecurityServiceProviderApplicationTests {

    @Resource
    AuthUserMapper authUserMapper;

    @Test
    void contextLoads() {
        System.out.println(authUserMapper.selectByPrimaryKey("13266746457").toString());
    }

}
