package demo.java.lang.thread;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * 线程的几种状态：
 * <li>NEW,创建后，尚未启动的线程。不会出现在Dump中。
 * <li>RUNNABLE,该状态表示线程具备所有运行条件，在运行队列中准备操作系统的调度，或者正在运行。
 * <li>BLOCKED,受阻塞并等待监视器锁。
 * <li>WATING,处于监视器所属对象的wait set中，等待着被其它（此对象监视器所有者）线程唤醒。
 * <li>TIMED_WATING,有时限的等待另一个线程的特定操作。
 * <li>TERMINATED,已经终止的线程处于这种状态。
 * 
 * Monitor: Monitor是 Java中用以实现线程之间的互斥与协作的主要手段，它可以看成是对象或者 Class的锁。每一个对象都有，也仅有一个 monitor。
 * <li>进入区(Entry Set):表示线程通过synchronized要求获取对象的锁。如果对象未被锁住,则迚入拥有者;否则则在进入区等待。一旦对象锁被其他线程释放,立即参与竞争。
 * <li>拥有者(The Owner):表示某一线程成功竞争到对象锁。
 * <li>等待区(Wait Set):表示线程通过对象的wait方法,释放对象的锁,并在等待区等待被唤醒。 一个 Monitor在某个时刻，只能被一个线程拥有，该线程就是 “Active Thread”，而其它线程都是 “Waiting
 * Thread”，分别在两个队列 “ Entry Set”和 “Wait Set”里面等候。在 “Entry Set”中等待的线程状态是 “Waiting for monitor entry”，而在 “Wait
 * Set”中等待的线程状态是 “in Object.wait()”。
 * 
 * 调用修饰:表示线程在方法调用时,额外的重要的操作。线程Dump分析的重要信息。修饰上方的方法调用。
 * 
 * <li>locked <地址> 目标：使用synchronized申请对象锁成功,监视器的拥有者。通过synchronized关键字,成功获取到了对象的锁,成为监视器的拥有者,在临界区内操作。对象锁是可以线程重入的。
 * <li>waiting to lock <地址>
 * 目标：使用synchronized申请对象锁未成功,在迚入区等待。通过synchronized关键字,没有获取到了对象的锁,线程在监视器的进入区等待。在调用栈顶出现,线程状态为Blocked。
 * <li>waiting on <地址>
 * 目标：使用synchronized申请对象锁成功后,释放锁幵在等待区等待。通过synchronized关键字,成功获取到了对象的锁后,调用了wait方法,进入对象的等待区等待。在调用栈顶出现,线程状态为WAITING或TIMED_WATING。
 * <li>parking to wait for <地址> 目标.park是基本的线程阻塞原语,不通过监视器在对象上阻塞。随concurrent包会出现的新的机制,不synchronized体系不同。
 * 
 * <p>
 * JVM的线程模型，其中最主要的是下面几个类：
 * <li>java.lang.Thread: 这个是Java语言里的线程类，由这个Java类创建的instance都会 1:1 映射到一个操作系统的osthread
 * <li>JavaThread: JVM中C++定义的类，一个JavaThread的instance代表了在JVM中的java.lang.Thread的instance,
 * 它维护了线程的状态，并且维护一个指针指向java.lang.Thread创建的对象(oop)。它同时还维护了一个指针指向对应的OSThread，来获取底层操作系统创建的osthread的状态
 * <li>OSThread: JVM中C++定义的类，代表了JVM中对底层操作系统的osthread的抽象，它维护着实际操作系统创建的线程句柄handle，可以获取底层osthread的状态
 * <li>VMThread: JVM中C++定义的类，这个类和用户创建的线程无关，是JVM本身用来进行虚拟机操作的线程，比如GC。
 * <p>
 * 有两种方式可以让用户在JVM中创建线程
 * <li>1. new java.lang.Thread().start()
 * <li>2. 使用JNI将一个native thread attach到JVM中
 * <p>
 * 针对 new java.lang.Thread().start()这种方式，只有调用start()方法的时候，才会真正的在JVM中去创建线程，主要的生命周期步骤有：
 * <li>1. 创建对应的JavaThread的instance
 * <li>2. 创建对应的OSThread的instance
 * <li>3. 创建实际的底层操作系统的native thread
 * <li>4. 准备相应的JVM状态，比如ThreadLocal存储空间分配等
 * <li>5. 底层的native thread开始运行，调用java.lang.Thread生成的Object的run()方法
 * <li>6. 当java.lang.Thread生成的Object的run()方法执行完毕返回后,或者抛出异常终止后，终止native thread
 * <li>7. 释放JVM相关的thread的资源，清除对应的JavaThread和OSThread
 * <p>
 * 针对JNI将一个native thread attach到JVM中，主要的步骤有：
 * <li>1. 通过JNI call AttachCurrentThread申请连接到执行的JVM实例
 * <li>2. JVM创建相应的JavaThread和OSThread对象
 * <li>3. 创建相应的java.lang.Thread的对象
 * <li>4. 一旦java.lang.Thread的Object创建之后，JNI就可以调用Java代码了
 * <li>5. 当通过JNI call DetachCurrentThread之后，JNI就从JVM实例中断开连接
 * <li>6. JVM清除相应的JavaThread, OSThread, java.lang.Thread对象
 * <p>
 * 从JVM的角度来看待线程状态的状态有以下几种:其中主要的状态是这5种:
 * <li>_thread_new: 新创建的线程
 * <li>_thread_in_Java: 在运行Java代码
 * <li>_thread_in_vm: 在运行JVM本身的代码
 * <li>_thread_in_native: 在运行native代码
 * <li>_thread_blocked: 线程被阻塞了，包括等待一个锁，等待一个条件，sleep，执行一个阻塞的IO等
 * <p>
 * 从OSThread的角度，JVM还定义了一些线程状态给外部使用，比如用jstack输出的线程堆栈信息中线程的状态: 比较常见有:
 * <li>Runnable: 可以运行或者正在运行的
 * <li>MONITOR_WAIT: 等待锁
 * <li>OBJECT_WAIT: 执行了Object.wait()之后在条件队列中等待的
 * <li>SLEEPING: 执行了Thread.sleep()的
 * 
 * 从JavaThread的角度，JVM定义了一些针对Java Thread对象的状态，基本类似，多了一个TIMED_WAITING的状态，用来表示定时阻塞的状态
 */
