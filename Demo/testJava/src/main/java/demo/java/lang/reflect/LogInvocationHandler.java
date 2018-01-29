package demo.java.lang.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 打印日志的切面。
 * <p>
 * 自定义请求处理程序类. 动态代理类只能代理接口，代理类都需要实现InvocationHandler类，实现invoke方法。
 * 该invoke方法就是调用被代理接口的所有方法时需要调用的，该invoke方法返回的值是被代理接口的一个实现类
 *
 */
public class LogInvocationHandler implements InvocationHandler {

    private Object target;// 目标对象

    // 自定义有参构造函数，用于注入一个需要提供代理的真实主题对象
    public LogInvocationHandler(Object object) {
        this.target = object;
    }

    // 实现invoke()方法，调用在真实主题类中定义的方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        beforeInvoke();
        Object result = method.invoke(target, args); // 转发调用
        afterInvoke();
        return result;
    }

    // 记录方法调用时间
    public void beforeInvoke() {
        System.out.println("调用开始时间：" + System.currentTimeMillis());
    }

    public void afterInvoke() {
        System.out.println("调用结束时间：" + System.currentTimeMillis());
    }
}
