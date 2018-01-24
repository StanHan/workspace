package demo.spring.ioc;

import java.beans.PropertyDescriptor;

import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import demo.spring.framework.beans.factory.StudentBean;

/**
 * 控制反转 (IoC：Inversion Of Control) 。 IoC 有时也被称为依赖注入 (DI:Dependence Injection)。 控制权由应用代码中转到了外部容器，控制权的转移，就是所谓反转。
 * 2004年，Martin Fowler探讨了同一个问题，既然IOC是控制反转，那么到底是“哪些方面的控制被反转了呢？”，经过详细地分析和论证后，他得出了答案：“获得依赖对象的过程被反转了”。
 * 控制被反转之后，获得依赖对象的过程由自身管理变为了由IOC容器主动注入。于是，他给“控制反转”取了一个更合适的名字叫做“依赖注入（Dependency
 * Injection）”。他的这个答案，实际上给出了实现IOC的方法：注入。所谓依赖注入，就是由IOC容器在运行期间，动态地将某种依赖关系注入到对象之中。
 * 所以，依赖注入(DI)和控制反转(IOC)是从不同的角度的描述的同一件事情，就是指通过引入IOC容器，利用依赖关系注入的方式，实现对象之间的解耦。
 * 
 * IOC中最基本的技术就是“反射(Reflection)”编程 。通俗的说反射就是根据给出的类名（字符串）来生成对象。这种编程方式可以让对象 在生成时才决定要生成哪一种对象。
 * 反射编程方式相对于正常的对象生成方式要慢。经SUN改良优化后，反射方式生成对象和通常对象生成方式，速度已经相差不大了（但依然有一倍以上的差距）
 * 
 * org.springframework.context.ApplicationContext 代表 Spring IoC 容器，并负责实例化，配置和组装上述 bean 的接口。
 * 容器是通过对象实例化，配置，和读取配置元数据汇编得到对象的构建。配置元数据可以是用 XML，Java 注解，或 Java 代码来展示。它可以让你描述组成应用程序的对象和对象间丰富的相互依赖。
 * <p>
 * IoC容器主要作用就是创建并管理Bean对象以及Bean属性注入。通过读取Bean配置文件生成相应对象。
 * 这些配置文件格式可能多种多样，xml、properties等格式，所以需要将其转换（ResourceLoader/Resolver）成统一资源对象（Resource）。
 * 这些配置文件存储的位置也不一样，可能是ClassPath，可能是FileSystem，也可能是URL路径，路径的不一样，说明的是应用环境可能不一样。
 * 获得Resource对象，还要将其转换成（BeanDefinitionReader）Spring内部对Bean的描述对象（BeanDefinition）。
 * 然后，将其注册（BeanRegister）到容器中（BeanFactory），供以后转换成Bean对象使用。
 * <p>
 * 所以，IoC容器包括了很多东西，但主要有以下六个组件：
 * <li>1.资源组件：Resource，对资源文件的描述，不同资源文件如xml、properties文件等，格式不同，最终都将被ResourceLoader加载获得相应的Resource对象；
 * <li>2.资源加载组件：ResourceLoader：加载xml、properties等各类格式文件，解析文件，并生成Resource对象。
 * <li>3.Bean容器组件：BeanFactory体系：IoC容器的核心，其他组件都是为它工作的（但不是仅仅为其服务）.
 * <li>4.Bean注册组件：SingletonBeanRegister/AliasRegister：将BeanDefinition对象注册到BeanFactory（BeanDefinition Map）中去。
 * <li>5.Bean描述组件：BeanDefinition体系，Spring内部对Bean描述的基本数据结构
 * <li>6.Bean构造组件：BeanDefinitionReader体系，读取Resource并将其数据转换成一个个BeanDefinition对象。
 * <p>
 * 生命周期执行的过程如下:
 * <li>1)spring对bean进行实例化,默认bean是单例
 * <li>2)spring对bean进行依赖注入
 * <li>3)如果bean实现了BeanNameAware接口,spring将bean的id传给setBeanName()方法
 * <li>4)如果bean实现了BeanFactoryAware接口,spring将调用setBeanFactory方法,将BeanFactory实例传进来
 * <li>5)如果bean实现了ApplicationContextAware()接口,spring将调用setApplicationContext()方法将应用上下文的引用传入
 * <li>6)如果bean实现了BeanPostProcessor接口,spring将调用它们的postProcessBeforeInitialization接口方法
 * <li>7)如果bean实现了InitializingBean接口,spring将调用它们的afterPropertiesSet接口方法,类似的如果bean使用了init-method属性声明了初始化方法,改方法也会被调用
 * <li>8)如果bean实现了BeanPostProcessor接口,spring将调用它们的postProcessAfterInitialization接口方法
 * <li>9)此时bean已经准备就绪,可以被应用程序使用了,他们将一直驻留在应用上下文中,直到该应用上下文被销毁
 * <li>10)若bean实现了DisposableBean接口,spring将调用它的distroy()接口方法。同样的,如果bean使用了destroy-method属性声明了销毁方法,则该方法被调用
 * <p>
 * 单例管理的对象:
 * <li>1.默认情况下,spring在读取xml文件的时候,就会创建对象。
 * <li>2.在创建的对象的时候(先调用构造器),会去调用init-method=".."属性值中所指定的方法.
 * <li>3.对象在被销毁的时候,会调用destroy-method="..."属性值中所指定的方法.(例如调用container.destroy()方法的时候)
 * <li>4.lazy-init="true",可以让这个对象在第一次被访问的时候创建
 * <p>
 * 非单例管理的对象:
 * <li>1.spring读取xml文件的时候,不会创建对象.
 * <li>2.在每一次访问这个对象的时候,spring容器都会创建这个对象,并且调用init-method=".."属性值中所指定的方法.
 * <li>3.对象销毁的时候,spring容器不会帮我们调用任何方法,因为是非单例,这个类型的对象有很多个,spring容器一旦把这个对象交给你之后,就不再管理这个对象了.
 *
 * <h2>配置元数据</h2>
 * <p>
 * 传统的配置元数据是一个简单而直观的 XML格式，基于 XML 的元数据并不是配置元数据的唯一允许的形式。更多其他格式的元数据见：
 * <li>基于注解的配置：Spring 2.5 的推出了基于注解的配置元数据支持。
 * <li>基于Java的配置：Spring3.0 开始，由 Spring JavaConfig 项目提供了很多功能成为核心 Spring 框架的一部分。因此，你可以通过使用Java，而不是 XML文件中定义外部 bean
 * 到你的应用程序类。参阅 @Configuration，@Bean，@Import 和 @DependsOn 注解。
 * <p>
 * 基于 XML的配置，元数据显示的 bean包含于顶层元素beans元素。 Java 配置通常使用 @Configuration 类中 @Bean 注解的方法。 容器内部,这些 bean 定义表示为 BeanDefinition 对象。
 * 除 bean 定义里包含的如何创建一个特定的bean的信息, ApplicationContext 的实现还允许由用户注册现有创建在容器之外的对象。 这是通过访问 ApplicationContext 的 BeanFactory 的
 * getBeanFactory() 方法 返回 BeanFactory 的实现 DefaultListableBeanFactory 。 DefaultListableBeanFactory 支持这种通过
 * registerSingleton(..) 和registerBeanDefinition(..) 方法来注册。
 * 
 */
