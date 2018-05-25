package demo.spring.cloud.zuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * <h1>Zuul简介</h1><br>
 * Zuul的主要功能是路由转发和过滤器。路由功能是微服务的一部分，比如／api/user转发到到user服务，/api/shop转发到到shop服务。zuul默认和Ribbon结合实现了负载均衡的功能。
 * 
 * zuul有以下功能：
 * <li>Authentication
 * <li>Insights
 * <li>Stress Testing
 * <li>Canary Testing
 * <li>Dynamic Routing
 * <li>Service Migration
 * <li>Load Shedding
 * <li>Security
 * <li>Static Response handling
 * <li>Active/Active traffic management
 *
 */
@EnableZuulProxy // 开启zuul的功能
@EnableEurekaClient
@SpringBootApplication
public class ZuulDemo {
    public static void main(String[] args) {
        SpringApplication.run(ZuulDemo.class, args);
    }
}
