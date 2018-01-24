package demo.spring.framework.core.io;

import java.net.MalformedURLException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.web.context.support.ServletContextResource;

/**
 * Spring 把所有能记录信息的载体，如各种类型的文件、二进制流等都称为资源，对 Spring 开发者来说，最常用的资源就是 Spring 配置文件（通常是一份 XML 格式的文件）。 Spring 改进了
 * Java资源访问的策略。Spring 为资源访问提供了一个 Resource 接口，该接口提供了更强的资源访问能力，Spring 框架本身大量使用了 Resource 接口来访问底层资源。 Resource 接口是
 * Spring资源访问策略的抽象，它本身并不提供任何资源访问实现，具体的资源访问由该接口的实现类完成——每个实现类代表一种资源访问策略。 Spring 为 Resource 接口提供了如下实现类：
 * <li>UrlResource：访问网络资源的实现类。
 * <li>ClassPathResource：访问类加载路径里资源的实现类。
 * <li>FileSystemResource：访问文件系统里资源的实现类。
 * <li>ServletContextResource：访问相对于 ServletContext 路径里的资源的实现类：
 * <li>InputStreamResource：访问输入流资源的实现类。
 * <li>ByteArrayResource：访问字节数组资源的实现类。
 *
 * <h2>策略模式的优势</h2>
 * <p>
 * 当 Spring 应用需要进行资源访问时，实际上并不需要直接使用 Resource 实现类，而是调用 ApplicationContext 实例的 getResource() 方法来获得资源，
 * ApplicationContext将会负责选择 Resource 的实现类，也就是确定具体的资源访问策略，从而将应用程序和具体的资源访问策略分离开来，这就体现了策略模式的优势。
 * <p>
 * 
 */
public class ResourceDemo {

    public static void main(String[] args) throws Exception {
        Resource resource = null;
        ResourceLoader resourceLoader = null;
        ResourceLoaderAware resourceLoaderAware = null;
        demoUrlResource();
        demoClassPathResource();
        demoFileSystemResource();

        demoApplicationContext();
    }

    /**
     * 使用 ApplicationContext 来访问资源。 Spring 提供两个标志性接口：
     * <li>ResourceLoader：该接口实现类的实例可以获得一个 Resource 实例。
     * <li>ResourceLoaderAware：该接口实现类的实例将获得一个 ResourceLoader 的引用。
     * <p>
     * ApplicationContext 的实现类都实现 ResourceLoader 接口，因此 ApplicationContext 可用于直接获取 Resource 实例。 此处 Spring 框架的
     * ApplicationContext 不仅是 Spring 容器，而且它还是资源访问策略的“决策者”，也就是策略模式中 Context 对象，它将为客户端代码“智能”地选择策略实现。 当 ApplicationContext
     * 实例获取Resource 实例时，系统将默认采用与 ApplicationContext 相同的资源访问策略。也就是说： 如果 ApplicationContext 是
     * FileSystemXmlApplicationContext，res 就是 FileSystemResource 实例； 如果 ApplicationContext 是
     * ClassPathXmlApplicationContext，res 就是 ClassPathResource 实例； 如果 ApplicationContext 是 XmlWebApplicationContext，res
     * 是 ServletContextResource 实例。另一方面使用 ApplicationContext 来访问资源时，也可不理会 ApplicationContext 的实现类，强制使用指定的
     * ClassPathResource、FileSystemResource 等实现类，这可通过不同前缀来指定。
     * <p>
     * ResourceLoaderAware 接口则用于指定该接口的实现类必须持有一个 ResourceLoader 实例。 类似于 Spring 提供的 BeanFactoryAware、BeanNameAware
     * 接口，ResourceLoaderAware 接口也提供了一个 setResourceLoader() 方法，该方法将由 Spring 容器负责调用，Spring 容器会将一个 ResourceLoader
     * 对象作为该方法的参数传入。 当我们把将 ResourceLoaderAware 实例部署在 Spring 容器中后，Spring 容器会将自身当成 ResourceLoader 作为 setResourceLoader()
     * 方法的参数传入，由于 ApplicationContext 的实现类都实现了 ResourceLoader 接口，Spring 容器自身完全可作为 ResourceLoader 使用。
     * 
     */
    static void demoApplicationContext() {
        // 创建 ApplicationContext 实例
        System.out.println("程序中，并未指定采用哪一种 Resource 实现类，仅仅通过 ApplicactionContext 获得 Resource。");
        ApplicationContext ctx = new ClassPathXmlApplicationContext("db/mybatis/generatorConfig.xml");
        Resource res = ctx.getResource("logback.xml");
        // 获取该资源的简单信息
        System.out.println(res.getFilename());
        System.out.println(res.getDescription());
        System.out.println("从运行结果可以看出，Resource 采用了 ClassPathResource 实现类，");
        System.out.println("如果将 ApplicationContext 改为使用 FileSystemXmlApplicationContext 实现类");
        ctx = new FileSystemXmlApplicationContext(
                "D:\\Stan\\Demo\\testJava\\src\\main\\resources\\db\\mybatis\\generatorConfig.xml");
        res = ctx.getResource("logback.xml");
        // 获取该资源的简单信息
        System.out.println(res.getFilename());
        System.out.println(res.getDescription());
        System.out.println("从上面的执行结果可以看出，程序的 Resource 实现类发了改变，变为 FileSystemResource 实现类。");
    }

