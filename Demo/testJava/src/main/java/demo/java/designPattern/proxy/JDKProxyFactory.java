package demo.java.designPattern.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JDKProxyFactory implements InvocationHandler {

    /**
     * 要处理的对象(也就是我们要在方法的前后加上业务逻辑的对象,如例子中的PersonServiceBean)
     */
    private Object targetObject;

    /**
     * 
     * 动态生成目标对象的代理
     * 
     * @paramtargetObject
     * @return
     * 
     */

    public Object createProxyIntance(Object targetObject) {
        this.targetObject = targetObject;
        return Proxy.newProxyInstance(this.targetObject.getClass().getClassLoader(),
                this.targetObject.getClass().getInterfaces(), this);

    }

    /**
     * 
     * 要处理的对象中的每个方法会被此方法送去JVM调用,也就是说,要处理的对象的方法只能通过此方法调用
     * 
     */

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable { // 环绕通知
        Object bean = this.targetObject;

        Object result = null;

        //
        System.out.println("before advice()-->前置通知");

        try {
            result = method.invoke(targetObject, args);
            System.out.println("after advice() -->后置通知");
        } catch (RuntimeException e) {
            System.out.println("exception advice()--> 例外通知");
        } finally {
            System.out.println("finally advice(); -->最终通知");
        }

        return result;
    }
}
