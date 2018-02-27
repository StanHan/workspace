package demo.db.mongo;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/**
 * Mongo数据迁移
 * 
 * @author hanjy
 *
 */
public class MongoDataTransfer {

    private static final Logger logger = LoggerFactory.getLogger(MongoDataTransfer.class);

    public static void main(String[] args) throws UnknownHostException {
        transferSpecialData("mx_carrier_calls", 20,170104667466891280L);
    }

    public static final String T1 = "mongodb://galaxy:ZmstZ2FsYXh5QDIzNDUu@172.16.0.140:27017/galaxy";
    public static final String T2 = "mongodb://galaxy:ZmstZ2FsYXh5QDIzNDUu@172.16.0.142:27018/galaxy";
    public static final String T3 = "mongodb://galaxy:ZmstZ2FsYXh5QDIzNDUu@172.16.0.146:27019/galaxy";
    public static final String PRO = "mongodb://galaxy:ZmstZ2FsYXh5QDIzNDUu@180.101.195.217:5043/galaxy";
    public static final String DEV = "mongodb://172.17.16.13:27017/galaxy";

    /**
     * 数据迁移
     * 
     * @param collectionName
     * @param limit
     */
    static void transfer(String collectionName, int limit) {
        MongoClientURI srcUri = new MongoClientURI(T1);
        MongoClientURI tergetUri = new MongoClientURI(DEV);

        try (MongoClient srcClient = new MongoClient(srcUri); MongoClient tarClient = new MongoClient(tergetUri);) {
            MongoDatabase srcDB = srcClient.getDatabase("galaxy");
            MongoCollection<Document> srcCollection = srcDB.getCollection(collectionName);
            FindIterable<Document> result = srcCollection.find().limit(limit);
            MongoCursor<Document> cursor = result.iterator();
            List<Document> documents = new ArrayList<>();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                logger.info(document.toJson());
                documents.add(document);
            }

            MongoDatabase tarDB = tarClient.getDatabase("galaxy");
            MongoCollection<Document> tarConnection = tarDB.getCollection(collectionName);
//            tarConnection.insertMany(documents);
            for (Document document2 : documents) {
                try {
                    tarConnection.insertOne(document2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 指定数据迁移
     * 
     * @param collectionName
     * @param limit
     */
    static void transferSpecialData(String collectionName, int limit,long customer_id) {
        MongoClientURI srcUri = new MongoClientURI(T1);
        MongoClientURI tergetUri = new MongoClientURI(DEV);

        try (MongoClient srcClient = new MongoClient(srcUri); MongoClient tarClient = new MongoClient(tergetUri);) {
            MongoDatabase srcDB = srcClient.getDatabase("galaxy");
            MongoCollection<Document> srcCollection = srcDB.getCollection(collectionName);
            FindIterable<Document> result = srcCollection.find(Filters.eq("customer_id",customer_id)).limit(limit);
            MongoCursor<Document> cursor = result.iterator();
            List<Document> documents = new ArrayList<>();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                logger.info(document.toJson());
                documents.add(document);
            }

            MongoDatabase tarDB = tarClient.getDatabase("galaxy");
            MongoCollection<Document> tarConnection = tarDB.getCollection(collectionName);
//            tarConnection.insertMany(documents);
            for (Document document2 : documents) {
                try {
                    tarConnection.insertOne(document2);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
