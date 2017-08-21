package demo.log.slf4j;

import java.io.File;
import java.io.IOException;
import java.time.Clock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * slf4j(全称是Simple Loging Facade For
 * Java)是一个为Java程序提供日志输出的统一接口，并不是一个具体的日志实现方案，就好像我们经常使用的JDBC一样，只是一种规则而已。因此单独的slf4j是不能工作的，它必须搭配其他具体的日志实现方案
 * 
 * @author hanjy
 *
 */
public class SLF4JDemo {

    static Clock clock = Clock.systemDefaultZone();
    
    public static void main(String[] args) throws JoranException {
        demoLogback();
    }
    
    static void demoLogback() throws JoranException{
        long t1=clock.millis();
        String filePath = "E:/Stan/Demo/testJava/src/main/java/demo/log/logback/logback.xml";
        loadLocbackConfig(filePath);
        Logger logger = LoggerFactory.getLogger(SLF4JDemo.class);
        
        System.out.println("hello world.");
        logger.trace("-------------------------------trace level");
        logger.debug("-------------------------------debug level");
        logger.info("-------------------------------info level");
        logger.warn("-------------------------------warn level");
        logger.error("-------------------------------error level");
        logger.info("hello world.{}" ,"Stan");
        long t2=clock.millis();
        logger.info("耗时(ms):{}",(t2 -t1));
    }
    
    /**
     * 加载外部的logback配置文件
     * 
     * @param filePath
     *            配置文件路径
     * @throws IOException
     * @throws JoranException
     */
     private static void loadLocbackConfig(String filePath) throws JoranException {
        File file = new File(filePath);
        JoranConfigurator configurator = new JoranConfigurator();
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        configurator.setContext(loggerContext);
        loggerContext.reset();
        configurator.doConfigure(file);
        StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
    }

}
