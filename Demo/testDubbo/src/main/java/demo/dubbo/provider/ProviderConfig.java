package demo.dubbo.provider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.spring.ServiceBean;
import com.loanking.galaxy.facade.MiGuanFacade;

import demo.dubbo.DubboBaseConfig;
/**
 * Dubbo生产者配置
 *
 */
@Configuration
public class ProviderConfig extends DubboBaseConfig {
    @Bean
    public ServiceBean<MiGuanFacade> personServiceExport(MiGuanFacade person) {
        ServiceBean<MiGuanFacade> serviceBean = new ServiceBean<>();
        serviceBean.setProxy("javassist");
        serviceBean.setVersion("myversion");
        serviceBean.setInterface(MiGuanFacade.class.getName());
        serviceBean.setRef(person);
        serviceBean.setTimeout(5000);
        serviceBean.setRetries(3);
        return serviceBean;
    }
}
