package demo.spring.aop;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.cglib.CglibDemoMethodInterceptor;
import demo.java.lang.instrument.MyClassFileTransformer;
import demo.java.lang.reflect.LogInvocationHandler;
import demo.java.lang.reflect.UserDaoStaticProxyDemo;
import demo.javassist.TranslatorDemo;
import demo.spring.service.IActions;
import demo.spring.service.IUserDAO;
import demo.spring.service.impl.DemoService;
import demo.spring.service.impl.UserDAO;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.Loader;
import javassist.NotFoundException;

/**
 * <h1>AOP(aspect object programming)面向切面编程</h1> 我们可以从以下几个层面来实现AOP：
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
 * <p>
 * 通知（Advice）类型
 * <li>@Before 前置通知（Before advice） ：在某连接点（JoinPoint）之前执行的通知，但这个通知不能阻止连接点前的执行。
 * <li>@After 后通知（After advice） ：当某连接点退出的时候执行的通知（不论是正常返回还是异常退出）。
 * <li>@AfterReturning 返回后通知（After return advice） ：在某连接点正常完成后执行的通知，不包括抛出异常的情况。
 * <li>@Around 环绕通知（Around advice） ：包围一个连接点的通知，类似Web中Servlet规范中的Filter的doFilter方法。可以在方法的调用前后完成自定义的行为，也可以选择不执行。
 * <li>@AfterThrowing 抛出异常后通知（After throwing advice） ： 在方法抛出异常退出时执行的通知。
 * 
 * <h>AOP功能</h2>
 * 
 * <li>性能监控：在方法调用前后记录调用时间，方法执行太长或超时报警
 * <li>缓存代理：缓存某方法的返回值，下次执行该方法时，直接从缓存里获取
 * <li>软件破解：使用AOP修改软件的验证类的判断逻辑
 * <li>记录日志：在方法执行前后记录系统日志
 * <li>工作流系统：工作流系统需要将业务代码和流程引擎代码混合在一起执行，那么我们可以使用AOP将其分离，并动态挂接业务
 * <li>权限验证：方法执行前验证是否有权限执行当前方法，没有则抛出没有权限执行异常，由业务代码捕捉
 * 
 * <h2>Spring的AOP</h2>
 * 
 * Spring默认采取动态代理机制实现AOP，当动态代理不可用时（代理类无接口）会使用cglib机制。但Spring的AOP有一定的缺点：
 * <li>第一，只能对方法进行切入，不能对接口、字段、静态代码块进行切入（切入接口的某个方法，则该接口下所有实现类的该方法都将被切入）
 * <li>第二，同类中的互相调用方法将不会使用代理类。因为要使用代理类必须从Spring容器中获取Bean
 * <li>第三，性能不是最好的。从前面几节得知，我们自定义的类加载器，性能优于动态代理和cglib
 */
public class AopDemo {

    public static void main(String[] args) {
        testSpringDynamicProxy();
    }

    static void test() {
        ApplicationContext cxt = new ClassPathXmlApplicationContext("spring/beans.xml");
        IActions personService = (IActions) cxt.getBean("personService");
        personService.say("hello aop.");
    }

