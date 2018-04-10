package demo.netflix.hystrix;

import java.util.concurrent.Future;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import rx.Observable;

/**
 * Hystrix 能使你的系统在出现依赖服务失效的时候，通过隔离系统所依赖的服务，防止服务级联失败，同时提供失败回退机制，更优雅地应对失效，并使你的系统能更快地从异常中恢复。
 * 在一个分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，如何能够保证在一个依赖出问题的情况下，不会导致整体服务失败，这个就是Hystrix需要做的事情。
 * Hystrix提供了熔断、隔离、Fallback、cache、监控等功能，能够在一个、或多个依赖同时出现问题时保证系统依然可用。
 * 
 * <h2>Hystrix能做什么？</h2>
 * <p>
 * <li>在通过第三方客户端访问（通常是通过网络）依赖服务出现高延迟或者失败时，为系统提供保护和控制
 * <li>在分布式系统中防止级联失败
 * <li>快速失败（Fail fast）同时能快速恢复
 * <li>提供失败回退（Fallback）和优雅的服务降级机制
 * <li>提供近实时的监控、报警和运维控制手段
 * 
 * <h2>Hystrix设计原则？</h2>
 * <p>
 * <li>防止单个依赖耗尽容器（例如 Tomcat）内所有用户线程
 * <li>降低系统负载，对无法及时处理的请求快速失败（fail fast）而不是排队
 * <li>提供失败回退，以在必要时让失效对用户透明化
 * <li>使用隔离机制（例如『舱壁』/『泳道』模式，熔断器模式等）降低依赖服务对整个系统的影响
 * <li>针对系统服务的度量、监控和报警，提供优化以满足近实时性的要求
 * <li>在 Hystrix 绝大部分需要动态调整配置并快速部署到所有应用方面，提供优化以满足快速恢复的要求
 * <li>能保护应用不受依赖服务的整个执行过程中失败的影响，而不仅仅是网络请求
 * 
 * <h2>Hystrix实现原理-命令模式</h2>
 * <p>
 * <li>将所有请求外部系统（或者叫依赖服务）的逻辑封装到 HystrixCommand或者HystrixObservableCommand（依赖RxJava）对象中
 * <li>Run()方法为要实现的业务逻辑，这些逻辑将会在独立的线程中被执行，当请求依赖服务时出现拒绝服务、超时或者短路（多个依赖服务顺序请求，前面的依赖服务请求失败，则后面的请求不会发出）时，执行该依赖服务的失败回退逻辑(Fallback)
 * <h2>Hystrix实现原理-舱壁模式</h2> 货船为了进行防止漏水和火灾的扩散,会将货仓分隔为多个，当发生灾害时，将所在货仓进行隔离就可以降低整艘船的风险。
 * 
 * <h2>Hystrix实现原理-隔离策略</h2>
 * <p>
 * <li>应用在复杂的分布式结构中，可能会依赖许多其他的服务，并且这些服务都不可避免地有失效的可能。如果一个应用没有与依赖服务的失效隔离开来，那么它将有可能因为依赖服务的失效而失效。
 * <li>Hystrix将货仓模式运用到了服务调用者上。为每一个依赖服务维护一个线程池（或者信号量），当线程池占满，该依赖服务将会立即拒绝服务而不是排队等待。
 * <li>每个依赖服务都被隔离开来，Hystrix 会严格控制其对资源的占用，并在任何失效发生时，执行失败回退逻辑。
 * 
 * <h2>Hystrix实现原理-观察者模式</h2>
 * <p>
 * <li>Hystrix通过观察者模式对服务进行状态监听。
 * <li>每个任务都包含有一个对应的Metrics，所有Metrics都由一个ConcurrentHashMap来进行维护，Key是CommandKey.name()
 * <li>在任务的不同阶段会往Metrics中写入不同的信息，Metrics会对统计到的历史信息进行统计汇总，供熔断器以及Dashboard监控时使用。
 * 
 * <h2>Hystrix实现原理-熔断机制</h2>
 * <p>
 * <li>熔断是参考电路而产生的一种保护性机制，即系统中如果存在某个服务失败率过高时，将开启熔断器，对于后续的调用，不在继续请求服务，而是进行Fallback操作。
 * <li>熔断所依靠的数据即是Metrics中的HealthCount所统计的错误率。
 * <h2>原理</h2>
 * <p>
 * <li>1）Hystrix使用命令模式HystrixCommand(Command)包装依赖调用逻辑，每个命令在单独线程中/信号 授权 下执行。 一般情况下，Hystrix
 * 会为Command分配专门的线程池，池中的线程数量是固定的，这也是一个保护机制，假设你依赖很多个服务，你不希望对其中一个服务的调用消耗过多的线程以致于其他服务都没线程调用了。
 * 默认这个线程池的大小是10，即并发执行的命令最多只能有是个了，超过这个数量的调用就得排队，如果队伍太长了（默认超过5），Hystrix就立刻走fallback 或者抛异常。
 * 根据你的具体需要，你可能会想要调整某个Command的线程池大小，例如你对某个依赖的调用平均响应时间为200ms，而峰值的QPS是200，那么这个并发至少就是 0.2 x 200 = 40 (Little's
 * Law)，考虑到一定的宽松度，这个线程池的大小设置为60可能比较合适.
 * 
 * <li>2）提供熔断器组件,可以自动运行或手动调用,停止当前依赖一段时间(10秒)，熔断器默认 错误 率阈值为50%,超过将自动运行。
 * 断路器机制默认是启用的，但是编程接口默认几乎不需要关心这个，机制和前面讲的也差不多，Hystrix会统计命令调用，看其中失败的比例，默认当超过50%失败后，开启断路器，那之后一段时间的命令调用直接返回失败（或者走fallback），
 * 5秒之后，Hystrix再尝试关闭断路器，看看请求是否能正常响应。
 * 
 * <li>3）可配置依赖调用 超时 时间,超时时间一般设为比99.5%平均时间略高即可.当调用超时时，直接返回或执行fallback逻辑。
 * 
 * <li>4）为每个依赖提供一个小的线程池（或信号），如果线程池已满调用将被立即拒绝，默认不采用排队.加速失败判定时间。
 * 
 * <li>5）依赖调用结果分:成功，失败（抛出 异常 ），超时，线程拒绝，短路。 请求失败(异常，拒绝，超时，短路)时执行fallback(降级)逻辑。
 * 
 * <li>6）提供近实时依赖的统计和监控
 * 
 * <li>7） 支持异步执行。支持并发请求缓存。自动批处理失败请求。
 * 
 *
 */
public class HystrixDemo {
    public static void main(String[] args) {

    }

    /**
     * blocks, then returns the single response received from the dependency (or throws an exception in case of an
     * error)
     */
    public void execute() {
        String s = new CommandHelloWorld("Bob").execute();
    }

    /**
     * returns a Future with which you can obtain the single response from the dependency
     */
    public void queue() {
        Future<String> s = new CommandHelloWorld("Bob").queue();
    }

    /**
     * subscribes to the Observable that represents the response(s) from the dependency and returns an Observable that
     * replicates that source Observable
     */
    public void observe() {
        Observable<String> s = new CommandHelloWorld("Bob").observe();
    }

    /**
     * returns an Observable that, when you subscribe to it, will execute the Hystrix command and emit its responses
     */
    public void toObservable() {
        Observable<String> s = new CommandHelloWorld("Bob").toObservable();
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
        return "Hello " + name + "!";
    }
}
