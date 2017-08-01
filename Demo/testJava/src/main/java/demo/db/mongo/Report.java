package demo.db.mongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;

public class Report {

    
    
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient("172.16.20.69", 27017);
        MongoCredential credential = MongoCredential.createCredential("xinyongjin", "xinyongjin", "eGlueW9uZ2ppbgo=".toCharArray());  
        
        mongoClient.getDatabase("xinyongjin");

    }

}
