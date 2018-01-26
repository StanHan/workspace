package demo.spring.service.impl;

import org.springframework.stereotype.Service;

@Service
public class DemoService {

    public String say(String msg) {
        System.err.println(msg);
        return msg;
    }

}
