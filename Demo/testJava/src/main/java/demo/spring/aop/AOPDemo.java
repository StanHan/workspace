package demo.spring.aop;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.spring.service.IActions;

/**
 * AOP术语：
 * 
 * <li>Aspect（切面）：指横切面关注点的抽象即为切面，它与类相似，只是两者的关注点不一样，类是对物体特征的抽象，而切面是横切性关注点的抽象
 * <li>Joinpoint（连接点）：所谓连接点是指那些被拦截到的点。在Spring中，这些点指的是方法，因为spring只支持方法类型的连接点，实际上joinpoint还可以是field或类构造器
 * <li>Pointcut（切入点）：所谓的切入点是指我们要对那些joinpoint进行拦截的定义
 * <li>Advice（通知）：所谓的通知是指拦截到joinpoint之后所要做的事情就是通知。通知分为前置通知，后置通知，异常通知，最终通知，环绕通知
 * <li>Target（目标对象）：代理的目标对象
 * <li>Weave（织入）：指将aspects应用到target对象并且导致proxy对象创建的过程称为织入
 * <li>Introduction（引入）：在不修改类代码的前提下，introduction可以在运行期为类动态的添加一些方法或field
 * 
 * @author hanjy
 *
 */
public class AOPDemo {

    public static void main(String[] args) {
        ApplicationContext cxt = new ClassPathXmlApplicationContext("spring/bean.xml");

        IActions personService = (IActions) cxt.getBean("personService");

        personService.say("hello aop.");

    }

}
