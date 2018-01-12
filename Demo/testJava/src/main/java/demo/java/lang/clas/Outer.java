package demo.java.lang.clas;

/**
 * 成员内部类，就是作为外部类的成员，可以直接使用外部类的所有成员和方法，即使是private的。同时外部类要访问内部类的所有成员变量/方法，则需要通过内部类的对象来获取。
 * 要注意的是，成员内部类不能含有static的变量和方法。因为成员内部类需要先创建了外部类，才能创建它自己的，
 *
 */
public class Outer {
    public static void main(String[] args) {
        Outer outer = new Outer();
        Outer.Inner inner = outer.new Inner();
        inner.print("Outer.new");

        inner = outer.getInner();
        inner.print("Outer.get");
        
        System.out.println(inner.a);
        System.out.println(Inner.b);
    }

    private String c = "C";
    
    // 个人推荐使用getxxx()来获取成员内部类，尤其是该内部类的构造函数无参数时
    public Inner getInner() {
        return new Inner();
    }

    public class Inner {
        private String a = "A";
        
        private static final String b = "B";
        
        public void print(String str) {
            System.out.println(str);
        }
        
        // public static void sayHello(String str) {
        // System.out.println("Hello "+str);
        // }
    }
}
