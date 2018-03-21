package demo.java;

/**
 * <h1>JVM</h1> JVM内存永久区已经被metaspace替换（JEP 122）。JVM参数 -XX:PermSize 和 –XX:MaxPermSize被XX:MetaSpaceSize 和
 * -XX:MaxMetaspaceSize代替。
 *
 */
public class JVMDemo {
    public static void main(String[] args) {
        System.out.println("A" == null);
    }
}
