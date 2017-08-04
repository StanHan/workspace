package demo;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Log4j2Demo {

    private static Logger logger = LoggerFactory.getLogger(Log4j2Demo.class);

    public static void main(String[] args) {
        logger.trace("=========trance");
        logger.debug("Here is some DEBUG");
        logger.info("Here is some INFO");
        logger.warn("Here is some WARN");
        logger.error("Here is some ERROR");
    }

    static void specificConfigurationFile() {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        File file = new File("path/to/a/different/log4j2.xml");
        // this will force a reconfiguration
        context.setConfigLocation(file.toURI());
        
        //shut down log4j2 in code
        LogManager.shutdown();


    }
}
