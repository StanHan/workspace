package demo.db.mongo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;

/**
 * MongoDB 是一个基于分布式文件存储的数据库。由 C++ 语言编写。旨在为 WEB 应用提供可扩展的高性能数据存储解决方案。 MongoDB
 * 是一个介于关系数据库和非关系数据库之间的产品，是非关系数据库当中功能最丰富，最像关系数据库的。
 * 
 * NoSQL，指的是非关系型的数据库。NoSQL有时也称作Not Only SQL的缩写，是对不同于传统的关系型数据库的数据库管理系统的统称。
 * NoSQL用于超大规模数据的存储。这些类型的数据存储不需要固定的模式，无需多余操作就可以横向扩展。
 * <p>
 * <h2>RDBMS vs NoSQL</h2>
 * <p>
 * <li>RDBMS : 高度组织化结构化数据 ；结构化查询语言（SQL） (SQL);数据和关系都存储在单独的表中；数据操纵语言，数据定义语言 ;严格的一致性； 基础事务
 * <li>NoSQL :代表着不仅仅是SQL;没有声明性查询语言;没有预定义的模式;键 - 值对存储，列存储，文档存储，图形数据库; 最终一致性，而非ACID属性;非结构化和不可预知的数据;CAP定理 ; 高性能，高可用性和可伸缩性
 * <p>
 * MongoDB 将数据存储为一个文档，数据结构由键值(key=>value)对组成。MongoDB 文档类似于 JSON 对象。字段值可以包含其他文档，数组及文档数组。
 * 
 * <h2>概念解析</h2>
 * <p>
 * database 数据库;collection 数据库表/集合;document 数据记录行/文档;field 数据字段/域;index 索引;primary key 主键,MongoDB自动将_id字段设置为主键
 * 
 * 文档是一组键值(key-value)对(即BSON)。MongoDB 的文档不需要设置相同的字段，并且相同的字段不需要相同的数据类型，这与关系型数据库有很大的区别，也是 MongoDB 非常突出的特点。
 * 
 * 需要注意的是： 文档中的键/值对是有序的。 文档中的值不仅可以是在双引号里面的字符串，还可以是其他几种数据类型（甚至可以是整个嵌入的文档)。 MongoDB区分类型和大小写。 MongoDB的文档不能有重复的键。
 * 文档的键是字符串。除了少数例外情况，键可以使用任意UTF-8字符。 文档键命名规范： 键不能含有\0 (空字符)。这个字符用来表示键的结尾。 .和$有特别的意义，只有在特定环境下才能使用。
 * 以下划线"_"开头的键是保留的(不是严格要求的)。
 */
public class MongoDbDemo {

    public static void main(String[] args) {
    }

    /**
     * <h2>MongoDB配置参数详解：</h2>
     * <p>
     * <li>#对mongo实例来说，每个host允许链接的最大链接数,这些链接空闲时会放入池中,如果链接被耗尽，任何请求链接的操作会被阻塞等待链接可用,推荐配置10 connectionsPerHost=10
     * <li>#当链接空闲时,空闲线程池中最大链接数 minPoolsSize=5
     * <li>#此参数跟connectionsPerHost的乘机为一个线程变为可用的最大阻塞数，超过此乘机数之后的所有线程将及时获取一个异常.eg.connectionsPerHost=10 and
     * threadsAllowedToBlockForConnectionMultiplier=5,最多50个线程等级一个链接，推荐配置为5
     * threadsAllowedToBlockForConnectionMultiplier=5
     * <li>#一个线程等待链接可用的最大等待毫秒数，0表示不等待，负数表示等待时间不确定，推荐配置120000 maxWaitTime=120000
     * <li>#链接超时的毫秒数,0表示不超时,此参数只用在新建一个新链接时，推荐配置10,000. connectTimeout=10000
     * <li>#此参数表示socket I/O读写超时时间,推荐为不超时，即 0 Socket.setSoTimeout(int) socketTimeout=0
     * <li>#该标志用于控制socket保持活动的功能，通过防火墙保持连接活着 socketKeepAlive=false
     * #true:假如链接不能建立时,驱动将重试相同的server,有最大的重试次数,默认为15次,这样可以避免一些server因为一些阻塞操作零时down而驱动抛出异常,这个对平滑过度到一个新的master,也是很有用的,注意:当集群为复制集时,驱动将在这段时间里,尝试链接到旧的master上,而不会马上链接到新master上
     * #false 当在进行socket读写时,不会阻止异常抛出,驱动已经有自动重建破坏链接和重试读操作. 推荐配置false autoConnectRetry=false
     * <li>#重新打开链接到相同server的最大毫秒数,推荐配置为0，如果 autoConnectRetry=true,表示时间为15s
     * #com.jd.mongodbclient2.mongo.JDClientMongo.maxAutoConnectRetryTime=false
     * <li>#表示当没有手动关闭游标时,是否有一个自动释放游标对象的方法,如果你总是很小心的关闭游标,则可以将其设为false 推荐配置true
     * #com.jd.mongodbclient2.mongo.JDClientMongo.cursorFinalizerEnabled=true
     * 
     * <li>#安全模式
     * 
     * com.jd.mongodbclient2.driver.MongoDBDriver.safe=true
     * <li>#为true表示读写分离
     * 
     * com.jd.mongodbclient2.driver.MongoDBDriver.slaveOk=false
     */
    static void config() {

    }

