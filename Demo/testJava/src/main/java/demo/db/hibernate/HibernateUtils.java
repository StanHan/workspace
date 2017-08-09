package demo.db.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.id.IdentifierGenerator;

public class HibernateUtils {

    public static void main(String[] args) {

    }

    /**
     * 在Hibernate中，id元素的<generator>子元素用于生成持久化类的对象的唯一标识符。 Hibernate框架中定义了许多生成器类。
     * 
     * 所有的生成器类都实现了org.hibernate.id.IdentifierGenerator接口。 应用程序员可以通过实现IdentifierGenerator接口来创建自己的生成器类。
     * Hibernate框架提供了许多内置的生成器类：
     * 
     * <li>assigned。如果没有使用<generator>元素，assigned是默认的生成器策略。在这种情况下，应用程序为对象分配ID
     * <li>increment。当没有其他进程将数据插入此表时，才会生成唯一的ID。 它生成short，int或long型标识符。 第一个生成的标识符通常为1，然后每次递增为1
     * <li>sequence。它使用数据库的顺序序列。如果没有定义序列，它会自动创建一个序列。 在Oracle数据库的情况下，它将创建一个名为HIBERNATE_SEQUENCE的序列。要定义自己的序列，请使用generator的
     * param 子元素
     * <li>hilo。使用高低算法来生成short，int和long类型的id
     * <li>native。使用标识，序列或希洛取决于数据库供应商。
     * <li>identity。它用于Sybase，Mysql，MS SQL Server，DB2和Hypersonic SQL以支持id列。 返回的ID类型为short，int或long
     * <li>seqhilo。它在指定的序列名称上使用高低算法。 返回的ID类型为short，int或long。
     * <li>uuid。它使用128位UUID算法生成id。 返回的ID是String类型，在网络中是唯一的(因为使用了IP)。 UUID以十六进制数字表示，长度为32。
     * <li>guid。它使用由字符串类型的数据库生成的GUID。 它适用于MS SQL Server和MySQL。
     * <li>select。它使用数据库触发器返回主键。
     * <li>foreign。它使用另一个关联对象的id，主要用于<一对一>关联。
     * <li>sequence-identity。它使用特殊的序列生成策略。 仅在Oracle 10g驱动程序中支持。
     */
    void testIdentifierGenerator() {
        IdentifierGenerator a = null;
    }

    public static SessionFactory getSessionFactory() {
        // creating configuration object
        Configuration configuration = new Configuration();
        configuration.configure("db/hibernate.cfg.xml");// populates the data of the configuration file

        // creating seession factory object
        SessionFactory sessionFactory = configuration.buildSessionFactory();

        return sessionFactory;

    }

    public static SessionFactory getSessionFactory2() {
        // 从持久化类获取映射的信息
        // Session session = new AnnotationConfiguration().configure().buildSessionFactory().openSession();
        // 1. 配置类型安全的准服务注册类，这是当前应用的单例对象，不作修改，所以声明为final
        // 在configure("cfg/hibernate.cfg.xml")方法中，如果不指定资源路径，默认在类路径下寻找名为hibernate.cfg.xml的文件
        final StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();
        registryBuilder.configure("db/hibernate.cfg.xml");
        StandardServiceRegistry registry = registryBuilder.build();
        // 2. 根据服务注册类创建一个元数据资源集，同时构建元数据并生成应用一般唯一的的session工厂
        MetadataSources metadataSources = new MetadataSources(registry);
        Metadata metadata = metadataSources.buildMetadata();
        SessionFactory sessionFactory = metadata.buildSessionFactory();
        return sessionFactory;
    }

}
