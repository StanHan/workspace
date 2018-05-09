package demo.java.util;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 定时器;
 * <li>每一个Timer仅对应唯一一个线程。
 * <li>Timer不保证任务执行的十分精确。
 * <li>Timer类的线程安全的。
 * 
 * Timer有以下几种危险：
 * <li>a. Timer是基于绝对时间的。容易受系统时钟的影响。
 * <li>b. Timer只新建了一个线程来执行所有的TimeTask。所有TimeTask可能会相关影响
 * <li>c. Timer不会捕获TimerTask的异常，只是简单地停止。这样势必会影响其他TimeTask的执行。
 * 
 * Timer有着不少缺陷，如Timer是单线程模式，调度多个周期性任务时，如果某个任务耗时较久就会影响其它任务的调度；如果某个任务出现异常而没有被catch则可能导致唯一的线程死掉而所有任务都不会再被调度。
 * 如果你是使用JDK1.5以上版本，建议用ScheduledThreadPoolExecutor代替Timer。 它基本上解决了上述问题。 它采用相对时间，用线程池来执行TimerTask，会出来TimerTask异常。
 *
 */
public class TimerDemo {

    public static void main(String[] args) {
        testScheduleMoreTask();
    }

    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static Timer timerDaemon = new Timer("TimerDaemon", true);
    private static Timer timer = new Timer("Timer", false);
    private static Random random = new Random();

    static void testScheduleMoreTask() {
        timer.schedule(task2, 0, 500);
        // 任务执行时间长，且固定周期，所以该任务会抢占上面的任务的执行资源
        timer.scheduleAtFixedRate(task1, 1000, 300);
        // 该任务会抛出异常，导致任务终止
        timer.schedule(task4, 6000, 1000);

    }

    /**
     * 线程池执行的任务，抛异常
     */
    static TimerTask task3 = new TimerTask() {
        @Override
        public void run() {
            executorService.submit(new Runnable() {
                int count = 0;

                @Override
                public void run() {
                    int limit = random.nextInt(5);
                    System.out.println("重新开始：limit = " + limit);
                    while (true) {
                        if (limit == count) {
                            throw new RuntimeException();
                        }
                        count++;
                        System.out.println(Thread.currentThread() + "哈哈！" + count);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    };

    /**
     * 循环执行的任务
     */
    static TimerTask task1 = new TimerTask() {
        @Override
        public void run() {
            int i = random.nextInt(5);
            String name = Thread.currentThread().getName();
            while (i > 0) {
                System.out.println(name + ":循环");
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                i--;
            }
        }
    };

    /**
     * 执行一次的任务
     */
    static TimerTask task2 = new TimerTask() {
        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            System.out.println(name + ":执行一次");
        }
    };

    /**
     * 异常任务
     */
    static TimerTask task4 = new TimerTask() {
        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            System.out.println(name);
            throw new RuntimeException("抛出异常咯");
        }
    };

    /**
     * 取消调度
     */
    static void testStopByDaemon() {
        timerDaemon.schedule(task2, 100, 1000);
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 取消调度
     */
    static void testCancel() {
        Timer timer = new Timer("T");
        timer.schedule(task2, 100, 1000);
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("timer.cancel()");
        timer.cancel();
    }

    /**
     * <li>schedule会保证任务的间隔是按照定义的period参数严格执行的，如果某一次调度时间比较长，那么后面的时间会顺延，保证调度间隔都是period
     * <li>scheduleAtFixedRate是严格按照调度时间来的，如果某次调度时间太长了，那么会通过缩短间隔的方式保证下一次调度在预定时间执行
     * 举个栗子：你每个3秒调度一次，那么正常就是0,3,6,9s这样的时间，如果第二次调度花了2s的时间，如果是schedule，就会变成0,3+2,8,11这样的时间，保证间隔，
     * 而scheduleAtFixedRate就会变成0,3+2,6,9，压缩间隔，保证调度时间。
     */
    static void scheduleVSscheduleAtFixedRate() {

    }

    /**
     * .如何终止Timer线程? 默认情况下，创建的timer线程会一直执行，主要有下面四种方式来终止timer线程：
     * 
     * <li>调用timer的cancle方法
     * <li>把timer线程设置成daemon线程，（new Timer(true)创建daemon线程），在jvm里，如果所有用户线程结束，那么守护线程也会被终止，不过这种方法一般不用。
     * <li>当所有任务执行结束后，删除对应timer对象的引用，线程也会被终止。
     * <li>调用System.exit方法终止程序
     */
    static void testStopTimer() {

    }

    /**
     * 不会被异常终止的定时任务
     */
    static void ignoreException() {
        timer.scheduleAtFixedRate(task3, 1000, 10000);
    }

    static void demo1() {
        TimerDemo1 demo = new TimerDemo1();
        demo.start();
    }

}

class TimerDemo1 extends Thread {
    private int a;
    private static int count;

    @Override
    public synchronized void start() {
        super.start();
        Timer timer = new Timer(true);// 把与timer关联的线程设置为守护线程
        TimerTask task = new TimerTask() {// 匿名类
            @Override
            public void run() {
                while (true) {
                    reset();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        timer.schedule(task, 10, 500);
    }

    public void reset() {
        a = 0;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println(getName() + " : a =" + a++);
            if (count++ == 100000000) {
                break;
            }
            yield();
        }
    }
}
