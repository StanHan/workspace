package demo.spring.cloud.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import demo.spring.cloud.vo.ApiResult;

@RestController
public class IndexController {

    @Value("${server.port}")
    String port;

    @Value("${testdb.rocketMqIp:aaaaaaaaaaaaaa}")
    String ip;

    @GetMapping("/index")
    public ApiResult<String> index() {
        return new ApiResult<>("Welcome to Galaxy-River System");
    }

    @RequestMapping("/hi")
    public ApiResult<String> home(@RequestParam String name) {
        return new ApiResult<>("hi " + name + ",i am from port:" + port);
    }

    @GetMapping("/ip/mq/rocket")
    public ApiResult<String> ip() {
        return new ApiResult<>(ip);
    }

}
