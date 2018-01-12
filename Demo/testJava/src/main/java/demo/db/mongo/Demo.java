package demo.db.mongo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import demo.vo.User;

public class Demo {

    public static void main(String[] args) {
        MongoClient mongoClient1 = new MongoClient("localhost");
        MongoClient mongoClient2 = new MongoClient("localhost", 27017);
        MongoClient mongoClient4 = new MongoClient(new ServerAddress("localhost"));
        MongoClient mongoClient5 = new MongoClient(new ServerAddress("localhost"), new MongoClientOptions.Builder().build());

        MongoClient mongoClient = new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017)));
        MongoDatabase mongoDatabase = mongoClient.getDatabase("demo");
        MongoCollection<Document> mongoCollection = mongoDatabase.getCollection("test1");
//        MongoCollection<User> MongoCollectionImpl = new MongoCollectionImpl<>();
//        mongoCollection.insertOne(user);

    }

    private MongoDBService mongoDBService = new MongoDBService();
    // 测试插入数据
    void testInsert() {
        // 数据一，包括用户名、密码，地址信息（省份、城市），爱好[…]
        BasicDBList dbList1 = new BasicDBList();
        dbList1.add("basketball");
        dbList1.add("music");
        dbList1.add("web");
        DBObject dbObject1 = new BasicDBObject("username", "insert1").append("age", 18)
                .append("address", new BasicDBObject("province", "广东").append("city", "广州"))
                .append("favourite", dbList1);
        // 数据二
        BasicDBList dbList2 = new BasicDBList();
        dbList2.add("football");
        dbList2.add("music");
        DBObject dbObject2 = new BasicDBObject("username", "insert2").append("age", 18)
                .append("address", new BasicDBObject("province", "陕西").append("city", "西安"))
                .append("favourite", dbList2);
        // 数据三
        BasicDBList dbList3 = new BasicDBList();
        dbList3.add("Linux");
        DBObject dbObject3 = new BasicDBObject("username", "insert3").append("age", 18)
                .append("address", new BasicDBObject("province", "河北").append("city", "保定"))
                .append("favourite", dbList3);
        // 数据四
        BasicDBList dbList4 = new BasicDBList();
        dbList4.add("swim");
        dbList4.add("android");
        DBObject dbObject4 = new BasicDBObject("username", "insert4").append("age", 18)
                .append("address", new BasicDBObject("province", "四川").append("city", "成都"))
                .append("favourite", dbList4);
        // 数据五
        DBObject dbObject5 = new BasicDBObject("username", "insert5").append("age", 28).append("address",
                new BasicDBObject("city", "杭州"));
        mongoDBService.printListDBObj(mongoDBService.findAll());
        System.out.println("——————————————————insert collection——————————————————");
        List<DBObject> list = new ArrayList<DBObject>();
        list.add(dbObject1);
        list.add(dbObject2);
        list.add(dbObject3);
        list.add(dbObject5);
        mongoDBService.insertBatch(list);
        System.out.println("——————————————————insert one——————————————————");
        mongoDBService.insert(dbObject4);
        mongoDBService.printListDBObj(mongoDBService.findAll());
    }

    // 测试查询数据
    void testFind() {
        DBObject dbObject = new BasicDBObject("username", "insert1");
        System.out.println("数量：" + mongoDBService.getCollectionCount());
        System.out.println("username=java的数据数量：" + mongoDBService.getCount(dbObject));
        System.out.println("——————————————————find all——————————————————");
        mongoDBService.printListDBObj(mongoDBService.findAll());
        System.out.println("——————————————————find obj——————————————————");
        mongoDBService.printListDBObj(mongoDBService.find(dbObject));
        System.out.println("——————————————————find sort——————————————————");
        mongoDBService.printListDBObj(mongoDBService.find(new BasicDBObject(), new BasicDBObject("age", 1)));
        System.out.println("——————————————————find sort limit——————————————————");
        mongoDBService.printListDBObj(mongoDBService.find(new BasicDBObject(), new BasicDBObject("age", 1), 1, 2));
    }

    // 测试数据更新
    void testUpdate() {
        BasicDBObject newDocument = new BasicDBObject("$set", new BasicDBObject("age", 11));

        BasicDBObject searchQuery = new BasicDBObject().append("username", "insert2");

        mongoDBService.printListDBObj(mongoDBService.find(searchQuery));
        System.out.println("——————————————————update——————————————————");
        mongoDBService.update(newDocument, searchQuery);
        mongoDBService.printListDBObj(mongoDBService.find(searchQuery));
    }

    // 测试数据删除
    void testDelete() {
        DBObject dbObject1 = new BasicDBObject("username", "insert1");
        DBObject dbObject2 = new BasicDBObject("username", "insert2");
        DBObject dbObject3 = new BasicDBObject("username", "insert3");
        DBObject dbObject4 = new BasicDBObject("username", "insert4");
        DBObject dbObject5 = new BasicDBObject("username", "insert5");
        List<DBObject> list = new ArrayList<DBObject>();
        list.add(dbObject1);
        list.add(dbObject2);
        list.add(dbObject3);
        list.add(dbObject4);
        mongoDBService.printListDBObj(mongoDBService.findAll());
        System.out.println("——————————————————delete list——————————————————");
        mongoDBService.deleteBatch(list);
        System.out.println("——————————————————delete one——————————————————");
        mongoDBService.delete(dbObject5);
        // System.out.println("——————————————————delete all——————————————————");
        // mongoDBService1.delete(new BasicDBObject());
        mongoDBService.printListDBObj(mongoDBService.findAll());
    }
}
