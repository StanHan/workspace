package demo.java.util.concurrent.locks;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * java.util.concurrent.lock 中的 Lock 框架是锁定的一个抽象，它允许把锁定的实现作为 Java 类，而不是作为语言的特性来实现。
 * <p>
 * Lock 和 synchronized 有一点明显的区别 —— lock 必须在 finally 块中释放。否则，如果受保护的代码将抛出异常，锁就有可能永远得不到释放！
 * <p>
 * <li>Lock类也可以实现线程同步，而Lock获得锁需要执行lock方法，释放锁需要执行unLock方法
 * <li>Lock类可以创建Condition对象，Condition对象用来是线程等待和唤醒线程，需要注意的是Condition对象的唤醒的是用同一个Condition执行await方法的线程，所以也就可以实现唤醒指定类的线程
 * <li>Lock类分公平锁和不公平锁，公平锁是按照加锁顺序来的，非公平锁是不按顺序的，也就是说先执行lock方法的锁不一定先获得锁
 * <li>Lock类有读锁和写锁，读读共享，写写互斥，读写互斥
 * 
 */
public class LockDemo {

    static void reentrantLockDemo() {
        /** 可重入锁 */
        Lock lock = new ReentrantLock();
        /** 读写锁 */
        ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        Condition condition = lock.newCondition();

    }

}
