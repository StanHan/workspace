package demo.log.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * slf4j(全称是Simple Loging Facade For
 * Java)是一个为Java程序提供日志输出的统一接口，并不是一个具体的日志实现方案，就好像我们经常使用的JDBC一样，只是一种规则而已。因此单独的slf4j是不能工作的，它必须搭配其他具体的日志实现方案
 * 
 * @author hanjy
 *
 */
public class SLF4JDemo {

    private static final Logger logger = LoggerFactory.getLogger(SLF4JDemo.class);
    
    public static void main(String[] args) {
        System.out.println("hello world.");
        logger.trace("-------------------------------trace level");
        logger.debug("-------------------------------debug level");
        logger.info("-------------------------------info level");
        logger.warn("-------------------------------warn level");
        logger.error("-------------------------------error level");
        logger.info("hello world.{}" ,"Stan");
    }

}
