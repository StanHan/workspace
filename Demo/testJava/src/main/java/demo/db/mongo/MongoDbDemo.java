package demo.db.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;

/**
 * MongoDB 是一个基于分布式文件存储的数据库。由 C++ 语言编写。旨在为 WEB 应用提供可扩展的高性能数据存储解决方案。
 * 
 * MongoDB 是一个介于关系数据库和非关系数据库之间的产品，是非关系数据库当中功能最丰富，最像关系数据库的。
 * 
 * NoSQL，指的是非关系型的数据库。NoSQL有时也称作Not Only SQL的缩写，是对不同于传统的关系型数据库的数据库管理系统的统称。
 * 
 * NoSQL用于超大规模数据的存储。这些类型的数据存储不需要固定的模式，无需多余操作就可以横向扩展。
 * <p>
 * RDBMS vs NoSQL
 * <li>RDBMS : 高度组织化结构化数据 ；结构化查询语言（SQL） (SQL);数据和关系都存储在单独的表中；数据操纵语言，数据定义语言 ;严格的一致性； 基础事务
 * <li>NoSQL :代表着不仅仅是SQL;没有声明性查询语言;没有预定义的模式;键 - 值对存储，列存储，文档存储，图形数据库; 最终一致性，而非ACID属性;非结构化和不可预知的数据;CAP定理 ; 高性能，高可用性和可伸缩性
 * <p>
 * CAP定理（CAP theorem）, 又被称作 布鲁尔定理（Brewer's theorem）, 它指出对于一个分布式计算系统来说，不可能同时满足以下三点:
 * <li>一致性(Consistency) (所有节点在同一时间具有相同的数据)
 * <li>可用性(Availability) (保证每个请求不管成功或者失败都有响应)
 * <li>分隔容忍(Partition tolerance) (系统中任意信息的丢失或失败不会影响系统的继续运作)
 * 
 * CAP理论的核心是：一个分布式系统不可能同时很好的满足一致性，可用性和分区容错性这三个需求，最多只能同时较好的满足两个。
 * 
 * MongoDB 将数据存储为一个文档，数据结构由键值(key=>value)对组成。MongoDB 文档类似于 JSON 对象。字段值可以包含其他文档，数组及文档数组。
 * 
 * 概念解析:database 数据库;collection 数据库表/集合;document 数据记录行/文档;field 数据字段/域;index 索引;primary key 主键,MongoDB自动将_id字段设置为主键
 * 
 * 文档是一组键值(key-value)对(即BSON)。MongoDB 的文档不需要设置相同的字段，并且相同的字段不需要相同的数据类型，这与关系型数据库有很大的区别，也是 MongoDB 非常突出的特点。
 * 
 * 需要注意的是： 文档中的键/值对是有序的。 文档中的值不仅可以是在双引号里面的字符串，还可以是其他几种数据类型（甚至可以是整个嵌入的文档)。 MongoDB区分类型和大小写。 MongoDB的文档不能有重复的键。
 * 文档的键是字符串。除了少数例外情况，键可以使用任意UTF-8字符。 文档键命名规范： 键不能含有\0 (空字符)。这个字符用来表示键的结尾。 .和$有特别的意义，只有在特定环境下才能使用。
 * 以下划线"_"开头的键是保留的(不是严格要求的)。
 */
public class MongoDbDemo {

    public static void main(String[] args) {
        demoObjectId();
    }

