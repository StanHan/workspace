package demo.netflix.hystrix;

import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixEventType;
import com.netflix.hystrix.HystrixInvokableInfo;
import com.netflix.hystrix.HystrixRequestLog;
import com.netflix.hystrix.HystrixThreadPool;
import com.netflix.hystrix.HystrixThreadPoolProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

import demo.vo.pojo.User;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;

import com.netflix.hystrix.contrib.javanica.conf.HystrixPropertiesManager;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * <h1>配置信息（default或HystrixCommandKey）最常用的几项</h1>
 * 
 * <h2>超时时间（默认1000ms，单位：ms）</h2>
 * <li>（1）hystrix.command.default.execution.isolation.thread.timeoutInMilliseconds
 * 在调用方配置，被该调用方的所有方法的超时时间都是该值，优先级低于下边的指定配置
 * <li>（2）hystrix.command.HystrixCommandKey.execution.isolation.thread.timeoutInMilliseconds
 * 在调用方配置，被该调用方的指定方法（HystrixCommandKey方法名）的超时时间是该值
 * 
 * <h2>线程池核心线程数</h2>
 * <li>hystrix.threadpool.default.coreSize（默认为10）
 * 
 * <h2>Queue</h2>
 * <li>（1）hystrix.threadpool.default.maxQueueSize（最大排队长度。默认-1，使用SynchronousQueue。其他值则使用
 * LinkedBlockingQueue。如果要从-1换成其他值则需重启，即该值不能动态调整，若要动态调整，需要使用到下边这个配置）
 * <li>（2）hystrix.threadpool.default.queueSizeRejectionThreshold（排队线程数量阈值，默认为5，达到时拒绝，如果配置了该选项，队列的大小是该队列）
 * 注意：如果maxQueueSize=-1的话，则该选项不起作用
 * 
 * <h2>断路器</h2>
 * <li>（1）hystrix.command.default.circuitBreaker.requestVolumeThreshold（当在配置时间窗口内达到此数量的失败后，进行短路。默认20个） For example, if
 * the value is 20, then if only 19 requests are received in the rolling window (say a window of 10 seconds) the circuit
 * will not trip open even if all 19 failed. 简言之，10s内请求失败数量达到20个，断路器开。
 * <li>（2）hystrix.command.default.circuitBreaker.sleepWindowInMilliseconds（短路多久以后开始尝试是否恢复，默认5s）
 * <li>（3）hystrix.command.default.circuitBreaker.errorThresholdPercentage（出错百分比阈值，当达到此阈值后，开始短路。默认50%）
 * 
 * <h2>fallback</h2>
 * <li>hystrix.command.default.fallback.isolation.semaphore.maxConcurrentRequests
 * （调用线程允许请求HystrixCommand.GetFallback()的最大数量，默认10。超出时将会有异常抛出，注意：该项配置对于THREAD隔离模式也起作用）
 * 
 * 
 * <h1>属性配置参数</h1>https://github.com/Netflix/Hystrix/wiki/Configuration
 * <h2>Command Properties</h2>以下属性控制HystrixCommand行为：
 * <h3>1、Execution</h3>以下属性控制HystrixCommand.run()如何执行。
 * <li>execution.isolation.strategy ：隔离策略，有THREAD和SEMAPHORE。 THREAD - 它在单独的线程上执行，并发请求受线程池中的线程数量的限制； SEMAPHORE
 * -它在调用线程上执行，并发请求受到信号量计数的限制。
 * 默认使用THREAD模式，以下几种场景可以使用SEMAPHORE模式：1、只想控制并发度；2、外部的方法已经做了线程隔离，调用的是本地方法或者可靠度非常高、耗时特别小的方法（如medis）
 * <li>execution.isolation.thread.timeoutInMilliseconds : 超时时间 ,默认值：1000。
 * 在THREAD模式下，达到超时时间，可以中断；在SEMAPHORE模式下，会等待执行完成后，再去判断是否超时。 设置标准：1、 有retry，99meantime + avg meantime 2、 没有retry，99.5
 * meantime
 * <li>execution.timeout.enabled ： HystrixCommand.run（）执行是否应该有超时。 默认值：true
 * <li>execution.isolation.thread.interruptOnTimeout ： 在发生超时时是否应中断HystrixCommand.run（）执行。 默认值：true THREAD模式有效
 * <li>execution.isolation.thread.interruptOnCancel ： 当发生取消时，执行是否应该中断。 默认值为false THREAD模式有效
 * <li>execution.isolation.semaphore.maxConcurrentRequests ： 设置在使用时允许到HystrixCommand.run（）方法的最大请求数。 默认值：10 SEMAPHORE模式有效
 * 
 * <h3>Fallback</h3>以下属性控制HystrixCommand.getFallback()如何执行。这些属性适用于ExecutionIsolationStrategy.THREAD和ExecutionIsolationStrategy.SEMAPHORE。
 * <li>fallback.isolation.semaphore.maxConcurrentRequests：
 * 设置从调用线程允许HystrixCommand.getFallback（）方法的最大请求数。SEMAPHORE模式有效，默认值：10
 * <li>fallback.enabled： 确定在发生失败或拒绝时是否尝试调用HystrixCommand.getFallback（）。默认值为true
 * 
 * <h3>Circuit Breaker</h3>断路器属性控制HystrixCircuitBreaker的行为。
 * <li>circuitBreaker.enabled ：确定断路器是否用于跟踪运行状况和短路请求（如果跳闸）。默认值为true
 * <li>circuitBreaker.requestVolumeThreshold ：熔断触发的最小个数/10s。默认值：20
 * <li>circuitBreaker.sleepWindowInMilliseconds ：熔断多少秒后去尝试请求。默认值：5000
 * <li>circuitBreaker.errorThresholdPercentage ：失败率达到多少百分比后熔断。默认值：50，主要根据依赖重要性进行调整
 * <li>circuitBreaker.forceOpen ：属性如果为真，强制断路器进入打开（跳闸）状态，其中它将拒绝所有请求。默认值为false，此属性优先于circuitBreaker.forceClosed
 * <li>circuitBreaker.forceClosed：该属性如果为真，则迫使断路器进入闭合状态，其中它将允许请求，而不考虑误差百分比。
 * 默认值为false，如果是强依赖，应该设置为true，circuitBreaker.forceOpen属性优先，因此如果forceOpen设置为true，此属性不执行任何操作。
 * 
 * <h3>Metrics</h3>以下属性与从HystrixCommand和HystrixObservableCommand执行捕获指标有关。
 * <li>metrics.rollingStats.timeInMilliseconds：此属性设置统计滚动窗口的持续时间（以毫秒为单位）。对于断路器的使用和发布Hystrix保持多长时间的指标。默认值：10000
 * <li>metrics.rollingStats.numBuckets：此属性设置rollingstatistical窗口划分的桶数。 以下必须为true -
 * “metrics.rollingStats.timeInMilliseconds%metrics.rollingStats.numBuckets == 0” -否则将抛出异常。默认值：10
 * <li>metrics.rollingPercentile.enabled：此属性指示是否应以百分位数跟踪和计算执行延迟。 如果禁用它们，则所有摘要统计信息（平均值，百分位数）都将返回-1。默认值为true
 * <li>metrics.rollingPercentile.timeInMilliseconds：此属性设置滚动窗口的持续时间，其中保留执行时间以允许百分位数计算，以毫秒为单位。默认值：60000
 * <li>metrics.rollingPercentile.numBuckets：此属性设置rollingPercentile窗口将划分的桶的数量。 以下内容必须为true -
 * “metrics.rollingPercentile.timeInMilliseconds%metrics.rollingPercentile.numBuckets == 0” -否则将抛出异常。默认值：6
 * <li>metrics.rollingPercentile.bucketSize：此属性设置每个存储桶保留的最大执行次数。如果在这段时间内发生更多的执行，它们将绕回并开始在桶的开始处重写。默认值：100
 * <li>metrics.healthSnapshot.intervalInMilliseconds：此属性设置在允许计算成功和错误百分比并影响断路器状态的健康快照之间等待的时间（以毫秒为单位）。默认值：500
 * 
 * <h3>Request Context</h3>这些属性涉及HystrixCommand使用的HystrixRequestContext功能。
 * <li>requestCache.enabled：HystrixCommand.getCacheKey（）是否应与HystrixRequestCache一起使用，以通过请求范围的缓存提供重复数据删除功能。 默认值为true
 * <li>requestLog.enabled： HystrixCommand执行和事件是否应记录到HystrixRequestLog。 默认值为true
 * 
 * <h2>Collapser Properties</h2>下列属性控制HystrixCollapser行为。
 * <li>maxRequestsInBatch：此属性设置在触发批处理执行之前批处理中允许的最大请求数。默认值：Integer.MAX_VALUE
 * <li>timerDelayInMilliseconds： 此属性设置创建批处理后触发其执行的毫秒数。 默认值：10
 * <li>requestCache.enabled： 此属性指示是否为HystrixCollapser.execute（）和HystrixCollapser.queue（）调用启用请求高速缓存。 默认值：true
 * 
 * <h2>ThreadPool Properties</h2>以下属性控制Hystrix命令在其上执行的线程池的行为。 大多数时候，默认值为10的线程会很好（通常可以做得更小）。
 * <li>coreSize：线程池coreSize，默认值：10，设置标准：qps*99meantime + breathing room
 * <li>maximumSize：此属性设置最大线程池大小。 这是在不开始拒绝HystrixCommands的情况下可以支持的最大并发数。
 * 请注意，此设置仅在您还设置allowMaximumSizeToDivergeFromCoreSize时才会生效。默认值：10
 * <li>maxQueueSize：请求等待队列。默认值：-1，如果使用正数，队列将从SynchronizeQueue改为LinkedBlockingQueue
 * <li>queueSizeRejectionThreshold：此属性设置队列大小拒绝阈值 - 即使未达到maxQueueSize也将发生拒绝的人为最大队列大小。
 * 此属性存在，因为BlockingQueue的maxQueueSize不能动态更改，我们希望允许您动态更改影响拒绝的队列大小。默认值：5，注意：如果maxQueueSize == -1，则此属性不适用。
 * <li>keepAliveTimeMinutes：此属性设置保持活动时间，以分钟为单位。默认值：1
 * <li>allowMaximumSizeToDivergeFromCoreSize：此属性允许maximumSize的配置生效。 那么该值可以等于或高于coreSize。 设置coreSize
 * <maximumSize会创建一个线程池，该线程池可以支持maximumSize并发，但在相对不活动期间将向系统返回线程。 （以keepAliveTimeInMinutes为准）。默认值：false
 * <li>metrics.rollingStats.timeInMilliseconds：此属性设置statistical rolling窗口的持续时间（以毫秒为单位）。 这是为线程池保留多长时间。默认值：10000
 * <li>metrics.rollingStats.numBuckets：此属性设置滚动统计窗口划分的桶数。 注意：以下必须为true -
 * “metrics.rollingStats.timeInMilliseconds%metrics.rollingStats.numBuckets == 0” -否则将引发异常。默认值：10
 * 
 * <h2>其他</h2>
 * <li>groupKey： 表示所属的group，一个group共用线程池 默认值：getClass().getSimpleName();
 * <li>commandKey： 默认值：当前执行方法名
 */
