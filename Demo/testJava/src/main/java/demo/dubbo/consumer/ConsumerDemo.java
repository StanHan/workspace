package demo.dubbo.consumer;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.spring.service.IUserDAO;

public class ConsumerDemo {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[] { "META-INF/spring/dubbo-demo-consumer.xml" });
        context.start();
        // obtain proxy object for remote invocation
        IUserDAO demoService = (IUserDAO) context.getBean("userDao");
        // execute remote invocation
        Boolean hello = demoService.findUserById("");
        // show the result
        System.out.println(hello);
    }
}
