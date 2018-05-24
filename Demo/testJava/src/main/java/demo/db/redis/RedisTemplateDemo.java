package demo.db.redis;

import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import demo.java.lang.ThreadDemo;
import demo.java.util.concurrent.ThreadFactoryDemo;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;

public class RedisTemplateDemo {

    private static final Logger logger = LoggerFactory.getLogger(RedisTemplateDemo.class);

    public static StringRedisTemplate template;

    public static final Random random = new Random();

    public static void main(String[] args) {
        demoRedisLock();
    }

    /**
     * 尝试用Redis实现一个分布式锁
     * <p>
     * Redis Setnx 命令:SETNX KEY_NAME VALUE //SET if Not eXists，设置成功，返回 1 。 设置失败，返回 0 。
     * 
     */
    static void demoRedisLock() {
        ThreadFactory threadFactory = new ThreadFactoryDemo.SpecialThreadFactory(
                new ThreadDemo.UncaughtExceptionHandlerDemo("线程未捕获的异常"));

        ThreadPoolExecutor executor = new ThreadPoolExecutor(50, 50, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(), threadFactory);

        String lockKey = "test:redis.lock";
        for (int i = 0; i < 50; i++) {

            executor.submit(() -> {
                while (true) {
                    // 获取锁
                    if (template.opsForValue().setIfAbsent(lockKey, "1")) {
                        logger.info("获取锁.");
                        ThreadDemo.safeSleep(random.nextInt(50));
                        logger.info("释放锁.");
                        template.delete(lockKey);
                        break;
                    } else {
                        ThreadDemo.safeSleep(random.nextInt(10));
                    }
                }
            });

        }
        executor.shutdown();
    }

    @Test
    public void demo1() {
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
        JedisConnectionFactory factory = buildJedisConnectionFactory("172.16.0.140", 6379, 2, "sider");
        template = buildStringRedisTemplate(factory);
    }

    public static JedisPoolConfig buildRedisPoolConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        return config;
    }

    public static JedisConnectionFactory buildJedisConnectionFactory(String hostName, int port, int db,
            String password) {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setPoolConfig(buildRedisPoolConfig());
        factory.setHostName(hostName);
        factory.setPort(port);
        factory.setDatabase(db);
        factory.setPassword(password);
        factory.setUsePool(true);
        factory.afterPropertiesSet();
        logger.info("JedisConnectionFactory bean init success.");
        return factory;
    }

    public static StringRedisTemplate buildStringRedisTemplate(JedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate();
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }
}
