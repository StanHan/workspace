package demo.java.util;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 定时器
 * @author hanjy
 *
 */
public class TimerDemo {
    
    public static void main(String[] args) {
        demo2();
    }
    
    /**
     * 不会被异常终止的定时任务
     */
    static void demo2() {
        Timer timer = new Timer();
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                executorService.submit(new Runnable() {
                    Random random = new Random();
                    int count =0;
                    @Override
                    public void run() {
                        int limit = random.nextInt(5);
                        System.out.println("重新开始：limit = " + limit);
                        while(true) {
                            if(limit == count) {
                                throw new RuntimeException();
                            }
                            count ++;
                            System.out.println(Thread.currentThread() + "哈哈！" + count);
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 1000, 10000);
    }
	
	
	static void demo1() {
	    TimerDemo1 demo = new TimerDemo1();
        demo.start();
	}

	

}

class TimerDemo1 extends Thread{
    private int a;
    private static int count;
    
    @Override
    public synchronized void start() {
        super.start();
        
        Timer timer = new Timer(true);//把与timer关联的现成设置为守护线程
        
        TimerTask task = new TimerTask(){//匿名类
            @Override
            public void run() {
                while (true) {
                    reset();
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        
        timer.schedule(task, 10, 500);
    }

    public void reset(){
        a = 0 ;
    }
    
    @Override
    public void run() {
        while(true){
            System.out.println(getName()+" : a ="+a++);
            if(count ++ == 100000000){
                break;
            }
            yield();
        }
    }
}