    /**
     * 连接数据库: 连接数据库，你需要指定数据库名称，如果指定的数据库不存在，mongo会自动创建数据库。
     */
    public static void connect2() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");
            System.out.println("Connect to database successfully");

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 本实例中 Mongo 数据库无需用户名密码验证。如果你的 Mongo 需要验证用户名及密码，可以使用以下代码：
     */
    public static void connect() {
        try {
            // 连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
            // ServerAddress()两个参数分别为 服务器地址 和 端口
            ServerAddress serverAddress = new ServerAddress("localhost", 27017);
            List<ServerAddress> addrs = new ArrayList<ServerAddress>();
            addrs.add(serverAddress);

            // MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
            MongoCredential credential = MongoCredential.createScramSha1Credential("username", "databaseName",
                    "password".toCharArray());
            List<MongoCredential> credentials = new ArrayList<MongoCredential>();
            credentials.add(credential);

            // 通过连接认证获取MongoDB连接
            MongoClient mongoClient = new MongoClient(addrs, credentials);

            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("databaseName");
            System.out.println("Connect to database successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 创建集合:我们可以使用 com.mongodb.client.MongoDatabase 类中的createCollection()来创建集合
     */
    public static void createCollection() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");
            System.out.println("Connect to database successfully");
            mongoDatabase.createCollection("test");
            System.out.println("集合创建成功");

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 获取集合 ,我们可以使用com.mongodb.client.MongoDatabase类的 getCollection() 方法来获取一个集合
     */
    public static void getCollection() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");
            System.out.println("Connect to database successfully");

            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            System.out.println("集合 test 选择成功");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 我们可以使用com.mongodb.client.MongoCollection类的 insertMany() 方法来插入一个文档
     * <li>1. 创建文档 org.bson.Document 参数为key-value的格式
     * <li>2. 创建文档集合List<Document>
     * <li>3. 将文档集合插入数据库集合中
     * <li>mongoCollection.insertMany(List<Document>)
     * <li>插入单个文档可以用 mongoCollection.insertOne(Document)
     */
    public static void insert() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");
            System.out.println("Connect to database successfully");

            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            System.out.println("集合 test 选择成功");
            // 插入文档
            Document document = new Document("title", "MongoDB").append("description", "database").append("likes", 100)
                    .append("by", "Fly");
            List<Document> documents = new ArrayList<Document>();
            documents.add(document);
            collection.insertMany(documents);
            System.out.println("文档插入成功");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 删除第一个文档: 要删除集合中的第一个文档，首先你需要使用com.mongodb.DBCollection类中的 findOne()方法来获取第一个文档，然后使用remove 方法删除。
     */
    public static void delete() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");
            System.out.println("Connect to database successfully");

            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            System.out.println("集合 test 选择成功");

            // 删除符合条件的第一个文档
            collection.deleteOne(Filters.eq("likes", 200));
            // 删除所有符合条件的文档
            collection.deleteMany(Filters.eq("likes", 200));
            // 检索查看结果
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            while (mongoCursor.hasNext()) {
                System.out.println(mongoCursor.next());
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 可以使用 com.mongodb.client.MongoCollection 类中的 updateMany() 方法来更新集合中的文档。
     */
    public static void update() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");
            System.out.println("Connect to database successfully");

            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            System.out.println("集合 test 选择成功");

            // 更新文档 将文档中likes=100的文档修改为likes=200
            collection.updateMany(Filters.eq("likes", 100), new Document("$set", new Document("likes", 200)));
            // 检索查看结果
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            while (mongoCursor.hasNext()) {
                System.out.println(mongoCursor.next());
            }

        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }

    /**
     * 检索所有文档:我们可以使用 com.mongodb.client.MongoCollection 类中的 find() 方法来获取集合中的所有文档。此方法返回一个游标，所以你需要遍历这个游标。
     * <li>1. 获取迭代器FindIterable<Document>
     * <li>2. 获取游标MongoCursor<Document>
     * <li>3. 通过游标遍历检索出的文档集合
     */
    static void find() {
        try {
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient("localhost", 27017);

            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("mycol");
            System.out.println("Connect to database successfully");

            MongoCollection<Document> collection = mongoDatabase.getCollection("test");
            System.out.println("集合 test 选择成功");

            // 检索所有文档
            FindIterable<Document> findIterable = collection.find();
            MongoCursor<Document> mongoCursor = findIterable.iterator();
            while (mongoCursor.hasNext()) {
                System.out.println(mongoCursor.next());
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
        }
    }


    /**
     * ObjectId 是一个12字节 BSON 类型数据，有以下格式：
     * <li>前4个字节表示时间戳
     * <li>接下来的3个字节是机器标识码
     * <li>紧接的两个字节由进程id组成（PID）
     * <li>最后三个字节是随机数。
     * <p>
     * MongoDB中存储的文档必须有一个"_id"键。这个键的值可以是任何类型的，默认是个ObjectId对象。 在一个集合里面，每个文档都有唯一的"_id"值，来确保集合里面每个文档都能被唯一标识。
     * MongoDB采用ObjectId，而不是其他比较常规的做法（比如自动增加的主键）的主要原因，因为在多个 服务器上同步自动增加主键值既费力还费时。
     */
    static void demoObjectId() {
        ObjectId objectId = new ObjectId("5a3143004ca8e458b3ef03fa");
        System.out.println(objectId.getTimestamp());
        long timestamp = objectId.getTimestamp() * 1000L;
        System.out.println(new Date(timestamp));
    }

    static void demo(MongoClient mongoClient) {
        // "show dbs" 命令可以显示所有数据的列表。
        MongoIterable<String> dbNames = mongoClient.listDatabaseNames();

        mongoClient.getConnectPoint();
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
