package demo.spring.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import demo.db.redis.CacheService;
import demo.java.lang.annotation.MethodCache;

/**
 * 方法级缓存拦截器。目前该实现存在限制：方法入参必须为基本数据类型或者字符串类型，使用其它引用类型的参数会导致缓存键构造有误；
 */
@Aspect
@Component
public class MethodCacheAspect {
    private static final Logger logger = LoggerFactory.getLogger("METHOD_CACHE");

    @Autowired
    private CacheService cacheService;

    /**
     * 搭配 AspectJ 指示器“@annotation()”可以使本切面成为某个注解的代理实现
     */
    @Around("@annotation(demo.java.lang.annotation.MethodCache)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String cacheKey = getCacheKey(joinPoint);
        Object object = cacheService.get(cacheKey);
        if (object != null) {
            logger.info("cache hit，key [{}]", cacheKey);
            return object;
        } else {
            logger.info("cache miss，key [{}]", cacheKey);
            Object result = joinPoint.proceed(joinPoint.getArgs());
            if (result == null) {
                logger.error("fail to get data from source，key [{}]", cacheKey);
            } else {
                MethodCache methodCache = getAnnotation(joinPoint, MethodCache.class);
                cacheService.set(cacheKey, result, methodCache.expire());
            }
            return result;
        }
    }

    /**
     * 根据类名、方法名和参数值获取唯一的缓存键
     * 
     * @return 格式为 "包名.类名.方法名.参数类型.参数值"，类似 "your.package.SomeService.getById(int).123"
     */
    private String getCacheKey(ProceedingJoinPoint joinPoint) {
        return String.format("%s.%s", joinPoint.getSignature().toString().split("\\s")[1],
                StringUtils.join(joinPoint.getArgs(), ","));
    }

    private <T extends Annotation> T getAnnotation(ProceedingJoinPoint jp, Class<T> clazz) {
        MethodSignature sign = (MethodSignature) jp.getSignature();
        Method method = sign.getMethod();
        return method.getAnnotation(clazz);
    }
}
