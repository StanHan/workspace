package demo.db.mabatis;

import java.io.InputStream;
import java.io.Reader;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.SimpleStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.Transaction;
import org.apache.ibatis.transaction.TransactionFactory;
import org.hibernate.boot.archive.scan.spi.PackageInfoArchiveEntryHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.db.mabatis.dao.UserMapper;
import demo.db.mabatis.service.UserService;
import demo.vo.pojo.User;

/**
 * <h2>简介</h2> MyBatis的前身叫iBatis，本是apache的一个开源项目, 2010年这个项目由apache software foundation 迁移到了google code，并且改名为MyBatis。
 * MyBatis是支持普通SQL查询，存储过程和高级映射的优秀持久层框架。MyBatis消除了几乎所有的JDBC代码和参数的手工设置以及结果集的检索。
 * MyBatis使用简单的XML或注解用于配置和原始映射，将接口和Java的POJOs（Plan Old Java Objects，普通的Java对象）映射成数据库中的记录。
 * <p>
 * Mybatis的功能架构分为三层
 * <li>1) API接口层：提供给外部使用的接口API，开发人员通过这些本地API来操纵数据库。接口层一接收到调用请求就会调用数据处理层来完成具体的数据处理。
 * <li>2) 数据处理层：负责具体的SQL查找、SQL解析、SQL执行和执行结果映射处理等。它主要的目的是根据调用的请求完成一次数据库操作。
 * <li>3) 基础支撑层：负责最基础的功能支撑，包括连接管理、事务管理、配置加载和缓存处理，这些都是共用的东西，将他们抽取出来作为最基础的组件。为上层的数据处理层提供最基础的支撑。
 */
public class MyBatisDemo {

    public static void main(String[] args) {
        // demoMybatis();
        demoSpringMybatis();
    }

    static void demoSpringMybatis() {
        // 使用"spring.xml"和"spring-mybatis.xml"这两个配置文件创建Spring上下文
        ApplicationContext ac = new ClassPathXmlApplicationContext(
                new String[] { "spring/beans.xml", "spring/spring-mybatis.xml" });
        // 从Spring容器中根据bean的id取出我们要使用的userService对象
        UserService serviceA = ac.getBean(UserService.class);
    }

    /**
     * <h2>SqlSession</h2>
     * 
     * SqlSession对象的主要功能是完成一次数据库的访问和结果的映射，它类似于数据库的session概念，由于不是线程安全的，所以SqlSession对象的作用域需限制方法内。
     * SqlSession的默认实现类是DefaultSqlSession，它有两个必须配置的属性：Configuration和Executor。 SqlSession对数据库的操作都是通过Executor来完成的.
     * SqlSession有一个重要的方法getMapper，顾名思义，这个方式是用来获取Mapper对象的。
     * 
     * <h2>什么是Mapper对象？</h2>
     * 
     * 根据Mybatis的官方手册，应用程序除了要初始并启动Mybatis之外，还需要定义一些接口，接口里定义访问数据库的方法，存放接口的包路径下需要放置同名的XML配置文件。
     * SqlSession的getMapper方法是联系应用程序和Mybatis纽带，应用程序访问getMapper时，Mybatis会根据传入的接口类型和对应的XML配置文件生成一个代理对象，这个代理对象就叫Mapper对象。
     * 应用程序获得Mapper对象后，就应该通过这个Mapper对象来访问Mybatis的SqlSession对象，这样就达到里插入到Mybatis流程的目的。
     * 
     * <h2>Executor</h2>
     * 
     * Executor对象在创建Configuration对象的时候创建，并且缓存在Configuration对象里。
     * Executor对象的主要功能是调用StatementHandler访问数据库，并将查询结果存入缓存中（如果配置了缓存的话）。
     * 
     * <h2>StatementHandler</h2> StatementHandler是真正访问数据库的地方，并调用ResultSetHandler处理查询结果。
     * 
     * <h2>ResultSetHandler</h2> 处理查询结果。
     */
    static void demoMybatis() {
        SqlSessionFactory sqlSessionFactory = buildSqlSessionFactory1("db/mybatis/conf.xml");

        // 创建能执行映射文件中sql的sqlSession
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userDao = sqlSession.getMapper(UserMapper.class);
        userDao.selectByPrimaryKey(1);
        /**
         * 映射sql的标识字符串， me.gacl.mapping.userMapper是userMapper.xml文件中mapper标签的namespace属性的值，
         * getUser是select标签的id属性值，通过select标签的id属性值就可以找到要执行的SQL
         */
        String statement = "selectByPrimaryKey";// 映射sql的标识字符串
        Map<String, Object> map = new HashMap<>();
        // 执行查询返回一个唯一user对象的sql
        User user = sqlSession.selectOne(statement, 1L);
        System.out.println(user);
    }

