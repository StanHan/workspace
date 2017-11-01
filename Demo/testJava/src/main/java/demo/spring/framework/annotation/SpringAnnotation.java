package demo.spring.framework.annotation;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import demo.spring.service.IActions;
import demo.vo.Person;

/**
 * Spring常用注解汇总 :
 * <li>@Configuration把一个类作为一个IoC容器，它的某个方法头上如果注册了@Bean，就会作为这个Spring容器中的Bean。
 * <li>@Scope注解 作用域
 * <li>@Lazy(true) 表示延迟初始化
 * <li>@Service用于标注业务层组件、
 * <li>@Controller用于标注控制层组件（如struts中的action）
 * <li>@Repository用于标注数据访问组件，即DAO组件。
 * <li>@Component泛指组件，当组件不好归类的时候，我们可以使用这个注解进行标注。
 * <li>@Scope用于指定scope作用域的（用在类上）
 * <li>@PostConstruct用于指定初始化方法（用在方法上）
 * <li>@PreDestory用于指定销毁方法（用在方法上）
 * <li>@DependsOn：定义Bean初始化及销毁时的顺序
 * <li>@Primary：自动装配时当出现多个Bean候选者时，被注解为@Primary的Bean将作为首选者，否则将抛出异常
 * <li>@Autowired 默认按类型装配，如果我们想使用按名称装配，可以结合@Qualifier注解一起使用。如下：
 * <li>@Autowired @Qualifier("personDaoBean") 存在多个实例配合使用
 * <li>@Resource默认按名称装配，当找不到与名称匹配的bean才会按类型装配。
 * <li>@PostConstruct 初始化注解
 * <li>@PreDestroy 摧毁注解 默认 单例 启动就加载
 * <li>@Async异步方法调用
 * 
 * 告诉spring一下我要使用注解了，告诉的方式有很多，<context:component-scan base-package="xxx[,pagkage2,…,pagkageN]"
 * />是一种最简单的，spring会自动扫描xxx路径下的注解。
 * 如果某个类的头上带有特定的注解【@Component/@Repository/@Service/@Controller】，就会将这个对象作为Bean注册进Spring容器。 也可以在<context:component-scan
 * base-package=” ”/>中指定多个包
 * 
 * @author hanjy
 *
 */

public class SpringAnnotation {

    /**
     * Autowired顾名思义，就是自动装配，其作用是为了消除代码Java代码里面的getter/setter与bean属性中的property。
     * Autowired默认按类型匹配的方式，在容器查找匹配的Bean，当有且仅有一个匹配的Bean时，Spring将其注入@Autowired标注的变量中。
     * 假如bean.xml里面有两个property，类里面也使用@Autowired注解标注这两个属性那会怎么样？ 答案是Spring会按照xml优先的原则去类中寻找这两个属性的getter/setter
     * 
     * 如果容器中有一个以上匹配的Bean，则可以通过@Qualifier注解限定Bean的名称，
     */
    @Autowired(required = false)
    @Qualifier("bmwCar")
    private IActions actions;

    /**
     * Resource注解与@Autowired注解作用非常相似
     * 
     * 说一下@Resource的装配顺序：
     * <ol>
     * <li>(1)、@Resource后面没有任何内容，默认通过name属性去匹配bean，找不到再按type去匹配
     * <li>(2)、指定了name或者type则根据指定的类型去匹配bean
     * <li>(3)、指定了name和type则根据指定的name和type去匹配bean，任何一个不匹配都将报错
     * </ol>
     * 
     * 然后，区分一下@Autowired和@Resource两个注解的区别：
     * <li>(1)、@Autowired默认按照byType方式进行bean匹配，@Resource默认按照byName方式进行bean匹配
     * <li>(2)、@Autowired是Spring的注解，@Resource是J2EE的注解，这个看一下导入注解的时候这两个注解的包名就一清二楚了
     */
    @Resource
    private Person person;

}

/**
 * Service注解，其实做了两件事情：
 * <li>(1)、声明该类是一个bean，这点很重要，因为该类是一个bean，其他的类才可以使用@Autowired将Zoo作为一个成员变量自动注入。
 * <li>(2)、该类在bean中的id是"serviceImpl"，即类名且首字母小写。也可以指定名字，这样就可以通过ApplicationContext的getBean("xx")方法来得到了。
 * 
 * 因为Spring默认产生的bean是单例的，假如我不想使用单例怎么办，xml文件里面可以在bean里面配置scope属性。
 * 注解也是一样，配置@Scope即可，默认是"singleton"即单例，"prototype"表示原型即每次都会new一个新的出来。
 */
@Service("serviceImpl")
@Scope("prototype")
class ServiceImpl {

}

/**
 * Component是所有受Spring 管理组件的通用形式，@Component注解可以放在类的头上，@Component不推荐使用。
 *
 */
@Component
class ComponentImpl {

}

/**
 * Controller对应表现层的Bean，也就是Action
 *
 */
@Controller
@Scope("prototype")
class ControllerImpl {

}

/**
 * Repository对应数据访问层Bean，用于将数据访问层 (DAO 层 ) 的类标识为 Spring Bean。
 *  为什么@Repository 只能标注在 DAO 类上呢？
 * 这是因为该注解的作用不只是将类识别为Bean，同时它还能将所标注的类中抛出的数据访问异常封装为 Spring 的数据访问异常类型。
 * Spring本身提供了一个丰富的并且是与具体的数据访问技术无关的数据访问异常结构，用于封装不同的持久层框架抛出的异常，使得异常独立于底层的框架。
 * 
 *
 */
@Repository(value = "userDao")
class RepositoryImpl {

}
