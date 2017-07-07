package demo.java.lang;

import java.util.concurrent.CountDownLatch;

public class Driver {

	private final static int N = 8;

	public static void main(String[] args) {

		CountDownLatch startSignal = new CountDownLatch(1);
		CountDownLatch doneSignal = new CountDownLatch(N);

		for (int i = 0; i < N; ++i) {
			new Thread(new Worker(startSignal, doneSignal)).start();// create and start threads
		}

		doSomethingElse(); // don't let run yet
		startSignal.countDown(); // let all threads proceed
		doSomethingElse();
		try {
			doneSignal.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // wait for all to finish
	}

	public static void doSomethingElse() {

		System.out.println("------------------");
	}

}

class Worker implements Runnable {

	private final CountDownLatch startSignal;
	private final CountDownLatch doneSignal;

	Worker(CountDownLatch startSignal, CountDownLatch doneSignal) {
		this.startSignal = startSignal;
		this.doneSignal = doneSignal;
	}

	public void run() {
		try {
			startSignal.await();
			doWork();
			doneSignal.countDown();
		} catch (InterruptedException ex) {// return;

		} // return;
	}

	void doWork() {
		System.out.println("hello Thread!!");
	}

}
