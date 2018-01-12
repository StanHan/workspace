package demo.java.util.concurrent;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;

/**
 * 限速器
 *
 * @param <T>
 */
public class SpeedLimiter<T> {
    // 两个参数maxUsedLimited和timeLimited用于限制访问速度，表示在timeLimited毫秒内，只允许资源resource被使用maxUsedLimited次
    /**
     * 
     * @param times 次数限制
     * @param milliseconds
     * @param resource
     */
    public SpeedLimiter(int times, int milliseconds, T resource) {
        this.times = times;
        this.duration = milliseconds;
        this.object = resource;
        int permits = times * 1000 / milliseconds;
        System.out.println("每秒允许访问次数permits="+permits);
        semaphore = new Semaphore(permits);
    }

    ConcurrentLinkedDeque<Long> concurrentLinkedDeque = new ConcurrentLinkedDeque<Long>();
    T object = null;
    // 下面两个参数用于限制访问速度
    private int times;
    private int duration;// 统计的时间长度为1000毫秒,即1秒

    // 用信号量来限制资源的访问
    private Semaphore semaphore = null;

    public synchronized T consume() {
        if (concurrentLinkedDeque.size() >= times) {// 已经有了使用时间记录
            long tmp = concurrentLinkedDeque.getLast() - concurrentLinkedDeque.getFirst();
            if (tmp < duration) {// 已经超限制了，即次数达到了，但是时间还没到STATISTICS_TIME，说明申请的太快了，还差多少时间就sleep多少时间
                System.out.println("-------------资源访问受限，当前已经在" + tmp + "毫秒内访问了" + concurrentLinkedDeque.size() + "次资源");
                safeSleep(duration - tmp);
            }
            concurrentLinkedDeque.removeFirst();// 总次数到了，每次都把最老的那个时间去掉
        }
        // 先申请再记录时间
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        concurrentLinkedDeque.add(System.currentTimeMillis());
        return object;
    }

    public void returnResource() {
        
        System.out.println("-------------回收一个资源，当前资源可用数：" + semaphore.drainPermits());
        semaphore.release();
    }

    private void safeSleep(long sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
