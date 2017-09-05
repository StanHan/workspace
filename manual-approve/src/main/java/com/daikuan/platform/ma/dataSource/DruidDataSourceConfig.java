/*
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.dataSource;

import java.util.Properties;

import javax.sql.DataSource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.github.abel533.mapperhelper.MapperInterceptor;

/**
 * 
 * @author taosj
 * @version DruidDataSourceConfig.java, v0.1 2017年3月7日 上午9:27:52
 */
@Configuration
@MapperScan("com.daikuan.platform.ma.dao")
public class DruidDataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource() {
        DataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }

    /**
     * added by yangqb at 2017/3/14
     * 
     * @return
     */
    @Bean
    public MapperInterceptor getCommonMapperInterceptor() {
        MapperInterceptor mapperInterceptor = new MapperInterceptor();
        Properties properties = new Properties();
        properties.setProperty("IDENTITY", "MYSQL");
        properties.setProperty("mappers", "com.github.abel533.mapper.Mapper");
        mapperInterceptor.setProperties(properties);
        return mapperInterceptor;
    }
}