public abstract class BasicCommandPropertiesTest extends BasicHystrixTest {

    private UserService userService;

    protected abstract UserService createUserService();

    @Before
    public void setUp() throws Exception {
        userService = createUserService();
        super.setUp();
    }

    @Test
    public void testGetUser() throws NoSuchFieldException, IllegalAccessException {
        User u1 = userService.getUser(1, "name: ");
        assertEquals("name: 1", u1.getName());
        assertEquals(1, HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().size());
        HystrixInvokableInfo<?> command = HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().iterator()
                .next();
        assertEquals("GetUserCommand", command.getCommandKey().name());
        assertEquals("UserGroupKey", command.getCommandGroup().name());
        assertEquals("Test", command.getThreadPoolKey().name());
        assertTrue(command.getExecutionEvents().contains(HystrixEventType.SUCCESS));
        // assert properties
        assertEquals(110, command.getProperties().executionTimeoutInMilliseconds().get().intValue());
        assertEquals(false, command.getProperties().executionIsolationThreadInterruptOnTimeout().get());

        Field field = command.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("threadPool");
        field.setAccessible(true);
        HystrixThreadPool threadPool = (HystrixThreadPool) field.get(command);

        Field field2 = HystrixThreadPool.HystrixThreadPoolDefault.class.getDeclaredField("properties");
        field2.setAccessible(true);
        HystrixThreadPoolProperties properties = (HystrixThreadPoolProperties) field2.get(threadPool);

        assertEquals(30, (int) properties.coreSize().get());
        assertEquals(101, (int) properties.maxQueueSize().get());
        assertEquals(2, (int) properties.keepAliveTimeMinutes().get());
        assertEquals(15, (int) properties.queueSizeRejectionThreshold().get());
        assertEquals(1440, (int) properties.metricsRollingStatisticalWindowInMilliseconds().get());
        assertEquals(12, (int) properties.metricsRollingStatisticalWindowBuckets().get());
    }

