package demo.java.util;

import java.util.Timer;
import java.util.TimerTask;

public class TimerDemo extends Thread{

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

	public static void main(String[] args) {
		TimerDemo demo = new TimerDemo();
		demo.start();
	}

}
