package demo.java.util.concurrent;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class FutureTaskDemo {

    public static void main(String[] args) {
        testFutureTask();
    }

    static void test() {
        ExecutorService exec = Executors.newCachedThreadPool();

        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {// FutrueTask的构造参数是一个Callable接口
            @Override
            public String call() throws Exception {
                return Thread.currentThread().getName();// 这里可以是一个异步操作
            }
        });

        try {
            exec.execute(task);// FutureTask实际上也是一个线程
            String result = task.get();// 取得异步计算的结果，如果没有返回，就会一直阻塞等待
            System.out.printf("get:%s%n", result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    static void testFutureTask() {

        Callable<Integer> callable = new Callable<Integer>() {
            public Integer call() throws Exception {
                return new Random().nextInt(100);
            }
        };

        FutureTask<Integer> future = new FutureTask<Integer>(callable);

        new Thread(future).start();

        try {
            Thread.sleep(2000);// 可能做一些事情
            System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

}

class RunnableFutureImpl<V> implements RunnableFuture<V> {

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
