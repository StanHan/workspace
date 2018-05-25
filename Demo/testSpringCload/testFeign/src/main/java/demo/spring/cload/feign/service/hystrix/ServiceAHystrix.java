package demo.spring.cload.feign.service.hystrix;

import org.springframework.stereotype.Component;

import demo.spring.cload.feign.service.ServiceA;

@Component
public class ServiceAHystrix implements ServiceA {

    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry " + name;
    }

}
