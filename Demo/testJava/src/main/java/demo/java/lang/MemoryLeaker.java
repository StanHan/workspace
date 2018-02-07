package demo.java.lang;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <h1>内存泄漏</h1>
 * 
 * 什么是内存泄漏？ 当某些对象不再被应用程序所使用,但是由于仍然被引用而导致垃圾收集器不能释放(Remove,移除)
 * 
 * 引起内存泄漏的原因:长生命周期的对象持有短生命周期对象的引用就很可能发生内存泄露（老年代引用新生代）。
 * <li>当集合里面的对象属性被修改后，由于hashcode改变当再调用remove（）方法时不起作用
 * <li>静态集合类引起内存泄漏
 * <li>监听器：在开发过程中通常会调用一个控件的诸如addXXXListener()等方法来增加监听器，但往往在释放对象时没有删除监听器，从而增加了内存泄漏的机会
 * <li>各种连接：如数据库连接（dataSource.getConnection()）、网络连接（socket）和IO连接，除非显示的调用了close()方法将其连接关闭，否则是不会自动被GC回收的
 * <li>单例模式：不正确的使用单例模式是引起内存泄漏的一个常见问题，单例对象在被初始化后将在JVM的整个生命周期中存在（以静态变量的方式），如果单例对象持有外部对象的引用，那么这个外部对象将不能被JVM正常回收，导致内存泄漏
 * 
 * <h2>如何预防内存泄漏</h2>
 * <li>当心集合类，比如HashMap、ArrayList等，因为容器是最容易发生内存泄漏的地方。当集合对象被声明为static时，他们的生命周期一般和整个应用程序一样长
 * <li>注意事件监听和回调。当注册的监听器不再使用以后，如果没有被注销，那么很可能会发生内存泄漏
 */
public class MemoryLeaker {

    public static void main(String[] args) {
        test2();

    }

    /**
     * 当集合里面的对象属性被修改后，由于hashcode改变当再调用remove（）方法时不起作用
     */
    static void test() {
        Set<String> set = new HashSet<String>();
        String str1 = new String("aaa");
        String str2 = new String("bbb");
        String str3 = new String("ccc");
        set.add(str1);
        set.add(str2);
        set.add(str3);
        System.out.println("Before: " + set.size());
        str3 = "ddd";
        set.remove(str3);
        set.add(str3);
        System.out.println("After: " + set.size());
        for (String str : set) {
            System.out.println(str);
        }
    }

    static Vector vector = new Vector(10);

    /**
     * 静态集合类引起内存泄漏
     */
    public static void test2() {
        for (int i = 1; i < 100; i++) {
            Object o = new Object();
            vector.add(o);
            o = null;
            System.out.println(vector.size());
        }
    }

}

class MapLeaker {
    public ExecutorService exec = Executors.newFixedThreadPool(5);
    public Map<Task, TaskStatus> taskStatus = Collections.synchronizedMap(new HashMap<Task, TaskStatus>());
    private Random random = new Random();

    private enum TaskStatus {
        NOT_STARTED, STARTED, FINISHED
    };

    private class Task implements Runnable {
        private int[] numbers = new int[random.nextInt(200)];

        public void run() {
            int[] temp = new int[random.nextInt(10000)];
            taskStatus.put(this, TaskStatus.STARTED);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            taskStatus.put(this, TaskStatus.FINISHED);
        }
    }

    public Task newTask() {
        Task t = new Task();
        taskStatus.put(t, TaskStatus.NOT_STARTED);
        exec.execute(t);
        return t;
    }
}
