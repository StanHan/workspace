package demo.db.mabatis;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import demo.db.mabatis.service.ServiceA;
import demo.vo.AdminOperationLog;

public class MyBatisDemo {

    public static void main(String[] args) {
//        demoMybatis();
        demoSpringMybatis();
    }

    static void demoSpringMybatis() {
        // 使用"spring.xml"和"spring-mybatis.xml"这两个配置文件创建Spring上下文
        ApplicationContext ac = new ClassPathXmlApplicationContext(new String[] { "spring/beans.xml", "spring/spring-mybatis.xml" });
        // 从Spring容器中根据bean的id取出我们要使用的userService对象
        ServiceA serviceA = ac.getBean(ServiceA.class);
        serviceA.selectAdminOperationLog();
    }

    static void demoMybatis() {
        // mybatis的配置文件
        String resource = "db/mybatis/conf.xml";
        // 使用类加载器加载mybatis的配置文件（它也加载关联的映射文件）
        InputStream is = MyBatisDemo.class.getClassLoader().getResourceAsStream(resource);
        // 构建sqlSession的工厂
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        // 使用MyBatis提供的Resources类加载mybatis的配置文件（它也加载关联的映射文件）
        // Reader reader = Resources.getResourceAsReader(resource);
        // 构建sqlSession的工厂
        // SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(reader);
        // 创建能执行映射文件中sql的sqlSession
        SqlSession session = sessionFactory.openSession();
        /**
         * 映射sql的标识字符串， me.gacl.mapping.userMapper是userMapper.xml文件中mapper标签的namespace属性的值，
         * getUser是select标签的id属性值，通过select标签的id属性值就可以找到要执行的SQL
         */
        String statement = "selectByPrimaryKey";// 映射sql的标识字符串
        Map<String, Object> map = new HashMap<>();
        // 执行查询返回一个唯一user对象的sql
        AdminOperationLog user = session.selectOne(statement, 1L);
        System.out.println(user);
    }

}
