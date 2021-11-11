package com.securityserviceprovider.config.redisconfig;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.stereotype.Component;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/11
 *
 *              以ObjectRedisSerializer----->去拓展
 *                      Redis的Value---->
 */
@Component
public class ObjectRedisSerializer implements RedisSerializer<Object> {


    @Override
    public byte[] serialize(Object o) throws SerializationException {
        return new byte[0];
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        return null;
    }
}
