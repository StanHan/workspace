package demo.java.util.concurrent;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
public class ScheduledThreadPoolExecutorDemo {

    public static void main(String[] args) {
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

}
