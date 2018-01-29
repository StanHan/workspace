package demo.spring.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.spring.service.IActions;
import demo.spring.service.impl.DemoService;

/**
 * Aop(aspect object programming)面向切面编程。我们可以从以下几个层面来实现AOP：
 * <li>在编译期修改源代码
 * <li>在运行期字节码加载前修改字节码
 * <li>在运行期字节码加载后动态创建代理类的字节码
 * <p>
 * <h2>AOP各种实现机制的比较:</h2>
 * <li>静态AOP。机制: 静态织入。原理：在编译期，切面直接以字节码的形式编译到目标字节码文件中。优点： 对系统无性能影响。缺点： 灵活性不够
 * <li>动态AOP。机制: 动态代理 。原理： 在运行期，目标类加载后，为接口动态生成代理类，将切面织入到代理类中 。优点： 相对于静态AOP更加灵活 。缺点：切入的关注点需要实现接口，对系统有一点性能影响
 * <li>动态字节码生成。机制: CGLIB。原理： 在运行期，目标类加载后，动态构建字节码文件生成目标类的子类，将切面逻辑加入到子类中 。优点：没有接口也可以织入 。缺点： 扩展类的实例方法为final时，则无法进行织入
 * <li>自定义类加载器 。原理： 在运行期，目标加载前，将切面逻辑加到目标字节码里 。优点：可以对绝大部分类进行织入 。缺点：代码中如果使用了其他类加载器，则这些类将不会被织入
 * <li>字节码转换 。原理： 在运行期，所有类加载器加载字节码前进行拦截 。优点： 可以对所有类进行织入
 * <p>
 * <h2>AOP术语：</h2>
 * <p>
 * <b>织入器通过在切面中定义Pointcut来搜索目标（被代理类）的JoinPoint（切入点），然后把要切入的逻辑（Advice）织入到目标对象里，生成代理类</b>
 * <li>Joinpoint：拦截点，如某个业务方法。所谓连接点是指那些被拦截到的点。在Spring中，这些点指的是方法，因为spring只支持方法类型的连接点，实际上joinpoint还可以是field或类构造器
 * <li>Pointcut：Joinpoint的表达式，表示拦截哪些方法。一个Pointcut对应多个Joinpoint。
 * <li>Advice：要切入的逻辑。所谓的通知是指拦截到joinpoint之后所要做的事情。通知分为前置通知，后置通知，异常通知，最终通知，环绕通知
 * <li>Before Advice：在方法前切入
 * <li>After Advice：在方法后切入，抛出异常则不会切入
 * <li>After Returning Advice：在方法返回后切入，抛出异常则不会切入
 * <li>After Throwing Advice：在方法抛出异常时切入
 * <li>Around Advice：在方法执行前后切入，可以中断或忽略原有流程的执行
 * <li>Aspect（切面）：指横切面关注点的抽象即为切面，它与类相似，只是两者的关注点不一样，类是对物体特征的抽象，而切面是横切性关注点的抽象
 * <li>Target（目标对象）：代理的目标对象
 * <li>Weave（织入）：指将Aspect应用到Target对象并且导致proxy对象创建的过程称为织入
 * <li>Introduction（引入）：在不修改类代码的前提下，introduction可以在运行期为类动态的添加一些方法或field
 * <li>AOP代理（AOP Proxy） 在Spring AOP中有两种代理方式，JDK动态代理和CGLIB代理。默认情况下，TargetObject实现了接口时，则采用JDK动态代理；反之，采用CGLIB代理。
 * <p>
 * 切入点表达式，Pointcut的定义包括两个部分：Pointcut表示式(expression)和Pointcut签名(signature)。让我们先看看execution表示式的格式：
 * <li>execution(modifier-pattern? ret-type-pattern declaring-type-pattern? name-pattern(param-pattern) throws-pattern?)
 * <p>
 * pattern分别表示
 * <li>修饰符匹配（modifier-pattern?）、
 * <li>返回值匹配（ret-type-pattern）、
 * <li>类路径匹配（declaring-type-pattern?）、
 * <li>方法名匹配（name-pattern）、
 * <li>参数匹配（(param-pattern)）、
 * <li>异常类型匹配（throws-pattern?）
 * <p>
 * 其中后面跟着“?”的是可选项。在各个pattern中可以使用“*”来表示匹配所有。在(param-pattern)中，可以指定具体的参数类型，多个参数间用“,”隔开，各个也可以用“*”来表示匹配任意类型的参数。
 * 如(String)表示匹配一个String参数的方法；(*,String)表示匹配有两个参数的方法，第一个参数可以是任意类型，而第二个参数是String类型；可以用(..)表示零个或多个任意参数。 现在来看看几个例子：
 * <li>1）execution(* *(..)) 表示匹配所有方法
 * <li>2）execution(public * com. savage.service.UserService.*(..)) 表示匹配com.savage.server.UserService中所有的公有方法
 * <li>3）execution(* com.savage.server..*.*(..)) 表示匹配com.savage.server包及其子包下的所有方法
 * 除了execution表示式外，还有within、this、target、args等Pointcut表示式。一个Pointcut定义由Pointcut表示式和Pointcut签名组成，例如：
 * 
 * 
 * <p>
 * 通知（Advice）类型
 * <li>@Before 前置通知（Before advice） ：在某连接点（JoinPoint）之前执行的通知，但这个通知不能阻止连接点前的执行。
 * <li>@After 后通知（After advice） ：当某连接点退出的时候执行的通知（不论是正常返回还是异常退出）。
 * <li>@AfterReturning 返回后通知（After return advice） ：在某连接点正常完成后执行的通知，不包括抛出异常的情况。
 * <li>@Around 环绕通知（Around advice） ：包围一个连接点的通知，类似Web中Servlet规范中的Filter的doFilter方法。可以在方法的调用前后完成自定义的行为，也可以选择不执行。
 * <li>@AfterThrowing 抛出异常后通知（After throwing advice） ： 在方法抛出异常退出时执行的通知。
 */
public class AopDemo {

    public static void main(String[] args) {
        testJavaDynamicProxy();
    }

    static void test() {
        ApplicationContext cxt = new ClassPathXmlApplicationContext("spring/beans.xml");
        IActions personService = (IActions) cxt.getBean("personService");
        personService.say("hello aop.");
    }

    /**
     * 目标对象有实现接口，spring会自动选择"jdk代理【动态代理】",动态代理的标识：class com.sun.proxy.$Proxy10
     */
    static void testJavaDynamicProxy() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring/testAopBean.xml");
        IActions dao = (IActions) ac.getBean("actions");
        System.err.println(dao.getClass());
        dao.say("hello aop.");
    }

    /**
     * 目标对象没有实现接口，spring会用"cglib代理哦"，标识：class **$$EnhancerByCGLIB$$4952a60a
     */
    static void testCglib() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring/testAopBean.xml");
        DemoService dao = (DemoService) ac.getBean("demoService");
        System.err.println(dao.getClass());
        dao.say("hello aop.");
    }

}