    /**
     * <h2>SqlSessionFactoryBuilder</h2>
     * 每一个MyBatis的应用程序的入口是SqlSessionFactoryBuilder，它的作用是通过XML配置文件创建Configuration对象（当然也可以在程序中自行创建），
     * 然后通过build方法创建SqlSessionFactory对象。没有必要每次访问Mybatis就创建一次SqlSessionFactoryBuilder，通常的做法是创建一个全局的对象就可以了。
     * 
     * <h2>SqlSessionFactory</h2> SqlSessionFactory对象由SqlSessionFactoryBuilder创建。
     * 它的主要功能是创建SqlSession对象，和SqlSessionFactoryBuilder对象一样，没有必要每次访问Mybatis就创建一次SqlSessionFactory，通常的做法是创建一个全局的对象就可以了。
     * SqlSessionFactory对象一个必要的属性是Configuration对象,它是保存Mybatis全局配置的一个配置对象，通常由SqlSessionFactoryBuilder从XML配置文件创建。
     * 
     * @param filePath
     * @return
     */
    public static SqlSessionFactory buildSqlSessionFactory2(String filePath) {
        SqlSessionFactory sqlSessionFactory = null;
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        // 使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
        try (Reader reader = Resources.getResourceAsReader(filePath)) {
            // 构建sqlSession的工厂
            sqlSessionFactory = sqlSessionFactoryBuilder.build(reader);
            return sqlSessionFactory;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlSessionFactory;
    }

    /**
     * 
     * @param sqlSessionFactory
     * @throws SQLException
     */
    public static void demoConfiguration(SqlSessionFactory sqlSessionFactory) throws SQLException {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        Executor executor = newExecutor(configuration);
        MappedStatement mappedStatement = null;
        Object parameter = null;
        RowBounds rowBounds = null;
        ResultHandler resultHandler = null;
        BoundSql boundSql = null;
        StatementHandler statementHandler = new SimpleStatementHandler(executor, mappedStatement, parameter, rowBounds,
                resultHandler, boundSql);
        
        Statement statement = null;
        statementHandler.query(statement, resultHandler);
    }

    static Executor newExecutor(Configuration configuration) {
        final Environment environment = configuration.getEnvironment();
        final DataSource dataSource = environment.getDataSource();
        final TransactionFactory transactionFactory = environment.getTransactionFactory();
        final Transaction transaction = transactionFactory.newTransaction(dataSource, null, false);
        return configuration.newExecutor(transaction, ExecutorType.SIMPLE);
    }

    public static SqlSessionFactory buildSqlSessionFactory1(String filePath) {
        SqlSessionFactory sqlSessionFactory = null;
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        // 使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
        try (InputStream inputStream = MyBatisDemo.class.getClassLoader().getResourceAsStream("db/mybatis/conf.xml");) {
            // 构建sqlSession的工厂
            sqlSessionFactory = sqlSessionFactoryBuilder.build(inputStream);
            return sqlSessionFactory;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlSessionFactory;
    }

}
