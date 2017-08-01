package demo.log.log4j2;

import java.io.File;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

public class Log4j2Demo {

    private static Logger logger = Logger.getLogger(Log4j2Demo.class);

    public static void main(String[] args) {

    }

    static void specificConfigurationFile() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        File file = new File("path/to/a/different/log4j2.xml");
        // this will force a reconfiguration
        context.setConfigLocation(file.toURI());
        
        //shut down log4j2 in code
        LogManager.shutdown();


    }

    public static void configuredXML() {
        DOMConfigurator.configure("WebContent/WEB-INF/resource/log4j.xml");
        logger.debug("Here is some DEBUG");
        logger.info("Here is some INFO");
        logger.warn("Here is some WARN");
        logger.error("Here is some ERROR");
        logger.fatal("Here is some FATAL");
    }

    public static void configuredProperties() {
        PropertyConfigurator.configure("WebContent/WEB-INF/resource/log4j.properties");

        logger.debug("Here is some DEBUG");
        logger.info("Here is some INFO");
        logger.warn("Here is some WARN");
        logger.error("Here is some ERROR");
        logger.fatal("Here is some FATAL");
    }
}
