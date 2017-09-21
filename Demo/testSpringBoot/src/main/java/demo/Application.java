package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class); 
    
	public static void main(String[] args) {
	    logger.info("start.");
	    new SpringApplicationBuilder(Application.class).web(true).run(args);
	}
}
