package demo.aspectJ;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

/**
 * <h2>@AspectJ的配置</h2> 配置@AspectJ，是为了以便被Bean容器发现。对@AspectJ的支持可以使用XML文件配置或Java风格的配置，其中，@AspectJ注解风格类似于纯Java注解的普通Java类。
 * <li>注解形式, 如：AspectJConfig.java
 * <li>XML配置形式 ,如：aop.xml
 * <p>
 * AOP的运行时仍旧是纯的Spring AOP，对AspectJ的编译器或者织入无依赖性，但是要使用AspectJ，必须引入aspectjweaver.jar库。
 * 
 * 有几点需要了解：
 * <li>（1）@Aspect切面使用@Aspect注解配置，拥有@Aspect的任何bean将被Spring自动识别并应用，可以拥有aop配置的一切特点。
 * <li>（2）用@Aspect注解的类可以有方法和字段，他们也可能包括切入点pointcut，通知Advice和引入Introduction声明。
 * <li>（3）@Aspect注解不能通过类路径被自动检测发现，需要配合使用@Component注释或在xml文件中配置对应的bean项。
 * <p>
 * 两种实现:
 * <li>注解形式，如：LogAspect.class
 * <li>XML配置形式 ,如：aop.xml
 * <p>
 * 需要注意的是，一个类的@Aspect注解只标识该类是一个切面，同时将自己从自动代理中排除出去了，其目的是为了避免出现死循环
 * （例如一个包下包含了业务类和切面类，将自己从自动代理中排除出去，避免了自己代理自己的情况一直寻找自己的循环情况）
 * 
 * <h2>Pointcut注解</h2>
 * <li>一般pointcut:一个切入点通过一个普通的方法定义来提供，并且切入点表达式使用@Pointcut注解来声明，注解的方法返回类型必须为void型。
 * <li>组合pointcut: 有时候根据实际情况，要建立复杂的切入点表达式，可以通过&&、||和 ! 进行组合，也可以通过名字引用切入点表达式。
 * 
 * <h2>Advice注解</h2>
 * <li>1 Before Advice：@Before
 * <li>2.After returning advice：@AfterReturning，可在通知体内得到返回的实际值；
 * <li>3.After throwing advice：@AfterThrowing
 * <li>4.After (finally) advice :@After 最终通知必须准备处理正常和异常两种返回情况，它通常用于释放资源。
 * <li>5.Around advice :@Around 环绕通知使用@Around注解来声明，通知方法的第一个参数必须是ProceedingJoinPoint类型，
 * 在通知内部调用ProceedingJoinPoint的Proceed()方法会导致执行真正的方法，传入一个Object[]对象，数组中的值将被作为一个参数传递给方法。
 */
@Configuration
@EnableAspectJAutoProxy
public class AspectJConfig {

}

