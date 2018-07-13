package demo.java.util.concurrent;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ThreadPoolExecutor 类就是一个线程池。客户端调用 ThreadPoolExecutor.submit(Runnable task) 提交任务，线程池内部维护的工作者线程的数量就是该线程池的线程池大小，有 3 种形态：
 * <li>当前线程池大小 ：表示线程池中实际工作者线程的数量；
 * <li>最大线程池大小 （maxinumPoolSize）：表示线程池中允许存在的工作者线程的数量上限；
 * <li>核心线程大小 （corePoolSize ）：表示一个不大于最大线程池大小的工作者线程数量上限。
 * <p>
 * <li>如果运行的线程少于 corePoolSize，则 Executor 始终首选添加新的线程，而不进行排队；
 * <li>如果运行的线程等于或者多于 corePoolSize，则 Executor 始终首选将请求加入队列，而不是添加新线程；
 * <li>如果无法将请求加入队列，即队列已经满了，则创建新的线程，除非创建此线程超出 maxinumPoolSize， 在这种情况下，任务将被拒绝。
 * 
 * <h2>线程池关闭</h2>A pool that is no longer referenced in a program and has no remaining threads will be shutdown
 * automatically. 如果程序中不再持有线程池的引用，并且线程池中没有线程时，线程池将会自动关闭。然而我们常用的FixedThreadPool的核心线程没有超时策略，所以并不会自动关闭。
 * <p>
 * 思考一下几个问题：
 * <li>是否可以继续接受新任务？继续提交新任务会怎样？
 * <li>等待队列里的任务是否还会执行？
 * <li>正在执行的任务是否会立即中断？
 * 
 */
public class ThreadPoolExecutorDemo {

    private static Logger logger = LoggerFactory.getLogger(ThreadPoolExecutorDemo.class);

    public static void main(String[] args) {

        System.out.println(102599268 & 3);
    }