    static void demoServletContextResource() {
        ServletContextResource servletContextResource = new ServletContextResource(null, null);
    }

    static void demoInputStreamResource() {
        InputStreamResource isr = new InputStreamResource(null);
    }

    static void demoByteArrayResource() {
        ByteArrayResource bar = new ByteArrayResource(null);
    }

    /**
     * 访问网络资源通过 UrlResource 类实现，UrlResource 是 java.net.URL 类的包装，主要用于访问之前通过 URL 类访问的资源对象。
     * 
     * URL 资源通常应该提供标准的协议前缀。例如：file: 用于访问文件系统；http: 用于通过 HTTP 协议访问资源；ftp: 用于通过 FTP 协议访问资源等。
     */
    static void demoUrlResource() throws MalformedURLException {
        // 创建一个 Resource 对象，指定从文件系统里读取资源
        UrlResource ur = new UrlResource("file:d:/test.mp4");
        // 获取该资源的简单信息
        System.out.println(ur.getFilename());
        System.out.println(ur.getDescription());
    }

    /**
     * ClassPathResource 用来访问类加载路径下的资源，相对于其他的 Resource 实现类，其主要优势是方便访问类加载路径里的资源。
     * 
     * 尤其对于 Web 应用，ClassPathResource 可自动搜索位于 WEB-INF/classes 下的资源文件，无须使用绝对路径访问。
     * 
     * ClassPathResource 实例可使用 ClassPathResource 构造器显式地创建，但更多的时候它都是隐式创建的，当执行 Spring 的某个方法时，该方法接受一个代表资源路径的字符串参数，当 Spring
     * 识别该字符串参数中包含 classpath: 前缀后，系统将会自动创建 ClassPathResource 对象。
     */
    static void demoClassPathResource() {
        // 创建一个 Resource 对象，从类加载路径里读取资源
        ClassPathResource cr = new ClassPathResource("db/ds/dataSource.xml");
        // 获取该资源的简单信息
        System.out.println(cr.getFilename());
        System.out.println(cr.getDescription());
    }

    /**
     * 使用 FileSystemResource 来访问文件系统资源并没有太大的优势，因为 Java 提供的 File 类也可用于访问文件系统资源。使用 FileSystemResource
     * 也可消除底层资源访问的差异，程序通过统一的 Resource API 来进行资源访问。 资源字符串确定的资源，位于本地文件系统内 ，而且无须使用任何前缀。
     * <p>
     * FileSystemResource 实例可使用 FileSystemResource 构造器显式地创建。但更多的时候它都是隐式创建的，执行 Spring 的某个方法时，该方法接受一个代表资源路径的字符串参数，当 Spring
     * 识别该字符串参数中包含 file: 前缀后，系统将会自动创建 FileSystemResource 对象。
     */
    static void demoFileSystemResource() {
        // 默认从文件系统的当前路径加载 book.xml 资源
        FileSystemResource fr = new FileSystemResource("d:/test.mp4");
        // 获取该资源的简单信息
        System.out.println(fr.getFilename());
        System.out.println(fr.getDescription());
    }

}
