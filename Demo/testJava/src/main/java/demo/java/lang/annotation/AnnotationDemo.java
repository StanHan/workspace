package demo.java.lang.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;

/**
 * J2SE5.0版本在 java.lang.annotation提供了四种元注解，专门注解其他的注解：
 * <p>
 * .@Documented –在默认的情况下javadoc命令不会将我们的annotation生成在doc中去的，所以使用该标记就是告诉jdk让它也将annotation生成到doc中去
 * <p>
 * .@Retention –定义该注解的生命周期。
 * <ul>
 * <li>RetentionPolicy.SOURCE –在编译阶段丢弃。这些注解在编译结束之后就不再有任何意义，所以它们不会写入字节码。@Deprecated,@Override,@SuppressWarnings都属于这类注解。
 * <li>RetentionPolicy.CLASS – 在类加载的时候丢弃。在字节码文件的处理中有用。注解默认使用这种方式。
 * <li>RetentionPolicy.RUNTIME– 始终不会丢弃，运行期也保留该注解，因此可以使用反射机制读取该注解的信息。我们自定义的注解通常使用这种方式。
 * </ul>
 * .@Target –表示该注解用于什么地方。如果不明确指出，该注解可以放在任何地方。
 * <ul>
 * <li>ElementType.TYPE：用于描述类、接口或enum声明
 * <li>ElementType.FIELD：用于描述实例变量
 * <li>ElementType.METHOD：用于描述方法。
 * <li>ElementType.PARAMETER：用于描述参数。
 * <li>ElementType.CONSTRUCTOR：用于描述构造器
 * <li>ElementType.LOCAL_VARIABLE：用于描述局部变量
 * <li>ElementType.ANNOTATION_TYPE： 另一个注释
 * <li>ElementType.PACKAGE： 用于记录java文件的package信息
 * </ul>
 * .@Inherited – 定义该注释和子类的关系。比如有一个类A，在他上面有一个标记annotation，那么A的子类B是否不用再次标记annotation就可以继承得到呢，答案是肯定的
 * <p>
 * 
 * 注意:
 * <p>
 * <li>Annotation是不可以继承其他接口的，这一点是需要进行注意，这也是annotation的一个规定吧。
 * <li>Annotation也是存在包结构的，在使用的时候直接进行导入即可。
 * <li>Annotation类型的类型只支持原声数据类型，枚举类型和Class类型的一维数组，其他的类型或者用户自定义的类都是不可以作为annotation的类型，
 */

@Author("Stan")
public class AnnotationDemo {

    public static void main(String[] args) throws Exception {
        testAnnotation();

    }

    static void testAnnotation() throws Exception {
        Class<AnnotationDemo> _class = AnnotationDemo.class;
        // 找到类上面的注解
        boolean isExist = _class.isAnnotationPresent(Author.class);
        Author a = _class.getAnnotation(Author.class);
        System.err.println(a.value());
        if (isExist) {
            // 拿到注解实例，解析类上面的注解
            Author author = _class.getAnnotation(Author.class);
            System.out.println(author.value());
        }

        Method[] mothods = _class.getDeclaredMethods();
        for (Method method : mothods) {
            Todo todoAnnotation = method.getAnnotation(Todo.class);
            if (todoAnnotation != null) {
                System.out.println(" Method Name : " + method.getName());
                System.out.println(" Author : " + todoAnnotation.author());
                System.out.println(" Priority : " + todoAnnotation.priority());
                System.out.println(" Status : " + todoAnnotation.status());
            }
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                System.out.println(annotation.toString());
            }
        }
    }

    @Author("Stan")
    @Todo(priority = Todo.Priority.MEDIUM, author = "Stan", status = Todo.Status.STARTED)
    static void test() {

    }

}

/**
 * Annotations只支持基本类型、String及枚举类型。注释中所有的属性被定义成方法，并允许提供默认值。
 *
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface Todo {
    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    public enum Status {
        STARTED, NOT_STARTED
    }

    String author() default "Yash";

    Priority priority() default Priority.LOW;

    Status status() default Status.NOT_STARTED;
}

/**
 * 如果注解中只有一个属性，可以直接命名为“value”，使用时无需再标明属性名。
 *
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@interface Author {
    String value();
}

/**
 * Java 8 新增加了两个注解的程序元素类型ElementType.TYPE_USE 和ElementType.TYPE_PARAMETER ，这两个新类型描述了可以使用注解的新场合。
 *
 */
class Annotations {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
    public @interface NonEmpty {
    }

    public static class Holder<@NonEmpty T> extends @NonEmpty Object {
        public void method() throws @NonEmpty Exception {
        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        final Holder<String> holder = new @NonEmpty Holder<String>();
        @NonEmpty
        Collection<@NonEmpty String> strings = new ArrayList<>();
    }
}
