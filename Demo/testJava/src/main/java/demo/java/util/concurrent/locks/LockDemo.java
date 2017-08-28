package demo.java.util.concurrent.locks;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * java.util.concurrent.lock 中的 Lock 框架是锁定的一个抽象，它允许把锁定的实现作为 Java 类，而不是作为语言的特性来实现。 ReentrantLock 类实现了 Lock ，它拥有与
 * synchronized 相同的并发性和内存语义，但是添加了类似轮询锁、定时锁等候和可中断锁等候的一些特性。 此外，它还提供了在激烈争用情况下更佳的性能。（换句话说，当许多线程都想访问共享资源时，JVM
 * 可以花更少的时候来调度线程，把更多时间用在执行线程上。）
 * 
 * reentrant 锁意味着什么呢？简单来说，它有一个与锁相关的获取计数器，如果拥有锁的某个线程再次得到锁，那么获取计数器就加1，然后锁需要被释放两次才能获得真正释放。 这模仿了 synchronized
 * 的语义；如果线程进入由线程已经拥有的监控器保护的 synchronized 块，就允许线程继续进行， 当线程退出第二个（或者后续） synchronized 块的时候，不释放锁，只有线程退出它进入的监控器保护的第一个
 * synchronized 块时，才释放锁。
 * 
 * Lock 和 synchronized 有一点明显的区别 —— lock 必须在 finally 块中释放。否则，如果受保护的代码将抛出异常，锁就有可能永远得不到释放！
 * 
 * ReentrantLock是具有和synchronized类似功能的性能功能加强版同步锁。 如：ArrayBlockingQueue。
 * 
 * <li>1. 一个ReentrantLock可以有多个Condition实例。
 * <li>2. ReentrantLock提供了lockInterruptibly()方法可以优先考虑响应中断，而不是像synchronized那样不响应interrupt()操作。
 * 响应中断是什么意思：比如A、B两线程去竞争锁，A得到了锁，B等待，但是A有很多事情要处理，所以一直不返回。B可能就会等不及了，想中断自己，不再等待这个锁了，转而处理其他事情。
 * 在这种情况下，synchronized的做法是，B线程中断自己（或者别的线程中断它），我不去响应，继续让B线程等待，你再怎么中断，我全当耳边风。
 * 而lockInterruptibly()的做法是，B线程中断自己（或者别的线程中断它），ReentrantLock响应这个中断，不再让B等待这个锁的到来。
 * 有了这个机制，使用ReentrantLock时就不会像synchronized那样产生死锁了。
 * 由于ReentrantLock在提供了多样的同步功能（除了可响应中断，还能设置时间限制），因此在同步比较激烈的情况下，性能比synchronized大大提高。
 * 
 * 不过，在同步竞争不激烈的情况下，synchronized还是非常合适的（因为JVM会进行优化，具体不清楚怎么优化的）。
 * 因此不能说ReentrantLock一定更好，只是两者适合情况不同而已，在同步竞争不激烈时用synchronized，激烈时用ReentrantLock。
 * 
 * 换句话说，ReentrantLock的可伸缩性可并发性要更好一些。 除非您对 ReentrantLock的某个高级特性有明确的需要，或者有明确的证据（而不是仅仅是怀疑）表明在特定情况下，同步已经成为可伸缩性的瓶颈，否则还是应当继续使用
 * synchronized。
 * 
 * 使用ReentrantLock时，切记要在finally中释放锁，这是与synchronized使用方式很大的一个不同。对于synchronized，JVM会自动释放锁，而ReentrantLock需要你自己来处理。
 * 
 * Condition 的方法与 wait 、 notify 和 notifyAll 方法类似，分别命名为 await 、 signal 和 signalAll ，因为它们不能覆盖 Object 上的对应方法。
 */
public class LockDemo {

    /**
     * ReentrantLock 构造器的一个参数是 boolean 值，它允许您选择想要一个 公平（fair）锁，还是一个 不公平（unfair）锁。
     * 公平锁使线程按照请求锁的顺序依次获得锁；而不公平锁则允许直接获取锁，在这种情况下，线程有时可以比先请求锁的其他线程先得到锁。
     * 公平保证了锁是非常健壮的锁，有很大的性能成本。要确保公平锁需要的记帐（bookkeeping）和同步，就意味着被争夺的公平锁要比不公平锁的吞吐率更低。 作为默认设置，应当把公平设置为 false
     * ，除非公平对您的算法至关重要，需要严格按照线程排队的顺序对其进行服务。
     */
    static void reentrantLockDemo() {
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10, true);

        final Lock lock = new ReentrantLock(false);
    }

}

class BoundedBuffer {
    final Lock lock = new ReentrantLock();
    // 为该ReentrantLock设置了两个Condition ,这里notEmpty和notFull作为lock的两个条件是可以分别负责管理想要加入元素的线程和想要取出元素的线程。

    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    final Object[] items = new Object[100];
    int putptr, takeptr, count;

    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            while (count == items.length) {
                notFull.await();
            }
            items[putptr] = x;
            if (++putptr == items.length) {
                putptr = 0;
            }
            ++count;
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Object take() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                // 这里针对notEmpty这个condition，如果队列为空则线程等待这个条件
                notEmpty.await();
            }

            Object x = items[takeptr];
            if (++takeptr == items.length) {
                takeptr = 0;
            }
            --count;
            notFull.signal();// 这里针对notFull这个condition，唤醒因该条件而等待的线程
            return x;
        } finally {
            lock.unlock();
        }
    }
}
