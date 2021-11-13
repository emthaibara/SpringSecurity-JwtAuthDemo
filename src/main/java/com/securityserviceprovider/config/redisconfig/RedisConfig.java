package com.securityserviceprovider.config.redisconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/11
 */

@Configuration
public class RedisConfig {

    @Bean
    public StringRedisTemplate redisValueOfStringTemplate(LettuceConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

}
