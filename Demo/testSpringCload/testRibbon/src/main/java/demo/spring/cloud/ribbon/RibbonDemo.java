package demo.spring.cloud.ribbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * <h1>负载均衡</h1><br>
 * 实现负载均衡，我们可以通过服务器端和客户端做负载均衡。服务器端做负载均衡，比如可以使用Nginx。而客户端做负载均衡，就是客户端有一个组件，知道有哪些可用的微服务，实现一个负载均衡的算法。
 *
 * <h2>Ribbon工作流程主要分为两步：</h2>
 * <li>第一：先选择Eureka Server，优先选择在同一个Zone且负载较少的Server；
 * <li>第二：再根据用户指定的策略，再从server取到的服务注册列表中选择一个地址。其中Ribbon提供了很多种策略，例如轮询round bin，随机Random，根据响应时间加权。
 * 
 * <h1>断路器</h1><br>
 * 服务与服务之间的依赖性，故障会传播，会对整个微服务系统造成灾难性的严重后果，这就是服务故障的“雪崩”效应。
 * 
 * Netflix开源了Hystrix组件，实现了断路器模式，SpringCloud对这一组件进行了整合。
 * 
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix // 开启Hystrix
@EnableHystrixDashboard // 开启hystrixDashboard，通过 http://localhost:8771/hystrix 访问
public class RibbonDemo {

    public static void main(String[] args) {
        SpringApplication.run(RibbonDemo.class, args);
    }

    /**
     * 加上注解@LoadBalanced，则负载均衡生效
     * 
     * @return
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
