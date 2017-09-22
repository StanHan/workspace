package demo.vo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@PropertySource("${config.properties}")
public class ConfigBean {
    
    @Autowired
    Environment env;

    @Value("${rocketMQ.namesrvAddr}")
    private String namesrvAddr;
    
    String url;
    String user;
    String pass;
    
    @Bean
    ConfigBean buildConfigBean(){
        ConfigBean bean = new ConfigBean();
        bean.setNamesrvAddr(env.getProperty("rocketMQ.namesrvAddr"));
        return bean;
    }
    
    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

}

