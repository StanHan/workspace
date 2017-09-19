package demo.spring.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Spring Boot 的目的是提供一组工具，以便快速构建容易配置的 Spring 应用程序。 Spring Boot 使您能轻松地创建独立的、生产级的、基于 Spring 且能直接运行的应用程序。我们对 Spring
 * 平台和第三方库有自己的看法，所以您从一开始只会遇到极少的麻烦。 查看配置：使用 --debug 选项启动您的 Spring Boot 应用程序，然后将向控制台生成一个自动配置报告。
 *
 */
@RestController
@EnableAutoConfiguration
public class SpringBootDemo {

    @RequestMapping("/")
    String home() {
        return "Hello World!";
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(SpringBootDemo.class, args);
    }

}
