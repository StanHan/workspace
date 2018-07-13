package demo.dubbo.provider.hystrix;

import java.util.concurrent.Future;

import com.alibaba.dubbo.config.annotation.Reference;
import com.loanking.galaxy.facade.UdspFacade;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixException;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.command.AsyncResult;

import demo.dubbo.exception.ServiceException;
import rx.Observable;
import rx.Subscriber;

/**
 * 
 * 
 *
 */
@DefaultProperties(defaultFallback = "defaultFallback")
public class HystrixJavaNicaDemo {

    @Reference
    UdspFacade udsp;

    /**
     * <h2>同步执行</h2>
     * 
     * 默认command key的名称是命令方法名称， group key 名称是被注释方法的类名称。当然，您也可以使用@HystrixCommand的属性更改它
     * 
     * 可以通过在@HystrixCommand中声明｀fallbackMethod｀来实现正常退化，
     * <h3>错误传播</h3>
     * 如果userResource.getUserById（id）;抛出类型为BadRequestException的异常，则此异常将被包装在HystrixBadRequestException中，并重新抛出，而不触发后备逻辑。
     * 你不需要手动执行，javanica会为你做这个。
     * 
     * 值得注意的是，默认情况下，一个调用者总是会得到根本原因异常，例如BadRequestException，而不是HystrixBadRequestException或HystrixRuntimeException（除非有执行代码显式抛出这些异常的情况）。
     * 
     * 可选地，通过使用raiseHystrixExceptions,可以禁用HystrixRuntimeException的拆箱。即所有未被忽略的异常会作为HystrixRuntimeException的cause出现。
     * 
     */
    @HystrixCommand(groupKey = "HystrixJavaNicaDemo", commandKey = "getUserById", fallbackMethod = "defaultUser", ignoreExceptions = {
            ServiceException.class }, raiseHystrixExceptions = { HystrixException.RUNTIME_EXCEPTION })
    public Object getUserById(String id) {
        return udsp.request(null);
    }

    /**
     * 重要的是要记住，Hystrix命令和回退方法应该放在同一个类中并具有相同的方法签名（执行失败的异常(诱发服务降级的异常)为可选参数）。回退方法可以有任何访问修饰符。
     * 方法defaultUser将用于在任何错误的情况下处理回退逻辑。如果您需要将fallback methoddefaultUser作为单独的Hystrix命令运行，那么您需要使用HystrixCommand注释对其进行注释，
     * 
     * 如果回退方法标记为@HystrixCommand，那么这种回退方法（defaultUser）也可以有自己的回退方法，
     */
    @HystrixCommand(fallbackMethod = "defaultUserSecond")
    private Object defaultUser(String id) {
        return new Object();
    }

    @HystrixCommand(fallbackMethod = "fallback2")
    private Object defaultUserSecond(String id) {
        return new Object();
    }

    /**
     * Javanica提供了在执行fallback中获取执行异常（导致命令失败抛出的异常）的能力。你可以使用附加参数扩展fallback方法签名，以获取命令抛出的异常。
     * Javanica通过fallback方法的附加参数来公开执行异常。执行异常是通过调用方法getExecutionException（）在vanilla hystrix中得到的。
     * Throwable参数不是强制性的，可以省略或指定。fallback可以得到父层方法执行失败的异常，因此fallback3会收到fallback2抛出的异常，而不是getUserById命令中的异常。
     * 
     */
    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500") })
    Object fallback2(String id, Throwable e) {
        assert "getUserById command failed".equals(e.getMessage());
        return new Object();
    }

    /**
     * <h2>异步执行</h2> 命令方法的返回类型应为Future，表示应该以异步方式执行命令
     */
    @HystrixCommand
    public Future<Object> getUserByIdAsync(final String id) {
        return new AsyncResult<Object>() {
            @Override
            public Object invoke() {
                return udsp.request(null);
            }
        };
    }

    /**
     * <h2>响应式执行</h2> 命令方法的返回类型应该是Observable。
     * 
     * HystrixObservable接口提供了两种方法：observe（） - 与HystrixCommand#queue()或HystrixCommand#execute()行为一样，立即开始执行命令；
     * toObservable() - 一旦Observable被订阅，懒惰地开始执行命令。
     * 为了控制这种行为，并且在两种模式之间切换,@HystrixCommand提供了名为observableExecutionMode的特定属性。
     * 
     * <li>@HystrixCommand（observableExecutionMode = EAGER）表示应该使用observe（）方法执行observable命令;
     * <li>@HystrixCommand（observableExecutionMode = LAZY）表示应该使用toObservable（）方法来执行observable命令。
     * <p>
     * 注意：默认情况下使用EAGER模式
     */
    @HystrixCommand
    public Observable<Object> getUserByIdObservable(final String id) {
        return Observable.create(new Observable.OnSubscribe<Object>() {

            @Override
            public void call(Subscriber<? super Object> observer) {
                try {
                    if (!observer.isUnsubscribed()) {
                        observer.onNext(new Object());
                        observer.onCompleted();
                    }
                } catch (Exception e) {
                    observer.onError(e);
                }
            }
        });
    }

    /**
     * <h2>Async/Sync fallback</h2>回退可以是异步或同步，在某些情况下，它取决于命令执行类型。
     * <p>
     * 支持
     * <li>case 1: 同步 command, 同步 fallback
     * <li>case 2: 异步 command, 同步 fallback
     * <li>case 3: 异步 command, 异步 fallback
     * <p>
     * 不支持(禁止)
     * <li>case 1: 同步 command, 异步 fallback。因为在本质上，一个调用者执行getUserById方法不会得到Future的结果
     * 
     */
    public void demo() {

    }

    /**
     * <h2>类或具体命令的默认回退</h2> 此功能允许为整个类或具体命令定义默认回退。如果您有一批具有完全相同的回退逻辑的命令，您仍然必须为每个命令定义回退方法，因为回退方法应该具有与命令完全相同的签名。
     * 默认回退功能允许采用DRY原则(Don’t Repeat Yourself，不要重复你自己)并摆脱冗余：
     * 默认的回退方法不应该有任何参数，除了可以附加获取执行异常参数，不应该抛出任何异常。以降序优先级列(如果设置1,则不执行2)出如下：
     * <ol>
     * <li>使用@HystrixCommand的fallbackMethod属性定义命令回退
     * <li>使用@HystrixCommand的defaultFallback属性定义命令默认回退
     * <li>使用@DefaultProperties的defaultFallback属性定义类默认回退
     * </ol>
     * 
     * @return
     */
    Object defaultFallback() {
        return new Object();
    }
}
