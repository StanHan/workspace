package demo.spring.framework.core.io;

import org.springframework.core.io.Resource;

/**
 * Spring 把所有能记录信息的载体，如各种类型的文件、二进制流等都称为资源，对 Spring 开发者来说，最常用的资源就是 Spring 配置文件（通常是一份 XML 格式的文件）。
 * Spring 改进了 Java 资源访问的策略。Spring 为资源访问提供了一个 Resource 接口，该接口提供了更强的资源访问能力，Spring 框架本身大量使用了 Resource 接口来访问底层资源。
 * Resource 接口是 Spring 资源访问策略的抽象，它本身并不提供任何资源访问实现，具体的资源访问由该接口的实现类完成——每个实现类代表一种资源访问策略。
 * 
 *
 */
public class ResourceDemo {

    public static void main(String[] args) {
        Resource resource = null;

    }

}
