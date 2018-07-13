package demo.spring.cloud.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class Beans {

    /**
     * <ol>
     * <li>如果此时线程池中的数量小于corePoolSize，即使线程池中的线程都处于空闲状态，也要创建新的线程来处理被添加的任务。
     * <li>如果此时线程池中的数量等于 corePoolSize，但是缓冲队列 workQueue未满，那么任务被放入缓冲队列。
     * <li>如果此时线程池中的数量大于corePoolSize，缓冲队列workQueue满，并且线程池中的数量小于maximumPoolSize，建新的线程来处理被添加的任务。
     * <li>如果此时线程池中的数量大于corePoolSize，缓冲队列workQueue满，并且线程池中的数量等于maximumPoolSize，那么通过
     * handler所指定的策略来处理此任务。也就是：处理任务的优先级为：核心线程corePoolSize、任务队列workQueue、最大线程 maximumPoolSize，如果三者都满了，使用handler处理被拒绝的任务。
     * <li>当线程池中的线程数量大于 corePoolSize时，如果某线程空闲时间超过keepAliveTime，线程将被终止。这样，线程池可以动态的调整池中的线程数。
     * 
     * </ol>
     * 
     * @return
     */
    @Bean
    ThreadPoolTaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // corePoolSize： 线程池维护线程的最少数量
        executor.setCorePoolSize(5);
        // keepAliveSeconds 线程池维护线程所允许的空闲时间
        executor.setKeepAliveSeconds(300);
        // maxPoolSize 线程池维护线程的最大数量
        executor.setMaxPoolSize(10);
        // queueCapacity 线程池所使用的缓冲队列
        executor.setQueueCapacity(25);
        return executor;
    }
}