    @Test
    public void testGetUserDefaultPropertiesValues() {
        User u1 = userService.getUserDefProperties(1, "name: ");
        assertEquals("name: 1", u1.getName());
        assertEquals(1, HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().size());
        HystrixInvokableInfo<?> command = HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().iterator()
                .next();
        assertEquals("getUserDefProperties", command.getCommandKey().name());
        assertEquals("UserService", command.getCommandGroup().name());
        assertEquals("UserService", command.getThreadPoolKey().name());
        assertTrue(command.getExecutionEvents().contains(HystrixEventType.SUCCESS));
    }

    @Test
    public void testGetUserDefGroupKeyWithSpecificThreadPoolKey() {
        User u1 = userService.getUserDefGroupKeyWithSpecificThreadPoolKey(1, "name: ");
        assertEquals("name: 1", u1.getName());
        assertEquals(1, HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().size());
        HystrixInvokableInfo<?> command = HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().iterator()
                .next();
        assertEquals("getUserDefGroupKeyWithSpecificThreadPoolKey", command.getCommandKey().name());
        assertEquals("UserService", command.getCommandGroup().name());
        assertEquals("CustomThreadPool", command.getThreadPoolKey().name());
        assertTrue(command.getExecutionEvents().contains(HystrixEventType.SUCCESS));
    }

