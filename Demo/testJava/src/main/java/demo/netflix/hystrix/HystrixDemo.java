package demo.netflix.hystrix;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixThreadPoolKey;
import com.netflix.hystrix.HystrixThreadPoolProperties;

import rx.Observable;

/**
 * <h2>HystrixCommand和HystrixObservableCommand</h2>
 * <li>HystrixObservableCommand只支持异步的调用方式，HystrixCommand同步异步都支持。
 * <li>HystrixObservableCommand支持请求合并功能，HystrixCommand不支持。
 *
 */
public class HystrixDemo {
    public static void main(String[] args) {
        observe();
    }

    /**
     * blocks, then returns the single response received from the dependency (or throws an exception in case of an
     * error)
     */
    static void execute() {
        String s = new CommandHelloWorld("Bob").execute();
        System.err.println(s);
    }

    /**
     * returns a Future with which you can obtain the single response from the dependency
     */
    static void queue() {
        Future<String> futrue = new CommandHelloWorld("Bob").queue();
        try {
            String s = futrue.get();
            System.err.println(s);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    /**
     * subscribes to the Observable that represents the response(s) from the dependency and returns an Observable that
     * replicates that source Observable
     */
    static void observe() {
        Observable<String> observable = new CommandHelloWorld("Bob").observe();
        System.err.println(observable);
    }

    /**
     * returns an Observable that, when you subscribe to it, will execute the Hystrix command and emit its responses
     */
    static void toObservable() {
        Observable<String> observable = new CommandHelloWorld("Bob").toObservable();
        System.err.println(observable);
    }
}

class CommandHelloWorld extends HystrixCommand<String> {

    private final String name;

    public CommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup")); // 必须
        this.name = name;
    }

    @Override
    protected String run() {
        /*
         * 网络调用 或者其他一些业务逻辑，可能会超时或者抛异常
         */
        return "Hello " + name + "! From " + Thread.currentThread().getName();
    }
}

class GetInfoFromSinaiCommand extends HystrixCommand<List<Object>> {

    private Object client;
    private List<Integer> poiIds;

    public GetInfoFromSinaiCommand(Object poiClient, List<Integer> poiIds) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("sinai"))
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
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter().withCoreSize(-1))

        );

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