public class ThreadDemo {

    public static void main(String[] args) throws InterruptedException {
        // testJoin();
        // testWaitNotify();
        // testInterrupte();
        // testUncaughtExceptionHandler();
        testThreadPool();
    }

    static ExecutorService executorService = Executors.newFixedThreadPool(3);

    static void testThreadPool() {
        for (int i = 0; i < 100; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    throw new RuntimeException("抛出异常");
                }
            });
        }
    }

    public static void testWaitNotify() {
        String a = "A";
        new Thread(new PrimeRun(a)).start();
        new Thread(new PrimeRun(a)).start();
    }

    public static void testYield() {

    }

    public static void testUncaughtExceptionHandler() {
        ThreadGroup group = new MachineGroup();

        UncaughtExceptionHandler handler = new MachineHandler("DefaultUncaughtExceptionHandler");

        Machine_E.setDefaultUncaughtExceptionHandler(handler);

        Machine_E machine1 = new Machine_E(group, "machine1");
        Machine_E machine2 = new Machine_E(group, "machine2");

        UncaughtExceptionHandler machineHandler = new MachineHandler("machineHandler");
        machine2.setUncaughtExceptionHandler(machineHandler);

        machine1.start();
        machine2.start();
    }

    public static void testInterrupte() {
        Machine machine = new Machine();
        // Thread.setDefaultUncaughtExceptionHandler(null);
        // machine.setUncaughtExceptionHandler(null);
        machine.start();
    }

    public static void testJoin() throws InterruptedException {

        // Delay, in milliseconds before we interrupt MessageLoop thread .
        long patience = 2000;

        threadMessage("Starting MessageLoop thread");
        long startTime = System.currentTimeMillis();
        Thread thread_MessageLoop = new Thread(new MessageLoop());
        thread_MessageLoop.start();

        threadMessage("Waiting for MessageLoop thread to finish");
        // loop until MessageLoop thread exits
        while (thread_MessageLoop.isAlive()) {
            threadMessage("Still waiting...");
            // Wait maximum of 1 second for MessageLoop thread to finish.
            thread_MessageLoop.join(10000);
            if (((System.currentTimeMillis() - startTime) > patience) && thread_MessageLoop.isAlive()) {
                threadMessage("Tired of waiting!");
                thread_MessageLoop.interrupt();
                // Shouldn't be long now -- wait indefinitely
                thread_MessageLoop.join();
            }
        }
        threadMessage("Finally!");
    }

    /**
     * Display a message, preceded by the name of the current thread
     * 
     * @param message
     */
    public static void threadMessage(String message) {
        String threadName = Thread.currentThread().getName();
        System.out.format("%s: %s%n", threadName, message);
    }
}

