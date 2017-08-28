package demo.java.util.concurrent.locks;

/**
 * 
 * 把代码块声明为 synchronized，有两个重要后果，通常是指该代码具有 原子性（atomicity）和 可见性（visibility）。
 * 原子性意味着一个线程一次只能执行由一个指定监控对象（lock）保护的代码，从而防止多个线程在更新共享状态时相互冲突。 可见性则更为微妙；它要对付内存缓存和编译器优化的各种反常行为。
 * 一般来说，线程以某种不必让其他线程立即可以看到的方式（不管这些线程在寄存器中、在处理器特定的缓存中，还是通过指令重排或者其他编译器优化），不受缓存变量值的约束，
 * 但是如果开发人员使用了同步，那么运行库将确保某一线程对变量所做的更新先于对现有 synchronized 块所进行的更新，当进入由同一监控器（lock）保护的另一个 synchronized 块时，
 * 将立刻可以看到这些对变量所做的更新。类似的规则也存在于 volatile 变量上。
 * 
 *  Object 包含某些特殊的方法， wait() 、 notify() 和 notifyAll() ,用来在线程的之间进行通信。
 * 
 * synchronized是针对对象的隐式锁使用的，注意是对象！
 * 
 * synchronized关键字的作用域有二种：
 * <li>某个对象实例内，synchronized aMethod(){}可以防止多个线程同时访问这个对象的synchronized方法
 * （如果一个对象有多个synchronized方法，只要一个线程访问了其中的一个synchronized方法，其它线程不能同时访问这个对象中任何一个synchronized方法）。
 * 这时，不同的对象实例的synchronized方法是不相干扰的。也就是说，其它线程照样可以同时访问相同类的另一个对象实例中的synchronized方法；
 * 
 * <li>某个类的范围，synchronized static aStaticMethod{}防止多个线程同时访问这个类中的synchronized static方法。
 * 它可以对类的所有对象实例起作用。（注：这个可以认为是对Class对象起作用）
 * 
 * 除了方法前用synchronized关键字，synchronized关键字还可以用于方法中的某个区块中，表示只对这个区块的资源实行互斥访问。 用法是:
 * synchronized(this){}，它的作用域是this，即是当前对象。当然这个括号里可以是任何对象，synchronized对方法和块的含义和用法并不本质不同；
 * 
 * synchronized关键字是不能继承的，也就是说，基类的方法synchronized f(){} 在继承类中并不自动是synchronized
 * f(){}，而是变成了f(){}。继承类需要你显式的指定它的某个方法为synchronized方法；
 * 
 * wait()/notify()：调用任意对象的 wait() 方法导致线程阻塞，并且该对象上的锁被释放。而调用任意对象的notify()方法则导致因调用该对象的 wait()
 * 方法而阻塞的线程中随机选择的一个解除阻塞（但要等到获得锁后才真正可执行）。
 * 
 * synchronized与这两个方法之间的关系：
 * 
 * <li>1.有synchronized的地方不一定有wait,notify
 * 
 * <li>2.有wait,notify的地方必有synchronized.这是因为wait和notify不是属于线程类，而是每一个对象都具有的方法（事实上，这两个方法是Object类里的），而且，这两个方法都和对象锁有关，有锁的地方，必有synchronized。
 * 
 * synchronized方法中由当前线程占有锁。另一方面，调用wait()notify()方法的对象上的锁必须为当前线程所拥有。
 * 因此，wait()notify()方法调用必须放置在synchronized方法中，synchronized方法的上锁对象就是调用wait()notify()方法的对象。
 * 若不满足这一条件，则程序虽然仍能编译，但在运行时会出现IllegalMonitorStateException 异常。
 * 
 * <li>1. 调用wait()方法前的判断最好用while，而不用if；因为while可以实现被唤醒后线程再次作条件判断；而if则只能判断一次
 * <li>2. 用notifyAll()优先于notify()。
 * 
 * 能调用wait()/notify()的只有当前线程，前提是必须获得了对象锁，就是说必须要进入到synchronized方法中。
 * 
 * JVM中（留神：马上讲到的这两个存储区只在JVM内部与物理存储区无关）存在一个主内存（Main
 * Memory），Java中所有的变量存储在主内存中，所有实例和实例的字段都在此区域，对于所有的线程是共享的（相当于黑板，其他人都可以看到的）。 每个线程都有自己的工作内存（Working
 * Memory），工作内存中保存的是主存中变量的拷贝，（相当于自己笔记本，只能自己看到），工作内存由缓存和堆栈组成，其中缓存保存的是主存中的变量的copy，堆栈保存的是线程局部变量。
 * 线程对所有变量的操作都是在工作内存中进行的，线程之间无法直接互相访问工作内存，变量的值得变化的传递需要主存来完成。
 * 
 * 看线程对某个变量的操作步骤：
 * <li>1.从主内存中复制数据到工作内存
 * <li>2.执行代码，对数据进行各种操作和计算
 * <li>3.把操作后的变量值重新写回主内存中
 * 
 * 所以
 * <li>1.多个线程共有的字段应该用synchronized或volatile来保护.
 * <li>2.synchronized负责线程间的互斥.即同一时候只有一个线程可以执行synchronized中的代码.
 * synchronized还有另外一个方面的作用：在线程进入synchronized块之前，会把工作存内存中的所有内容映射到主内存上，然后把工作内存清空再从主存储器上拷贝最新的值。而在线程退出synchronized块时，同样会把工作内存中的值映射到主内存，不过此时并不会清空工作内存。这样保证线程在执行完代码块后，工作内存中的值和主内存中的值是一致的，保证了数据的一致性！
 * <li>3.volatile负责线程中的变量与主存储区同步.但不负责每个线程之间的同步. volatile的含义是：线程在试图读取一个volatile变量时，会从主内存区中读取最新的值。
 * 
 * synchronized有什么缺点：
 * <li>只有一个condition与锁相关联，这个condition是什么？就是synchronized对针对的对象锁。
 * <li>多线程竞争一个锁时，其余未得到锁的线程只能不停的尝试获得锁，而不能中断。这种情况对于大量的竞争线程会造成性能的下降等后果。
 */
public class SynchronizedDemo {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
