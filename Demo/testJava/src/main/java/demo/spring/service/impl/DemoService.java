package demo.spring.service.impl;

import org.springframework.stereotype.Service;

import demo.java.lang.annotation.MethodCache;

/**
 * 业务类
 */
@Service
public class DemoService {

    @MethodCache(expire = 500L)
    public String say(String msg) {
        System.err.println("Stan said:" + msg);
        return msg;
    }

}
