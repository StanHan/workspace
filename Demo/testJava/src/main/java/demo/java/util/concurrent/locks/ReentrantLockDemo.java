package demo.java.util.concurrent.locks;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <h1>ReentrantLock 可重入锁</h1>
 * <p>
 * ReentrantLock 类实现了 Lock ，它拥有与 synchronized 相同的并发性和内存语义，但是添加了类似轮询锁、定时锁等候和可中断锁等候的一些特性。
 * 此外，它还提供了在激烈争用情况下更佳的性能。（换句话说，当许多线程都想访问共享资源时，JVM 可以花更少的时候来调度线程，把更多时间用在执行线程上。
 * 
 * <h2>reentrant 锁意味着什么呢？</h2>
 * <p>
 * 简单来说，它有一个与锁相关的获取计数器，如果拥有锁的某个线程再次得到锁，那么获取计数器就加1，然后锁需要被释放两次才能获得真正释放。 这模仿了 synchronized
 * 的语义:如果线程进入由线程已经拥有的监控器保护的synchronized块，就允许线程继续进行， 当线程退出第二个（或者后续） synchronized 块的时候，不释放锁，只有线程退出它进入的监控器保护的第一个
 * synchronized 块时，才释放锁。
 * <p>
 * ReentrantLock是具有和synchronized类似功能的性能功能加强版同步锁。 如：ArrayBlockingQueue。
 * <li>1. 一个ReentrantLock可以有多个Condition实例。
 * <li>2. ReentrantLock提供了lockInterruptibly()方法可以优先考虑响应中断，而不是像synchronized那样不响应interrupt()操作。
 * <p>
 * 响应中断是什么意思：比如A、B两线程去竞争锁，A得到了锁，B等待，但是A有很多事情要处理，所以一直不返回。B可能就会等不及了，想中断自己，不再等待这个锁了，转而处理其他事情。
 * 在这种情况下，synchronized的做法是，B线程中断自己（或者别的线程中断它），我不去响应，继续让B线程等待，你再怎么中断，我全当耳边风。
 * 而lockInterruptibly()的做法是，B线程中断自己（或者别的线程中断它），ReentrantLock响应这个中断，不再让B等待这个锁的到来。
 * 有了这个机制，使用ReentrantLock时就不会像synchronized那样产生死锁了。
 * 由于ReentrantLock在提供了多样的同步功能（除了可响应中断，还能设置时间限制），因此在同步比较激烈的情况下，性能比synchronized大大提高。
 * <p>
 * 不过，在同步竞争不激烈的情况下，synchronized还是非常合适的（因为JVM会进行优化，具体不清楚怎么优化的）。
 * 因此不能说ReentrantLock一定更好，只是两者适合情况不同而已，在同步竞争不激烈时用synchronized，激烈时用ReentrantLock。 换句话说，ReentrantLock的可伸缩性可并发性要更好一些。 除非您对
 * ReentrantLock的某个高级特性有明确的需要，或者有明确的证据（而不是仅仅是怀疑）表明在特定情况下，同步已经成为可伸缩性的瓶颈，否则还是应当继续使用 synchronized。
 * <p>
 * 使用ReentrantLock时，切记要在finally中释放锁，这是与synchronized使用方式很大的一个不同。对于synchronized，JVM会自动释放锁，而ReentrantLock需要你自己来处理。
 * <p>
 * 
 */
public class ReentrantLockDemo {
    public static void main(String[] args) {
        final ReentrantLock lock = new ReentrantLock(false);

        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<String>(10, true);
    }

