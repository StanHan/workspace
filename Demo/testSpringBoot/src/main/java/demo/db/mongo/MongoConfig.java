package demo.db.mongo;

import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import com.mongodb.MongoClientURI;

//@Configuration
//@EnableAutoConfiguration
public class MongoConfig {

//    @Value("#{spring.data.mongodb.uri}")
    private String uri;
//    private String uri = "mongodb://127.0.0.1:27017/test";

//    @Bean
    MongoClientURI getMongoClientURI() {
        MongoClientURI mongoClientURI = new MongoClientURI(uri);
        return mongoClientURI;
    }

//    @Bean
    MongoDbFactory getSimpleMongoDbFactory() throws UnknownHostException {
        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(getMongoClientURI());
        return mongoDbFactory;
    }

//    @Bean
    MongoTemplate getMongoTemplate() throws UnknownHostException {
        MongoTemplate mongoTemplate = new MongoTemplate(getSimpleMongoDbFactory());
        return mongoTemplate;
    }
}