    /**
     * 
     * @param corePoolSize
     *            核心线程池大小
     * @param maximumPoolSize
     *            最大线程池大小
     * @param keepAliveTime
     *            线程最大空闲时间
     * @param unit
     *            时间单位
     * @param workQueue
     *            线程等待队列
     * @param threadFactory
     *            线程创建工厂
     * @param handler
     *            拒绝策略
     */
    static void demoThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime,
                unit, workQueue, threadFactory, handler);
    }

    /**
     * 只有任务0执行完毕，其他任务都被drop掉了，dropList的size为100。通过dropList我们可以对未处理的任务进行进一步的处理，如log记录，转发等；
     */
    static void demoShutdownNow() {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        for (int i = 1; i <= 100; i++) {
            workQueue.add(new Task(String.valueOf(i)));
        }
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, workQueue);
        executor.execute(new Task("0"));
        // shutdownNow有返回值，返回被抛弃的任务list
        List<Runnable> dropList = executor.shutdownNow();
        logger.info("workQueue size = " + workQueue.size() + " after shutdown");
        logger.info("dropList size = " + dropList.size());
    }

    /**
     * 我们用LinkedBlockingQueue构造了一个线程池，在线程池启动前，我们先将工作队列填充100个任务，然后执行task 0 后立即shutdown()线程池，来验证线程池关闭队列的任务运行状态。
     * 从结果中我们可以看到，线程池虽然关闭，但是队列中的任务任然继续执行，所以用 shutdown()方式关闭线程池时需要考虑是否是你想要的效果。
     */
    static void demoWaitqueueTest() {
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        for (int i = 1; i <= 100; i++) {
            workQueue.add(new Task(String.valueOf(i)));
        }
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, workQueue);
        executor.execute(new Task("0"));
        executor.shutdown();
        logger.info("workQueue size = " + workQueue.size() + " after shutdown");
    }

    static class Task implements Runnable {
        String name;

        public Task(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                logger.info("task " + name + " is running");
            }
            logger.info("task " + name + " is over");
        }
    }

    /**
     * 为了体现在任务执行中打断，在主线程进行短暂 sleep ， task 中 调用 Thread.yield() ，出让时间片。 从结果中可以看到，线程池被关闭后，正则运行的任务没有被 interrupt。
     * 说明shutdown()方法不会 interrupt 运行中线程。
     */
    static void demoInterrupt1() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        executor.execute(new TaskWithInterrupt("0"));
        Thread.sleep(1);
        executor.shutdown();
        logger.info("executor has been shutdown");
    }

    /**
     * 修改为shutdownNow() 后，task任务没有执行完，执行到中间的时候就被 interrupt 后没有继续执行了。
     */
    static void demoInterrupt2() throws InterruptedException {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        executor.execute(new TaskWithInterrupt("0"));
        Thread.sleep(1);
        executor.shutdownNow();
        logger.info("executor has been shutdown");
    }

    /**
     * 任务执行时判断了中断标志
     */
    static class TaskWithInterrupt implements Runnable {
        String name;

        public TaskWithInterrupt(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            for (int i = 1; i <= 100 && !Thread.interrupted(); i++) {
                Thread.yield();
                logger.info("task " + name + " is running, round " + i);
            }
        }
    }

    /**
     * 当线程池关闭后，继续提交新任务会抛出异常。这句话也不够准确，不一定是抛出异常，而是执行拒绝策略，默认的拒绝策略是抛出异常。
     */
    static void demoShutdown() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(4, 4, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        executor.execute(() -> logger.info("before shutdown"));
        executor.shutdown();
        executor.execute(() -> logger.info("after shutdown"));
    }

    /**
     * 会爆出：java.lang.OutOfMemoryError。 因为FixedThreadPool的核心线程不会自动超时关闭，使用时必须在适当的时候调用shutdown()方法。
     * <p>
     * <li>corePoolSize与maximumPoolSize相等，即其线程全为核心线程，是一个固定大小的线程池，是其优势；
     * <li>keepAliveTime = 0 该参数默认对核心线程无效，而FixedThreadPool全部为核心线程；
     * <li>workQueue
     * 为LinkedBlockingQueue（无界阻塞队列），队列最大值为Integer.MAX_VALUE。如果任务提交速度持续大余任务处理速度，会造成队列大量阻塞。因为队列很大，很有可能在拒绝策略前，内存溢出。是其劣势；
     * <li>FixedThreadPool的任务执行是无序的；
     */
    static void demoFixedThreadPool() {
        while (true) {
            ExecutorService executorService = Executors.newFixedThreadPool(8);
            executorService.execute(() -> logger.info("running"));
            executorService = null;
        }
    }

    /**
     * CachedThreadPool 的线程 keepAliveTime 默认为 60s ，核心线程数量为 0 ，所以不会有核心线程存活阻止线程池自动关闭。 实际开发中，如果CachedThreadPool
     * 确实忘记关闭，在一定时间后是可以被回收的。但仍然建议显示关闭。
     * <p>
     * <li>corePoolSize = 0，maximumPoolSize = Integer.MAX_VALUE，即线程数量几乎无限制；
     * <li>keepAliveTime = 60s，线程空闲60s后自动结束。
     * <li>workQueue 为 SynchronousQueue
     * 同步队列，这个队列类似于一个接力棒，入队出队必须同时传递，因为CachedThreadPool线程创建无限制，不会有队列等待，所以使用SynchronousQueue；
     * <p>
     * 适用场景：快速处理大量耗时较短的任务，如Netty的NIO接受请求时，可使用CachedThreadPool。
     */
    static void demoCachedThreadPool() {
        while (true) {
            // 默认keepAliveTime为 60s
            ExecutorService executorService = Executors.newCachedThreadPool();
            ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executorService;
            // 为了更好的模拟，动态修改为1纳秒
            threadPoolExecutor.setKeepAliveTime(1, TimeUnit.NANOSECONDS);
            threadPoolExecutor.execute(() -> logger.info("running"));
        }
    }

    /**
     * 创建线程以及线程池时候要指定与业务相关的名字，以便于追溯问题
     */
    public void namedThreadPoolExecutor() {
        ThreadPoolExecutor executorOne = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

        ThreadPoolExecutor namedThreadPool = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(), new ThreadFactoryDemo.NamedThreadFactory("我是有名字的线程池"));
    }

    /**
     * <h2>任务调度线程池ScheduledThreadPoolExecutor</h2>
     * 
     * 先来看看ScheduledThreadPoolExecutor的实现模型，它通过继承ThreadPoolExecutor来重用线程池的功能，里面做了几件事情：
     * 
     * <li>为线程池设置了一个DelayedWorkQueue，该queue同时具有PriorityQueue（优先级大的元素会放到队首）和DelayQueue（如果队列里第一个元素的getDelay返回值大于0，则take调用会阻塞）的功能
     * <li>将传入的任务封装成ScheduledFutureTask，这个类有两个特点，实现了java.lang.Comparable和java.util.concurrent.Delayed接口，也就是说里面有两个重要的方法：compareTo和getDelay。
     * ScheduledFutureTask里面存储了该任务距离下次调度还需要的时间（使用的是基于System#nanoTime实现的相对时间，不会因为系统时间改变而改变，如距离下次执行还有10秒，不会因为将系统时间调前6秒而变成4秒后执行）。
     * getDelay方法就是返回当前时间（运行getDelay的这个时刻）距离下次调用之间的时间差；compareTo用于比较两个任务的优先关系，距离下次调度间隔较短的优先级高。
     * 那么，当有任务丢进上面说到的DelayedWorkQueue时，因为它有DelayQueue（DelayQueue的内部使用PriorityQueue来实现的）的功能，所以新的任务会与队列中已经存在的任务进行排序，距离下次调度间隔短的任务排在前面，也就是说这个队列并不是先进先出的；
     * 另外，在调用DelayedWorkQueue的take方法的时候，如果没有元素，会阻塞，如果有元素而第一个元素的getDelay返回值大于0（前面说过已经排好序了，第一个元素的getDelay不会大于后面元素的getDelay返回值），也会一直阻塞。
     * <li>ScheduledFutureTask提供了一个run的实现，线程池执行的就是这个run方法。
     *
     */
    static void demoScheduledThreadPoolExecutor() {
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);

        exec.scheduleAtFixedRate(new Runnable() {// 每隔一段时间就触发异常
            @Override
            public void run() {
                logger.info("抛异常咯");
                // try {
                // throw new RuntimeException();
                // } catch (Exception e) {
                // logger.info("捕获异常,否则该任务不再执行");
                // }
                throw new RuntimeException();

            }
        }, 1000, 200, TimeUnit.MILLISECONDS);

        exec.scheduleAtFixedRate(new Runnable() {// 每隔一段时间打印系统时间，证明两者是互不影响的
            @Override
            public void run() {
                logger.info("" + System.nanoTime());
            }
        }, 20, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * 采用线程池开启多个子线程，主线程等待所有的子线程执行完毕
     */
    static void testShutDownThreadPool() {
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(3);
            for (int i = 0; i < 10; i++) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            logger.info(Thread.currentThread().getName());
                            Thread.sleep(random.nextInt(3000));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            logger.info(Thread.currentThread().getName() + " 结束");
                        }
                    }
                });
            }

            logger.info("已经开启所有的子线程");
            executorService.shutdown();
            logger.info("shutdown()：启动一次顺序关闭，执行以前提交的任务，但不接受新任务。");
            while (true) {
                if (executorService.isTerminated()) {
                    logger.info("所有的子线程都结束了！");
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            logger.info("主线程结束");
        }
    }

    /**
     * 如果抛出异常，则线程WAITING (parking)，该定时任务不会再被调度
     */
    static void testScheduledThreadPool() {
        /** 创建一个可安排在给定延迟后运行命令或者定期地执行的线程池。效果类似于Timer定时器 */
        ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(5);
        // 5秒后执行任务
        scheduledService.schedule(new Runnable() {
            public void run() {
                logger.info("schedulePool.schedule");
            }
        }, 1, TimeUnit.SECONDS);

        // 5秒后执行任务，以后每2秒执行一次
        scheduledService.scheduleAtFixedRate(new Runnable() {
            int count = 0;

            @Override
            public void run() {
                count++;
                logger.info("schedulePool.scheduleAtFixedRate");
                if (count == 3) {// 抛出异常，线程 WAITING (parking)
                    throw new RuntimeException();
                }
            }
        }, 1, 2, TimeUnit.SECONDS);

    }

    static void testSingleThreadExecutor() {
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {

            @Override
            public void run() {
                int i = 0;
                while (i < 10) {
                    logger.info("hello.");
                    i++;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        singleThreadExecutor.shutdown();
        logger.info("over.");
    }

    static void testFixedThreadPool() {
        /** 可重用固定线程集合的线程池，以共享的无界队列方式来运行这些线程。创建可以容纳3个线程的线程池 */
        ExecutorService threadPool_1 = Executors.newFixedThreadPool(3);
        /** 可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们,线程池的大小会根据执行的任务数动态分配 */
        ExecutorService threadPool_2 = Executors.newCachedThreadPool();
        /** 创建单个线程的线程池，如果当前线程在执行任务时突然中断，则会创建一个新的线程替代它继续执行任务 */
        ExecutorService threadPool_3 = Executors.newSingleThreadExecutor();

        for (int i = 1; i < 5; i++) {
            final int taskID = i;
            threadPool_3.execute(new Runnable() {
                public void run() {
                    for (int i = 1; i < 5; i++) {
                        try {
                            Thread.sleep(1000);// 为了测试出效果，让每次任务执行都需要一定时间
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        logger.info(Thread.currentThread().getName() + "：第" + taskID + "次任务的第" + i + "次执行");
                    }
                }
            });
        }
        threadPool_1.shutdown();// 任务执行完毕，关闭线程池
        threadPool_2.shutdown();// 任务执行完毕，关闭线程池
        threadPool_3.shutdown();// 任务执行完毕，关闭线程池
    }

    static Random random = new Random();

}

/**
 * 可暂停的线程池
 *
 */
class PausableThreadPoolExecutor extends ThreadPoolExecutor {
    private boolean isPaused;
    private ReentrantLock pauseLock = new ReentrantLock();// ['rɪː'entrənt]
    private Condition unpaused = pauseLock.newCondition();

    public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    protected void beforeExecute(Thread t, Runnable r) {
        super.beforeExecute(t, r);
        pauseLock.lock();
        try {
            while (isPaused) {
                unpaused.await();
            }
        } catch (InterruptedException ie) {
            t.interrupt();
        } finally {
            pauseLock.unlock();
        }
    }

    public void pause() {
        pauseLock.lock();
        try {
            isPaused = true;
        } finally {
            pauseLock.unlock();
        }
    }

    public void resume() {
        pauseLock.lock();
        try {
            isPaused = false;
            unpaused.signalAll();
        } finally {
            pauseLock.unlock();
        }
    }
}
