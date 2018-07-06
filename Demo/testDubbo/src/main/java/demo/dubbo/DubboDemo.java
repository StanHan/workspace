package demo.dubbo;

/**
 * Dubbo is a high-performance, java based RPC framework open-sourced by Alibaba
 * <h2>服务调用超时设置</h2>
 * <p>
 * 配置的查找顺序，其它retries, loadbalance, actives也类似。 方法级优先，接口级次之，全局配置再次之。 如果级别一样，则消费方优先，提供方次之。
 * 
 * 其中，服务提供方配置，通过URL经由注册中心传递给消费方。 建议由服务提供方设置超时，因为一个方法需要执行多长时间，服务提供方更清楚，如果一个消费方同时引用多个服务，就不需要关心每个服务的超时设置。
 * 理论上ReferenceConfig的非服务标识配置，在ConsumerConfig，ServiceConfig, ProviderConfig均可以缺省配置。
 * <h2>启动时检查</h2>
 * <p>
 * Dubbo缺省会在启动时检查依赖的服务是否可用，不可用时会抛出异常，阻止Spring初始化完成，以便上线时，能及早发现问题，默认check=true。
 * 如果你的Spring容器是懒加载的，或者通过API编程延迟引用服务，请关闭check，否则服务临时不可用时，会抛出异常，拿到null引用，如果check=false，总是会返回引用，当服务恢复时，能自动连上。
 * 
 * <h2>订阅</h2>
 * <li>1、问题： 为方便开发测试，经常会在线下共用一个所有服务可用的注册中心，这时，如果一个正在开发中的服务提供者注册，可能会影响消费者不能正常运行。
 * <li>2、解决方案： 可以让服务提供者开发方，只订阅服务(开发的服务可能依赖其它服务)，而不注册正在开发的服务，通过直连测试正在开发的服务。
 * <li>禁用注册配置： <dubbo:registry address="10.20.153.10:9090" register="false" />
 * <li>或者： <dubbo:registry address="10.20.153.10:9090?register=false" />
 * 
 * <h2>回声测试(测试服务是否可用)</h2>
 * <p>
 * 回声测试用于检测服务是否可用，回声测试按照正常请求流程执行，能够测试整个调用是否通畅，可用于监控。 所有服务自动实现EchoService接口，只需将任意服务引用强制转型为EchoService，即可使用。
 * 
 * <h2>延迟连接</h2>
 * <p>
 * 延迟连接，用于减少长连接数，当有调用发起时，再创建长连接。 只对使用长连接的dubbo协议生效。 <dubbo:protocol name="dubbo" lazy="true" />
 * 
 * <h2>令牌验证</h2>
 * <p>
 * 防止消费者绕过注册中心访问提供者，在注册中心控制权限，以决定要不要下发令牌给消费者，注册中心可灵活改变授权方式，而不需修改或升级提供者
 * 
 * <li>1、全局设置开启令牌验证： <!--随机token令牌，使用UUID生成--> <dubbo:provider interface="com.foo.BarService" token="true" />
 * <!--固定token令牌，相当于密码--> <dubbo:provider interface="com.foo.BarService" token="123456" />
 * 
 * <li>2、服务级别设置开启令牌验证： <!--随机token令牌，使用UUID生成--> <dubbo:service interface="com.foo.BarService" token="true" />
 * <!--固定token令牌，相当于密码--> <dubbo:service interface="com.foo.BarService" token="123456" />
 * 
 * <li>3、协议级别设置开启令牌验证： <!--随机token令牌，使用UUID生成--> <dubbo:protocol name="dubbo" token="true" /> <!--固定token令牌，相当于密码-->
 * <dubbo:protocol name="dubbo" token="123456" />
 * 
 * <h2>日志适配</h2>
 * <p>
 * 缺省自动查找：log4j、slf4j、jcl、jdk
 * 
 * 可以通过以下方式配置日志输出策略：dubbo:application logger="log4j"/> 访问日志： 如果你想记录每一次请求信息，可开启访问日志，类似于apache的访问日志。此日志量比较大，请注意磁盘容量。
 * 将访问日志输出到当前应用的log4j日志： <dubbo:protocol accesslog="true" /> 将访问日志输出到指定文件：
 * <dubbo:protocol accesslog="http://10.20.160.198/wiki/display/dubbo/foo/bar.log" />
 * 
 * 
 * <h2>配置Dubbo缓存文件</h2> <dubbo:registryfile=”${user.home}/output/dubbo.cache” />
 * 注意：文件的路径，应用可以根据需要调整，保证这个文件不会在发布过程中被清除。如果有多个应用进程注意不要使用同一个文件，避免内容被覆盖。 这个文件会缓存： 注册中心的列表 服务提供者列表。
 * 有了这项配置后，当应用重启过程中，Dubbo注册中心不可用时则应用会从这个缓存文件读取服务提供者列表的信息，进一步保证应用可靠性。
 * 
 * @author http://dubbo.io/
 */
public class DubboDemo {

    
    void demo1() {

    }
}
