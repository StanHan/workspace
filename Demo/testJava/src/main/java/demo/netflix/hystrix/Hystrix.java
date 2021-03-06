package demo.netflix.hystrix;

/**
 * Hystrix 能使你的系统在出现依赖服务失效的时候，通过隔离系统所依赖的服务，防止服务级联失败，同时提供失败回退机制，更优雅地应对失效，并使你的系统能更快地从异常中恢复。
 * 在一个分布式系统里，许多依赖不可避免的会调用失败，比如超时、异常等，如何能够保证在一个依赖出问题的情况下，不会导致整体服务失败，这个就是Hystrix需要做的事情。
 * Hystrix提供了熔断、隔离、Fallback、cache、监控等功能，能够在一个、或多个依赖同时出现问题时保证系统依然可用。
 * 
 * <h2>涉及概念</h2>
 * <li>服务等级(service-level)： 核心(core) 重要(important) 普通(normal) 次要(secondary) 非必需(dispensable)
 * <li>服务隔离:消费者的每个消费的服务之间互相独立，互不影响，不会因为某个服务的故障或者不可用造成其他服务的故障或者不可用。
 * <li>服务限流:按照服务隔离的原则，对每个服务的流量进行限制，不会因为某个或某几个服务的请求量过大而造成其他服务的不可用
 * <li>服务熔断:当消费方依赖的某个服务不可用时，动态的隔绝对该服务的依赖。消费方不再继续请求该服务，尝试使用降级逻辑。当服务恢复可用时，能立即感知并恢复对该服务的依赖。
 * <li>服务降级：消费方依赖的某个服务不可用（异常或者超时），需要采取的补偿性措施。
 * 
 * <h2>Hystrix能做什么？</h2>
 * <li>在通过第三方客户端访问（通常是通过网络）依赖服务出现高延迟或者失败时，为系统提供保护和控制
 * <li>在分布式系统中防止级联失败
 * <li>快速失败（Fail fast）同时能快速恢复
 * <li>提供失败回退（Fallback）和优雅的服务降级机制
 * <li>提供近实时的监控、报警和运维控制手段
 * 
 * <h2>Hystrix设计原则？</h2>
 * <li>防止单个依赖耗尽容器（例如 Tomcat）内所有用户线程
 * <li>降低系统负载，对无法及时处理的请求快速失败（fail fast）而不是排队
 * <li>提供失败回退，以在必要时让失效对用户透明化
 * <li>使用隔离机制（例如『舱壁』/『泳道』模式，熔断器模式等）降低依赖服务对整个系统的影响
 * <li>针对系统服务的度量、监控和报警，提供优化以满足近实时性的要求
 * <li>在 Hystrix 绝大部分需要动态调整配置并快速部署到所有应用方面，提供优化以满足快速恢复的要求
 * <li>能保护应用不受依赖服务的整个执行过程中失败的影响，而不仅仅是网络请求
 * 
 * <h2>hystrix实现原理</h2> hystrix语义为“豪猪”，具有自我保护的能力。hystrix的出现即为解决雪崩效应，它通过四个方面的机制来解决这个问题
 * <li>隔离（线程池隔离和信号量隔离）：限制调用分布式服务的资源使用，某一个调用的服务出现问题不会影响其他服务调用。
 * <li>优雅的降级机制：超时降级、资源不足时(线程或信号量)降级，降级后可以配合降级接口返回托底数据。
 * <li>融断：当失败率达到阀值自动触发降级(如因网络故障/超时造成的失败率高)，熔断器触发的快速失败会进行快速恢复。
 * <li>缓存：提供了请求缓存、请求合并实现。
 * <li>支持实时监控、报警、控制（修改配置）
 *
 * 
 * 
 * <h2>Hystrix实现原理-命令模式</h2>
 * <p>
 * <li>将所有请求外部系统（或者叫依赖服务）的逻辑封装到 HystrixCommand或者HystrixObservableCommand（依赖RxJava）对象中
 * <li>Run()方法为要实现的业务逻辑，这些逻辑将会在独立的线程中被执行，当请求依赖服务时出现拒绝服务、超时或者短路（多个依赖服务顺序请求，前面的依赖服务请求失败，则后面的请求不会发出）时，执行该依赖服务的失败回退逻辑(Fallback)
 * 
 * <h2>Hystrix实现原理-舱壁模式</h2> 货船为了进行防止漏水和火灾的扩散,会将货仓分隔为多个，当发生灾害时，将所在货仓进行隔离就可以降低整艘船的风险。
 * 
 * 
 * <h2>Hystrix实现原理-观察者模式</h2>
 * <p>
 * <li>Hystrix通过观察者模式对服务进行状态监听。
 * <li>每个任务都包含有一个对应的Metrics，所有Metrics都由一个ConcurrentHashMap来进行维护，Key是CommandKey.name()
 * <li>在任务的不同阶段会往Metrics中写入不同的信息，Metrics会对统计到的历史信息进行统计汇总，供熔断器以及Dashboard监控时使用。
 * 
 * <h2>原理</h2>
 * <p>
 * <li>1）Hystrix使用命令模式HystrixCommand(Command)包装依赖调用逻辑，每个命令在单独线程中/信号 授权 下执行。 一般情况下，Hystrix
 * 会为Command分配专门的线程池，池中的线程数量是固定的，这也是一个保护机制，假设你依赖很多个服务，你不希望对其中一个服务的调用消耗过多的线程以致于其他服务都没线程调用了。
 * 默认这个线程池的大小是10，即并发执行的命令最多只能有是个了，超过这个数量的调用就得排队，如果队伍太长了（默认超过5），Hystrix就立刻走fallback 或者抛异常。
 * 根据你的具体需要，你可能会想要调整某个Command的线程池大小，例如你对某个依赖的调用平均响应时间为200ms，而峰值的QPS是200，那么这个并发至少就是 0.2 x 200 = 40 (Little's
 * Law)，考虑到一定的宽松度，这个线程池的大小设置为60可能比较合适.
 * <li>2）提供熔断器组件,可以自动运行或手动调用,停止当前依赖一段时间(10秒)，熔断器默认 错误 率阈值为50%,超过将自动运行。
 * 断路器机制默认是启用的，但是编程接口默认几乎不需要关心这个，机制和前面讲的也差不多，Hystrix会统计命令调用，看其中失败的比例，默认当超过50%失败后，开启断路器，那之后一段时间的命令调用直接返回失败（或者走fallback），
 * 5秒之后，Hystrix再尝试关闭断路器，看看请求是否能正常响应。
 * <li>3）可配置依赖调用 超时 时间,超时时间一般设为比99.5%平均时间略高即可.当调用超时时，直接返回或执行fallback逻辑。
 * <li>4）为每个依赖提供一个小的线程池（或信号），如果线程池已满调用将被立即拒绝，默认不采用排队.加速失败判定时间。
 * <li>5）依赖调用结果分:成功，失败（抛出 异常 ），超时，线程拒绝，短路。 请求失败(异常，拒绝，超时，短路)时执行fallback(降级)逻辑。
 * <li>6）提供近实时依赖的统计和监控
 * <li>7） 支持异步执行。支持并发请求缓存。自动批处理失败请求。
 * 
 *
 */
