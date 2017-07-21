package demo.web;

import java.util.Date;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.service.MongoOpsService;
import demo.vo.TestMongoBean;
import demo.vo.TestMongoBeanHis;
import demo.vo.User;

@RestController
@EnableAutoConfiguration
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    Random random = new Random();
    
    @Autowired
    MongoOpsService mongoOpsService;

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
    
    @GetMapping("/demo/mongo/test")
    public void testMongo() {
        TestMongoBean bean = new TestMongoBean();
        bean.setId((long)random.nextInt(50000));
        bean.setValue("test "+ random.nextInt(500));
        bean.setUpdateAt(new Date());
        mongoOpsService.insert(bean);
        
        TestMongoBeanHis his = new TestMongoBeanHis();
        his.setId((long)random.nextInt(50000));
        his.setValue("test "+ random.nextInt(500));
        his.setUpdateAt(new Date());
        mongoOpsService.insert(his);
        
    }
    
    
}
