package demo.spring.cloud.ribbon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import demo.spring.cloud.ribbon.vo.User;

@Service
public class ServiceB {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * If fallback method was marked with @HystrixCommand then this fallback method (defaultUser) also can has own
     * fallback method, as in the example below:
     * 
     * @param id
     * @return
     */
    @HystrixCommand(fallbackMethod = "defaultUser")
    public User getUserById(String id) {
        String name = restTemplate.getForObject("http://Module/hi?name=" + id, String.class);
        return new User(id, name);
    }

    @HystrixCommand(fallbackMethod = "defaultUserSecond")
    private User defaultUser(String id) {
        return new User();
    }

    @HystrixCommand
    private User defaultUserSecond(String id) {
        return new User("def", "def");
    }
}
