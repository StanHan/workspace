package demo.spring.framework.beans.factory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.HierarchicalBeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * Spring Bean的创建是典型的工厂模式，这一系列的Bean工厂，也即IOC容器为开发者管理对象间的依赖关系提供了很多便利和基础服务。
 * 
 * BeanFactory作为最顶层的一个接口类，它定义了IOC容器的基本功能规范，BeanFactory 有三个子类：ListableBeanFactory、HierarchicalBeanFactory
 * 和AutowireCapableBeanFactory。 但是从上图中我们可以发现最终的默认实现类是 DefaultListableBeanFactory，他实现了所有的接口。
 * 那为何要定义这么多层次的接口呢？查阅这些接口的源码和说明发现，每个接口都有他使用的场合，它主要是为了区分在 Spring 内部在操作过程中对象的传递和转化过程中，对对象的数据访问所做的限制。
 *
 */
public class BeanFactoryDemo {

    public static void main(String[] args) {
        BeanFactory beanFactory;
        
    }

    /**
     * ListableBeanFactory 接口表示这些 Bean 是可列表的
     */
    static void demoListableBeanFactory() {
        ListableBeanFactory listableBeanFactory = new DefaultListableBeanFactory();
    }

    /**
     * HierarchicalBeanFactory 表示的是这些 Bean 是有继承关系的，也就是每个Bean 有可能有父 Bean
     */
    static void demoHierarchicalBeanFactory() {
        HierarchicalBeanFactory listableBeanFactory = new DefaultListableBeanFactory();
    }

    /**
     * AutowireCapableBeanFactory 接口定义 Bean 的自动装配规则
     */
    static void demoAutowireCapableBeanFactory() {
        AutowireCapableBeanFactory listableBeanFactory = new DefaultListableBeanFactory();
    }

}
