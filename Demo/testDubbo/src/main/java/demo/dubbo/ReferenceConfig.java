package demo.dubbo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.dubbo.config.spring.ReferenceBean;
import com.loanking.galaxy.facade.MiGuanFacade;

/**
 * Dubbo消费者配置
 * 
 * @author hanjy
 *
 */
@Configuration
public class ReferenceConfig extends DubboBaseConfig {

    @Bean
    public ReferenceBean<MiGuanFacade> person() {
        ReferenceBean<MiGuanFacade> ref = new ReferenceBean<>();
        ref.setVersion("myversion");
        ref.setInterface(MiGuanFacade.class);
        ref.setTimeout(5000);
        ref.setRetries(3);
        ref.setCheck(false);
        return ref;
    }
}