    /**
     * 目标对象有实现接口，spring会自动选择"jdk代理【动态代理】",动态代理的标识：class com.sun.proxy.$Proxy10
     */
    static void testSpringDynamicProxy() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring/testAopBean.xml");
        IActions dao = (IActions) ac.getBean("actions");
        System.err.println(dao.getClass());
        dao.say("hello aop.");
    }

    /**
     * 目标对象没有实现接口，spring会用"cglib代理哦"，标识：class **$$EnhancerByCGLIB$$4952a60a
     */
    static void testSpringCglib() {
        ApplicationContext ac = new ClassPathXmlApplicationContext("spring/testAopBean.xml");
        DemoService dao = (DemoService) ac.getBean("demoService");
        System.err.println(dao.getClass());
        dao.say("hello aop.");
    }

    /**
     * <h2>使用Instrumentation和javassist来实现AOP</h2>
     * 
     * 自定义类加载器实现AOP只能拦截自己加载的字节码，那么有一种方式能够监控所有类加载器加载的字节码吗？
     * 有，使用Instrumentation，它是Java5的新特性，使用Instrument，开发者可以构建一个字节码转换器，在字节码加载前进行转换。
     * <h3>配置和执行</h3>
     * 
     * 需要告诉JVM在启动main函数之前，需要先执行premain函数。
     * 
     * <li>首先，需要将premain函数所在的类打成jar包，并修改jar包里的META-INF\MANIFEST.MF文件 <code>
    Manifest-Version: 1.0 
    Premain-Class: bci. MyClassFileTransformer
     </code>
     * <li>其次，在JVM的启动参数里加上-javaagent:D:\java\projects\opencometProject\Aop\lib\aop.jar
     */
    static void testInstrumentation() {
        MyClassFileTransformer.premain("", null);
    }

    /**
     * <h2>自定义类加载器</h2>
     * 
     * 如果我们实现了一个自定义类加载器，在类加载到JVM之前直接修改某些类的方法，并将切入逻辑织入到这个方法里，然后将修改后的字节码文件交给虚拟机运行，那岂不是更直接。
     * Javassist是一个编辑字节码的框架，可以让你很简单地操作字节码。它可以在运行期定义或修改Class。使用Javassist实现AOP的原理是在字节码加载前直接修改需要切入的方法。
     * 这比使用cglib实现AOP更加高效，并且没有太多限制：我们使用类加载器启动我们自定义的类加载器，在这个类加载器里加一个类加载监听器，监听器发现目标类被加载时就织入切入逻辑。
     * 使用自定义的类加载器实现AOP在性能上有优于动态代理和cglib，因为它不会产生新类，但是它仍人存在一个问题，就是如果其他的类加载器来加载类的话，这些类就不会被拦截。
     */
    static void testJavassistAop() throws Throwable {
        // 获取存放CtClass的容器ClassPool
        ClassPool classPool = ClassPool.getDefault();
        // 创建一个类加载器
        Loader loader = new Loader();
        // 增加一个转换器
        try {
            loader.addTranslator(classPool, new TranslatorDemo());
        } catch (NotFoundException | CannotCompileException e) {
            e.printStackTrace();
        }
        // 启动MyTranslator的main函数
        try {
            loader.run("demo.javassist.TranslatorDemo", null);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 动态代理：即在运行期动态创建代理类，使用动态代理实现AOP需要4个角色：
     * 
     * <li>被代理的类：即AOP里所说的目标对象
     * <li>被代理类的接口
     * <li>织入器：使用接口反射机制生成一个代理类，在这个代理类中织入代码
     * <li>InvocationHandler切面：切面，包含了Advice和Pointcut
     * <p>
     * 动态代理在运行期通过接口动态生成代理类，这为其带来了一定的灵活性，但这个灵活性却带来了两个问题：
     * <li>第一，代理类必须实现一个接口，如果没实现接口会抛出一个异常
     * <li>第二，性能影响，因为动态代理是使用反射机制实现的，首先反射肯定比直接调用要慢，其次使用反射大量生成类文件可能引起full gc。
     * 因为字节码文件加载后会存放在JVM运行时方法区（或者叫永久代、元空间）中，当方法区满时会引起full gc， 所以当你大量使用动态代理时，可以将永久代设置大一些，减少full gc的次数。
     */
    static void testDynamicProxy() {
        // 需要代理的类接口，被代理类实现的多个接口都必须在这这里定义
        Class[] proxyInterface = new Class[] { IUserDAO.class };
        IUserDAO userDAO = new UserDAO();
        // 构建AOP的Advice，这里需要传入业务类的实例
        InvocationHandler invocationHandler = new LogInvocationHandler(userDAO);
        // 生成代理类的字节码加载器
        ClassLoader classLoader = IUserDAO.class.getClassLoader();
        // 织入器，织入代码并生成代理类 。动态创建代理对象，用于代理一个AbstractUserDAO类型的真实主题对象
        IUserDAO proxy = (IUserDAO) Proxy.newProxyInstance(classLoader, proxyInterface, invocationHandler);
        proxy.findUserById("张无忌"); // 调用代理对象的业务方法
    }

    /**
     * 动态代理的核心其实就是代理对象的生成，即Proxy.newProxyInstance(classLoader, proxyInterface, handler)。 核心代码就三行：
     * <li>获取代理类
     * <li>获取带有InvocationHandler参数的构造方法
     * <li>把handler传入构造方法生成实例
     */
    static void testDynamicProxy2() {
        IUserDAO userDAO = new UserDAO();
        // 构建AOP的Advice，这里需要传入业务类的实例
        InvocationHandler invocationHandler = new LogInvocationHandler(userDAO);
        // 获取代理类
        Class<?> proxyClass = Proxy.getProxyClass(IUserDAO.class.getClassLoader(), IUserDAO.class);
        // 获取带有InvocationHandler参数的构造方法
        Constructor<?> constructor = null;
        try {
            constructor = proxyClass.getConstructor(InvocationHandler.class);
        } catch (NoSuchMethodException | SecurityException e1) {
            e1.printStackTrace();
        }
        // 把handler传入构造方法生成实例
        IUserDAO userDao = null;
        try {
            userDao = (IUserDAO) constructor.newInstance(invocationHandler);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            e.printStackTrace();
        }
        userDao.findUserById("张三丰"); // 调用代理对象的业务方法
    }

    /**
     * 静态代理：由程序员创建或特定工具自动生成源代码，再对其编译。在程序运行前，代理类的.class文件就已经存在了
     */
    static void testStaticProxy() {
        IUserDAO userDAO = new UserDAO();
        IUserDAO staticProxy = new UserDaoStaticProxyDemo(userDAO);
        staticProxy.findUserById("张无忌");
    }

    /**
     * CGLIB动态字节码生成。使用动态字节码生成技术实现AOP原理是在运行期间目标字节码加载后，生成目标类的子类，将切面逻辑加入到子类中，所以cglib实现AOP不需要基于接口。
     * CGLIB是一个强大的、高性能的Code生成类库，它可以在运行期间扩展Java类和实现Java接口，它封装了Asm，所以使用cglib前需要引入Asm的jar
     */
    static void testCglibProxy() {
        CglibDemoMethodInterceptor cglibProxy = new CglibDemoMethodInterceptor();
        UserDAO userDao = (UserDAO) cglibProxy.createProxy(new UserDAO());
        userDao.findUserById("张无忌");
    }

}
