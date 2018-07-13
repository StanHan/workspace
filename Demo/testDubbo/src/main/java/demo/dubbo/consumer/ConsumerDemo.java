package demo.dubbo.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConsumerDemo {

    private static Logger logger = LoggerFactory.getLogger(ConsumerDemo.class);

    public static void main(String[] args) throws Exception {
        System.setProperty("dubbo.application.logger", "slf4j");
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "consumer.xml" });
        context.start();

        HystrixDemo hystrixDemo = context.getBean(HystrixDemo.class);
        // show the result
        System.err.println(hystrixDemo.demoHystrix());
    }

}
