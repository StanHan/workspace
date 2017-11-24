package demo.java.lang.thread;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程的几种状态：
 * <li>NEW,未启动的。不会出现在Dump中。
 * <li>RUNNABLE,该状态表示线程具备所有运行条件，在运行队列中准备操作系统的调度，或者正在运行。
 * <li>BLOCKED,受阻塞并等待监视器锁。
 * <li>WATING,无限期等待另一个线程执行特定操作。
 * <li>TIMED_WATING,有时限的等待另一个线程的特定操作。
 * <li>TERMINATED,已退出的。
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
 */
public class ThreadDemo {

    public static void main(String[] args) throws InterruptedException {
        // testJoin();
        // testWaitNotify();
        // testInterrupte();
//        testUncaughtExceptionHandler();
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