package demo.spring.cloud.ribbon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.exception.HystrixBadRequestException;

import demo.spring.cloud.ribbon.exception.ServiceException;
import demo.spring.cloud.ribbon.vo.User;

@Service
public class ServiceE {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Based on this description, @HystrixCommand has an ability to specify exceptions types which should be ignored.
     * 
     * If getUserById(id) throws an exception that type is ServiceException then this exception will be wrapped in
     * HystrixBadRequestException and re-thrown without triggering fallback logic. You don't need to do it manually,
     * javanica will do it for you under the hood.
     * 
     * @return
     */
    @HystrixCommand(ignoreExceptions = { ServiceException.class,
            HystrixBadRequestException.class }, fallbackMethod = "fallback")
    public User getUserById(String id) {
        String name = restTemplate.getForObject("http://Module/hi?name=" + id, String.class);
        return new User(id, name);
    }

    public User fallback(String id) {
        return new User(id, "");
    }

}
