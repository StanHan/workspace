package demo.java.lang;

import java.util.ArrayList;
import java.util.List;

import demo.vo.Person;
import demo.vo.Student;

public class ClassDemo {
    public static void main(String[] args) {
        demoGetResource();
    }

    /**
     * 
     */
    static void printFilePath() {

        System.out.println(ClassDemo.class.getClassLoader().getResource("").getPath());
    }

    /**
     * 获取资源的路径
     * <p>
     * Class.getResource(String path): path 不以’/'开头时，默认是从此类所在的包下取资源；path 以’/'开头时，则是从ClassPath根下获取；
     * <p>
     * Class.getResource和Class.getResourceAsStream在使用时，路径选择上是一样的。
     * <p>
     * Class.getClassLoader().getResource(String path):path不能以’/'开头时；path是从ClassPath根下获取；
     * <p>
     * Class.getClassLoader().getResource和Class.getClassLoader().getResourceAsStream在使用时，路径选择上也是一样的。
     */
    public static void demoGetResource() {
        System.out.println("path 以’/'开头时，则是从ClassPath根下获取:" + ClassDemo.class.getResource("/").getPath());
        System.out.println("path 不以’/'开头时，默认是从此类所在的包下取资源:" + ClassDemo.class.getResource("").getPath());

        System.out.println("path不能以’/'开头时:" + ClassDemo.class.getClassLoader().getResource("/"));
        System.out.println("path是从ClassPath根下获取:" + ClassDemo.class.getClassLoader().getResource("").getPath());
    }

    /**
     * 正确的强转
     */
    static void testRightCase() {
        Person person = new Student();
        person.setLastName("HAN");
        Student student = (Student) person;
        System.out.println(student.getLastName());
    }

    /**
     * 错误的强转
     */
    static void testWrongCase() {
        Person person = new Person();
        person.setLastName("HAN");
        Student student = (Student) person;
        System.out.println(student.getLastName());
    }

    /**
     * 打印数组所属类型
     * 
     * @param ids
     */
    static void testComponentType(int... ids) {
        Class<?> cls = ids.getClass().getComponentType();
        System.out.println(cls);
        cls = String.class.getComponentType();
        System.out.println(cls);
        List<String> list = new ArrayList<>();
        cls = list.getClass().getComponentType();
        System.out.println(cls);
        String[] arrayString = {};
        cls = arrayString.getClass().getComponentType();
        System.out.println(cls);
    }
}
