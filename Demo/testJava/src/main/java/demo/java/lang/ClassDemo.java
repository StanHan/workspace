package demo.java.lang;

import java.util.ArrayList;
import java.util.List;

public class ClassDemo {

    public static void main(String[] args) {
        testPrintFilePath();
    }

    static void testPrintFilePath() {
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
