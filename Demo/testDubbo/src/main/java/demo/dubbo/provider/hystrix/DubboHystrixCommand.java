package demo.dubbo.provider.hystrix;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.dubbo.rpc.Invocation;
import com.alibaba.dubbo.rpc.Invoker;
import com.alibaba.dubbo.rpc.Result;
import com.alibaba.dubbo.rpc.RpcResult;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixCommandProperties.ExecutionIsolationStrategy;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

public class DubboHystrixCommand extends HystrixCommand<Result> { // 统计一定时间内成功请求数

    private static final int STATUSTIME = 20000; // 用于计算百分比的滚动窗口时间长度(毫秒)

    private static final int ROLLINGTIME = 60000;
    private Invoker<?> invoker;
    private Invocation invocation;

    public DubboHystrixCommand(Invoker<?> invoker, Invocation invocation, String group) { // 使用dubbo配置的优先级 method >
                                                                                          // interface > application
                                                                                          // 同等级别 consumer > provider

        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("DubboHystrixCommand"))// 组名使用模块名称
                // CommandKey：依赖名称，依赖隔离的根本就是对相同CommandKey的依赖做隔离
                .andCommandKey(HystrixCommandKey.Factory.asKey("DubboHystrixCommand"))
                // 配置线程池隔离
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("DH_"))
                // Command 配置
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        // 设置隔离方式为线程隔离（默认也是采用线程隔离）
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                        // 是否启用超时配置。默认true
                        .withExecutionTimeoutEnabled(true)
                        // 配置超时时间
                        .withExecutionTimeoutInMilliseconds(18000)
                        // 当执行线程执行超时时，是否进行中断处理，默认为true
                        .withExecutionIsolationThreadInterruptOnTimeout(true))
                // 线程池配置
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        // 线程池大小，默认值10个。
                        .withCoreSize(20)
                        // 最大队列长度，默认-1。如果设置为-1，使用的是SynchronousQueue，否则正数将会使用LinkedBlockingQueue
                        .withMaxQueueSize(-1)));

        this.invoker = invoker;
        this.invocation = invocation;
    }

    @Override
    protected Result run() throws Exception {
        return invoker.invoke(invocation);
    }

    @Override
    protected Result getFallback() {
        if (executionResult.isResponseSemaphoreRejected()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", 0);
            map.put("msg", "ok");

            Result result = new RpcResult(map);
            return result;

        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("code", -1);
        map.put("msg", "system error.");
        Result result = new RpcResult(map);
        return result;

    }

}
