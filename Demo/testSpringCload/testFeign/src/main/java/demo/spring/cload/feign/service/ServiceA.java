package demo.spring.cload.feign.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import demo.spring.cload.feign.service.hystrix.ServiceAHystrix;

@FeignClient(value = "Module", fallback = ServiceAHystrix.class)
public interface ServiceA {

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);

}
