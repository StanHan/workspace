package demo.netflix.hystrix.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

public class GetInfoFromSinaiCommand extends HystrixCommand<List<Object>> {

    private Object client;
    private List<Integer> poiIds;

    private static final Setter cachedSetter = Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("sinai"))
            // command配置
            .andCommandKey(HystrixCommandKey.Factory.asKey("GetInfoFromSinaiCommand"))
            .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                    // 信号隔离或线程隔离，默认:采用线程隔离
                    .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.THREAD)
                    // 隔离时间，即多长时间后进行重试
                    .withExecutionTimeoutInMilliseconds(1))
            .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withRequestCacheEnabled(true))
            // 融断器配置
            .andCommandPropertiesDefaults(HystrixCommandProperties.Setter().withCircuitBreakerEnabled(true))
            .andCommandPropertiesDefaults(
                    HystrixCommandProperties.Setter().withCircuitBreakerRequestVolumeThreshold(20))
            .andCommandPropertiesDefaults(
                    HystrixCommandProperties.Setter().withCircuitBreakerSleepWindowInMilliseconds(5000))
            .andCommandPropertiesDefaults(
                    HystrixCommandProperties.Setter().withCircuitBreakerErrorThresholdPercentage(50))
            // ThreadPool配置
            .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GetInfoFromSinaiCommand"))
            .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(10))
            .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(-1));

    public GetInfoFromSinaiCommand(Object poiClient, List<Integer> poiIds) {
        super(cachedSetter);

        this.client = poiClient;
        this.poiIds = poiIds;
    }

    @Override
    public List<Object> run() throws Exception {
        if (poiIds.isEmpty()) {
            return Collections.emptyList();
        }
        // 请求远程服务
        Object tmp = client.getClass();
        return Arrays.asList(tmp);
    }

    @Override
    protected String getCacheKey() {
        return String.valueOf(poiIds);
    }

    @Override
    protected List<Object> getFallback() {
        return Collections.emptyList();
    }
}