package demo.java.util.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPoolTest {

	public static void main(String[] args) {
		testScheduledThreadPool();
	   
	}
	
	static void demo() {
	    ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            
            @Override
            public void run() {
                int i = 0;
                while(i<10){
                    System.out.println("hello.");
                    i++;
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });
        singleThreadExecutor.shutdown();
        System.out.println("over.");
	}

	/**
	 * 如果抛出异常，则线程WAITING (parking)，该定时任务不会再被调度
	 */
	static void testScheduledThreadPool() {
		/** 创建一个可安排在给定延迟后运行命令或者定期地执行的线程池。效果类似于Timer定时器 */
		ScheduledExecutorService schedulePool = Executors.newScheduledThreadPool(2);
		// 5秒后执行任务
		schedulePool.schedule(new Runnable() {
			public void run() {
				System.out.println(Thread.currentThread().getName()+"爆炸");
			}
		}, 1, TimeUnit.SECONDS);
		
		
		// 5秒后执行任务，以后每2秒执行一次
		schedulePool.scheduleAtFixedRate(new Runnable() {
		    int count = 0 ;
			@Override
			public void run() {
			    count++;
				System.out.println(Thread.currentThread().getName()+"爆炸 "+count);
				if(count == 3) {//抛出异常，线程 WAITING (parking)
				    throw new RuntimeException();
				}
			}
		}, 1, 2, TimeUnit.SECONDS);
		
	}

	public static void testFixedThreadPool() {
		/** 可重用固定线程集合的线程池，以共享的无界队列方式来运行这些线程。创建可以容纳3个线程的线程池 */
		ExecutorService threadPool_1 = Executors.newFixedThreadPool(3);
		/** 可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们,线程池的大小会根据执行的任务数动态分配 */
		ExecutorService threadPool_2 = Executors.newCachedThreadPool();
		/** 创建单个线程的线程池，如果当前线程在执行任务时突然中断，则会创建一个新的线程替代它继续执行任务 */
		ExecutorService threadPool_3 = Executors.newSingleThreadExecutor();
		
		for (int i = 1; i < 5; i++) {
			final int taskID = i;
			threadPool_3.execute(new Runnable() {
				public void run() {
					for (int i = 1; i < 5; i++) {
						try {
							Thread.sleep(1000);// 为了测试出效果，让每次任务执行都需要一定时间
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						System.out.println(Thread.currentThread().getName() + "：第" + taskID + "次任务的第" + i + "次执行");
					}
				}
			});
		}
		threadPool_1.shutdown();// 任务执行完毕，关闭线程池
		threadPool_2.shutdown();// 任务执行完毕，关闭线程池
		threadPool_3.shutdown();// 任务执行完毕，关闭线程池
	}

}

class PausableThreadPoolExecutor extends ThreadPoolExecutor {
	private boolean isPaused;
	private ReentrantLock pauseLock = new ReentrantLock();// ['rɪː'entrənt]
	private Condition unpaused = pauseLock.newCondition();

	public PausableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
	}

	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		pauseLock.lock();
		try {
			while (isPaused) {
				unpaused.await();
			}
		} catch (InterruptedException ie) {
			t.interrupt();
		} finally {
			pauseLock.unlock();
		}
	}

	public void pause() {
		pauseLock.lock();
		try {
			isPaused = true;
		} finally {
			pauseLock.unlock();
		}
	}

	public void resume() {
		pauseLock.lock();
		try {
			isPaused = false;
			unpaused.signalAll();
		} finally {
			pauseLock.unlock();
		}
	}
}
