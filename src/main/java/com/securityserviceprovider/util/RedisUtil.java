package com.securityserviceprovider.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @Author:SCBC_LiYongJie
 * @time:2021/11/11
 */
public class RedisUtil {

    @Resource
    private static StringRedisTemplate redisTemplate;

    /**
     *  一个对Redis进行操作的工具类---->全部实例方法
     */

    private static final Logger log = LoggerFactory.getLogger(RedisUtil.class);

    /**
     *  通过key 查找对应的 value
     * @param key key
     * @return 查找到返回value 否则 返回 null
     */
    public static String get(String key) {
        return Objects.isNull(key) ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     *
     * @param key key
     * @param value value
     * @return  false or true
     */
    public static Boolean set(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key , value);
            return Boolean.TRUE;
        } catch (Exception e) {
            log.error(e.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     * 根据key 获取数据的到期时间(s)
     * @param key key
     * @return 0(forever) or expire (s) or null(key does not exist)
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * put数据并设置到期时间
     * @param key key
     * @param value value
     * @param time 到期时间 ，若time小于0则会设置成无期限，单位(s)
     * @return false or true
     */
    public static Boolean set(String key, String value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
                return Boolean.TRUE;
            } else {
                return set(key, value);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     * 指定key对应的数据的到期时间
     * @param key key
     * @param time  expire time (s)
     * @return false or true , null
     */
    public Boolean expire(String key, long time) {
        try {
            if (time >= 0) {
                return redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }else {
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     * 判断是否存在key
     * @param key key
     * @return false or true
     */
    public static Boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error(e.getMessage());
            return Boolean.FALSE;
        }
    }

    /**
     *  不定参数传入多个key，并删除对应的数据
     * @param keys 单个或多个key
     */
    public static void delete(String... keys){
        if (!Objects.isNull(keys) && keys.length > 0) {
            if (keys.length == 1) {
                redisTemplate.delete(keys[0]);
            } else {
                redisTemplate.delete(Arrays.asList(keys));
            }
        }
    }



}