    /**
     * Mongodb的replication主要有两种：主从和副本集（replica set）。
     * <li>主从的原理和mysql类似，主节点记录在其上的所有操作oplog，从节点定期轮询主节点获取这些操作，然后对自己的数据副本执行这些操作，从而保证从节点的数据与主节点一致。
     * <li>mongodb官方建议用副本集替代主从复制。
     * <p>
     * <b>默认情况下，slave是不允许读写数据的，这个和replica set(副本集)一样，但是我们可以通过设置db.getMongo().setSlaveOk()或rs.slaveOk()，让slave允许读。</b>
     * 
     * 1.当slave的oplog更新不及时导致落后master太多，将会导致slave不同步，replication会停止，此时必须通过管理员进行手动干预重启replication。
     * 此时有两种方法，一是使用resync；另一种是在slave上加–autoresync参数，允许slave在落后master10s后自动重启replicaiton。其中使用resync的效果和删除slave上的数据文件并重启同步一样。
     * 
     * 为什么会出现以上情况呢？
     * 因为oplog在启动时会有一个初始大小，随着数据的不断进入oplog会达到预定的大小，并且会重新循环对oplog进行重写。因此当slave落后太多，此时oplog若重写将导致数据不一致，因此同步将会停止。
     * 为避免以上状况发生，我们也可通过加到oplog来预防。
     * 
     * 对于replica set 中的secondary 节点默认是不可读的。 在写多读少的应用中，使用Replica
     * Sets来实现读写分离。通过在连接时指定或者在主库指定slaveOk，由Secondary来分担读的压力，Primary只承担写操作。
     */
    static void replication(DB db) {
        // 在复制集中优先读secondary，如果secondary访问不了的时候就从master中读
        db.setReadPreference(ReadPreference.secondaryPreferred());
        // 只从secondary中读，如果secondary访问不了的时候就不能进行查询
        db.setReadPreference(ReadPreference.secondary());
    }

    /**
     * 索引
     */
    static void indexDemo() {

    }

    /**
     * 连接数据库: 连接数据库，你需要指定数据库名称，如果指定的数据库不存在，mongo会自动创建数据库。
     */
    static void connectWithoutPassword() {
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
    static void connect() {
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
    static void createCollection() {
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
    static void getCollection() {
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
    static void insert() {
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
    static void delete() {
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
    static void update() {
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
    @Test
    public void demoObjectId() {
        ObjectId objectId = new ObjectId("5ae033294ca8e4179661344c");
        System.out.println(objectId.getTimestamp());
        long timestamp = objectId.getTimestamp() * 1000L;
        System.out.println(new Date(timestamp));
        System.out.println(JSONObject.toJSONString(objectId));
        Set<ObjectId> set = new HashSet<>(100_000_000);
        Date now = new Date();
        IntStream.range(0, 100_000_000).forEach(e -> {
            ObjectId tmp = new ObjectId(now);
            boolean notExisted = set.add(tmp);
            if (!notExisted) {
                System.out.println(set.size());
            }
        });
        System.out.println("循环 100_000_000 次耗时毫秒：" + (System.currentTimeMillis() - now.getTime()));
    }

    static void demo(MongoClient mongoClient) {
        // "show dbs" 命令可以显示所有数据的列表。
        MongoIterable<String> dbNames = mongoClient.listDatabaseNames();

        mongoClient.getConnectPoint();
    }

}
