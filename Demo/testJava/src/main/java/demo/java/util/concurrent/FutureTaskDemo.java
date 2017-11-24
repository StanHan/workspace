package demo.java.util.concurrent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * FutureTask一个可取消的异步计算，FutureTask 实现了Future的基本方法，提供start、cancel操作，可以查询计算是否已经完成，并且可以获取计算的结果。
 * 结果只可以在计算完成之后获取，get方法会阻塞当计算没有完成的时候，一旦计算已经完成， 那么计算就不能再次启动或是取消。
 * 
 * @author hanjy
 *
 */
public class FutureTaskDemo {

    public static void main(String[] args) throws InterruptedException {
        testGetResult();
    }

    public static final Map<Integer, FutureTask<String>> map = new HashMap<>();
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private static Random random = new Random();

    /**
     * 异步获取任务执行结果
     * 
     * @throws InterruptedException
     */
    static void testGetResult() throws InterruptedException {
        // 创建10个任务并执行，将任务存到Map里
        for (int i = 0; i < 20; i++) {
            FutureTask<String> task = new FutureTask<String>(new Callable<String>() {// FutrueTask的构造参数是一个Callable接口
                @Override
                public String call() throws Exception {
                    int tmp = random.nextInt(1000);
                    Thread.sleep(tmp);
                    if (tmp % 2 == 1) {
                        throw new Exception("抛个异常试试");
                    }
                    return Thread.currentThread().getName() + ":" + System.currentTimeMillis();// 这里可以是一个异步操作
                }
            });
            executorService.submit(task);
            map.put(i, task);
        }
        // 主任务定期查看任务执行结果
        while (true) {
            // System.out.println(map.size());
            if (map.isEmpty()) {
                continue;
            }
            Integer[] array = map.keySet().stream().toArray(Integer[]::new);
            for (Integer taskId : array) {
                FutureTask<String> futureTask = map.get(taskId);
                if (futureTask.isDone()) {
                    map.remove(taskId);
                    try {
                        String result = futureTask.get();
                        System.out.println(taskId + " result is :" + result);
                    } catch (InterruptedException e) {
                        System.out.println("中断异常:"+e.getMessage());
                    } catch (ExecutionException e) {
                        System.out.println("执行异常:"+e.getMessage());
                    }
                }
            }
            Thread.sleep(50);
            if (map.size() == 0) {
                break;
            }
        }
        executorService.shutdown();
        System.out.println("over.");
    }

    static void test() {

        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {// FutrueTask的构造参数是一个Callable接口
            @Override
            public String call() throws Exception {
                return Thread.currentThread().getName();// 这里可以是一个异步操作
            }
        });

        try {
            executorService.execute(task);// FutureTask实际上也是一个线程
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
