package demo.java.util.concurrent;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorDemo {

    static Random random = new Random();
    public static final Logger logger = LoggerFactory.getLogger(ExecutorDemo.class);

    public static void main(String[] args) {
        // TODO Auto-generated method stub

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
                    System.out.println("hello.");
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
        System.out.println("over.");
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
                        System.out.println(Thread.currentThread().getName() + "：第" + taskID + "次任务的第" + i + "次执行");
                    }
                }
            });
        }
        threadPool_1.shutdown();// 任务执行完毕，关闭线程池
        threadPool_2.shutdown();// 任务执行完毕，关闭线程池
        threadPool_3.shutdown();// 任务执行完毕，关闭线程池
    }

    static void testThreadException() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        System.out.println(Thread.currentThread().getName());
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        // throw new RuntimeException();
                    }
                }
            });
        }
        executorService.shutdown();
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
                            System.out.println(Thread.currentThread().getName());
                            Thread.sleep(random.nextInt(3000));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } finally {
                            System.out.println(Thread.currentThread().getName() + " 结束");
                        }
                    }
                });
            }

            System.out.println("已经开启所有的子线程");
            executorService.shutdown();
            System.out.println("shutdown()：启动一次顺序关闭，执行以前提交的任务，但不接受新任务。");
            while (true) {
                if (executorService.isTerminated()) {
                    System.out.println("所有的子线程都结束了！");
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("主线程结束");
        }
    }

    /**
     * 任务调度线程池ScheduledThreadPoolExecutor
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
                System.out.println("抛异常咯");
                throw new RuntimeException();
            }
        }, 2000, 5000, TimeUnit.MILLISECONDS);

        exec.scheduleAtFixedRate(new Runnable() {// 每隔一段时间打印系统时间，证明两者是互不影响的
            @Override
            public void run() {
                System.out.println(System.nanoTime());
            }
        }, 1000, 2000, TimeUnit.MILLISECONDS);
    }

    /**
     * 创建线程以及线程池时候要指定与业务相关的名字，以便于追溯问题
     */
    public void namedThreadPool() {
        ThreadPoolExecutor executorOne = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES, new LinkedBlockingQueue<>());

        ThreadPoolExecutor namedThreadPool = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>(), new NamedThreadFactory("我是有名字的线程池"));
    }

}

/**
 * 命名线程工厂
 *
 */
class NamedThreadFactory implements ThreadFactory {
    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    NamedThreadFactory(String name) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        if (null == name || name.isEmpty()) {
            name = "pool";
        }

        namePrefix = name + "-" + poolNumber.getAndIncrement() + "-thread-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}

/**
 * 直接执行
 *
 */
class DirectExecutor implements Executor {
    public void execute(Runnable r) {
        r.run();
    }
}

class ThreadPerTaskExecutor implements Executor {
    public void execute(Runnable r) {
        new Thread(r).start();
    }
}

class CustomThreadPoolExecutor extends ThreadPoolExecutor {

    public CustomThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
            BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    static class CustomTask<V> implements RunnableFuture<V> {

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isCancelled() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public boolean isDone() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public V get() throws InterruptedException, ExecutionException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub

        }
    }

}

class SerialExecutor implements Executor {
    final Queue<Runnable> tasks = new ArrayDeque<Runnable>();
    final Executor executor;
    Runnable active;

    SerialExecutor(Executor executor) {
        this.executor = executor;
    }

    public synchronized void execute(final Runnable r) {
        tasks.offer(new Runnable() {
            public void run() {
                try {
                    r.run();
                } finally {
                    scheduleNext();
                }
            }
        });
        if (active == null) {
            scheduleNext();
        }
    }

    protected synchronized void scheduleNext() {
        if ((active = tasks.poll()) != null) {
            executor.execute(active);
        }
    }
}

interface ArchiveSearcher {
    String search(String target);
}

class App {
    ExecutorService executor = null;
    ArchiveSearcher searcher = null;

    void showSearch(final String target) throws InterruptedException {
        Future<String> future = executor.submit(new Callable<String>() {
            public String call() {
                return searcher.search(target);
            }
        });

        Thread.sleep(500); // do other things while searching
        try {
            System.out.println(future.get());// use future
        } catch (ExecutionException ex) {
            executor.shutdown();
            return;
        } finally {
            executor.shutdown();
        }
    }
}

class Driver { // ...
    private final int N = 5;

    void main() throws InterruptedException {
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(N);

        for (int i = 0; i < N; ++i) // create and start threads
        {
            new Thread(new Worker(startSignal, doneSignal)).start();
        }

        Thread.sleep(500); // don't let run yet
        startSignal.countDown(); // let all threads proceed
        Thread.sleep(500);
        doneSignal.await(); // wait for all to finish
    }
}

class Worker implements Runnable {
    private final CountDownLatch startSignal;
    private final CountDownLatch doneSignal;

    Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
        this.startSignal = startSignal;
        this.doneSignal = doneSignal;
    }

    public void run() {
        try {
            startSignal.await();
            doWork();
            doneSignal.countDown();
        } catch (InterruptedException ex) {
            return;
        }
    }

    void doWork() {
    }
}

class Driver2 {
    void main() throws InterruptedException {
        CountDownLatch doneSignal = new CountDownLatch(6);
        Executor executor = Executors.newFixedThreadPool(1);

        for (int i = 0; i < 10; ++i) { // create and start threads
            executor.execute(new WorkerRunnable(doneSignal, i));
        }
        doneSignal.await(); // wait for all to finish
    }
}

class WorkerRunnable implements Runnable {
    private final CountDownLatch doneSignal;
    private final int i;

    WorkerRunnable(CountDownLatch doneSignal, int i) {
        this.doneSignal = doneSignal;
        this.i = i;
    }

    public void run() {
        try {
            doWork(i);
            doneSignal.countDown();
        } catch (InterruptedException ex) {
        } // return;
    }

    void doWork(int i) throws InterruptedException {
    }
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
