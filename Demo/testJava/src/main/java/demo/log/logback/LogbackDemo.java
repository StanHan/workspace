package demo.log.logback;

import java.io.File;
import java.io.IOException;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * Logback是由log4j创始人Ceki Gülcü设计的又一个开源日志组件。logback当前分成三个模块：logback-core,logback- classic和logback-access。
 * 
 * Logback的核心对象：Logger、Appender、Layout。
 * 
 * <li>Logger:日志的记录器，把它关联到应用的对应的context上后，主要用于存放日志对象，也可以定义日志类型、级别。
 * <li>Appender:用于指定日志输出的目的地，目的地可以是控制台、文件、远程套接字服务器、 MySQL、 PostreSQL、Oracle和其他数据库、 JMS和远程UNIX Syslog守护进程等。
 * <li>Layout:负责把事件转换成字符串，格式化的日志信息的输出。具体的Layout通配符，可以直接查看帮助文档。
 * 
 * Level 有效级别：
 * 
 * Logger可以被分配级别。级别包括：TRACE、DEBUG、INFO、WARN和ERROR，定义于ch.qos.logback.classic.Level类。
 * 程序会打印高于或等于所设置级别的日志，设置的日志等级越高，打印出来的日志就越少。
 * 如果设置级别为INFO，则优先级高于等于INFO级别（如：INFO、WARN、ERROR）的日志信息将可以被输出,小于该级别的如DEBUG将不会被输出。
 * 为确保所有logger都能够最终继承一个级别，根logger总是有级别，默认情况下，这个级别是DEBUG。
 * 
 * <h2>三值逻辑</h2>
 * 
 * Logback的过滤器基于三值逻辑（ternary logic），允许把它们组装或成链，从而组成任意的复合过滤策略。
 * 过滤器很大程度上受到Linux的iptables启发。这里的所谓三值逻辑是说，过滤器的返回值只能是ACCEPT、DENY和NEUTRAL的其中一个。
 * 
 * <li>如果返回DENY，那么记录事件立即被抛弃，不再经过剩余过滤器；
 * <li>如果返回NEUTRAL，那么有序列表里的下一个过滤器会接着处理记录事件；
 * <li>如果返回ACCEPT，那么记录事件被立即处理，不再经过剩余过滤器。
 * 
 * <h2>Filter 过滤器</h2>
 * 
 * Logback-classic提供两种类型的过滤器：常规过滤器和TuroboFilter过滤器。Logback整体流程：Logger
 * 产生日志信息；Layout修饰这条msg的显示格式；Filter过滤显示的内容；Appender具体的显示，即保存这日志信息的地方。
 * 
 * logback的默认配置 如果配置文件 logback-test.xml 和 logback.xml 都不存在，那么 logback 默认地会调用BasicConfigurator ，创建一个最小化配置。最小化配置由一个关联到根
 * logger 的ConsoleAppender 组成。输出用模式为%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n 的 PatternLayoutEncoder
 * 进行格式化。root logger 默认级别是 DEBUG。
 * 
 * 1、Logback的配置文件 Logback 配置文件的语法非常灵活。正因为灵活，所以无法用 DTD 或 XML schema
 * 进行定义。尽管如此，可以这样描述配置文件的基本结构：以<configuration>开头，后面有零个或多个<appender>元素，有零个或多个<logger>元素，有最多一个<root>元素。
 * 
 * 2、Logback默认配置的步骤
 * <li>(1). 尝试在 classpath下查找文件logback-test.xml；
 * <li>(2). 如果文件不存在，则查找文件logback.xml；
 * <li>(3). 如果两个文件都不存在，logback用BasicConfigurator自动对自己进行配置，这会导致记录输出到控制台。
 * 
 * @author hanjy
 *
 */
public class LogbackDemo {

    public static void main(String[] args) throws Exception {
        demo();
    }

    static void demo() throws JoranException {
        String filePath = "E:/Stan/Demo/testJava/src/main/java/demo/log/logback/logback.xml";
        LoggerContext loggerContext = load(filePath);
        Logger log = loggerContext.getLogger("monitor");
        log.trace("----------------------trace");
        log.debug("----------------------debug");
        log.info("----------------------info");
        log.warn("----------------------warn");
        log.error("----------------------error");
    }

    /**
     * 加载外部的logback配置文件
     * 
     * @param filePath
     *            配置文件路径
     * @throws IOException
     * @throws JoranException
     */
    public static LoggerContext load(String filePath) throws JoranException {
        File file = new File(filePath);
        JoranConfigurator configurator = new JoranConfigurator();
        LoggerContext loggerContext = new LoggerContext();
        configurator.setContext(loggerContext);
        loggerContext.reset();
        configurator.doConfigure(file);
        StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
        return loggerContext;
    }

}
