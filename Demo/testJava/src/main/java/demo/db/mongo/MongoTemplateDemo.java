package demo.db.mongo;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.IndexOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteResult;

import demo.vo.Person;

/**
 * MongoTemplate是线程安全的。
 * 
 * MongoTemplate实现了interface MongoOperations，一般推荐使用MongoOperations来进行相关的操作。
 * 
 * MongoDB documents和domain classes之间的映射关系是通过实现了MongoConverter这个interface的类来实现的。
 * 
 * 默认提供了两个SimpleMappingConverter（default） 和 MongoMappingConverter，但也可以自己定义。
 */
public class MongoTemplateDemo {

    private static MongoTemplate mongoTemplate = null;

    public static void main(String[] args) throws Exception {
        mongoTemplate = buildMongoTemplate(MongoDataTransfer.T1);
        listIndex("mx_carrier_calls");

        listIndex("mx_carrier_report_notify");
    }

    @Test
    public void commands() throws UnknownHostException {
        MongoTemplate mongoTemplate = buildMongoTemplate(MongoDataTransfer.T1);
        DB db = mongoTemplate.getDb();
        // 获取当前数据库的信息，比如Obj总数、数据库总大小、平均Obj大小等
        CommandResult commandResult = db.getStats();
        System.err.println(commandResult);
        CommandResult dbStats = db.command("dbStats");
        System.err.println(dbStats);

        CommandResult dbStats2 = db.command("listCommands");
        System.err.println(dbStats2);
        /*
         * The ping command is a no-op used to test whether a server is responding to commands. This command will return
         * immediately even if the server is write-locked:
         */
        dbStats2 = db.command("ping");
        System.err.println(dbStats2);
        System.out.println(db.getMongo().getAddress());
        
        dbStats2 = db.command("shardConnPoolStats");
        System.err.println(dbStats2);
        
        dbStats2 = db.command("top");
        System.err.println(dbStats2);
        
    }

    /**
     * 查询索引信息
     * 
     * @param collectionName
     */
    static void listIndex(String collectionName) {
        IndexOperations indexOperations = mongoTemplate.indexOps(collectionName);
        // 查询索引
        List<IndexInfo> indexInfos = indexOperations.getIndexInfo();
        if (indexInfos != null && !indexInfos.isEmpty()) {
            indexInfos.forEach(e -> {
                System.err.println(e.toString());
            });
        }
        System.err.println("================");
    }

    /**
     * 索引
     */
    static void indexDemo() {
        IndexOperations indexOperations = mongoTemplate.indexOps("dkw_user_loan_info");
        // 创建索引
        Index index = new Index("idcard", Direction.ASC);
        index.named("idx_idcard");
        index.background();
        indexOperations.ensureIndex(index);

    }

    /**
     * 构建MongoTemplate对象
     * 
     * @param uri
     * @return
     * @throws UnknownHostException
     */
    static MongoTemplate buildMongoTemplate(final String uri) throws UnknownHostException {
        MongoClientURI tergetUri = new MongoClientURI(uri);
        MongoDbFactory mongoDbFactory = new SimpleMongoDbFactory(tergetUri);
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory);
        return mongoTemplate;
    }

    /**
     * spring-mongodb-data 返回指定字段
     */
    void testFields() {
        // 查询条件
        DBObject queryObject = new BasicDBObject();
        queryObject.put("name", "zhangsan");

        BasicDBObject fieldsObject = new BasicDBObject();
        // 指定返回的字段
        fieldsObject.put("name", true);
        fieldsObject.put("age", true);
        fieldsObject.put("sex", true);

        Query query = new BasicQuery(queryObject, fieldsObject);
        query.with(new Sort(Direction.DESC, "timestamp"));
        List<Person> user = mongoTemplate.find(query, Person.class);
    }

    // @Query(value = "{'status':?0 }", fields = "{ '_id' : 1, 'catalogName': 1}")
    // List<Object> findAllForeCatalogIdAndName(String status);

    /**
     * 查询一条数据:(多用于保存时判断db中是否已有当前数据,这里 is 精确匹配,模糊匹配 使用 regex...)
     * 
     * @param url
     * @return
     */
    public Object findOneBy(String url) {
        return mongoTemplate.findOne(new Query(Criteria.where("url").is(url)), Object.class);
    }

    /**
     * 查询多条数据:linkUrl.id 属于分级查询
     * 
     * @param begin
     * @param end
     * @param linkUrlid
     * @return
     */
    public List<Object> pageBy(int begin, int end, String linkUrlid) {
        Query query = new Query();
        query.addCriteria(Criteria.where("linkUrl.id").is(linkUrlid));
        return mongoTemplate.find(query.limit(end - begin).skip(begin), Object.class);
    }

    /**
     * 模糊查询
     * 
     * @param conditions
     * @return
     */
    public long getProcessLandLogsCount(Map<String, String> conditions) {
        Query query = new Query();
        if (conditions != null && conditions.size() > 0) {
            for (Map.Entry<String, String> condition : conditions.entrySet()) {
                query.addCriteria(
                        Criteria.where(condition.getKey()).regex(".*?\\" + condition.getValue().toString() + ".*"));
            }
        }
        return mongoTemplate.count(query, Object.class);
    }

    /**
     * gte: 大于等于,lte小于等于...注意查询的时候各个字段的类型要和mongodb中数据类型一致
     * 
     * @param begin
     * @param end
     * @param beginDate
     * @param endDate
     * @return
     */
    public List<Object> getDpsLandsByTime(int begin, int end, Date beginDate, Date endDate) {
        return mongoTemplate.find(
                new Query(Criteria.where("updateTime").gte(beginDate).lte(endDate)).limit(end - begin).skip(begin),
                Object.class);
    }

    /**
     * 查询字段不存在的数据
     * 
     * @param begin
     * @param end
     * @return
     */
    public List<Object> getGoodsDetails2(int begin, int end) {
        // 查询字段不为空的数据
        Criteria.where("key1").ne("").ne(null);
        // 查询或语句：a || b
        Criteria criteria = new Criteria();
        criteria.orOperator(Criteria.where("key1").is("0"), Criteria.where("key1").is(null));
        // 查询且语句：a && b
        Criteria criteria1 = new Criteria();
        criteria1.and("key1").is(false);
        criteria1.and("key2").is("a");
        // 查询一个属性的子属性

        Query query = new Query();
        query.addCriteria(Criteria.where("goodsSummary").not());
        return mongoTemplate.find(query.limit(end - begin).skip(begin), Object.class);
    }

    /**
     * 更新一条数据的一个字段:
     */
    public WriteResult updateTime(Object pageUrl) {
        String id = "a";
        return mongoTemplate.updateFirst(new Query(Criteria.where("id").is(id)),
                Update.update("updateTime", new Date()), Object.class);
    }

    /**
     * 删除数据:
     */
    public <T> void deleteObject(Class<T> clazz, String id) {
        mongoTemplate.remove(new Query(Criteria.where("id").is(id)), clazz);
    }

    /**
     * 插入一条数据
     */
    public void saveObject(Object obj) {
        mongoTemplate.insert(obj);
    }
}
