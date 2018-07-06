package demo.aspectJ;

/**
 * <h1>AspectJ</h1> AspectJ是一个面向切面的框架，它扩展了Java语言，定义了AOP语法，所以它有一个专门的编译器用来生成遵守Java字节编码规范的Class文件。
 * AspectJ是一种编译期的用注解形式实现的AOP。
 * 
 * <h1>AOP/Spring AOP/AspectJ的区别</h1> AspectJ和Spring AOP 是AOP的两种实现方案。
 */
public interface AspectJInfo {

    /**
     * <h2>AOP</h2> 是一种面向切面的编程范式，是一种编程思想，旨在通过分离横切关注点，提高模块化，可以跨越对象关注点。 Aop的典型应用即spring的事务机制，日志记录。
     * 利用AOP可以对业务逻辑的各个部分进行隔离，从而使得业务逻辑各部分之间的耦合度降低，提高程序的可重用性，同时提高了开发的效率。 主要功能是：日志记录，性能统计，安全控制，事务处理，异常处理等等；
     * 主要的意图是：将日志记录，性能统计，安全控制，事务处理，异常处理等代码从业务逻辑代码中划分出来，通过对这些行为的分离，我们希望可以将它们独立到非指导业务逻辑的方法中，进而改变这些行为的时候不影响业务逻辑的代码。
     */
    void aop();

    /**
     * <h2>AAspectJ:Aspectj是aop的java实现方案，AspectJ是一种编译期的用注解形式实现的AOP。</h2>
     * 
     * <li>（1）AspectJ是一个代码生成工具（Code Generator），其中AspectJ语法就是用来定义代码生成规则的语法。
     * 基于自己的语法编译工具，编译的结果是JavaClass文件，运行的时候classpath需要包含AspectJ的一个jar文件（Runtime
     * lib），支持编译时织入切面，即所谓的CTW机制，可以通过一个Ant或Maven任务来完成这个操作。
     * <li>（2）AspectJ有自己的类装载器，支持在类装载时织入切面，即所谓的LTW机制。 使用AspectJ LTW有两个主要步骤：
     * 第一，通过JVM的-javaagent参数设置LTW的织入器类包，以代理JVM默认的类加载器； 第二，LTW织入器需要一个 aop.xml文件，在该文件中指定切面类和需要进行切面织入的目标类。
     * <li>（3）AspectJ同样也支持运行时织入，运行时织入是基于动态代理的机制。（默认机制）
     */
    void aspectJ();

    /**
     * <h2>Spring AOP</h2>是aop实现方案的一种，它支持在运行期基于动态代理的方式将aspect织入目标代码中来实现aop。 但是spring
     * AOP的切入点支持有限，而且对于static方法和final方法都无法支持aop（因为此类方法无法生成代理类）； 另外spring AOP只支持对于ioc容器管理的bean，其他的普通java类无法支持aop。
     * 现在的spring整合了aspectj，在spring体系中可以使用aspectj语法来实现aop。
     */
    void springAOP();

    /**
     * <h2>Pointcut</h2> 是指那些方法需要被执行"AOP",是由"Pointcut Expression"来描述的。
     * 切入点表达式，Pointcut的定义包括两个部分：Pointcut表示式(expression)和Pointcut签名(signature)。
     * 
     * Pointcut可以有下列方式来定义或者通过 && || 和 ! 的方式进行组合.
     * args()、@args()、execution()、this()、target()、@target()、within()、@within()、@annotation 其中 execution 是用的最多的,其格式为:
     * 
     * <pre>
     * execution(modifier-pattern? ret-type-pattern declaring-type-pattern? name-pattern(param-pattern ) throws-pattern?)
     * </pre>
     * 
     * <li>修饰符匹配（modifier-pattern?）、
     * <li>返回值匹配（ret-type-pattern）、
     * <li>类路径匹配（declaring-type-pattern?）、
     * <li>方法名匹配（name-pattern）、
     * <li>参数匹配（(param-pattern)）、
     * <li>异常类型匹配（throws-pattern?）
     * <p>
     * <li>其中后面跟着“?”的是可选项。
     * <li>在各个pattern中可以使用“*”来表示匹配所有。
     * <li>在(param-pattern)中，可以指定具体的参数类型，多个参数间用“,”隔开，各个也可以用“*”来表示匹配任意类型的参数。
     * 如(String)表示匹配一个String参数的方法；(*,String)表示匹配有两个参数的方法，第一个参数可以是任意类型，而第二个参数是String类型；可以用(..)表示零个或多个任意参数。
     * 
     * <h3>举例</h3>
     * <li>任意公共方法的执行：execution(public * *(..))
     * <li>任何一个以“set”开始的方法的执行： execution(* set*(..))
     * <li>AccountService 接口的任意方法的执行： execution(* com.xyz.service.AccountService.*(..))
     * <li>定义在service包里的任意方法的执行： execution(* com.xyz.service.*.*(..))
     * <li>定义在service包和所有子包里的任意类的任意方法的执行： execution(* com.xyz.service..*.*(..))
     * <li>定义在pointcutexp包和所有子包里的JoinPointObjP类的任意方法的执行： execution(* com.pointcutexp..JoinPointObjP.*(..))")
     * <li>pointcutexp包里的任意类. within(com.aop.pointcutexp.*)
     * <li>pointcutexp包和所有子包里的任意类. within(com.aop.pointcutexp..*)
     * <li>实现了MyInterface接口的所有类,如果MyInterface不是接口,限定MyInterface单个类. this(com.pointcutexp.MyInterface)
     * <li>带有@MyTypeAnnotation标注的所有类的任意方法:@within(com.elong.annotation.MyTypeAnnotation) @target(com.elong.annotation.MyTypeAnnotation)
     * <li>带有@MyTypeAnnotation标注的任意方法:@annotation(com.elong.annotation.MyTypeAnnotation)
     * <b>@within和@target针对类的注解,@annotation是针对方法的注解</b>
     * <li>参数带有@MyMethodAnnotation标注的方法.@args(com.elong.annotation.MyMethodAnnotation)
     * <li>参数为String类型(运行是决定)的方法. args(String)
     * 
     */
    void pointCut();
}
