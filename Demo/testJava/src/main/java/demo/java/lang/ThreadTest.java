package demo.java.lang;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Timer;
import java.util.TimerTask;


public class ThreadTest  {

	public static void main(String[] args) throws InterruptedException {
//		testJoin();
//		testWaitNotify();
//		testInterrupte();
		testUncaughtExceptionHandler();
	}

	public static void testWaitNotify(){
		String a = "A";
		new Thread(new PrimeRun(a)).start();
		new Thread(new PrimeRun(a)).start();
	}
	
	public static void testYield(){
		
	}

	public static void testUncaughtExceptionHandler(){
		ThreadGroup group = new MachineGroup();
		
		UncaughtExceptionHandler handler = new MachineHandler("DefaultUncaughtExceptionHandler");
		
		Machine_E.setDefaultUncaughtExceptionHandler(handler);
		
		Machine_E machine1 = new Machine_E(group,"machine1");
		Machine_E machine2 = new Machine_E(group,"machine2");
		
		UncaughtExceptionHandler machineHandler = new MachineHandler("machineHandler");
		machine2.setUncaughtExceptionHandler(machineHandler);
		
		machine1.start();
		machine2.start();
	}
	
	public static void testInterrupte(){
		Machine machine = new Machine();
//		Thread.setDefaultUncaughtExceptionHandler(null);
//		machine.setUncaughtExceptionHandler(null);
		machine.start();
	}
	
	public static void testJoin() throws InterruptedException {

		// Delay, in milliseconds before we interrupt MessageLoop thread .
		long patience = 2000 ;

		threadMessage("Starting MessageLoop thread");
		long startTime = System.currentTimeMillis();
		Thread thread_MessageLoop = new Thread(new MessageLoop());
		thread_MessageLoop.start();

		threadMessage("Waiting for MessageLoop thread to finish");
		// loop until MessageLoop thread exits
		while (thread_MessageLoop.isAlive()) {
			threadMessage("Still waiting...");
			// Wait maximum of 1 second for MessageLoop thread to finish.
			thread_MessageLoop.join(10000);
			if (((System.currentTimeMillis() - startTime) > patience) && thread_MessageLoop.isAlive()) {
				threadMessage("Tired of waiting!");
				thread_MessageLoop.interrupt();
				// Shouldn't be long now -- wait indefinitely
				thread_MessageLoop.join();
			}
		}
		threadMessage("Finally!");
	}
	
	/**
	 * Display a message, preceded by the name of the current thread
	 * 
	 * @param message
	 */
	public static void threadMessage(String message) {
		String threadName = Thread.currentThread().getName();
		System.out.format("%s: %s%n", threadName, message);
	}
}


class PrimeRun implements Runnable {
	private String minPrime;

	PrimeRun(String minPrime) {
		this.minPrime = minPrime;
	}

	public void run() {
		for (int i = 1; i <= 100; i++) {
			synchronized (minPrime) {
				System.out.println(Thread.currentThread().getName() + ": " + i);
				if (i % 10 != 0) {
					minPrime.notify();
				} else {
					try {
						minPrime.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}

class StoppableTask extends Thread {
	private volatile boolean pleaseStop;

	public void run() {
		while (!pleaseStop) {
			// do some stuff...
		}
	}

	public void tellMeToStop() {
		pleaseStop = true;
	}
}

class MessageLoop implements Runnable {
	public void run() {
		String importantInfo[] = { "Mares eat oats", "Does eat oats", "Little lambs eat ivy",
				"A kid will eat ivy too" };
		try {
			for (int i = 0; i < importantInfo.length; i++) {
				// Pause for 4 seconds
				Thread.sleep(1500);// Pausing Execution with Sleep
				// Print a message
				ThreadTest.threadMessage(importantInfo[i]);

				if (Thread.interrupted()) {
					throw new InterruptedException();
				}
			}
		} catch (InterruptedException e) {
			ThreadTest.threadMessage("I wasn't done!");
		}
	}
	
	
}


class Machine extends Thread {

	private int a = 0;
	
	private Timer timer = new Timer(true);
	
	public synchronized void resize(){
		a = 0;
	}
	
	@Override
	public void run() {
		while(true){
			synchronized(this){
				while( a > 30){
					final Thread thread = Thread.currentThread();
					
					timer.schedule(new TimerTask() {
						@Override
						public void run() {
							System.out.println("Thread Name: "+ thread.getName() +" has waited 3 seconds.");
							thread.interrupt();
						}
					}, 3000);
					
					try {
						this.wait();
					} catch (InterruptedException e) {
						System.err.println("Thread Name: "+ thread.getName() +" has interruped.");
						return;
					}
				}
				
				a++;
				System.out.println( "a = "+ a);
			}
		}
	}
}


class Machine_E extends Thread {

	public Machine_E(ThreadGroup threadGroup ,String name){
		super(threadGroup, name);
	}
	
	@Override
	public void run() {
		int a = 1/0;
	}
}

class MachineGroup extends ThreadGroup{

	public MachineGroup(String name) {
		super(name);
	}
	
	public MachineGroup(){
		super("MachineGroup");
	}

	@Override
	public void uncaughtException(Thread t, Throwable e) {

		System.out.println(getName() +" catch a exception from "+ t.getName());
		super.uncaughtException(t, e);
	}
	
}

class MachineHandler implements Thread.UncaughtExceptionHandler{

	private String name;
	
	public MachineHandler(String name){
		this.name = name;
	}
	
	@Override
	public void uncaughtException(Thread t, Throwable e) {
		System.out.println( name +" catch a exception from "+ t.getName());
		
	}
	
}