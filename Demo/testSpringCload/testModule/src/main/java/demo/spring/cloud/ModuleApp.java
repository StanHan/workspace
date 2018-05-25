package demo.spring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * <p>
 * 当client向server注册时，它会提供一些元数据，例如主机和端口，URL，主页等。Eureka server 从每个client实例接收心跳消息。 如果心跳超时，则通常将该实例从注册server中删除。
 * <p>
 * 在微服务架构中，业务都会被拆分成一个独立的服务，服务与服务的通讯是基于http restful的。Spring cloud有两种服务调用方式，一种是ribbon+restTemplate，另一种是feign。
 * <p>
 * Feign默认集成了ribbon。ribbon 已经默认实现了这些配置bean：
 * <li>IClientConfig ribbonClientConfig: DefaultClientConfigImpl
 * <li>IRule ribbonRule: ZoneAvoidanceRule
 * <li>IPing ribbonPing: NoOpPing
 * <li>ServerList ribbonServerList: ConfigurationBasedServerList
 * <li>ServerListFilter ribbonServerListFilter: ZonePreferenceServerListFilter
 * <li>ILoadBalancer ribbonLoadBalancer: ZoneAwareLoadBalancer
 */
@EnableEurekaClient
@SpringBootApplication
public class ModuleApp {

    public static void main(String[] args) {
        SpringApplication.run(ModuleApp.class, args);
    }
}
