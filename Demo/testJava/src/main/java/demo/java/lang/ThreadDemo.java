package demo.java.lang;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.IntStream;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import demo.java.util.concurrent.ThreadFactoryDemo;
import demo.vo.pojo.User;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadDemo.class);

    public static void main(String[] args) throws InterruptedException {
        // testJoin();
        // testWaitNotify();
        // testInterrupte();
        // testUncaughtExceptionHandler();
        testUncaughtExceptionHandler2();

    }

    private static Thread helloThead = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                LOGGER.info("hello Thead!");
                LOGGER.info("该线程是否被中断：{}", Thread.currentThread().isInterrupted());
                safeSleep(500);
                LOGGER.info("再次判断该线程是否被中断：{}", Thread.currentThread().isInterrupted());
            }
        }
    }, "hello");

    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static User user = new User();

    // 改变user变量的线程
    public static class ChangeObjectThread extends Thread {
        @Override
        public void run() {

            while (true) {
                synchronized (ThreadDemo.class) {
                    int v = (int) (System.currentTimeMillis() / 1000);
                    user.setId(v);
                    // to do sth
                    safeSleep(100);
                    user.setName(String.valueOf(v));
                }
                // 让出CPU，给其他线程执行
                Thread.yield();
            }
        }
    }

    // 读取user变量的线程
    public static class ReadObjectThread extends Thread {
        @Override
        public void run() {
            safeSleep(1000);
            while (true) {
                synchronized (ThreadDemo.class) {
                    if (user.getId() != Integer.parseInt(user.getName())) {
                        System.out.println(user.toString());
                    }
                }
                // 让出CPU，给其他线程执行
                Thread.yield();
            }

        }
    }

    /**
     * 创建线程以及线程池时候要指定与业务相关的名字，以便于追溯问题
     */
    @Test
    public static void demoThreadName() {
        Thread threadOne = new Thread(new Runnable() {
            public void run() {
                System.out.println("保存订单的线程");
                throw new NullPointerException();
            }
        }, "订单线程");

        ThreadPoolExecutor a;
    }

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

    /**
     * 让出CPU，给其他线程执行
     */
    public static void testYield() {
        Thread.yield();
    }

    /**
     * Thread的run方法是不抛出任何检查型异常的，但是它自身却可能因为一个异常而被中止，导致这个线程的终结。
     * 
     * 如果需要捕获系统的未捕获异常（如系统抛出了未知错误，这种异常没有捕获，这将导致系统莫名奇妙的关闭，使得用户体验差），
     * 可以通过Thread.setUncaughtExceptionHandler(UncaughtExceptionHandler)来处理这种异常。
     * 
     * 另外，这个例子同时也说明了异常处理函数UncaughtExceptionHandle是运行在抛出异常的线程中的。
     * 
     */
    static void testUncaughtExceptionHandler() {
        ThreadGroup group1 = new ThreadGroupDemo1();

        UncaughtExceptionHandler handler = new UncaughtExceptionHandlerDemo("该类线程默认的handler");

        // 为所有的Thread设置一个默认的UncaughtExceptionHandler
        RuntimeExceptionThread.setDefaultUncaughtExceptionHandler(handler);

        RuntimeExceptionThread t1 = new RuntimeExceptionThread(group1, "t1");
        RuntimeExceptionThread t2 = new RuntimeExceptionThread(group1, "t2");

        UncaughtExceptionHandler handler2 = new UncaughtExceptionHandlerDemo("线程指定的handler");
        t2.setUncaughtExceptionHandler(handler2);

        t1.start();
        t2.start();

        testUncaughtExceptionHandler2();
    }

    /**
     * 如果是使用了线程池，那应该如何处理呢？ 我们知道线程池中我们只能提交Runable,Callable对象，而不是Thread对象，
     * 
     * 为了能够也能设置UncaughtExceptionHandler，我们能够通过线程池的ThreadFactory，使得线程池中的线程特殊化。
     */
    static void testUncaughtExceptionHandler2() {
        UncaughtExceptionHandler handler = new UncaughtExceptionHandlerDemo("UncaughtExceptionHandler");

        ExecutorService executors = Executors.newFixedThreadPool(10,
                new ThreadFactoryDemo.SpecialThreadFactory(handler));

        for (int i = 0; i < 10; i++) {
            executors.execute(new Runnable() {
                @Override
                public void run() {
                    LOGGER.info("我要抛出运行期异常咯");
                    throw new RuntimeException("故意抛出的运行期异常");
                }
            });
        }
        executors.shutdown();
    }

    public static void safeSleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
            Thread.currentThread().stop();
        }
    }

    /**
     * <h2>终止线程的几种方式</h2>
     * <li>1、使用stop()方法，已被弃用。
     * <li>2、使用volatile标志位来终止线程
     * <li>3、使用interrupt()中断的方式，注意使用interrupt()方法中断正在运行中的线程只会修改中断状态位，可以通过isInterrupted()判断。
     * 如果使用interrupt()方法中断阻塞中的线程，那么就会抛出InterruptedException异常，可以通过catch捕获异常，然后进行处理后终止线程。有些情况，我们不能判断线程的状态，所以使用interrupt()方法时一定要慎重考虑。
     * 
     */
    static void demoStop() {
        // 使用stop()方法
        testStop();
    }

    /**
     * 模拟一个过程，读线程ReadObjectThread在读到对象的ID和Name不一致时，会输出这些对象； 而写线程ChangeObjectThread总是写入连个相同的数值。
     * 但是在代码中因为使用了stop()强行停止线程，造成了数据的不同步。
     */
    static void testStop2() {
        new ReadObjectThread().start();
        while (true) {
            Thread t = new ChangeObjectThread();
            t.start();
            safeSleep(150);
            // 使用stop()方法，强制停止线程
            t.stop();
        }
    }

    /**
     * 使用stop()方法，已被弃用。原因是：stop()是立即终止，会导致一些数据被到处理一部分就会被终止，而用户并不知道哪些数据被处理，哪些没有被处理，产生了不完整的“残疾”数据，不符合完整性，所以被废弃。
     * 原因是stop()方法太过于暴力，会强行把执行一半的线程终止。这样会就不会保证线程的资源正确释放，通常是没有给与线程完成资源释放工作的机会，因此会导致程序工作在不确定的状态下。
     */
    static void testStop() {
        helloThead.start();
        long t1 = System.currentTimeMillis();
        while (true) {
            long cost = System.currentTimeMillis() - t1;
            if (cost > 2_000 && helloThead.isAlive()) {
                helloThead.stop();
                LOGGER.info("timeout and stop");
                break;
            } else {
                safeSleep(3);
            }
        }
        LOGGER.info(String.valueOf(helloThead.isAlive()));
    }

    private static class Machine extends Thread {

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

    static void testInterrupte() {
        Machine machine = new Machine();
        Thread.setDefaultUncaughtExceptionHandler(null);
        machine.setUncaughtExceptionHandler(null);
        machine.start();
    }

    /**
     * <h2>使用interrupt()方法中断当前线程</h2>
     * <p>
     * 一个线程不应该由其他线程来强制中断或停止，而是应该由线程自己自行停止。 所以，Thread.stop, Thread.suspend, Thread.resume 都已经被废弃了。 而
     * Thread.interrupt的作用其实也不是中断线程，而是「通知线程应该中断了」， 具体到底中断还是继续运行，应该由被通知的线程自己处理。
     * <p>
     * 具体来说，当对一个线程，调用 interrupt() 时，
     * <li>如果线程处于被阻塞状态（例如处于sleep, wait, join 等状态），那么线程将立即退出被阻塞状态，并抛出一个InterruptedException异常。仅此而已。
     * <li>如果线程处于正常活动状态，那么会将该线程的中断标志设置为 true，仅此而已。被设置中断标志的线程将继续正常运行，不受影响。
     * <p>
     * interrupt() 并不能真正的中断线程，需要被调用的线程自己进行配合才行。也就是说，一个线程如果有被中断的需求，那么就可以这样做。
     * <li>在正常运行任务时，经常检查本线程的中断标志位，如果被设置了中断标志就自行停止线程。
     * <li>在调用阻塞方法时正确处理InterruptedException异常。（例如，catch异常后就结束线程。）
     * <p>
     * Thread.interrupted()清除标志位是为了下次继续检测标志位。
     * 如果一个线程被设置中断标志后，选择结束线程那么自然不存在下次的问题，而如果一个线程被设置中断标识后，进行了一些处理后选择继续进行任务，而且这个任务也是需要被中断的，那么当然需要清除标志位了。
     * <p>
     * 使用interrupt()方法来中断线程有两种情况：
     * <li>1.线程处于阻塞状态，如使用了sleep,同步锁的wait,socket中的receiver,accept等方法时，会使线程处于阻塞状态。
     * 当调用线程的interrupt()方法时，会抛出InterruptException异常。 阻塞中的那个方法抛出这个异常，通过代码捕获该异常，然后break跳出循环状态，从而让我们有机会结束这个线程的执行。
     * 通常很多人认为只要调用interrupt方法线程就会结束，实际上是错的， 一定要先捕获InterruptedException异常之后通过break来跳出循环，才能正常结束run方法。
     * <li>2.线程未处于阻塞状态，使用isInterrupted()判断线程的中断标志来退出循环。当使用interrupt()方法时，中断标志就会置true，和使用自定义的标志来控制循环是一样的道理。
     * <p>
     * <h2>为什么要区分进入阻塞状态和和非阻塞状态两种情况</h2>
     * <p>
     * 是因为当阻塞状态时，如果有interrupt()发生，系统除了会抛出InterruptedException异常外，还会调用interrupted()函数，
     * 调用时能获取到中断状态是true的状态，调用完之后会复位中断状态为false，所以异常抛出之后通过isInterrupted()是获取不到中断状态是true的状态，从而不能退出循环.
     * 因此在线程未进入阻塞的代码段时是可以通过isInterrupted()来判断中断是否发生来控制循环，在进入阻塞状态后要通过捕获异常来退出循环。 因此使用interrupt()来退出线程的最好的方式应该是两种情况都要考虑。
     * 
     * <h2>如何处理中断？</h2>上文都在介绍如何获取中断状态，那么当我们捕获到中断状态后，究竟如何处理呢？
     * <li>Java类库中提供的一些可能会发生阻塞的方法都会抛InterruptedException异常，如：BlockingQueue#put、BlockingQueue#take、Object#wait、Thread#sleep。
     * <li>当你在某一条线程中调用这些方法时，这个方法可能会被阻塞很长时间，你可以在别的线程中调用当前线程对象的interrupt方法触发这些函数抛出InterruptedException异常。
     * <li>当一个函数抛出InterruptedException异常时，表示这个方法阻塞的时间太久了，别人不想等它执行结束了。
     * <li>当你的捕获到一个InterruptedException异常后，亦可以处理它，或者向上抛出。
     * <li>抛出时要注意？？？：当你捕获到InterruptedException异常后，当前线程的中断状态已经被修改为false(表示线程未被中断)；
     * 此时你若能够处理中断，则不用理会该值；但如果你继续向上抛InterruptedException异常，你需要再次调用interrupt方法，将当前线程的中断状态设为true。
     * <li>注意：绝对不能“吞掉中断”！即捕获了InterruptedException而不作任何处理。这样违背了中断机制的规则，别人想让你线程中断，然而你自己不处理，也不将中断请求告诉调用者，调用者一直以为没有中断请求。
     */
    static void testInterrupt() {
        //
        testInterrupt1();
        //
        testInterrupt2();
        //
        testInterrupt3();

    }

    /**
     * 标记中断后，线程会继续执行
     */
    static void testInterrupt4() {
        helloThead.start();
        helloThead.interrupt();
    }

    /**
     * 阻塞状态:当调用线程的interrupt()方法时，会抛出InterruptException异常。 通过代码捕获该异常，然后break跳出循环状态，从而让我们有机会结束这个线程的执行。
     */
    static void testInterrupt1() {
        Thread thread = new Thread(() -> {
            while (true) {
                LOGGER.info("hello");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                    break;
                }
            }
        }, "hello");

        thread.start();
        long t1 = System.currentTimeMillis();
        while (true) {
            long cost = System.currentTimeMillis() - t1;
            if (cost > 2_000) {
                thread.interrupt();
                LOGGER.info("timeout and interrupt");
                break;
            } else {
                safeSleep(10);
            }
        }
    }

    /**
     * 非阻塞状态:使用isInterrupted()判断线程的中断标志来退出循环
     */
    static void testInterrupt2() {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                LOGGER.info("hello");
            }
        }, "hello");
        thread.start();

        long t1 = System.currentTimeMillis();
        while (true) {
            long cost = System.currentTimeMillis() - t1;
            if (cost > 2_000) {
                thread.interrupt();
                LOGGER.info("timeout and interrupt");
                break;
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        }
    }

    /**
     * 两种情况都要考虑
     */
    static void testInterrupt3() {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) { // 非阻塞过程中通过判断中断标志来退出
                try {
                    LOGGER.info("hello.");
                    Thread.sleep(5 * 1000);// 阻塞过程捕获中断异常来退出
                } catch (InterruptedException e) {
                    LOGGER.info(e.getMessage(), e);
                    break;// 捕获到异常之后，执行break跳出循环。
                }
            }
        }, "hello");
        thread.start();
        long t1 = System.currentTimeMillis();
        while (true) {
            long cost = System.currentTimeMillis() - t1;
            if (cost > 2_000) {
                thread.interrupt();
                LOGGER.info("timeout and interrupt");
                break;
            } else {
                safeSleep(10);
            }
        }
    }

    static void testJoin() throws InterruptedException {

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

    public static void testDaemon() {
        Thread d = new Daemon();
        System.out.println("d.isDaemon() = " + d.isDaemon());
        // Allow the daemon threads to finish
        // their startup processes:
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Waiting for CR");
        try {
            stdin.readLine();
        } catch (IOException e) {
        }
    }

    /**
     * 
     * 用于处理线程抛出的未捕获的异常
     *
     */
    public static class UncaughtExceptionHandlerDemo implements Thread.UncaughtExceptionHandler {
        private String name;

        public UncaughtExceptionHandlerDemo(String name) {
            this.name = name;
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            LOGGER.error(name + " catch a exception from " + t.getName() + "。需要怎么处理呢？");
        }

    }
}

