package demo.java.util.concurrent;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Exchanger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 可以在pair中对元素进行配对和交换的线程的同步点。每个线程将条目上的某个方法呈现给 exchange 方法，与伙伴线程进行匹配，并且在返回时接收其伙伴的对象。
 * 
 * Exchanger 可能被视为 SynchronousQueue的双向形式。Exchanger 可能在应用程序（比如遗传算法和管道设计）中很有用。
 * 
 * Exchanger在特定的使用场景比较有用（两个伙伴线程之间的数据交互）
 *
 */
public class ExchangerDemo {

    private static Logger logger = LoggerFactory.getLogger(ExchangerDemo.class);

    static Random random = new Random();

    public static void main(String[] args) {
        final Exchanger<ArrayList<Integer>> exchanger = new Exchanger<ArrayList<Integer>>();
        final ArrayList<Integer> buff1 = new ArrayList<Integer>(10);
        final ArrayList<Integer> buff2 = new ArrayList<Integer>(10);

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Integer> buff = buff1;
                try {
                    while (true) {
                        if (buff.size() >= 10) {
                            buff = exchanger.exchange(buff);// 开始跟另外一个线程交互数据
                            logger.info("exchange buff1");
                            buff.clear();
                        }
                        buff.add(random.nextInt(10000));
                        Thread.sleep(random.nextInt(1000));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Integer> buff = buff2;
                while (true) {
                    try {
                        for (Integer i : buff) {
                            System.out.println(i);
                        }
                        Thread.sleep(1000);
                        buff = exchanger.exchange(buff);// 开始跟另外一个线程交换数据
                        logger.info("exchange buff2");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
}
