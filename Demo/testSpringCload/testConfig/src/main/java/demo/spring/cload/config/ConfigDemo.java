package demo.spring.cload.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * <h1>分布式配置中心</h1> <br>
 * 在分布式系统中，由于服务数量巨多，为了方便服务配置文件统一管理，实时更新，所以需要分布式配置中心组件。在Spring Cloud中，有分布式配置中心组件spring cloud config
 * ，它支持配置服务放在配置服务的内存中（即本地），也支持放在远程Git仓库中。在spring cloud config 组件中，分两个角色，一是config server，二是config client。
 *
 * <h2>http请求地址和资源文件映射如下:</h2>
 * <li>/{application}/{profile}[/{label}]
 * <li>/{application}-{profile}.yml
 * <li>/{label}/{application}-{profile}.yml
 * <li>/{application}-{profile}.properties
 * <li>/{label}/{application}-{profile}.properties
 */
@SpringBootApplication
@EnableConfigServer
public class ConfigDemo {
    public static void main(String[] args) {
        SpringApplication.run(ConfigDemo.class, args);
    }
}
