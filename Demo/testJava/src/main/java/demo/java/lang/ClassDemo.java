package demo.java.lang;

import java.util.ArrayList;
import java.util.List;

import demo.vo.Person;
import demo.vo.Student;

public class ClassDemo {
    public static void main(String[] args) {
        printFilePath();
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
     * 获取相对路径
     */
    static void printFilePath() {
        System.out.println(ClassDemo.class.getResource("/").getPath());
        System.out.println(ClassDemo.class.getResource("").getPath());
        System.out.println(ClassDemo.class.getClassLoader().getResource("").getPath());
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
