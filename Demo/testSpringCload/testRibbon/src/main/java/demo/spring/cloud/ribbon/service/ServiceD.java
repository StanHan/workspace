package demo.spring.cloud.ribbon.service;

import org.springframework.stereotype.Service;

import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import demo.spring.cloud.ribbon.vo.User;

/**
 * 
 * Default fallback method should not have any parameters except extra one to get execution exception and shouldn't
 * throw any exceptions. Below fallbacks listed in descending order of priority:
 * 
 * <li>command fallback defined using fallbackMethod property of @HystrixCommand
 * <li>command default fallback defined using defaultFallback property of @HystrixCommand
 * <li>class default fallback defined using defaultFallback property of @DefaultProperties
 *
 */
@Service
@DefaultProperties(defaultFallback = "fallback")
public class ServiceD {

    @HystrixCommand(fallbackMethod = "fallback")
    public User getUserBy() {
        return new User();
    }

    @HystrixCommand(fallbackMethod = "fallback")
    public User getUserBy(String id) {
        User user = new User();
        user.setId(id);
        return user;
    }

    @HystrixCommand(fallbackMethod = "fallback")
    public User getUserBy(String id, String name) {
        return new User(id, name);
    }

    public User fallback() {
        return new User("", "");
    }

    public User fallback(String id) {
        return new User(id, "");
    }

    public User fallback(String id, String name) {
        return new User(id, name);
    }
}
