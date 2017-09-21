package demo.vo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("${config.properties}")
@ConfigurationProperties
public class Config {

    @Value("${rocketMQ.namesrvAddr}")
    private String namesrvAddr;

    @Autowired
    private DBInfo db;

    @Override
    public String toString() {
        return "Config [namesrvAddr=" + namesrvAddr + ", db=" + db + "]";
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    public DBInfo getDb() {
        return db;
    }

    public void setDb(DBInfo db) {
        this.db = db;
    }


}

@Component
@ConfigurationProperties(prefix = "db")
@PropertySource("${config.properties}")
class DBInfo {
    
    String url;
    String user;
    String pass;

    @Override
    public String toString() {
        return "DBInfo [url=" + url + ", user=" + user + ", pass=" + pass + "]";
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