public interface Hystrix {

    /**
     * <h2>dubbo的限流，降级方案</h2>
     * <li>消费端通过配置acitves限制消费端调用的并发量，在达到最大并发量之后等待一个timeout时间再重试。
     * <li>服务端通过配置executes限制服务端接口的线程最大数量，达到最大数量之后直接抛出异常。
     * <li>超时配置，当超时且超过重试次数之后，抛出异常。消费方实现自己的降级逻辑。
     * <li>当没有可用的服务提供者之后，消费者直接短路，消费方实现自己的短路逻辑。
     * <li>通过注册中心的URL实现服务运行时参数的动态配置。
     * <li>限流或隔离的粒度是以接口方法为粒度。
     * <li>dubbo自带的监控不够强大，需要自己扩展或者使用第三方扩展。
     */
    void dubbo();

    /**
     * <h2>hystrix的限流，降级方案</h2>
     * <li>自定义限流位置。
     * <li>提供超时时间配置，当超时或者抛出非BadRequestException之后,其他任何错误，异常或者超时时，尝试降级逻辑。
     * <li>对一段时间内的错误，超时率进行统计，达到配置的阈值时自动短路，调用降级逻辑。
     * <li>服务短路后提供自动恢复机制，快速恢复服务。
     * <li>通过内置的archaius或者第三方配置框架实现服务运行时参数的动态配置。
     * <li>隔离粒度可以自定义，模块，接口，方法粒度都支持。
     * <li>提供了基于event-stream的扩展工具和官方的dashboard进行监控。但目前官方提供的even-stream是基于servlet的
     */
    void hystrix();

