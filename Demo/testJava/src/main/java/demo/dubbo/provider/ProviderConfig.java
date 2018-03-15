package demo.dubbo.provider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.spring.ServiceBean;

import demo.dubbo.DubboBaseConfig;
import demo.vo.Person;
/**
 * Dubbo生产者配置
 *
 */
@Configuration
public class ProviderConfig extends DubboBaseConfig {
    @Bean
    public ServiceBean<Person> personServiceExport(Person person) {
        ServiceBean<Person> serviceBean = new ServiceBean<Person>();
        serviceBean.setProxy("javassist");
        serviceBean.setVersion("myversion");
        serviceBean.setInterface(Person.class.getName());
        serviceBean.setRef(person);
        serviceBean.setTimeout(5000);
        serviceBean.setRetries(3);
        return serviceBean;
    }
}
