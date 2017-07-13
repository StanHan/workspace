package demo.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.vo.User;

@RestController
@EnableAutoConfiguration
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);

    @PostMapping("/demo/user")
    public void saveBannerImage(@RequestBody User request) {
        if (request == null) {
            return;
        }
        System.out.println(request.toString());
    }

    @GetMapping("/demo/user/id/{id}")
    public User getUserById(@PathVariable("id") Integer id) {
        logger.info("query user by id={}", id);
        id = 0Xab;
        return new User(id, "Name" + id);
    }

    @GetMapping(value = "/demo/user/list")
    public User queryUserBy(@RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "name", required = false) String name) {
        System.out.printf("find by id %d, name %s", id, name);
        return new User(id, name);
    }

    @DeleteMapping("/demo/user/delete/{id}")
    public User deleteUserById(@PathVariable("id") Integer id) {
        logger.info("query user by id={}", id);
        return new User(id, "Name" + id);
    }
}
