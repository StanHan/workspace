package demo.spring.cloud.ribbon.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.spring.cloud.ribbon.service.ServiceA;

@RestController
public class ControllerA {

    @Autowired
    ServiceA serviceA;

    @RequestMapping(value = "/hello")
    public String hi(@RequestParam String name) {
        return serviceA.hiService(name);
    }
}
