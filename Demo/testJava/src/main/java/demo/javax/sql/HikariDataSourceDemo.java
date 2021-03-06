package demo.javax.sql;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import demo.db.mysql.MySqlDemo;

/**
 * HikariCP是一个高效的数据库连接池。 HikariCP号称是现在性能最好的JDBC连接池组件。 Hikari来自日文，是“光”，阳光的光。
 * 
 * 官网详细地说明了HikariCP所做的一些优化，总结如下：
 * <li>字节码精简：优化代码，直到编译后的字节码最少，这样，CPU缓存可以加载更多的程序代码；
 * <li>优化代理和拦截器：减少代码，例如HikariCP的Statement proxy只有100行代码，只有BoneCP的十分之一；
 * <li>自定义数组类型（FastStatementList）代替ArrayList：避免每次get()调用都要进行range check，避免调用remove()时的从头到尾的扫描；
 * <li>自定义集合类型（ConcurrentBag）：提高并发读写的效率；
 * <li>其他针对BoneCP缺陷的优化，比如对于耗时超过一个CPU时间片的方法调用的研究（但没说具体怎么优化）。
 * 
 * <p>
 * 关于可靠性方面，也是有实验和数据支持的。对于数据库连接中断的情况，通过测试getConnection()，各种CP的不相同处理方法如下： （所有CP都配置了跟connectionTimeout类似的参数为5秒钟）
 * <li>HikariCP：等待5秒钟后，如果连接还是没有恢复，则抛出一个SQLExceptions 异常；后续的getConnection()也是一样处理；
 * <li>C3P0：完全没有反应，没有提示，也不会在“CheckoutTimeout”配置的时长超时后有任何通知给调用者；然后等待2分钟后终于醒来了，返回一个error；
 * <li>Tomcat：返回一个connection，然后……调用者如果利用这个无效的connection执行SQL语句……结果可想而知；大约55秒之后终于醒来了，这时候的getConnection()终于可以返回一个error，但没有等待参数配置的5秒钟，而是立即返回error；
 * <li>BoneCP：跟Tomcat的处理方法一样；也是大约55秒之后才醒来，有了正常的反应，并且终于会等待5秒钟之后返回error了；
 *
 */
public class HikariDataSourceDemo {

    public static void main(String[] args) throws SQLException {
        
        
    }


    public void demo() throws SQLException {
        HikariDataSource ds = init("", "", "");
        Connection conn = ds.getConnection();
        
        // ......
        // 最后关闭链接
        conn.close();
    }

    /**
     * 初始化连接池
     * 
     * @param minimum
     * @param Maximum
     */
    public static HikariDataSource init(String jdbcUrl, String username, String password) {
        // 连接池配置
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(MySqlDemo.JDBC_DRIVER);
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", true);
        config.addDataSourceProperty("prepStmtCacheSize", 500);
        config.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        config.setConnectionTestQuery("SELECT 1");
        config.setAutoCommit(true);
        // 池中最小空闲链接数量
        config.setMinimumIdle(1);
        // 池中最大链接数量
        config.setMaximumPoolSize(10);

        HikariDataSource ds = new HikariDataSource(config);
        ds.setJdbcUrl(jdbcUrl);
        ds.setUsername(username);
        ds.setPassword(password);
        /*连接只读数据库时配置为true， 保证安全*/
        ds.setReadOnly(false);
        /*等待连接池分配连接的最大时长（毫秒），超过这个时长还没可用的连接则发生SQLException， 缺省:30秒*/
        ds.setConnectionTimeout(30-000);
        /*一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟 */
        ds.setIdleTimeout(600_000);
        /*一个连接的生命时长（毫秒），超时而且没被使用则被释放（retired），缺省:30分钟，建议设置比数据库超时时长少30秒，
         * 参考MySQL wait_timeout参数（show variables like '%timeout%';）*/
        ds.setMaxLifetime(1_800_000);
        /*接池中允许的最大连接数。缺省值：10；推荐的公式：((core_count * 2) + effective_spindle_count)*/
        ds.setMaximumPoolSize(10);
        return ds;

    }

    /**
     * 销毁连接池
     */
    public void shutdown(HikariDataSource ds) {
        ds.shutdown();
    }

}
