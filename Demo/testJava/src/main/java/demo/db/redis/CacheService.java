/*
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package demo.db.redis;

/**
 * 缓存
 * 
 * @author hanjy
 *
 */
public interface CacheService {

    /**
     * 缓存seconds秒
     * 
     * @param key
     * @param value
     * @param seconds
     * @return value
     */
    void set(String key, Object value, long seconds);

    Object get(String key);
}
