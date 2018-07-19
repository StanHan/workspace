package demo.spring.cloud.ribbon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.config.ConfigurationManager;
import com.netflix.hystrix.contrib.javanica.annotation.DefaultProperties;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;

/**
 * 
 * annotation @DefaultProperties is class (type) level annotation that allows to default commands properties such as
 * groupKey, threadPoolKey, commandProperties, threadPoolProperties, ignoreExceptions and raiseHystrixExceptions.
 * Properties specified using this annotation will be used by default for each hystrix command defined within annotated
 * class unless a command specifies those properties explicitly using corresponding @HystrixCommand parameters.
 *
 * 参考 https://github.com/Netflix/Hystrix/wiki/Configuration
 */
@DefaultProperties(groupKey = "DefaultGroupKey")
@Service
public class ServiceF {

    /**
     * Javanica dynamically sets properties using Hystrix ConfigurationManager.
     */
    static {
        ConfigurationManager.getConfigInstance()
                .setProperty("hystrix.command.hiService.execution.isolation.thread.timeoutInMilliseconds", "500");
    }

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "500") }, threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "30"),
                    @HystrixProperty(name = "maxQueueSize", value = "101"),
                    @HystrixProperty(name = "keepAliveTimeMinutes", value = "2"),
                    @HystrixProperty(name = "queueSizeRejectionThreshold", value = "15"),
                    @HystrixProperty(name = "metrics.rollingStats.numBuckets", value = "12"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "1440") })
    public String hiService(String name) {
        return restTemplate.getForObject("http://Module/hi?name=" + name, String.class);
    }

}
