package demo.db.redis;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * <li>1. 掌控储存在Redis中的所有键: 使用合适的命名方法会简化你的数据库管理，当你通过你的应用程序或者服务做键的命名空间时（通常情况下是使用冒号来划分键名），你就可以在数据迁移、转换或者删除时轻松的识别。
 * <li>2.控制所有键名的长度
 * <li>3.使用合适的数据结构:Sorted
 * sets是最昂贵的数据结构，不管是内存消耗还是基本操作的复杂性。Redis中一个经常被忽视的功能就是bitmaps或者bitsets（V2.2之后）。Bitsets允许你在Redis值上执行多个bit-level操作，比如一些轻量级的分析。
 * <li>4. 使用SCAN时别使用键:对比KEYS命令，虽然SCAN无法一次性返回所有匹配结果，但是却规避了阻塞系统这个高风险，从而也让一些操作可以放在主节点上执行。SCAN 命令是一个基于游标的迭代器。SCAN 命令每次被调用之后，
 * 都会向用户返回一个新的游标，用户在下次迭代时需要使用这个新游标作为 SCAN 命令的游标参数， 以此来延续之前的迭代过程。同时，使用SCAN，用户还可以使用keyname模式和count选项对命令进行调整。
 * SCAN相关命令还包括SSCAN 命令、HSCAN 命令和 ZSCAN 命令，分别用于集合、哈希键及有续集等
 * <li>5. 使用服务器端Lua脚本:
 * 在Redis使用过程中，Lua脚本的支持无疑给开发者提供一个非常友好的开发环境，从而大幅度解放用户的创造力。如果使用得当，Lua脚本可以给性能和资源消耗带来非常大的改善。取代将数据传送给CPU，脚本允许你在最接近数据的地方执行逻辑，从而减少网络延时和数据的冗余传输。
 * 在Redis中，Lua一个非常经典的用例就是数据过滤或者将数据聚合到应用程序。通过将处理工作流封装到一个脚本中，你只需要调用它就可以在更短的时间内使用很少的资源来获取一个更小的答案。
 * 专家提示：Lua确实非常棒，但是同样也存在一些问题，比如很难进行错误报告和处理。一个明智的方法就是使用Redis的Pub/Sub功能，并且让脚本通过专用信道来推送日志消息。然后建立一个订阅者进程，并进行相应的处理。。
 */
public class JedisDemo {

    public static void main(String[] args) {
        listMA生产();
        // clearMA();
    }

    static void listMA生产() {
        try (Jedis jedis = new Jedis("180.101.195.217", 5004);) {
            jedis.auth("daikuanwang_webserver");
            jedis.select(0);
            Set<String> set = jedis.keys("ma_*");
            set.forEach(System.out::println);
        }
    }

    static void clearMA() {
        try (Jedis jedis = new Jedis("180.101.195.162", 63790);) {
            jedis.auth("daikuanwang_wuxi_test");
            jedis.select(0);
            Set<String> set = jedis.keys("ma_*");
            set.forEach(System.out::println);
            String[] array = new String[set.size()];
            set.toArray(array);
            jedis.del(array);
            System.out.println("------------------------------");
            Set<String> set2 = jedis.keys("ma_*");
            set2.forEach(System.out::println);
            
        }
    }

    static void demoTransfer() {
        try (Jedis jedis = new Jedis("180.101.195.162", 63790);) {
            jedis.auth("daikuanwang_wuxi_test");
            jedis.select(2);
            // String key1 = "dubbo_customer_recall_audit_list";
            // String key2 = "dubbo_customer_recall_survey_list";
            // String key3 = "tel.verify.recall.dkw.list";
            //
            // String[] customers =
            // {"49abb01332417aaa2dc5;2","4f9295a8c83d179decbc;2","4eb9a20f9dc9343b9c25;2","414584b2cfcb5a77133d;2","4a64a9ed4c31c4a97c75;2","4cba83d855f760ebff28;2","4266ad0276af4399acea;2"};
            // System.out.println(jedis.lpush(key1, customers));
            // System.out.println(jedis.lpush(key2, customers));
            // System.out.println(jedis.lpush(key3, customers));

            String[] userids = { "2216;2", "2231;1", "2235;2", "2237;2" };
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