class PrimeRun implements Runnable {
    private String minPrime;

    PrimeRun(String minPrime) {
        this.minPrime = minPrime;
    }

    public void run() {
        for (int i = 1; i <= 100; i++) {
            synchronized (minPrime) {
                System.out.println(Thread.currentThread().getName() + ": " + i);
                if (i % 10 != 0) {
                    minPrime.notify();
                } else {
                    try {
                        minPrime.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

class StoppableTask extends Thread {
    private volatile boolean pleaseStop;

    public void run() {
        while (!pleaseStop) {
            // do some stuff...
        }
    }

    public void tellMeToStop() {
        pleaseStop = true;
    }
}

class MessageLoop implements Runnable {
    public void run() {
        String importantInfo[] = { "Mares eat oats", "Does eat oats", "Little lambs eat ivy",
                "A kid will eat ivy too" };
        try {
            for (int i = 0; i < importantInfo.length; i++) {
                // Pause for 4 seconds
                Thread.sleep(1500);// Pausing Execution with Sleep
                // Print a message
                ThreadDemo.threadMessage(importantInfo[i]);

                if (Thread.interrupted()) {
                    throw new InterruptedException();
                }
            }
        } catch (InterruptedException e) {
            ThreadDemo.threadMessage("I wasn't done!");
        }
    }

}

class Machine extends Thread {

    private int a = 0;

    private Timer timer = new Timer(true);

    public synchronized void resize() {
        a = 0;
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                while (a > 30) {
                    final Thread thread = Thread.currentThread();

                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            System.out.println("Thread Name: " + thread.getName() + " has waited 3 seconds.");
                            thread.interrupt();
                        }
                    }, 3000);

                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        System.err.println("Thread Name: " + thread.getName() + " has interruped.");
                        return;
                    }
                }

                a++;
                System.out.println("a = " + a);
            }
        }
    }
}

class Machine_E extends Thread {

    public Machine_E(ThreadGroup threadGroup, String name) {
        super(threadGroup, name);
    }

    @Override
    public void run() {
        int a = 1 / 0;
    }
}

class MachineGroup extends ThreadGroup {

    public MachineGroup(String name) {
        super(name);
    }

    public MachineGroup() {
        super("MachineGroup");
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        System.out.println(getName() + " catch a exception from " + t.getName());
        super.uncaughtException(t, e);
    }

}

class MachineHandler implements Thread.UncaughtExceptionHandler {

    private String name;

    public MachineHandler(String name) {
        this.name = name;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println(name + " catch a exception from " + t.getName());

    }

}