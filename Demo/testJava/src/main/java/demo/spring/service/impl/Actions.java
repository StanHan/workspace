package demo.spring.service.impl;

import org.springframework.stereotype.Service;

import demo.spring.service.IActions;

@Service
public class Actions implements IActions {

    @Override
    public String say(String msg) {
        System.err.println(msg);
        return msg;
    }

}
