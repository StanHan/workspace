package demo.java.util.concurrent;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 一个同步辅助类，它允许一组线程互相等待，直到到达某个公共屏障点 (common barrier point)。在涉及一组固定大小的线程的程序中，这些线程必须不时地互相等待，此时 CyclicBarrier 很有用。
 * 因为该barrier 在释放等待线程后可以重用，所以称它为循环 的 barrier。
 * 
 * CyclicBarrier 支持一个可选的 Runnable命令，在一组线程中的最后一个线程到达之后（但在释放所有线程之前），该命令只在每个屏障点运行一次。若在继续所有参与线程之前更新共享状态，此屏障操作 很有用。
 *
 * CyclicBarrier就是一个栅栏，等待所有线程到达后再执行相关的操作。barrier 在释放等待线程后可以重用。
 */
public class CyclicBarrierDemo {
    static ExecutorService exec = Executors.newCachedThreadPool();
    static Random random = new Random();

    public static void main(String[] args) {
        test();
    }

    static void test() {
        final CyclicBarrier barrier = new CyclicBarrier(4, new Runnable() {
            @Override
            public void run() {
                System.out.println("大家都到齐了，开始happy去");
            }
        });

        for (int i = 0; i < 4; i++) {
            exec.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(random.nextInt(9000));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(Thread.currentThread().getName() + "到了，其他哥们呢");
                    try {
                        barrier.await();// 等待其他哥们
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        exec.shutdown();
    }
}
