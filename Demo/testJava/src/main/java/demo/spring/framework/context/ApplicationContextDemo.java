package demo.spring.framework.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

/**
 * ApplicationContext是Spring提供的一个高级的IoC容器，它除了能够提供IoC容器的基本功能外，还为用户提供了以下的附加服务。从ApplicationContext接口的实现，我们看出其特点：
 * <li>1. 支持信息源，可以实现国际化。（实现MessageSource接口）
 * <li>2. 访问资源。(实现ResourcePatternResolver接口，这个后面要讲)
 * <li>3. 支持应用事件。(实现ApplicationEventPublisher接口)
 * 
 * 不管以怎样的方式创建 ApplicationContext 实例，都需要为 ApplicationContext 指定配置文件，Spring 允许使用一份或多份 XML 配置文件。 当程序创建 ApplicationContext
 * 实例化时，通常也是以 Resource 的方式来访问配置文件的，所以 ApplicationContext 完全支持 ClassPathResource，FileSystemResource，ServletContextResouce
 * 等资源访问方式。ApplicationContext 确定资源访问策略通常有两个方法：
 * <li>ApplicationContext 实现类指定访问策略。
 * <li>前缀指定访问策略。
 * <p>
 * 当我们创建 ApplicationContext 对象时，通常可以使用如下三个实现类：
 * <li>ClassPathXmlApplicatinContext：对应使用 ClassPathResource 进行资源访问。
 * <li>FileSystemXmlApplicationContext：对应使用 FileSystemResoure 进行资源访问。
 * <li>XmlWebApplicationContext：对应使用 ServletContextResource 进行资源访问。
 * <p>
 * Spring 也允许前缀来指定资源访问策略: ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath:bean.xml"); 虽然上面代码采用了
 * FileSystemXmlApplicationContext 实现类，但程序将从类加载路径里搜索 bean.xml 配置文件，而不是相对当前路径搜索。相应的，还可以使用 http:、ftp: 等前缀，用来确定对应的资源访问策略。
 * 通过 classpath: 前缀指定资源访问策略仅仅对当次访问有效，程序后面进行资源访问时，还是会根据 AppliactionContext 的实现类来选择对应的资源访问策略。 因此如果程序需要使用
 * ApplicationContext 访问资源，建议显式采用对应的实现类来加载配置文件，而不是通过前缀来指定资源访问策略。当然，我们也可在每次进行资源访问时都指定前缀，让程序根据前缀来选择资源访问策略。
 * <p>
 * 
 * Spring 框架能发展到今天绝非偶然，很大程度上来自于两方面原因：
 * <li>一方面 Spring 框架既提供了简单、易用的编程接口，因此深得用户拥护；
 * <li>另一方面 Spring 框架自身具有极为优秀的设计，这种优秀的设计保证了 Spring 框架具有强大生命力。
 * <p>
 * 对于一个有志于向架构师发展的软件工程师而言，精研 Spring 框架的源码，深入理解 Spring 框架的设计是一个不错的途径。本文主要从策略模式的角度来分析了 Spring 资源访问方面的设计，从而帮助读者更好地理解 Spring
 * 框架。
 */
public class ApplicationContextDemo {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    /**
     * <h2>classpath*: 前缀的用法</h2>
     * <p>
     * classpath*: 前缀提供了装载多个 XML 配置文件的能力，当使用 classpath*: 前缀来指定 XML 配置文件时，系统将搜索类加载路径，找出所有与文件名的文件，分别装载文件中的配置定义，最后合并成一个
     * ApplicationContext。
     * <p>
     * 如果不是采用 classpath*: 前缀，而是改为使用 classpath: 前缀，Spring 只加载第一份符合条件的 XML 文件。 当使用 classpath:前缀时，系统通过类加载路径搜索
     * bean.xml文件，如果找到文件名匹配的文件，系统立即停止搜索，装载该文件，即使有多份文件名匹配的文件，系统只装载第一份文件。 资源文件的搜索顺序则取决于类加载路径的顺序，排在前面的配置文件将优先被加载。
     * <p>
     * 另外，还有一种可以一次性装载多份配置文件的方式：指定配置文件时指定使用通配符。
     * <li>ApplicationContext ctx = newClassPathXmlApplicationContext("bean*.xml");
     * <p>
     * Spring 甚至允许将 classpath*:前缀和通配符结合使用，如下语句也是合法的：
     * <li>ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath*:bean*.xml");
     * <p>
     */
    static void demo1() {
        /**
         * 使用 classpath: 前缀，Spring 只加载第一份符合条件的 XML 文件。 当使用classpath:前缀时，系统通过类加载路径搜索
         * bean.xml文件，如果找到文件名匹配的文件，系统立即停止搜索，装载该文件，即使有多份文件名匹配的文件，系统只装载第一份文件。 资源文件的搜索顺序则取决于类加载路径的顺序，排在前面的配置文件将优先被加载。
         */
        ApplicationContext ctx = new FileSystemXmlApplicationContext("classpath:bean.xml");
        /** 使用 classpath* 装载多份配置文件。系统将搜索类加载路径，找出所有与文件名的文件，分别装载文件中的配置定义，最后合并成一个ApplicationContext */
        ctx = new FileSystemXmlApplicationContext("classpath*:bean.xml");
        /** 还有一种可以一次性装载多份配置文件的方式：指定配置文件时指定使用通配符 */
        ctx = new FileSystemXmlApplicationContext("classpath:bean*.xml");
        /** Spring 甚至允许将 classpath*:前缀和通配符结合使用 */
        ctx = new FileSystemXmlApplicationContext("classpath*:bean*.xml");
    }

    /**
     * file: 前缀的用法
     * <p>
     * 当 FileSystemXmlApplicationContext 作为 ResourceLoader 使用时，它会发生变化，FileSystemApplicationContext 会简单地让所有绑定的
     * FileSystemResource 实例把绝对路径都当成相对路径处理，而不管是否以斜杠开头，所以上面两行代码的效果是完全一样的。
     * <p>
     * 如果程序中需要访问绝对路径，则不要直接使用 FileSystemResource 或 FileSystemXmlApplicationContext 来指定绝对路径。建议强制使用 file: 前缀来区分相对路径和绝对路径，
     */
    static void demo2() {
        ApplicationContext ctx = new FileSystemXmlApplicationContext("bean.xml");
        ctx = new FileSystemXmlApplicationContext("/bean.xml");
        /** 访问相对路径下的 bean.xml，相对路径以当前工作路径为路径起点 */
        ctx = new FileSystemXmlApplicationContext("file:bean.xml");
        /** 访问绝对路径下 bean.xml，绝对路径以文件系统根路径为路径起点。 */
        ctx = new FileSystemXmlApplicationContext("file:/bean.xml");

    }

}