    @Test
    public void testHystrixCommandProperties() {
        User u1 = userService.getUsingAllCommandProperties(1, "name: ");
        assertEquals("name: 1", u1.getName());
        assertEquals(1, HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().size());
        HystrixInvokableInfo<?> command = HystrixRequestLog.getCurrentRequest().getAllExecutedCommands().iterator()
                .next();
        assertTrue(command.getExecutionEvents().contains(HystrixEventType.SUCCESS));
        // assert properties
        assertEquals(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE,
                command.getProperties().executionIsolationStrategy().get());
        assertEquals(500, command.getProperties().executionTimeoutInMilliseconds().get().intValue());
        assertEquals(true, command.getProperties().executionTimeoutEnabled().get().booleanValue());
        assertEquals(false, command.getProperties().executionIsolationThreadInterruptOnTimeout().get().booleanValue());
        assertEquals(10, command.getProperties().executionIsolationSemaphoreMaxConcurrentRequests().get().intValue());
        assertEquals(15, command.getProperties().fallbackIsolationSemaphoreMaxConcurrentRequests().get().intValue());
        assertEquals(false, command.getProperties().fallbackEnabled().get().booleanValue());
        assertEquals(false, command.getProperties().circuitBreakerEnabled().get().booleanValue());
        assertEquals(30, command.getProperties().circuitBreakerRequestVolumeThreshold().get().intValue());
        assertEquals(250, command.getProperties().circuitBreakerSleepWindowInMilliseconds().get().intValue());
        assertEquals(60, command.getProperties().circuitBreakerErrorThresholdPercentage().get().intValue());
        assertEquals(false, command.getProperties().circuitBreakerForceOpen().get().booleanValue());
        assertEquals(true, command.getProperties().circuitBreakerForceClosed().get().booleanValue());
        assertEquals(false, command.getProperties().metricsRollingPercentileEnabled().get().booleanValue());
        assertEquals(400, command.getProperties().metricsRollingPercentileWindowInMilliseconds().get().intValue());
        assertEquals(5, command.getProperties().metricsRollingPercentileWindowBuckets().get().intValue());
        assertEquals(6, command.getProperties().metricsRollingPercentileBucketSize().get().intValue());
        assertEquals(10, command.getProperties().metricsRollingStatisticalWindowBuckets().get().intValue());
        assertEquals(500, command.getProperties().metricsRollingStatisticalWindowInMilliseconds().get().intValue());
        assertEquals(312, command.getProperties().metricsHealthSnapshotIntervalInMilliseconds().get().intValue());
        assertEquals(false, command.getProperties().requestCacheEnabled().get().booleanValue());
        assertEquals(true, command.getProperties().requestLogEnabled().get().booleanValue());
    }

