package demo.netflix.hystrix;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.config.sources.URLConfigurationSource;

import demo.netflix.hystrix.command.CommandHelloWorld;
import rx.Observable;
import rx.Observer;
import rx.functions.Action1;

/**
 * https://github.com/Netflix/Hystrix/wiki/Configuration
 * 
 * <h2>HystrixCommand和HystrixObservableCommand</h2>
 * <li>HystrixObservableCommand只支持异步的调用方式，HystrixCommand同步异步都支持。
 * <li>HystrixObservableCommand支持请求合并功能，HystrixCommand不支持。
 * 
 * <h2>Execution Exception types</h2>
 * <table>
 * <td><tr>aa</tr></td>
 * </table>
 * 
 * 
 * <h2>什么情况下会触发fallback方法?</h2>
 * <li>FAILURE: 执行抛出异常，Exception.cause：underlying exception (user-controlled)
 * <li>TIMEOUT: 执行开始，但没有在允许的时间内完成，Exception.cause：j.u.c.TimeoutException
 * <li>SHORT_CIRCUITED: 断路器打开，不尝试执行，Exception.cause：j.l.RuntimeException
 * <li>THREAD_POOL_REJECTED: 线程池拒绝，不尝试执行，Exception.cause：j.u.c.RejectedExecutionException
 * <li>SEMAPHORE_REJECTED: 信号量拒绝，不尝试执行，Exception.cause：j.l.RuntimeException
 * 
 * <h2>什么情况下不会触发fallback方法?</h2>
 * <li>EMIT: 值传递
 * <li>SUCCESS: 执行抛出HystrixBadRequestException
 * <li>BAD_REQUEST: 执行抛出HystrixBadRequestException，Exception.cause：underlying exception (user-controlled)
 * 
 * <h2>fallback方法抛出异常</h2>
 * <li>FALLBACK_EMIT : Fallback值传递 ，不抛异常
 * <li>FALLBACK_SUCCESS : Fallback执行完成，没有错误 ，不抛异常
 * <li>FALLBACK_FAILURE : Fallback执行抛出出错 ，抛异常
 * <li>FALLBACK_REJECTED : Fallback信号量拒绝，不尝试执行 ，抛异常
 * <li>FALLBACK_MISSING : 没有Fallback实例 ，抛异常
 * 
 * 
 * 
 */
public class HystrixDemo {

    private static Logger logger = LoggerFactory.getLogger(HystrixDemo.class);

    public static void main(String[] args) {
        System.setProperty("archaius.configurationSource.additionalUrls",
                "file:E:\\Projects\\Stan\\Demo\\testJava\\src\\main\\java\\demo\\netflix\\hystrix\\hystrix_config_default.properties");
        URLConfigurationSource source = new URLConfigurationSource(
                "file:E:\\Projects\\Stan\\Demo\\testJava\\src\\main\\java\\demo\\netflix\\hystrix\\hystrix_config_default.properties");
        observe();

        execute();
    }

    /**
     * blocks, then returns the single response received from the dependency (or throws an exception in case of an
     * error)
     */
    static void execute() {
        String s = new CommandHelloWorld("Bob").execute();
        System.err.println(s);

        String a = new CommandHelloWorld("StanHan").execute();
        System.err.println(a);
    }

    /**
     * returns a Future with which you can obtain the single response from the dependency
     */
    static void queue() throws InterruptedException, ExecutionException {
        Future<String> futrue = new CommandHelloWorld("Bob").queue();
        try {
            String s = futrue.get();
            System.err.println(s);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.err.println(futrue.get());
    }

    /**
     * <h1>observe()</h1> — returns a “hot” Observable that executes the command immediately, though because the
     * Observable is filtered through a ReplaySubject you are not in danger of losing any items that it emits before you
     * have a chance to subscribe
     * <p>
     * subscribes to the Observable that represents the response(s) from the dependency and returns an Observable that
     * replicates that source Observable
     * 
     */
    static void observe() {
        Observable<String> observe = new CommandHelloWorld("Bob").observe();
        // blocking
        logger.info(observe.toBlocking().single());

        // non-blocking - this is a verbose anonymous inner-class approach and doesn't do assertions
        observe.subscribe(new Observer<String>() {

            @Override
            public void onCompleted() {
                // nothing needed here
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(String v) {
                logger.info("onNext: " + v);
            }

        });

        // non-blocking
        // - also verbose anonymous inner-class
        // - ignore errors and onCompleted signal
        observe.subscribe(new Action1<String>() {
            @Override
            public void call(String v) {
                logger.info("onNext: " + v);
            }
        });

        observe.subscribe((v) -> {
            logger.info("onNext: " + v);
        });

        observe.subscribe((v) -> {
            logger.info("onNext: " + v);
        }, (exception) -> {
            exception.printStackTrace();
        });

    }

    /**
     * <h1>toObservable()</h1> — returns a “cold” Observable that won’t execute the command and begin emitting its
     * results until you subscribe to the Observable
     * <p>
     * returns an Observable that, when you subscribe to it, will execute the Hystrix command and emit its responses
     */
    static void toObservable() {
        Observable<String> observable = new CommandHelloWorld("Bob").toObservable();
        System.err.println(observable);
    }
}