public class IocDemo {

    public static void main(String[] args) {

        testCyclelife();
    }

    /**
     * 这段代码非常有代表性地叙述了IOC容器使用方式：
     * <li>（1）创建配置文件的抽象资源，其中包含了BeanDefinition，比如applicationcontext.xml
     * <li>（2）然后创建BeanFactory
     * <li>（3）最后创建个读取BeanDefinition的读取器
     * <li>（4）读完配置信息后，解析过程就由XmlBeanDefinitionReader来完成。
     * <p>
     * IOC容器初始化过程： IOC容器启动包括了BeanDefinition的Resource定位，载入和注册三个过程。
     * <li>（1）、resource定位：主要是由ResourceLoader通过统一的Resource接口来完成，Resource对各种形式的BeanDefinition的使用都提供了统一的接口。这个过程就是寻找注入的数据。
     * <li>（2）、BeanDefinition的载入： 这个载入就是把用户定义好的Bean表示成IOC容器内部的数据结构，这个数据结构就是BeanDefinition。
     * <li>（3）、向IOC容器注册BeanDefinition：通过调用BeanDefinition接口的实现来完成，IOC容器内部将BeanDefinition注入到一个hashMap中，通过这个HashMap来持有BeanDefinition。
     * <p>
     * 初始化过程中，一般不包含Bean依赖注入，IOC设计中，Bean载入和依赖注入是两个独立的过程。
     * 依赖注入一般发生在应用第一次通过getBean向容器索取Bean的时候，当然，如果你使用了lazyinit属性，那么依赖注入在IOC初始化时就完成了，否则都要等到第一次使用getBean才注入。
     */
    static void demoInitIoc() {
        Resource resource = new ClassPathResource("bean.xml");
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(defaultListableBeanFactory);
        xmlBeanDefinitionReader.loadBeanDefinitions(resource);
    }

