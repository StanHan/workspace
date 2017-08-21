package demo.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import demo.vo.ApiResult;
import demo.vo.ErrorCode;
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
    public ApiResult<Integer> saveUser(@RequestBody User request) {
        ApiResult<Integer> result = new ApiResult<>();
        if (request == null) {
            result.setErrorMsg(new ErrorCode("-1","arguments is null."));
        }
        System.out.println(request.toString());
        result.setResult(0);
        return result;
    }

    @GetMapping("/demo/user/id/{id}")
    public ApiResult<User> getUserById(@PathVariable("id") Integer id) {
        ApiResult<User> result = new ApiResult<>();
        logger.info("query user by id={}", id);
        id = 0Xab;
        result.setResult(new User(id, "Name" + id));
        return result;
    }

    @GetMapping(value = "/demo/user/list")
    public ApiResult<List<User>> queryUserBy(@RequestParam(name = "id", required = false) Integer id,
            @RequestParam(name = "name", required = false) String name) {
        ApiResult<List<User>> result = new ApiResult<>();
        System.out.printf("find by id %d, name %s", id, name);
        List<User> list = new ArrayList<User>();
        list.add(new User(0, "Stan0"));
        list.add(new User(1, "Stan1"));
        result.setResult(list);
        return result;
    }

    @DeleteMapping("/demo/user/delete/{id}")
    public ApiResult<Integer> deleteUserById(@PathVariable("id") Integer id) {
        ApiResult<Integer> result = new ApiResult<>();
        logger.info("query user by id={}", id);
        result.setResult(0);
        return result;
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
