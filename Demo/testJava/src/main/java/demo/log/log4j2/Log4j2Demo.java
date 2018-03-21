package demo.log.log4j2;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.Logger;

/**
 * 注意：在JVM启动参数中增加 -DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector 开启异步日志.
 * 
 * <p>
 * 
 * log4j 2.x版本不再支持像1.x中的.properties后缀的文件配置方式，2.x版本配置文件后缀名只能为".xml",".json"或者".jsn".
 * 
 * 系统选择配置文件的优先级(从先到后)如下：
 * <ol>
 * <li>(1).classpath下的名为log4j2-test.json 或者log4j2-test.jsn的文件.
 * <li>(2).classpath下的名为log4j2-test.xml的文件.
 * <li>(3).classpath下名为log4j2.json 或者log4j2.jsn的文件.
 * <li>(4).classpath下名为log4j2.xml的文件.
 * </ol>
 * 我们一般默认使用log4j2.xml进行命名。如果本地要测试，可以把log4j2-test.xml放到classpath，而正式环境使用log4j2.xml，则在打包部署的时候不要打包log4j2-test.xml即可。
 * 
 * @author hanjy
 *
 */
public class Log4j2Demo {

    public static void main(String[] args) throws InterruptedException {
        demo();
    }

    static void demo() throws InterruptedException {
        File file = new File("E:/Stan/Demo/testJava/src/main/java/demo/log/log4j2/log4j2.xml");
        try (FileInputStream fileInputStream = new FileInputStream(file);
                BufferedInputStream in = new BufferedInputStream(fileInputStream)) {
            final ConfigurationSource source = new ConfigurationSource(in);
            Configurator.initialize(null, source);

            Logger logger = LogManager.getLogger(Log4j2Demo.class);
            while(true) {
                logger.trace("-------------------------------trace level");
                logger.debug("-------------------------------debug level");
                logger.info("-------------------------------info level");
                logger.warn("-------------------------------warn level");
                logger.error("-------------------------------error level");
                logger.fatal("-------------------------------fatal level");
                logger.info("hello {}.", "world");
                Thread.sleep(2000);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 注意：log4j2不再支持properties文件了，只支持xml，json或是yaml，不指定位置的情况下默认在src/main/resources下查找。如果需要自定义位置，需要在上面的web.xml中添加以下代码
     * <p>
     * <pre>
     * <context-param> 
     * <param-name>log4jConfiguration</param-name>
     * <param-value>/WEB-INF/conf/log4j2.xml</param-value> 
     * </context-param></pre>
     * <p>
     * web.xml中设置log4j2的监听器和过滤器（servlet3.0及以上版本不需要该步操作） <pre>
     * <listener> 
     * <listener-class>org.apache.logging.log4j.web.Log4jServletContextListener</listener-class> 
     * </listener>
     * </pre>
     * <p>
     * 
     */
    static void demoWeb() {

    }

}
