package demo.db.redis;

import java.util.Set;

import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import com.alibaba.fastjson.JSONObject;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

public class RedisTemplateDemo {

    public static StringRedisTemplate template;

    public static void main(String[] args) {
        template.opsForValue().set("1", "1");
        RedisConnectionFactory connectionFactory = template.getConnectionFactory();
        RedisConnection redisConnection = connectionFactory.getConnection();
        String pingResult = redisConnection.ping();
        System.err.println(pingResult);
        String clientName = redisConnection.getClientName();
        System.err.println(clientName);
        JedisConnectionFactory jedisConnectionFactory = (JedisConnectionFactory) connectionFactory;
        String hostName = jedisConnectionFactory.getHostName();
        System.err.println(hostName);
        JedisShardInfo jediShardInfo = jedisConnectionFactory.getShardInfo();
        System.out.println(jediShardInfo);
    }

    static {
        JedisShardInfo jedisShardInfo = new JedisShardInfo("172.16.0.140", 6379);
        jedisShardInfo.setPassword("sider");
        JedisPoolConfig config = new JedisPoolConfig();
        JedisConnectionFactory factory = new JedisConnectionFactory(jedisShardInfo);

        factory.setPoolConfig(config);
        factory.setHostName("172.16.0.140");
        factory.setPort(6379);
        factory.setDatabase(2);
        factory.setPassword("sider");
        template = new StringRedisTemplate(factory);
    }

    static void listMA生产() {
        try (Jedis jedis = new Jedis("180.101.195.217", 5004);) {
            jedis.auth("daikuanwang_webserver");
            jedis.select(0);
            Set<String> set = jedis.keys("ma_*");
            set.forEach(System.out::println);
        }
    }
}