    public static class UserService {

        @HystrixCommand(commandKey = "GetUserCommand", groupKey = "UserGroupKey", threadPoolKey = "Test", commandProperties = {
                @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, value = "110"),
                @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_INTERRUPT_ON_TIMEOUT, value = "false") }, threadPoolProperties = {
                        @HystrixProperty(name = HystrixPropertiesManager.CORE_SIZE, value = "30"),
                        @HystrixProperty(name = HystrixPropertiesManager.MAX_QUEUE_SIZE, value = "101"),
                        @HystrixProperty(name = HystrixPropertiesManager.KEEP_ALIVE_TIME_MINUTES, value = "2"),
                        @HystrixProperty(name = HystrixPropertiesManager.METRICS_ROLLING_STATS_NUM_BUCKETS, value = "12"),
                        @HystrixProperty(name = HystrixPropertiesManager.QUEUE_SIZE_REJECTION_THRESHOLD, value = "15"),
                        @HystrixProperty(name = HystrixPropertiesManager.METRICS_ROLLING_STATS_TIME_IN_MILLISECONDS, value = "1440") })
        public User getUser(Integer id, String name) {
            return new User(id, name + id); // it should be network call
        }

        @HystrixCommand
        public User getUserDefProperties(Integer id, String name) {
            return new User(id, name + id); // it should be network call
        }

        @HystrixCommand(threadPoolKey = "CustomThreadPool")
        public User getUserDefGroupKeyWithSpecificThreadPoolKey(Integer id, String name) {
            return new User(id, name + id); // it should be network call
        }

        @HystrixCommand(commandProperties = {
                @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_STRATEGY, value = "SEMAPHORE"),
                @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_TIMEOUT_IN_MILLISECONDS, value = "500"),
                @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_TIMEOUT_ENABLED, value = "true"),
                @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_THREAD_INTERRUPT_ON_TIMEOUT, value = "false"),
                @HystrixProperty(name = HystrixPropertiesManager.EXECUTION_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS, value = "10"),
                @HystrixProperty(name = HystrixPropertiesManager.FALLBACK_ISOLATION_SEMAPHORE_MAX_CONCURRENT_REQUESTS, value = "15"),
                @HystrixProperty(name = HystrixPropertiesManager.FALLBACK_ENABLED, value = "false"),
                @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_ENABLED, value = "false"),
                @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_REQUEST_VOLUME_THRESHOLD, value = "30"),
                @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_SLEEP_WINDOW_IN_MILLISECONDS, value = "250"),
                @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_ERROR_THRESHOLD_PERCENTAGE, value = "60"),
                @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_FORCE_OPEN, value = "false"),
                @HystrixProperty(name = HystrixPropertiesManager.CIRCUIT_BREAKER_FORCE_CLOSED, value = "true"),
                @HystrixProperty(name = HystrixPropertiesManager.METRICS_ROLLING_PERCENTILE_ENABLED, value = "false"),
                @HystrixProperty(name = HystrixPropertiesManager.METRICS_ROLLING_PERCENTILE_TIME_IN_MILLISECONDS, value = "400"),
                @HystrixProperty(name = HystrixPropertiesManager.METRICS_ROLLING_STATS_TIME_IN_MILLISECONDS, value = "500"),
                @HystrixProperty(name = HystrixPropertiesManager.METRICS_ROLLING_STATS_NUM_BUCKETS, value = "10"),
                @HystrixProperty(name = HystrixPropertiesManager.METRICS_ROLLING_PERCENTILE_NUM_BUCKETS, value = "5"),
                @HystrixProperty(name = HystrixPropertiesManager.METRICS_ROLLING_PERCENTILE_BUCKET_SIZE, value = "6"),
                @HystrixProperty(name = HystrixPropertiesManager.METRICS_HEALTH_SNAPSHOT_INTERVAL_IN_MILLISECONDS, value = "312"),
                @HystrixProperty(name = HystrixPropertiesManager.REQUEST_CACHE_ENABLED, value = "false"),
                @HystrixProperty(name = HystrixPropertiesManager.REQUEST_LOG_ENABLED, value = "true") })
        public User getUsingAllCommandProperties(Integer id, String name) {
            return new User(id, name + id); // it should be network call
        }

    }
}
