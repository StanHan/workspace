package demo.db.redis;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisDemo {

    public static void main(String[] args) {
        clearMA();
    }
    
    static void clearMA(){
        try(Jedis jedis = new Jedis("180.101.195.162", 63790);){
            jedis.auth("daikuanwang_wuxi_test");
            jedis.select(0);
            Set<String> set = jedis.keys("ma*");
            set.forEach(System.out::println);
            String[] array = new String[set.size()];
            set.toArray(array);
            jedis.del(array);
            System.out.println("------------------------------");
            Set<String> set2 = jedis.keys("ma*");
            set2.forEach(System.out::println);
        }
    }
    
    static void demoTransfer(){
        try(Jedis jedis = new Jedis("180.101.195.162", 63790);){
            jedis.auth("daikuanwang_wuxi_test");
            jedis.select(2);
//            String key1 = "dubbo_customer_recall_audit_list";
//            String key2 = "dubbo_customer_recall_survey_list";
//            String key3 = "tel.verify.recall.dkw.list";
//            
//            String[] customers = {"49abb01332417aaa2dc5;2","4f9295a8c83d179decbc;2","4eb9a20f9dc9343b9c25;2","414584b2cfcb5a77133d;2","4a64a9ed4c31c4a97c75;2","4cba83d855f760ebff28;2","4266ad0276af4399acea;2"};
//            System.out.println(jedis.lpush(key1, customers));
//            System.out.println(jedis.lpush(key2, customers));
//            System.out.println(jedis.lpush(key3, customers));
            
            String[] userids = {"2216;2","2231;1","2235;2","2237;2"};
            String key4 = "dubbo_question_recall_survey_list";
            String key5 = "dubbo_question_recall_audit_list";
            System.out.println(jedis.lpush(key4, userids));
            System.out.println(jedis.lpush(key5, userids));
        }
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
