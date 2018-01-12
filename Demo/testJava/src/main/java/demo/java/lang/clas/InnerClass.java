package demo.java.lang.clas;

/**
 * 在一个类中创建另外一个类，叫做成员内部类。内部类可以是静态static的，也可用public，default，protected和private修饰。
 * 
 * 注意：内部类是一个编译时的概念，一旦编译成功，就会成为完全不同的两类。对于一个名为outer的外部类和其内部定义的名为inner的内部类。
 * 编译完成后出现outer.class和outer$inner.class两类。所以内部类的成员变量/方法名可以和外部类的相同。
 * 
 * <li>成员内部类，就是作为外部类的成员，可以直接使用外部类的所有成员和方法，即使是private的。同时外部类要访问内部类的所有成员变量/方法，则需要通过内部类的对象来获取。
 * 要注意的是，成员内部类不能含有static的变量和方法。因为成员内部类需要先创建了外部类，才能创建它自己的，
 * 
 */
public class InnerClass {

    public static void main(String[] args) {
        new InnerClass().print();
        InnerClass.Main.main();
        new InnerClass.Main();
    }

    static class Main {
        static void main() {
            // 将主方法写到静态内部类中，从而不必为每个源文件都这种一个类似的主方法
            new InnerClass().print();
        }
    }

    public void print() {
        System.out.println("main in static inner class");
    }

    public class Inner {
        public void print(String str) {
            System.out.println(str);
        }
    }
}
