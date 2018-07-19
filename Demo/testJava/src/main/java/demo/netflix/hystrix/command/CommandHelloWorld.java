package demo.netflix.hystrix.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;

import demo.java.lang.ThreadDemo;

public class CommandHelloWorld extends HystrixCommand<String> {

    private static Logger logger = LoggerFactory.getLogger(CommandHelloWorld.class);

    private final String name;

    public CommandHelloWorld(String name) {
        super(HystrixCommandGroupKey.Factory.asKey("ExampleGroup"));
        this.name = name;
    }

    @Override
    protected String run() {
        ThreadDemo.safeSleep(800);

        logger.info("Hello {}", name);
        
        if(name.endsWith("Han")) {
            throw new RuntimeException("this command always fails");
        }
        
        /*
         * 网络调用 或者其他一些业务逻辑，可能会超时或者抛异常
         */
        return "Hello " + name + "! From " + Thread.currentThread().getName();
    }
    
    @Override
    protected String getFallback() {
        return "Hello Failure " + name + "!";
    }
}