package demo.aspectJ;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import demo.spring.service.impl.DemoService;

public class AspectJDemo {

    public static void main(String[] args) {
        ApplicationContext context = new FileSystemXmlApplicationContext("E:\\Projects\\Stan\\Demo\\testJava\\src\\main\\java\\demo\\aspectJ\\aop.xml");
        org.springframework.core.annotation.OrderUtils a = null;
        DemoService service = context.getBean(DemoService.class);
        service.say("hello aspectJ!");
    }

}

