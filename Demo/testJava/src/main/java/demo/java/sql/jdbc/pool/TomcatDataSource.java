package demo.java.sql.jdbc.pool;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

public class TomcatDataSource {
    
    /**
     * 获取MySQL 数据源
     * 
     * @param url
     * @param user
     * @param password
     * @return
     */
    public static DataSource getMySqlDataSource(String url, String user, String password) {
        PoolProperties p = new PoolProperties();
        p.setUrl(url);
        p.setDriverClassName("com.mysql.jdbc.Driver");
        p.setUsername(user);
        p.setPassword(password);
        p.setJmxEnabled(true);
        p.setTestWhileIdle(false);
        p.setTestOnBorrow(true);
        p.setValidationQuery("SELECT 1");
        p.setTestOnReturn(false);
        p.setValidationInterval(30_000);
        p.setTimeBetweenEvictionRunsMillis(30_000);
        p.setMaxActive(100);
        p.setInitialSize(10);
        p.setMaxWait(10_000);
        p.setRemoveAbandonedTimeout(6000);
        p.setMinEvictableIdleTimeMillis(300_000);
        p.setMinIdle(10);
        p.setLogAbandoned(true);
        p.setRemoveAbandoned(true);
        p.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
                + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        
        DataSource datasource = new DataSource();
        datasource.setPoolProperties(p);
        return datasource;
    }

}
