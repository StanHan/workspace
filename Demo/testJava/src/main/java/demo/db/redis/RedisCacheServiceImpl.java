/*
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package demo.db.redis;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisCacheServiceImpl implements CacheService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private ValueOperations<String, Object> valueOperations;
    // private ListOperations<String, Object> listOperations;
    // private HashOperations<String, ?, ?> hashOperations;

    @PostConstruct
    public void init() {
        valueOperations = redisTemplate.opsForValue();
        // listOperations = redisTemplate.opsForList();
        // hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void set(String key, Object value, long seconds) {
        try {
            valueOperations.set(key, value, seconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public Object get(String key) {
        try {
            return valueOperations.get(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

}
