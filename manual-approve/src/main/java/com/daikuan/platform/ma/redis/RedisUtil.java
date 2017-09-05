/*
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.redis;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * 
 * @author taosj
 * @version RedisUtil.java, v0.1 2017年3月7日 上午9:31:00
 */
@Component
public class RedisUtil {

    public static final Charset UTF8 = Charset.forName("UTF8");
    private static Logger log = LoggerFactory.getLogger(RedisUtil.class);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 保存数据/更新数据 并设置超时时间<br/>
     * 如果value没有实现Serializable接口,则会保存不成功(内部已经处理了异常)
     * 
     * @param key
     *            传入相同的key会覆盖之前同名key的数据的内容及设置
     * @param value
     *            缓存的值，object类型
     * @param timeout
     *            超时时间，单位为秒，如果传值<=0，则为永不过期
     */
    public void saveOrUpdate(String key, Object value, long timeout) {
        if (timeout <= 0) {
            set(key, value);
        } else {
            setEx(key, value, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 保存数据/更新数据 并设置超时时间<br/>
     * 如果value没有实现Serializable接口,则会保存不成功(内部已经处理了异常)
     * 
     * @param key
     *            传入相同的key会覆盖之前同名key的数据的内容及设置
     * @param value
     *            缓存的值，object类型
     */
    public void set(String key, Object value) {
        ValueOperations<String, Object> valueOper = redisTemplate.opsForValue();
        valueOper.set(key, value);
    }

    /**
     *
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     */
    public void setEx(String key, Object value, long timeout, TimeUnit timeUnit) {
        ValueOperations<String, Object> valueOper = redisTemplate.opsForValue();
        valueOper.set(key, value, timeout, timeUnit);
    }

    /**
     * 根据key查询得到对应的值
     *
     * @param key
     * @return
     */
    public Object get(String key) {
        ValueOperations<String, Object> valueOper = redisTemplate.opsForValue();
        return valueOper.get(key);
    }

    /**
     * 检查key和对应的值在缓存中是否存在
     *
     * @param key
     * @return 存在返回 true，否则返回false
     */
    public boolean exist(String key) {
        ValueOperations<String, Object> valueOper = redisTemplate.opsForValue();
        boolean exist = valueOper.getOperations().hasKey(key);
        return exist;
    }

    /**
     * 根据key删除key及对应的值
     *
     * @param id
     */
    public void delete(String id) {
        ValueOperations<String, Object> valueOper = redisTemplate.opsForValue();
        RedisOperations<String, Object> redisOperations = valueOper.getOperations();
        redisOperations.delete(id);
    }

    /**
     * 从 key SET里随机移动n个元素 到destKey SET里
     * 
     * @param key
     * @return 成功移动的个数
     */
    public long move(String key, long n, String destKey) {
        SetOperations<String, String> setOps = stringRedisTemplate.opsForSet();
        if (n > 0) {
            Long srcSize = setOps.size(key);
            log.info("the size of {} is "+ srcSize,key);
            n = Math.min(n, srcSize);
            List<String> list = setOps.randomMembers(key, n);
            if (list == null || list.size() == 0) {
                return 0;
            }
            String[] array = new String[list.size()];
            list.toArray(array);
            String s = list.stream().collect(Collectors.joining(","));
            log.info("Redis will move {} from {} to {}.", s, key, destKey);
            setOps.remove(key, list.toArray());
            return setOps.add(destKey, array);
        } else {
            return 0;
        }
    }
    
    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    public RedisTemplate<String, Object> getRedisTemplate() {
        return redisTemplate;
    }
}