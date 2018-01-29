package demo.java.lang.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

import demo.cglib.CglibDemoMethodInterceptor;
import demo.java.lang.instrument.MyClassFileTransformer;
import demo.javassist.TranslatorDemo;
import demo.spring.service.IUserDAO;
import demo.spring.service.impl.UserDAO;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.Loader;
import javassist.NotFoundException;

public class ProxyDemo {

    public static void main(String[] args) throws Throwable {
        testJavassistAop();
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
    static void test() {
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
