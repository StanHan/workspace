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

/**
 * Mongo数据迁移
 * 
 * @author hanjy
 *
 */
public class MongoDataTransfer {

    private static final Logger logger = LoggerFactory.getLogger(MongoDataTransfer.class);

    public static void main(String[] args) throws UnknownHostException {
        transfer("dkw_user_loan_info", 20);
    }

    public static final String T1 = "mongodb://fk-galaxy:ZmstZ2FsYXh5QDIzNDUu@172.16.0.140:27017/fk-galaxy";
    public static final String T1_galaxy = "mongodb://galaxy:ZmstZ2FsYXh5QDIzNDUu@172.16.0.140:27017/galaxy";
    public static final String T2 = "mongodb://fk-galaxy:ZmstZ2FsYXh5QDIzNDUu@172.16.0.142:27018/fk-galaxy";
    public static final String T3 = "mongodb://fk-galaxy:ZmstZ2FsYXh5QDIzNDUu@172.16.0.146:27019/fk-galaxy";
    public static final String PRO = "mongodb://galaxy:ZmstZ2FsYXh5QDIzNDUu@180.101.195.217:5043/galaxy";

    /**
     * 数据迁移
     * 
     * @param collectionName
     * @param limit
     */
    static void transfer(String collectionName, int limit) {
        MongoClientURI srcUri = new MongoClientURI(PRO);
        MongoClientURI tergetUri = new MongoClientURI(T1_galaxy);

        try (MongoClient srcClient = new MongoClient(srcUri); MongoClient tarClient = new MongoClient(tergetUri);) {
            MongoDatabase srcDB = srcClient.getDatabase("fk-galaxy");
            MongoCollection<Document> srcCollection = srcDB.getCollection(collectionName);
            FindIterable<Document> result = srcCollection.find().limit(limit);
            MongoCursor<Document> curson = result.iterator();
            List<Document> documents = new ArrayList<>();
            while (curson.hasNext()) {
                Document document = curson.next();
                logger.info(document.toJson());
                documents.add(document);
            }

            MongoDatabase tarDB = tarClient.getDatabase("fk-galaxy");
            MongoCollection<Document> tarConnection = tarDB.getCollection(collectionName);
            tarConnection.insertMany(documents);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