    /**
     * 用 ReentrantLock 保护代码块。 效果和synchronized一样，都可以同步执行，lock方法获得锁，unlock方法释放锁
     * 
     */
    void demo1() throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        lock.lock();
        condition.await();
        try {
            // update object state
        } finally {
            lock.unlock();
        }
    }

    /**
     * ReentrantLock 构造器的一个参数是 boolean 值，它允许您选择想要一个 公平（fair）锁，还是一个 不公平（unfair）锁。
     * <li>公平锁使线程按照请求锁的顺序依次获得锁；而不公平锁则允许直接获取锁，在这种情况下，线程有时可以比先请求锁的其他线程先得到锁。
     * <li>公平保证了锁是非常健壮的锁，有很大的性能成本。要确保公平锁需要的记帐（bookkeeping）和同步，就意味着被争夺的公平锁要比不公平锁的吞吐率更低。 作为默认设置，应当把公平设置为 false
     * ，除非公平对您的算法至关重要，需要严格按照线程排队的顺序对其进行服务。
     */
    void demo2() {
        ReentrantLock fairLock = new ReentrantLock(true);// 公平锁
        ReentrantLock unfairLock = new ReentrantLock(false);// 非公平锁
    }

    /**
     * <h2>ReentrantLock类的方法</h2>
     * 
     * <li>getHoldCount() 查询当前线程保持此锁的次数，也就是执行此线程执行lock方法的次数
     * <li>getQueueLength()返回正等待获取此锁的线程估计数，比如启动10个线程，1个线程获得锁，此时返回的是9
     * <li>getWaitQueueLength（Condition
     * condition）返回等待与此锁相关的给定条件的线程估计数。比如10个线程，用同一个condition对象，并且此时这10个线程都执行了condition对象的await方法，那么此时执行此方法返回10
     * <li>hasWaiters(Condition condition)查询是否有线程等待与此锁有关的给定条件(condition)，对于指定contidion对象，有多少线程执行了condition.await方法
     * <li>hasQueuedThread(Thread thread)查询给定线程是否等待获取此锁
     * <li>hasQueuedThreads()是否有线程等待此锁
     * <li>isFair()该锁是否公平锁
     * <li>isHeldByCurrentThread() 当前线程是否保持锁锁定，线程的执行lock方法的前后分别是false和true
     * <li>isLock()此锁是否有任意线程占用
     * <li>lockInterruptibly()如果当前线程未被中断，获取锁
     * <li>tryLock（）尝试获得锁，仅在调用时锁未被线程占用，获得锁
     * <li>tryLock(long timeout TimeUnit unit)如果锁在给定等待时间内没有被另一个线程保持，则获取该锁
     * 
     * <h2>tryLock和lock和lockInterruptibly的区别</h2>
     * <li>tryLock能获得锁就返回true，不能就立即返回false，tryLock(long timeout,TimeUnit unit)，可以增加时间限制，如果超过该时间段还没获得锁，返回false
     * <li>lock能获得锁就返回true，不能的话一直等待获得锁
     * <li>lock和lockInterruptibly，如果两个线程分别执行这两个方法，但此时中断这两个线程，前者不会抛出异常，而后者会抛出异常
     */
    void demo3() {
    }

}

/**
 * 
 * 类 Object 包含某些特殊的方法，用来在线程的 wait() 、 notify() 和 notifyAll() 之间进行通信。这些是高级的并发性特性。
 * 
 * 通知与锁定之间有一个交互————为了在对象上 wait 或 notify ，您必须持有该对象的锁。 就像 Lock 是同步的概括一样， Lock 框架包含了对 wait 和 notify 的概括，这个概括叫作条件（Condition）
 * 。 Lock对象则充当绑定到这个锁的条件变量的工厂对象，与标准的 wait 和 notify 方法不同，对于指定的 Lock ，可以有不止一个条件变量与它关联。这样就简化了许多并发算法的开发。
 * 
 * <li>通过创建Condition对象来使线程wait，必须先执行lock.lock方法获得锁
 * <li>condition对象的signal方法可以唤醒wait线程
 * 
 * <h2>Condition类和Object类</h2>
 * <li>Condition类的awiat方法和Object类的wait方法等效
 * <li>Condition类的signal方法和Object类的notify方法等效
 * <li>Condition类的signalAll方法和Object类的notifyAll方法等效
 * 
 * 
 * 
 * <h2>创建多个condition对象</h2>
 * <li>一个condition对象的signal（signalAll）方法和该对象的await方法是一一对应的，也就是一个condition对象的signal（signalAll）方法不能唤醒其他condition对象的await方法
 * <li>ReentrantLock类可以唤醒指定条件的线程，而object的唤醒是随机的
 * 
 */
class BoundedBuffer {

    final ReentrantLock lock = new ReentrantLock();
    /** 为该ReentrantLock设置了两个Condition ,这里notEmpty和notFull作为lock的两个条件是可以分别负责管理想要加入元素的线程和想要取出元素的线程。 */
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
