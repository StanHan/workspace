package demo.java.util.concurrent;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * <li>add 增加一个元索 如果队列已满，则抛出一个IIIegaISlabEepeplian异常
 * <li>remove 移除并返回队列头部的元素 如果队列为空，则抛出一个NoSuchElementException异常
 * <li>element 返回队列头部的元素 如果队列为空，则抛出一个NoSuchElementException异常
 * <li>offer 添加一个元素并返回true 如果队列已满，则返回false
 * <li>poll 移除并返问队列头部的元素 如果队列为空，则返回null
 * <li>peek 返回队列头部的元素 如果队列为空，则返回null
 * <li>put 添加一个元素 如果队列满，则阻塞
 * <li>take 移除并返回队列头部的元素 如果队列为空，则阻塞
 *
 */
public class BlockingQueueDemo {

    public static void main(String[] args) {
        BlockingQueue<Object> blockingQueue = null;
        Scanner in = new Scanner(System.in);
    }

    /**
     * 默认情况下，LinkedBlockingQueue的容量是没有上限的（在不指定时容量为Integer.MAX_VALUE），但是也可以选择指定其最大容量，它是基于链表的队列，此队列按 FIFO（先进先出）排序元素。
     */
    static void testLinkedBlockingQueue() {
        LinkedBlockingQueue<Object> blockingQueue = new LinkedBlockingQueue<Object>();
    }

    /**
     * ArrayBlockingQueue在构造时需要指定容量， 并可以选择是否需要公平性，如果公平参数被设置true，等待时间最长的线程会优先得到处理（其实就是通过将ReentrantLock设置为true来
     * 达到这种公平性的：即等待时间最长的线程会先操作）。通常，公平性会使你在性能上付出代价，只有在的确非常需要的时候再使用它。 它是基于数组的阻塞循环队 列，此队列按 FIFO（先进先出）原则对元素进行排序。
     */
    static void testArrayBlockingQueue() {
        ArrayBlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<Object>(10);
    }

    /**
     * PriorityBlockingQueue是一个带优先级的 队列，而不是先进先出队列。元素按优先级顺序被移除，该队列也没有上限（看了一下源码，PriorityBlockingQueue是对
     * PriorityQueue的再次包装，是基于堆数据结构的，而PriorityQueue是没有容量限制的，与ArrayList一样，所以在优先阻塞
     * 队列上put时是不会受阻的。虽然此队列逻辑上是无界的，但是由于资源被耗尽，所以试图执行添加操作可能会导致
     * OutOfMemoryError），但是如果队列为空，那么取元素的操作take就会阻塞，所以它的检索操作take是受阻的。另外，往入该队列中的元 素要具有比较能力。
     */
    static void testPriorityBlockingQueue() {
        PriorityBlockingQueue<Object> blockingQueue = new PriorityBlockingQueue<Object>();
    }

}

class TestBlockingQueue {

    public static void main(String[] args) {
        final BlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(3);
        final Random random = new Random();

        class Producer implements Runnable {
            @Override
            public void run() {
                while (true) {
                    try {
                        int i = random.nextInt(100);
                        queue.put(i);// 当队列达到容量时候，会自动阻塞的
                        if (queue.size() == 3) {
                            System.out.println("full");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        class Consumer implements Runnable {
            @Override
            public void run() {
                while (true) {
                    try {
                        queue.take();// 当队列为空时，也会自动阻塞
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        new Thread(new Producer()).start();
        new Thread(new Consumer()).start();
    }

}

/**
 * DelayQueue（基于PriorityQueue来实现的）是一个存放Delayed 元素的无界阻塞队列，只有在延迟期满时才能从中提取元素。该队列的头部是延迟期满后保存时间最长的 Delayed
 * 元素。如果延迟都还没有期满，则队列没有头部，并且poll将返回null。当一个元素的 getDelay(TimeUnit.NANOSECONDS)
 * 方法返回一个小于或等于零的值时，则出现期满，poll就以移除这个元素了。此队列不允许使用 null 元素。
 */
class DelayQueueDemo {

    private class Stadium implements Delayed {
        long trigger;

        public Stadium(long i) {
            trigger = System.currentTimeMillis() + i;
        }

        @Override
        public long getDelay(TimeUnit arg0) {
            long n = trigger - System.currentTimeMillis();
            return n;
        }

        @Override
        public int compareTo(Delayed arg0) {
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) - arg0.getDelay(TimeUnit.MILLISECONDS));
        }

        public long getTriggerTime() {
            return trigger;
        }

    }

    public static void main(String[] args) throws Exception {
        Random random = new Random();
        DelayQueue<Stadium> queue = new DelayQueue<Stadium>();
        DelayQueueDemo t = new DelayQueueDemo();

        for (int i = 0; i < 5; i++) {
            queue.add(t.new Stadium(random.nextInt(30000)));
        }
        Thread.sleep(2000);

        while (true) {
            Stadium s = queue.take();// 延时时间未到就一直等待
            if (s != null) {
                System.out.println(System.currentTimeMillis() - s.getTriggerTime());// 基本上是等于0
            }
            if (queue.size() == 0)
                break;
        }
    }
}

/**
 * SynchronousQueue主要用于单个元素在多线程之间的传递 。 “一种阻塞队列，其中每个插入操作必须等待另一个线程的对应移除操作 ，反之亦然。同步队列没有任何内部容量，甚至连一个队列的容量都没有。不能在同步队列上进行
 * peek，因为仅在试图要移除元素时，该元素才存在；除非另一个线程试图移除某个元素，否则也不能（使用任何方法）插入元素；也不能迭代队列，因为其中没有元素可用于迭代。队列的头
 * 是尝试添加到队列中的首个已排队插入线程的元素；如果没有这样的已排队线程，则没有可用于移除的元素并且 poll() 将会返回 null。对于其他 Collection 方法（例如 contains），SynchronousQueue
 * 作为一个空 collection。此队列不允许 null 元素。 同步队列类似于 CSP 和 Ada 中使用的 rendezvous
 * 信道。它非常适合于传递性设计，在这种设计中，在一个线程中运行的对象要将某些信息、事件或任务传递给在另一个线程中运行的对象，它就必须与该对象同步。 “它给我们提供了在线程之间交换单一元素的极轻量级方法
 */
class SynchronousQueueDemo {

    class Producer implements Runnable {
        private BlockingQueue<String> queue;
        List<String> objects = Arrays.asList("one", "two", "three");

        public Producer(BlockingQueue<String> q) {
            this.queue = q;
        }

        @Override
        public void run() {
            try {
                for (String s : objects) {
                    queue.put(s);// 产生数据放入队列中
                    System.out.printf("put:%s%n", s);
                }
                queue.put("Done");// 已完成的标志
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class Consumer implements Runnable {
        private BlockingQueue<String> queue;

        public Consumer(BlockingQueue<String> q) {
            this.queue = q;
        }

        @Override
        public void run() {
            String obj = null;
            try {
                while (!((obj = queue.take()).equals("Done"))) {
                    System.out.println(obj);// 从队列中读取对象
                    Thread.sleep(3000); // 故意sleep，证明Producer是put不进去的
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        BlockingQueue<String> q = new SynchronousQueue<String>();
        SynchronousQueueDemo t = new SynchronousQueueDemo();
        new Thread(t.new Producer(q)).start();
        new Thread(t.new Consumer(q)).start();
    }

}
