package demo.spring.cloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * 在微服务架构中，需要几个基础的服务治理组件，包括服务注册与发现、服务消费、负载均衡、断路器、智能路由、配置管理等，由这几个基础组件相互协作，共同组建了一个简单的微服务系统。
 * 
 * 在Spring Cloud微服务系统中，一种常见的负载均衡方式是，客户端的请求首先经过负载均衡（zuul、Ngnix），再到达服务网关（zuul集群），然后再到具体的服务。
 * 服务统一注册到高可用的服务注册中心集群，服务的所有的配置文件由配置服务管理，配置服务的配置文件放在git仓库，方便开发人员随时改配置。
 * 
 * <h1>Spring Cloud</h1> <br>
 * Spring Cloud和云计算没有关系，只是一个基于Spring Boot的快速构建分布式系统的工具集。 Spring Cloud特点: # 约定优于配置#
 * 开箱即用，快速启动#适用于各种环境，可以部署在PC、server或者云环境# 轻量级的组件# 组件的支持很丰富，功能齐全# 选型中立
 * <p>
 * spring cloud 为开发人员提供了快速构建分布式系统的一些工具，包括
 * <li>配置管理、
 * <li>服务发现、
 * <li>断路器、
 * <li>路由、
 * <li>微代理、
 * <li>事件总线、
 * <li>全局锁、
 * <li>决策竞选、
 * <li>分布式会话等等。
 * 
 * <h1>服务发现和注册</h1> <br>
 * 为什么需要服务注册与发现？
 * <li>服务重启或者升级后IP地址变化
 * <li>水平伸缩后服务实例的变化
 * <li>同一个节点运行多个服务
 * <p>
 * 所以需要一种注册机制，帮助我们去获取响应。 <br>
 * 核心机制：
 * <li>将实例的信息注册到注册中心
 * <li>调用者通过注册中心查找服务
 * <li>调用者获取服务实例列表
 * <li>调用者通过负载均衡通信
 * <p>
 * <h2>基本流程</h2> <br>
 * <li>首先：服务消费者和服务注册者向服务发现组件注册
 * <li>其次：服务消费者要调用的时候会从服务发现组件中进行查询
 * <p>
 * 需求：
 * <li>每一个服务实例都会在启动的时候通过HTTP/REST或者Thrift等方式发布远程API
 * <li>服务端实例的具体数量及位置会发生动态变化
 * <li>虚拟机与容器通常会被分配动态IP地址
 * 
 * <h2>服务发现组件的功能</h2> <br>
 * <li>服务注册表： 是一个记录当前可用服务实例的网络信息的数据库，是服务发现机制的核心。服务注册表提供查询API和管理API,使用API 获得可用的服务实例，使用管理API实现注册和注销。
 * <li>服务注册
 * <li>健康检查
 * 
 * <h2>服务发现的方式</h2> <br>
 * <li>客户端发现：它的主要特点是客户端决定服务实例的网络位置，并且对请求进行负载均衡。客户端查询服务注册表(可用服务实例数据库)，使用负载均衡算法选择一个实例，并发出请求。典型代表Eureka或者ZooKeeper
 * <li>服务器端发现：向某一服务发送请求，客户端会通过向运行位置已知的路由器或者负载均衡器发送请求。他们会查询服务注册表，并向可用的服务实例转发该请求。典型代表Consul + Nginx
 * <p>
 * <b>客户端发现模式的优缺点</b>
 * <li>优点：不需要很多的网络跳转
 * <li>缺点：客户端和服务注册表耦合，需要为应用程序每一种编程语言、框架等建立客户端发现逻辑，比如 Netflix Prana就为非JVM客户端提供一套基于HTTP代理服务发现方案
 * <p>
 * <b>服务器端发现模式优缺点：</b>
 * <li>优点：客户端无需实现发现功能，只需要向路由器或者负载均衡器发送请求即可
 * <li>缺点：除非成为云环境的一部分，否则该路由机制必须作为另一系统组件进行安装与配置。为实现可用性和一定的接入能力，还需要为其配置一定数量的副本。
 * <p>
 * 相较于客户端发现，服务器端发现机制需要更多的网络跳转。
 * 
 * <h2>服务发现组件Eureka</h2> <br>
 * Eureka是Netflix开发的服务发现框架，本身是一个基于REST的服务，主要用于定位运行在AWS域中的中间层服务，以达到负载均衡和中间层服务故障转移的目的。 Spring
 * Cloud将它集成在其他子项目spring-cloud-netflix中，以实现spring cloud服务发现功能。
 * <h3>Eureka原理</h3> 先需要明白AWS几个概念:
 * <li>Region: AWS云服务在全球不同的地方都有数据中心，比如北美、南美和欧洲亚洲等。与此对应，根据地理位置我们把某个地区的基础设施服务集合称为一个区域。不同区域之间是相互独立的。说白了就类似于不同地方的机房。
 * <li>Available Zone: 基于容灾背景提出，简单而言，就是相同region区域不同的机房
 * <p>
 * <li>首先是服务注册到Eureka
 * <li>每30s发送心跳检测重新进行租约，如果客户端不能多次更新租约，它将在90s内从服务器注册中心移除。
 * <li>注册信息和更新会被复制到其他Eureka 节点，来自任何区域的客户端科可以查找到注册中心信息，每30s发生一次复制来定位他们的服务，并进行远程调用
 * <li>客户端还可以缓存一些服务实例信息，所以即使Eureka全挂掉，客户端也是可以定位到服务地址的
 * <p>
 * 
 * <h3>搭建Euraka Server</h3> 启动一个服务注册中心，只需要一个注解@EnableEurekaServer，这个注解需要在springboot工程的启动application类上加。
 * eureka是一个高可用的组件，它没有后端缓存，每一个实例注册之后需要向注册中心发送心跳（因此可以在内存中完成），在默认情况下erureka server也是一个eureka client,必须要指定一个 server。
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaDemo {

    public static void main(String[] args) {
        SpringApplication.run(EurekaDemo.class, args);
    }
}
