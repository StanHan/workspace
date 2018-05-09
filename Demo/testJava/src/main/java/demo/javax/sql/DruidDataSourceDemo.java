package demo.javax.sql;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;

import demo.db.mysql.MySqlDemo;

/**
 * DRUID是阿里巴巴开源平台上一个数据库连接池实现，它结合了C3P0、DBCP、PROXOOL等DB池的优点，同时加入了日志监控，可以很好的监控DB池连接和SQL的执行情况，
 * 可以说是针对监控而生的DB连接池(据说是目前最好的连接池,不知道速度有没有BoneCP快)。
 * 
 *
 */
public class DruidDataSourceDemo {

    public static void main(String[] args) throws SQLException {
        DruidDataSource druidDataSource = initDruidDataSource("", "", "");

        druidDataSource.getActiveCount();
        druidDataSource.getMaxActive();
        druidDataSource.close();
        
        druidDataSource.getConnection();
    }
    
    /**
     * 关闭数据源
     * @param druidDataSource
     */
    public static void closeDruidDataSource(DruidDataSource druidDataSource) {
        druidDataSource.close();
    }

    /**
     * 初始化数据源
     * @param jdbcUrl
     * @param username
     * @param password
     * @return
     * @throws SQLException
     */
    public static DruidDataSource initDruidDataSource(String jdbcUrl, String username, String password)
            throws SQLException {
        DruidDataSource ds = new DruidDataSource();
        /* 配置这个属性的意义在于，如果存在多个数据源，监控的时候可以通过名字来区分开来。 */
        ds.setName("DruidDS1");
        ds.setDriverClassName(MySqlDemo.JDBC_DRIVER);
        ds.setUrl(jdbcUrl);
        ds.setUsername(username);
        ds.setPassword(password);
        /* 配置初始化大小、最小、最大 */
        /* 初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时 */
        ds.setInitialSize(1);
        /* 最小连接池数量 */
        ds.setMinIdle(1);
        /* 最大连接池数量 */
        ds.setMaxActive(10);
        /* 获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。 */
        ds.setMaxWait(20000);
        ds.setTimeBetweenEvictionRunsMillis(10000);
        /*
         * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒. 有两个含义： 1) Destroy线程会检测连接的间隔时间2) testWhileIdle的判断依据，详细看testWhileIdle属性的说明
         */
        ds.setTimeBetweenEvictionRunsMillis(60000);
        /* 配置一个连接在池中最小生存的时间，单位是毫秒 */
        ds.setMinEvictableIdleTimeMillis(300000);
        ds.setTestWhileIdle(true);
        /* 这里建议配置为TRUE，防止取到的连接不可用 */
        ds.setTestOnBorrow(true);
        ds.setTestOnReturn(false);
        /* 物理连接初始化的时候执行的sql */
        ds.setConnectionInitSqls(null);
        /* 打开PSCache，并且指定每个连接上PSCache的大小.PSCache对支持游标的数据库性能提升巨大，比如说oracle。在mysql下建议关闭。 */
        ds.setPoolPreparedStatements(true);
        /*
         * 要启用PSCache，必须配置大于0，当大于0时，poolPreparedStatements自动触发修改为true。
         * 在Druid中，不会存在Oracle下PSCache占用内存过多的问题，可以把这个数值配置大一些，比如说100
         */
        ds.setMaxPoolPreparedStatementPerConnectionSize(20);
        /* 这里配置提交方式，默认就是TRUE，可以不用配置 */
        ds.setDefaultAutoCommit(true);
        /* 验证连接有效与否的SQL，不同的数据配置不同 .如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会其作用。 */
        ds.setValidationQuery("select 1");
        /*
         * 属性类型是字符串，通过别名的方式配置扩展插件，常用的插件有： 监控统计用的filter:stat;日志用的filter:log4j;防御sql注入的filter:wall
         */
        ds.setFilters("stat");
        List<Filter> filters = Arrays.asList();
        /* 类型是List<com.alibaba.druid.filter.Filter>，如果同时配置了filters和proxyFilters，是组合关系，并非替换关系 */
        ds.setProxyFilters(filters);
        return ds;
    }

}
