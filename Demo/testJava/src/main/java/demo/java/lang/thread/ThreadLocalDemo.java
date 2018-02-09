package demo.java.lang.thread;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 在JDK的API文档中ThreadLocal的定义第一句道出：This class provides thread-local variables. 好，这个类提供了线程本地的变量。
 * 对比synchronized和ThreadLocal首先要清楚，两者的使用目的不同，关键点就在是否需要共享变量。就是说，ThreadLocal根本不是同步。
 * 
 * ThreadLocal和Synchonized都用于解决多线程并发访问。 但是ThreadLocal与synchronized有本质的区别，synchronized是利用锁的机制，使变量或代码块在某一时该只能被一个线程访问。
 * 而ThreadLocal为每一个线程都提供了变量的副本，使得每个线程在某一时间访问到的并不是同一个对象，这样就隔离了多个线程对数据的数据共享。
 * 
 * Synchronized用于线程间的数据共享，而ThreadLocal则用于线程间的数据隔离。两者处于不同的问题域。
 * 
 * ThreadLocal的作用是提供线程内的局部变量，这种变量在线程的生命周期内起作用。
 * 作用：提供一个线程内公共变量（比如本次请求的用户信息），减少同一个线程内多个函数或者组件之间一些公共变量的传递的复杂度，或者为线程提供一个私有的变量副本，这样每一个线程都可以随意修改自己的变量副本，而不会对其他线程产生影响。
 * 
 */
public class ThreadLocalDemo {

    public static void main(String[] args) {
        demo1();

    }

    static void demo1() {

        ThreadLocalTester tester = new ThreadLocalTester();

        for (int i = 0; i < 9; i++) {
            Thread t = new Thread(tester);
            t.start();
        }
    }

}

class ThreadLocalTester implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " " + UniqueThreadIdGenerator.getCurrentThreadId());
        }
    }

}

/**
 * 唯一线程ID生成器
 *
 */
class UniqueThreadIdGenerator {

    private static final AtomicInteger uniqueId = new AtomicInteger(0);

    private static final ThreadLocal<Integer> uniqueNum = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return uniqueId.getAndIncrement();
        }
    };

    public static int getCurrentThreadId() {
        return uniqueNum.get();
    }
}
