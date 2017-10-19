package demo.java.util.concurrent;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author hanjy
 *
 */
public class CallableDemo {

    public static void main(String[] args) {
        testCallableAndFuture();
    }

    /**
     * Callable接口支持返回执行结果，此时需要调用FutureTask.get()方法实现，此方法会阻塞主线程直到获取‘将来’结果；当不调用此方法时，主线程不会阻塞！
     */
    static void testCallableAndFuture() {

        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Callable<String> callable = new Callable<String>() {
            public String call() throws Exception {
                Random random = new Random();
                String threadName = Thread.currentThread().getName();
                Thread.sleep(2000);
                return threadName + " : " + random.nextInt(100);
            }
        };

        Future<String> future = executorService.submit(callable);

        try {
            Thread.sleep(500);// 可能做一些事情
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } finally {
            System.out.println("会阻塞主线程吗?");
            executorService.shutdown();
        }
    }

}
