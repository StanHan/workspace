package demo.java.sql.jdbc.pool;

import org.slf4j.LoggerFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;

/**
 * DRUID是阿里巴巴开源平台上一个数据库连接池实现，它结合了C3P0、DBCP、PROXOOL等DB池的优点，同时加入了日志监控，可以很好的监控DB池连接和SQL的执行情况，
 * 可以说是针对监控而生的DB连接池(据说是目前最好的连接池,不知道速度有没有BoneCP快)。
 *
 */
public class DruidDemo {

    public static void loadLoggerContext() {
        System.getProperties().put("logback.configurationFile", "./conf/logback.xml");
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.setPrintStream(System.err);
        StatusPrinter.print(lc);
    }

    public static void main(String[] args) {
        try {
            loadLoggerContext();
            FileSystemXmlApplicationContext context = new FileSystemXmlApplicationContext("src/main/resources/db/ds/ds.xml");
//            E:\Stan\Demo\testJava\src\main\resources\db\ds\ds.xml
        } catch (Exception e) {
            System.out.println(e);
        }
    }

}
