package demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@SpringBootApplication
public class DemoApplication {

    private static Logger log = LoggerFactory.getLogger(DemoApplication.class); 
    
	public static void main(String[] args) {
	    log.trace("======trace");  
        log.debug("======debug");  
        log.info("======info");  
        log.warn("======warn");  
        log.error("======error");  
		SpringApplication.run(DemoApplication.class, args);
	}
}
