package demo.db.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisDemo {

    public static void main(String[] args) {
        demoJedisPool();
        demoJedis();
    }

    private static void demoJedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        JedisPool jedisPool = new JedisPool(poolConfig, "172.16.20.205", 6379, 2000, null, 2);
        Jedis jedis = jedisPool.getResource();
        String value = jedis.get("tel.verify.count.totalUnprocessedNum");
        System.out.println(value);
    }

    private static void demoJedis() {
        // 连接redis服务器，192.168.0.100:6379
        Jedis jedis = new Jedis("172.16.20.205", 6379);
        jedis.select(2);
        long dbSize = jedis.dbSize();
        System.out.println("dbSize=" + dbSize);
        long idx = jedis.getDB();
        System.out.println("db=" + idx);
        String value = jedis.get("s_user_app_version_maxId");
        System.out.println(value);
        value = jedis.get("tel.verify.count.totalUnprocessedNum");
        System.out.println(value);
    }

}
