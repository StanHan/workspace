package demo.cglib;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibDemoMethodInterceptor implements MethodInterceptor {

    // 要代理的原始对象
    private Object target;

    /**
     * 创建代理对象
     */
    public Object createProxy(Object target) {
        this.target = target;
        Enhancer enhancer = new Enhancer();
        // 设置代理目标
        enhancer.setSuperclass(this.target.getClass());
        // 设置回调
        enhancer.setCallback(this);
        enhancer.setClassLoader(target.getClass().getClassLoader());
        // 创建代理
        return enhancer.create();
    }

    /**
     * 在代理实例上处理方法调用并返回结果
     * 
     * @param proxy
     *            代理类
     * @param method
     *            被代理的方法
     * @param params
     *            该方法的参数数组
     * @param methodProxy
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] params, MethodProxy methodProxy) throws Throwable {
        Object result = null;
        // 调用之前
        doBefore();
        // 调用原始对象的方法
        result = methodProxy.invokeSuper(proxy, params);
        // 调用之后
        doAfter();
        return result;
    }

    private void doBefore() {
        System.out.println("调用时间：" + System.currentTimeMillis());
    }

    private void doAfter() {
        System.out.println("结束时间：" + System.currentTimeMillis());
    }

}
