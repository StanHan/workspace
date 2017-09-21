package demo.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.vo.ApiResult;
import demo.vo.Config;
import demo.vo.ErrorCode;
import demo.vo.User;

@RestController
@EnableAutoConfiguration
public class DemoController {

    private static final Logger logger = LoggerFactory.getLogger(DemoController.class);
    Random random = new Random();

    // @Autowired
    // MongoOpsService mongoOpsService;
    @Autowired
    private Config config;
    
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    
    @GetMapping("/demo/redis/{value}")
    public ApiResult<String> testRedis(HttpServletRequest request, @PathVariable("value") String value) {
        ApiResult<String> result = new ApiResult<>();
        ValueOperations<String,String> vo = stringRedisTemplate.opsForValue();
        vo.set("tmp",value);
        stringRedisTemplate.expire("tmp", 60, TimeUnit.SECONDS);
        String t = vo.get("tmp");
        result.setResult(t);
        return result;
    }

    @RequestMapping("/showConfig")
    public ApiResult<Config> showConfig(HttpServletRequest request, HttpServletResponse response) {
        ApiResult<Config> result = new ApiResult<>();
        result.setResult(config);
        return result;
    }

    @RequestMapping("/login")
    public ApiResult<Integer> adminLogin(HttpServletRequest request, HttpServletResponse response) {
        ApiResult<Integer> result = new ApiResult<>();
        Object user = request.getAttribute("user");
        if (user == null) {
            user = request.getParameter("user");
        }

        if (user == null) {
            result.setErrorMsg(new ErrorCode("need Login", "need Login"));
        } else {
            request.getSession().setAttribute("user", user);
            result.setResult(1);
        }
        return result;
    }

    @PostMapping("/demo/user")
    public ApiResult<Integer> saveUser(@RequestBody User request) {
        ApiResult<Integer> result = new ApiResult<>();
        if (request == null) {
            result.setErrorMsg(new ErrorCode("-1", "arguments is null."));
        }
        System.out.println(request.toString());
        result.setResult(0);
        return result;
    }
    
    @GetMapping("/demo/user/ids")
    public ApiResult<List<User>> getUserById(@RequestParam("id") Integer[] ids) {
        ApiResult<List<User>> result = new ApiResult<>();
        logger.info("query users by ids={}", Arrays.toString(ids));
        List<User> users = new ArrayList<>();
        for (Integer integer : ids) {
            users.add(new User(integer, "User_"+integer));
        }
        result.setResult(users);
        return result;
    }

    @GetMapping("/demo/user/id/{id}")
    public ApiResult<User> getUserById(HttpServletRequest request, @PathVariable("id") Integer id) {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        ApiResult<User> result = new ApiResult<>();
        logger.info("query user by id={}", id);
        id = 0Xab;
        result.setResult(new User(id, "Name" + sessionId));
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

    // @GetMapping("/demo/mongo/test")
    // public void testMongo() {
    // TestMongoBean bean = new TestMongoBean();
    // bean.setId((long)random.nextInt(50000));
    // bean.setValue("test "+ random.nextInt(500));
    // bean.setUpdateAt(new Date());
    // mongoOpsService.insert(bean);
    //
    // TestMongoBeanHis his = new TestMongoBeanHis();
    // his.setId((long)random.nextInt(50000));
    // his.setValue("test "+ random.nextInt(500));
    // his.setUpdateAt(new Date());
    // mongoOpsService.insert(his);
    //
    // }

}
