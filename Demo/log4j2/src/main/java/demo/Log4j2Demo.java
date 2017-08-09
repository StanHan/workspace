package demo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志级别从低到高分为: TRACE < DEBUG < INFO < WARN < ERROR < FATAL. PatternLayout定义了输出日志时的格式：
 * <li>%d{HH:mm:ss.SSS} 表示输出到毫秒的时间
 * <li>%t 输出当前线程名称
 * <li>%-5level 输出日志级别，-5表示左对齐并且固定输出5个字符，如果不足在右边补0
 * <li>%logger 输出logger名称，因为Root Logger没有名称，所以没有输出
 * <li>%msg 日志文本
 * <li>%n 换行
 * <li>%F 输出所在的类文件名，如Client.java
 * <li>%L 输出行号
 * <li>%M 输出所在方法名
 * <li>%l 输出语句所在的行数, 包括类名、方法名、文件名、行数
 * 
 * @author hanjy
 *
 */
public class Log4j2Demo {

    private static Logger logger = LoggerFactory.getLogger(Log4j2Demo.class);

    public static void main(String[] args) {
        logger.trace("trace level");
        logger.debug("debug level");
        logger.info("info level");
        logger.warn("warn level");
        logger.error("error level");
        
        System.out.println("-------------------");
        demo();
    }

    static void demo(){
        File file = new File("E:/Stan/Demo/log4j2/src/main/resources/log4j2.xml");
        try (FileInputStream fileInputStream = new FileInputStream(file);
                BufferedInputStream in= new BufferedInputStream(fileInputStream)){
            final ConfigurationSource source = new ConfigurationSource(in);
            Configurator.initialize(null, source);

            org.apache.logging.log4j.Logger logger = LogManager.getLogger("mylog");
            logger.trace("trace level");
            logger.debug("debug level");
            logger.info("info level");
            logger.warn("warn level");
            logger.error("error level");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
       
        
    }

    /**
     * 如果是web项目，在web.xml中添加 <code>
     * <context-param> 
     * <param-name>log4jConfiguration</param-name>
     * <param-value>/WEB-INF/conf/log4j2.xml</param-value> 
     * </context-param>
     * 
     * <listener> 
     * <listener-class>org.apache.logging.log4j.web.Log4jServletContextListener</listener-class> 
     * </listener>
     * </code>
     */
    static void demoWeb() {

    }

}