    /**
     * <h2>两种方案的优劣</h2>
     * <li>dubbo同时提供消费端和服务端的限流。hystrix只提供消费端限流。
     * <li>dubbo的消费端限流的信号量是以服务器为粒度，而hystrix的消费端限流是以整个提供方集群为粒度（更合理）。
     * <li>dubbo不提供服务容错降级后的自动短路。hystrix支持自动短路和自动恢复。
     * <li>dubbo管理平台中的动态配置用通知的方式通知消费者，但存在不生效等一些bug。hystrix利用archaius的动态配置方案从本地或URL中轮询拉取配置。
     * <li>dubbo的限流其实是基于信号量的，而hystrix同时支持信号量和线程池的限流。
     */
    void compareDubboHystrix();

    /**
     * <h2>隔离</h2> Hystrix将货仓模式运用到了服务调用者上。为每一个依赖服务维护一个线程池（或者信号量），当线程池占满，该依赖服务将会立即拒绝服务而不是排队等待。
     * 每个依赖服务都被隔离开来，Hystrix会严格控制其对资源的占用，并在任何失效发生时，执行失败回退逻辑。
     * 
     * <li>线程池隔离：使用线程池作为隔离的实现方式，每个隔离单元拥有自己单独的线程池，调用依赖服务时，申请一个新的线程执行真正的调用逻辑，线程池或者队列满了之后，拒绝服务。
     * 这种方式需要为每个依赖的服务申请线程池，有一定的资源消耗，好处是可以应对突发流量（流量洪峰来临时，处理不完可将数据存储到线程池队里慢慢处理）
     * <li>信号量隔离： 使用信号量作为隔离的实现方式，每个隔离单元拥有配置了自己的信号量阈值，调用依赖服务时，在原请求线程中申请新的信号量，如果申请到，继续在原线程中执行调用逻辑，信号量超过阈值之后，拒绝服务。
     * 使用一个原子计数器（或信号量）来记录当前有多少个线程在运行，请求来先判断计数器的数值，若超过设置的最大线程个数则丢弃该类型的新请求，若不超过则执行计数操作请求来计数器+1，请求返回计数器-1。
     * 这种方式是严格的控制线程且立即返回模式，无法应对突发流量（流量洪峰来临时，处理的线程超过数量，其他的请求会直接返回，不继续去请求依赖的服务）
     * 
     */
    void 隔离();

    /**
     * <h2>熔断机制</h2>熔断是参考电路而产生的一种保护性机制，即系统中如果存在某个服务失败率过高时，将开启熔断器，对于后续的调用，不再继续请求服务，而是进行Fallback操作。
     * 熔断所依靠的数据即是Metrics中的HealthCount所统计的错误率。
     * <p>
     * 正常状态下，电路处于关闭状态(Closed)，如果调用持续出错或者超时，电路被打开进入熔断状态(Open)，后续一段时间内的所有调用都会被拒绝(Fail-Fast)，
     * 一段时间以后，保护器会尝试进入半熔断状态(Half-Open)，允许少量请求进来尝试，如果调用仍然失败，则回到熔断状态，如果调用成功，则回到电路闭合状态;
     * 
     */
    void 熔断();

    /**
     * 整体资源快不够了，忍痛将某些服务先关掉，待渡过难关，再开启回来。
     * 
     * 根据业务场景的不同，一般采用以下两种模式：
     * <li>第一种（最常用）如果服务失败，则我们通过fallback进行降级，返回静态值。
     * <li>第二种采用服务级联的模式，如果第一个服务失败，则调用备用服务，例如失败重试或者访问缓存失败再去取数据库。
     * 服务级联的目的则是尽最大努力保证返回数据的成功性，但如果考虑不充分，则有可能导致级联的服务崩溃（比如，缓存失败了，把全部流量打到数据库，瞬间导致数据库挂掉）。因此级联模式，也要慎用，增加了管理的难度。
     * 
     */
    void 降级();
}