    /**
     * spring中，实际上是把DefaultListableBeanFactory作为一个默认的功能完整的IOC容器来使用的，XmlBeanFactory就是继承了它的同时，增加了一些功能。
     * 在spring中，ApplicationContext作为更高级的容器，其原理也和XmlBeanFactory一样，扩展或持有了DefaultListableBeanFactory来获得容器功能。
     */
    static void demoApplicationContext() {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring/beansDemo.xml");
        Object bean = context.getBean("beanName", Object.class);

        AutowireCapableBeanFactory beanFactory = context.getAutowireCapableBeanFactory();
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;

        BeanDefinition beanDefinition = new GenericBeanDefinition();
        defaultListableBeanFactory.registerBeanDefinition("bean1", beanDefinition);
        defaultListableBeanFactory.registerSingleton("bean2", new Object());
    }

    /**
     * 验证spring bean的生命周期
     */
    static void testCyclelife() {
        System.out.println("--------------【初始化容器】---------------");

        ApplicationContext context = new ClassPathXmlApplicationContext("spring/beansDemo.xml");
        System.out.println("-------------------【容器初始化成功】------------------");
        // 得到studentBean，并显示其信息
        StudentBean studentBean = context.getBean("studentBean", StudentBean.class);
        System.out.println(studentBean);

        System.out.println("--------------------【销毁容器】----------------------");
        ((ClassPathXmlApplicationContext) context).registerShutdownHook();
    }

