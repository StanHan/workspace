package demo.aspectJ;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import demo.java.lang.annotation.MethodCache;

/**
 * 注解形式定义切面类
 *
 */
@Aspect
@Component
public class LogAspect {
    /**
     * 表示包demo.spring.service.impl下任何以Biz结尾的类的所有方法都将被执行。
     */
    @Pointcut("execution(public * demo.spring.service.impl.DemoService.*(..))")
    public void pointcut() {
    }

    /**
     * 表示当前demo.spring.service.impl包下的任何类都会匹配给这个方法。
     */
    @Pointcut("within(demo.spring.service.impl.*)")
    public void bizPointcut() {
    }

    /**
     * 前置通知使用@Before("pointcut()")，表示继承了方法pointcut（）的切入表达式.
     */
    // @Before("execution(*demo.spring.service.impl.*Service.**(..))")
    @Before("pointcut()")
    public void before() {
        System.err.println("Before");
    }

    /**
     * 返回通知使用@AfterReturning(pointcut="bizPointcut()",returning="returnValue")，
     * 表示继承了pointcut（）方法的切入表达式，并接受了业务类执行方法的返回参数returnValue。
     */
    @AfterReturning(pointcut = "bizPointcut()", returning = "returnValue")
    public void afterReturning(Object returnValue) {
        System.err.println("AfterReturning:" + returnValue);
    }

    /**
     * 异常抛出通知：@AfterThrowing(pointcut="pointcut()",throwing="e")和返回通知一样
     * 
     */
    @AfterThrowing(pointcut = "pointcut()", throwing = "e")
    public void afterThrowing(RuntimeException e) {
        System.err.println("AfterThrowing:" + e.getMessage());
        System.err.println("AfterThrowing:" + e);
    }

    /**
     * 后置通知使用@After("pointcut()")
     */
    @After("pointcut()")
    public void after() {
        System.err.println("After");
    }

    /**
     * 环绕通知使用@Around("pointcut()")
     * 
     * @param proceedingJoinPoint
     * @return
     * @throws Throwable
     */
    @Around("pointcut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        System.err.println("Around 1");
        Object obj = proceedingJoinPoint.proceed();
        System.err.println("Around 2");
        return obj;
    }

    @Before("pointcut()&&args(arg)")
    public void beforeWithParam(String arg) {
        System.err.println("Before Param:" + arg);
    }

    /**
     * 通过@Before("pointcut()&&@annotation(MethodCache)")的声明，首先集成pointcut（）方法的切入表达式，
     * 然后用@annotation(terenceMethod)引入自定义的注解，和前面继承的表达式组合起来。
     * 
     * 组合表达式，表示同时执行
     * 
     */
    @Before("pointcut() && @annotation(demo.java.lang.annotation.MethodCache)")
    public void beforeWithAnnotation(MethodCache methodCache) {
        System.err.println("beforeWithAnnotation:" + methodCache.expire());
    }

}
