package demo.java.lang;

import java.util.ArrayList;
import java.util.List;

import demo.vo.Person;
import demo.vo.Student;

public class ClassDemo {
    public static final byte ID_CARD_BACK_FLAG_2 = 2;

    public static void main(String[] args) {
        Person person = new Person();
        person.setLastName("HAN");
        Student student = (Student) person;
        System.out.println(student.getLastName());
    }

    static void printFilePath() {
        // 下面三种方法都可以获取相对路径
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
        Class cls = ids.getClass().getComponentType();
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