class Daemon extends Thread {
    private static final int SIZE = 10;
    private Thread[] t = new Thread[SIZE];

    public Daemon() {
        setDaemon(true);
        start();
    }

    public void run() {
        for (int i = 0; i < SIZE; i++)
            t[i] = new DaemonSpawn(i);
        for (int i = 0; i < SIZE; i++)
            System.out.println("t[" + i + "].isDaemon() = " + t[i].isDaemon());
        while (true)
            yield();
    }
}

class DaemonSpawn extends Thread {
    public DaemonSpawn(int i) {
        System.out.println("DaemonSpawn " + i + " started");
        start();
    }

    public void run() {
        while (true)
            yield();
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

/**
 * <h2>终止线程的几种方式</h2>
 * <li>1、使用stop()方法，已被弃用。原因是：stop()是立即终止，会导致一些数据被到处理一部分就会被终止，而用户并不知道哪些数据被处理，哪些没有被处理，产生了不完整的“残疾”数据，不符合完整性，所以被废弃。
 * <li>2、使用volatile标志位来终止线程
 * <li>3、使用interrupt()中断的方式，注意使用interrupt()方法中断正在运行中的线程只会修改中断状态位，可以通过isInterrupted()判断。
 * 如果使用interrupt()方法中断阻塞中的线程，那么就会抛出InterruptedException异常，可以通过catch捕获异常，然后进行处理后终止线程。有些情况，我们不能判断线程的状态，所以使用interrupt()方法时一定要慎重考虑。
 * 
 * @author hanjy
 *
 */
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

/**
 * 抛出运行期异常的线程
 * 
 */
class RuntimeExceptionThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeExceptionThread.class);

    public RuntimeExceptionThread(ThreadGroup threadGroup, String name) {
        super(threadGroup, name);
    }

    @Override
    public void run() {
        LOGGER.info("我要抛出运行期异常咯");
        throw new RuntimeException("故意抛出的运行期异常");
    }
}