    /**
     * Spring bean 生命周期。 若容器实现了如下涉及的接口，程序将按照流程进行。需要我们注意的是，这些接口并不是必须实现的，可根据自己开发中的需要灵活地进行选择，没有实现相关接口时，将略去流程图中的相关步骤。
     * 
     * <li>1. 容器启动，实例化所有实现了BeanFactoyPostProcessor接口的类。他会在任何普通Bean实例化之前加载.
     * <li>2. 实例化剩下的Bean，对这些Bean进行依赖注入。
     * <li>3. 如果Bean有实现BeanNameAware的接口那么对这些Bean进行调用
     * <li>4. 如果Bean有实现BeanFactoryAware接口的那么对这些Bean进行调用
     * <li>5. 如果Bean有实现ApplicationContextAware接口的那么对这些Bean进行调用
     * <li>6. 如果配置有实现BeanPostProcessor的Bean，那么调用它的postProcessBeforeInitialization方法
     * <li>7. 如果Bean有实现InitializingBean接口那么对这些Bean进行调用
     * <li>8. 如果Bean配置有init属性，那么调用它属性中设置的方法
     * <li>9. 如果配置有实现BeanPostProcessor的Bean，那么调用它的postProcessAfterInitialization方法
     * <li>10. Bean正常是使用
     * <li>11. 调用DisposableBean接口的destory方法
     * <li>12. 调用Bean定义的destory方法
     * <p>
     * 如果从大体上区分值分只为四个阶段
     * <li>1. BeanFactoryPostProcessor实例化
     * <li>2. Bean实例化，然后通过某些BeanFactoyPostProcessor来进行依赖注入
     * <li>3. BeanPostProcessor的调用.Spring内置的BeanPostProcessor负责调用Bean实现的接口: BeanNameAware, BeanFactoryAware,
     * ApplicationContextAware等等，等这些内置的BeanPostProcessor调用完后才会调用自己配置的BeanPostProcessor
     * <li>4. Bean销毁阶段
     * <p>
     * 接口方法的分类:
     * <li>分类类型 所包含方法：Bean自身的方法 配置文件中的init-method和destroy-method配置的方法、Bean对象自己调用的方法
     * <li>Bean级生命周期接口方法 ： BeanNameAware、BeanFactoryAware、InitializingBean、DiposableBean等接口中的方法
     * <li>容器级生命周期接口方法： InstantiationAwareBeanPostProcessor、BeanPostProcessor等后置处理器实现类中重写的方法
     */
    static void demoSpringBean() throws Exception {
        ConfigurableListableBeanFactory configurableListableBeanFactory = null;
        // 1.容器启动，实例化所有实现了BeanFactoryPostProcessor接口的类。他会在任何普通Bean实例化之前加载.
        BeanFactoryPostProcessor beanFactoryPostProcessor = null;
        // 2.执行beanFactoryPostProcessor.postProcessBeanFactory方法
        beanFactoryPostProcessor.postProcessBeanFactory(configurableListableBeanFactory);
        // 3.实例化beanPostProcessor实现类
        BeanPostProcessor beanPostProcessor = null;
        // 4.实例化InstantiationAwareBeanPostProcessor实现类
        InstantiationAwareBeanPostProcessor instantiationAwareBeanPostProcessor = null;
        // 5.执行instantiationAwareBeanPostProcessor.postProcessBeforeInstantiation方法
        instantiationAwareBeanPostProcessor.postProcessBeforeInstantiation(Object.class, "bean");
        // 6.执行Bean的构造方法
        Object bean = new Object();
        // 7.执行InstantiationAwareBeanPostProcessor.postProcessPropertyValues方法
        PropertyValues pvs = null;
        PropertyDescriptor[] pds = null;
        instantiationAwareBeanPostProcessor.postProcessPropertyValues(pvs, pds, bean, "beanName");

        // 8.为bean注入属性

        // 9.调用beanNameAware.setBeanName方法
        BeanNameAware beanNameAware = null;
        beanNameAware.setBeanName("beanName");

        // 10.调用beanFactoryAware.setBeanFactory 方法
        BeanFactoryAware beanFactoryAware = null;
        BeanFactory beanFactory = null;
        beanFactoryAware.setBeanFactory(beanFactory);
        // 11.执行beanPostProcessor.postProcessBeforeInitialization方法
        beanPostProcessor.postProcessBeforeInitialization(new Object(), "beanName");
        // 12.执行initializingBean.afterPropertiesSet方法
        InitializingBean initializingBean = null;
        initializingBean.afterPropertiesSet();
        // 13.调用<bean> init-method属性指定的方法

        // 14.执行beanPostProcessor.postProcessAfterInitialization方法
        beanPostProcessor.postProcessAfterInitialization(new Object(), "beanName");
        // 15.执行instantiationAwareBeanPostProcessor.postProcessAfterInitialization方法
        instantiationAwareBeanPostProcessor.postProcessAfterInitialization(new Object(), "beanName");
        // 16.容器初始化成功，执行业务代码。销毁容器

        // 17.调用disposableBean.destroy方法
        DisposableBean disposableBean = null;
        disposableBean.destroy();
        // 18.调用<bean> destroy-method属性指定的方法

    }

}
