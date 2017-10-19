package demo.java.util.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * CompletionService实际上可以看做是Executor和BlockingQueue的结合体。
 * CompletionService在接收到要执行的任务时，通过类似BlockingQueue的put和take获得任务执行的结果。
 * CompletionService的一个实现是ExecutorCompletionService，ExecutorCompletionService把具体的计算任务交给Executor完成。
 * 
 * 在Java5的多线程中，可以使用Callable接口来实现具有返回值的线程。使用线程池的submit方法提交Callable任务，利用submit方法返回的Future存根，调用此存根的get方法来获取整个线程池中所有任务的运行结果。
 * 
 * 方法一：如果是自己写代码，应该是自己维护一个Collection保存submit方法返回的Future存根，然后在主线程中遍历这个Collection并调用Future存根的get()方法取到线程的返回值。
 * 
 * 方法二：使用CompletionService类，它整合了Executor和BlockingQueue的功能。你可以将Callable任务提交给它去执行，然后使用类似于队列中的take方法获取线程的返回值。
 * 
 * 使用方法一，自己创建一个集合来保存Future存根并循环调用其返回结果的时候，主线程并不能保证首先获得的是最先完成任务的线程返回值。它只是按加入线程池的顺序返回。因为take方法是阻塞方法，后面的任务完成了，前面的任务却没有完成，主程序就那样等待在那儿，只到前面的完成了，它才知道原来后面的也完成了。
 * 
 * 使用方法二，使用CompletionService来维护处理线程不的返回结果时，主线程总是能够拿到最先完成的任务的返回值，而不管它们加入线程池的顺序。
 * 
 * @author hanjy
 *
 */
public class CompletionServiceDemo {

    public static void main(String[] args) {
        testCompletionService();

    }

    // 方法二，通过CompletionService来实现获取线程池中任务的返回结果
    public void testByCompetion() throws Exception {
        // 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        CompletionService<String> cService = new ExecutorCompletionService<String>(pool);

        // 向里面扔任务
        for (int i = 0; i < TOTAL_TASK; i++) {
            cService.submit(new MyThread("Thread" + i));
        }

        // 检查线程池任务执行结果
        for (int i = 0; i < TOTAL_TASK; i++) {
            Future<String> future = cService.take();
            System.out.println("method2:" + future.get());
        }

        // 关闭线程池
        pool.shutdown();
    }

    // 具有返回值的测试线程
    class MyThread implements Callable<String> {
        private String name;

        public MyThread(String name) {
            this.name = name;
        }

        @Override
        public String call() {
            int sleepTime = new Random().nextInt(1000);
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 返回给调用者的值
            String str = name + " sleep time:" + sleepTime;
            System.out.println(name + " finished...");

            return str;
        }
    }

    private final int POOL_SIZE = 5;
    private final int TOTAL_TASK = 20;

    // 方法一，自己写集合来实现获取线程池中任务的返回结果
    public void testByQueue() throws Exception {
        // 创建线程池
        ExecutorService pool = Executors.newFixedThreadPool(POOL_SIZE);
        BlockingQueue<Future<String>> queue = new LinkedBlockingQueue<Future<String>>();

        // 向里面扔任务
        for (int i = 0; i < TOTAL_TASK; i++) {
            Future<String> future = pool.submit(new MyThread("Thread" + i));
            queue.add(future);
        }

        // 检查线程池任务执行结果
        for (int i = 0; i < TOTAL_TASK; i++) {
            System.out.println("method1:" + queue.take().get());
        }

        // 关闭线程池
        pool.shutdown();
    }

    static Random random = new Random();

    /**
     * 使用任务集中的第一个非 null 结果，而忽略任何遇到异常的任务，并且在第一个任务就绪时取消其他所有任务
     * 
     * @param excutor
     * @param solvers
     * @throws InterruptedException
     */
    public static void solve2(Executor excutor, Collection<Callable<String>> solvers) throws InterruptedException {

        CompletionService<String> completionService = new ExecutorCompletionService<String>(excutor);

        int n = solvers.size();

        List<Future<String>> futureList = new ArrayList<Future<String>>(n);
        String result = null;
        try {
            for (Callable<String> s : solvers) {
                futureList.add(completionService.submit(s));
            }
            for (int i = 0; i < n; ++i) {
                try {
                    String r = completionService.take().get();
                    if (r != null) {
                        result = r;
                        break;
                    }
                } catch (ExecutionException ignore) {
                    System.out.println(ignore);
                }
            }
        } finally {
            for (Future<String> future : futureList) {
                future.cancel(true);
            }
        }

        if (result != null) {
            System.out.println(result);
        }

    }

    public static void solve1(Executor executor, Collection<Callable<String>> callables)
            throws InterruptedException, ExecutionException {

        CompletionService<String> completionService = new ExecutorCompletionService<String>(executor);

        for (Callable<String> callable : callables) {
            completionService.submit(callable);
        }

        int n = callables.size();
        for (int i = 0; i < n; ++i) {
            Future<String> future = completionService.take();
            String result = future.get();
            if (result != null) {
                System.out.println(result);
            }
        }
    }

    /**
     * 提交到CompletionService中的Future是按照完成的顺序排列的
     */
    static void testCompletionService() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Callable<String>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    int r = random.nextInt(10000);
                    Thread.sleep(random.nextInt(r));
                    return Thread.currentThread().getName() + " # Hello World." + r;
                }
            });
        }

        try {
            solve1(executorService, list);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            solve2(executorService, list);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
    }
}
