package demo.java.lang;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * <h2>ThreadLocal</h2>
 * <p>
 * 在JDK的API文档中ThreadLocal的定义第一句道出：This class provides thread-local variables。 好，这个类提供了线程本地的变量。
 * 
 * ThreadLocal的作用是提供线程内的局部变量，这种变量在线程的生命周期内起作用。
 * 作用：提供一个线程内公共变量（比如本次请求的用户信息），减少同一个线程内多个函数或者组件之间一些公共变量的传递的复杂度，或者为线程提供一个私有的变量副本，这样每一个线程都可以随意修改自己的变量副本，而不会对其他线程产生影响。
 * 
 * 对比synchronized和ThreadLocal首先要清楚，两者的使用目的不同，关键点就在是否需要共享变量。就是说，ThreadLocal根本不是同步。
 * 
 * ThreadLocal和Synchonized都用于解决多线程并发访问。 但是ThreadLocal与synchronized有本质的区别，synchronized是利用锁的机制，使变量或代码块在某一时该只能被一个线程访问。
 * 而ThreadLocal为每一个线程都提供了变量的副本，使得每个线程在某一时间访问到的并不是同一个对象，这样就隔离了多个线程对数据的数据共享。
 * 
 * Synchronized用于线程间的数据共享，而ThreadLocal则用于线程间的数据隔离。两者处于不同的问题域。
 * 
 * ThreadLocal的作用是提供线程内的局部变量，这种变量在线程的生命周期内起作用。
 * 作用：提供一个线程内公共变量（比如本次请求的用户信息），减少同一个线程内多个函数或者组件之间一些公共变量的传递的复杂度，或者为线程提供一个私有的变量副本，这样每一个线程都可以随意修改自己的变量副本，而不会对其他线程产生影响。
 * <h2>Thread与ThreadLocal有什么关系</h2>
 * <p>
 * 在Thread类中的一行：ThreadLocal.ThreadLocalMap threadLocals = null; 其中ThreadLocalMap的定义是在ThreadLocal类中，真正的引用却是在Thread类中。
 * ThreadLocalMap究竟是什么呢？ ThreadLocalMap is a customized hash map suitable only for maintaining thread local values。
 * 接下来的重点是ThreadLocalMap中用于存储数据的entry。这个Map的key是ThreadLocal变量，value为用户的值，并不是网上大多数的列子key是线程的名字或者标识
 * 
 * <h2>ThreadLocal究竟是如何工作的了</h2>
 * <p>
 * <li>1.Thread类中有一个成员变量叫做ThreadLocalMap，它是一个Map，他的Key是ThreadLocal类
 * <li>2.每个线程拥有自己的申明为ThreadLocal类型的变量,所以这个类的名字叫'ThreadLocal'：线程自己的（变量）
 * <li>3.此变量生命周期是由该线程决定的，开始于第一次初始（get或者set方法）
 * <li>4.由ThreadLocal的工作原理决定了：每个线程独自拥有一个变量，并非共享或者拷贝
 * 
 * 每个Thread返回各自的ThreadLocalMap，所以各个线程中的ThreadLocal均为独立的
 * 
 * <h2>关于内存泄漏：</h2>
 * <p>
 * ThreadLocalMap使用ThreadLocal的弱引用作为key，如果一个ThreadLocal没有外部强引用引用他，那么系统gc的时候，这个ThreadLocal势必会被回收，
 * 这样一来，ThreadLocalMap中就会出现key为null的Entry，就没有办法访问这些key为null的Entry的value，如果当前线程再迟迟不结束的话，这些key为null的Entry的value就会一直存在一条强引用链：
 * Thread Ref -> Thread -> ThreaLocalMap -> Entry -> value 永远无法回收，造成内存泄露。
 * 虽然ThreadLocalMap已经使用了weakReference，但是还是建议能够显示的使用remove方法。
 * <p>
 * ThreadLocalMap的getEntry函数的流程大概为：
 * <li>首先从ThreadLocal的直接索引位置(通过ThreadLocal.threadLocalHashCode & (table.length-1)运算得到)获取Entry e，如果e不为null并且key相同则返回e；
 * <li>如果e为null或者key不一致则向下一个位置查询，如果下一个位置的key和当前需要查询的key相等，则返回对应的Entry。
 * <li>否则，如果key值为null，则擦除该位置的Entry，并继续向下一个位置查询。
 * <p>
 * 在这个过程中遇到的key为null的Entry都会被擦除，那么Entry内的value也就没有强引用链，自然会被回收。仔细研究代码可以发现，set操作也有类似的思想，将key为null的这些Entry都删除，防止内存泄露。
 * 但是光这样还是不够的，上面的设计思路依赖一个前提条件：要调用ThreadLocalMap的getEntry函数或者set函数。
 * 这当然是不可能任何情况都成立的，所以很多情况下需要使用者手动调用ThreadLocal的remove函数，手动删除不再需要的ThreadLocal，防止内存泄露。 所以JDK建议将ThreadLocal变量定义成private
 * static的，这样的话ThreadLocal的生命周期就更长，由于一直存在ThreadLocal的强引用，所以ThreadLocal也就不会被回收，
 * 也就能保证任何时候都能根据ThreadLocal的弱引用访问到Entry的value值，然后remove它，防止内存泄露。
 */
public class ThreadLocalDemo {

    private static Random random = new Random();

    public static void main(String[] args) {
        demo3();

    }

    /**
     * 通过ThreadLocal变量来统计线程的耗时
     */
    static void demo3() {
        System.out.println("通过ThreadLocal变量来统计线程的耗时");
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        ThreadLocal<Long> startAt = new ThreadLocal<>();
        IntStream.range(0, 100).forEach(e -> {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    startAt.set(System.currentTimeMillis());
                    String threadName = Thread.currentThread().getName();
                    try {
                        Thread.sleep(e);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.printf("Thread name: %s 耗时%d %n", threadName,
                            System.currentTimeMillis() - startAt.get());
                    startAt.remove();
                }
            };
            executorService.submit(runnable);
        });
        executorService.shutdown();
    }

    static void demo1() {

        ThreadLocalTester tester = new ThreadLocalTester();

        for (int i = 0; i < 9; i++) {
            Thread t = new Thread(tester);
            t.start();
        }
    }

    static void demo2() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        IntStream.range(0, 100).forEach(e -> {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    int threadId = UniqueThreadIdGenerator.getCurrentThreadId();
                    String threadName = Thread.currentThread().getName();
                    System.out.printf("Thread id: %d, name: %s %n", threadId, threadName);
                }
            };
            executorService.submit(runnable);
        });
        executorService.shutdown();
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

    private static final ThreadLocal<Integer> threadLocal_uniqueNum = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return uniqueId.getAndIncrement();
        }
    };

    public static int getCurrentThreadId() {
        return threadLocal_uniqueNum.get();
    }
}
