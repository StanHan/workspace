package demo.java.util.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;


/**
 * 一个计数信号量。从概念上讲，信号量维护了一个许可集。 Semaphore 只对可用许可的号码进行计数，并采取相应的行动。 拿到信号量的线程可以进入代码，否则就等待。通过acquire()和release()获取和释放访问许可。
 *
 */
public class SemaphoreDemo {

    public static void main(String[] args) {
        testSpeedLimiter2();
    }

    /**
     * 只允许5个线程同时进入执行acquire()和release()之间的代码
     */
    static void testThreadLimit() {
        // 线程池
        ExecutorService exec = Executors.newCachedThreadPool();
        // 只能5个线程同时访问
        final Semaphore semaphore = new Semaphore(5);
        // 模拟20个客户端访问
        for (int index = 0; index < 20; index++) {
            final int NO = index;
            Runnable run = new Runnable() {
                public void run() {
                    try {
                        // 获取许可
                        semaphore.acquire();
                        System.out.println("Accessing: " + NO);
                        Thread.sleep((long) (Math.random() * 10000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // 访问完后，释放 ，如果屏蔽下面的语句，则在控制台只能打印5条记录，之后线程一直阻塞
                        semaphore.release();
                    }
                }
            };
            exec.execute(run);
        }
        // 退出线程池
        exec.shutdown();
    }

    /**
     * 限速
     */
    static void testSpeedLimiter2() {
        SpeedLimiter<Object> speedLimiter = new SpeedLimiter<>(2, 1000, new Object());
        for (int i=0;i<300;i++) {
            speedLimiter.consume();
            System.out.println(i);
            speedLimiter.returnResource();
        }
        
    }
    
    /**
     * 限速
     */
    static void testSpeedLimiter() {
        String resource = "myresource";
        SpeedLimiter<String> speedLimiter = new SpeedLimiter<String>(10, 1000, resource);
        
        TestSpeedLimitedThread<String> testThread1 = new TestSpeedLimitedThread<String>(speedLimiter);
        testThread1.start();
        TestSpeedLimitedThread<String> testThread2 = new TestSpeedLimitedThread<String>(speedLimiter);
        testThread2.start();
        TestSpeedLimitedThread<String> testThread3 = new TestSpeedLimitedThread<String>(speedLimiter);
        testThread3.start();
    }
}

/**
 * 限速器
 *
 * @param <T>
 */

class TestSpeedLimitedThread<T> extends Thread {
    SpeedLimiter<T> limiter = null;

    public TestSpeedLimitedThread(SpeedLimiter<T> limiter) {
        this.limiter = limiter;
    }

    public void run() {
        long i = 0;
        while (true) {
            T tmpResource = limiter.consume();
            System.out.println("thread id: " + currentThread().getName() + " : " + (i + 1) + ": 已经获取到资源：" + tmpResource);
            i++;
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            limiter.returnResource();
        }
    }
}
