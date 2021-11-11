package com.securityserviceprovider.config.redisconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/11
 */

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String,String> redisValueOfStringTemplate(LettuceConnectionFactory redisConnectionFactory) {
            RedisTemplate<String,String> stringTemplate = new RedisTemplate<>();
            StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
            stringTemplate.setKeySerializer(stringRedisSerializer);
            stringTemplate.setValueSerializer(stringRedisSerializer);
            stringTemplate.setConnectionFactory(redisConnectionFactory);
            return stringTemplate;
    }

    /**
     *              key     value
     *
     *
     *             String   Object
     *
     *
     */
//    @Bean
//    public RedisTemplate<String,Object> redisValueOfObjectTemplate(LettuceConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String,Object> stringTemplate = new RedisTemplate<>();
//        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
//        stringTemplate.setKeySerializer(stringRedisSerializer);
//
//        stringTemplate.setValueSerializer();
//        stringTemplate.setConnectionFactory(redisConnectionFactory);
//        return stringTemplate;
//    }

}
