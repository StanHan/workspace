package demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Service;

import demo.vo.TestMongoBean;
import demo.vo.TestMongoBeanHis;

@Service
public class MongoOpsService {

    private Logger log = LoggerFactory.getLogger(MongoOpsService.class);
    
    private LocalDateTime now = LocalDateTime.now();

    @Autowired
    protected MongoTemplate mongoTemplate;
    
    public void insert(TestMongoBean bean){
        mongoTemplate.insert(bean);
    }

    public void insert(TestMongoBeanHis his){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddhhmm");
        String collectionName = "test_"+now.format(formatter);
        log.info(collectionName);
        
        if(mongoTemplate.collectionExists(collectionName)){
            mongoTemplate.insert(his, collectionName);
        }else{
            mongoTemplate.insert(his, collectionName);
            Index index = new Index();
            index.on("value", Direction.DESC).on("createAt", Direction.DESC);
            mongoTemplate.indexOps(collectionName).ensureIndex(index);
        }
        
    }
}
