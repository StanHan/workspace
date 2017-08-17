package demo.java.lang.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

public class Driver2 {
	private static final int N = 1000;

	void main() throws InterruptedException {
		CountDownLatch doneSignal = new CountDownLatch(N);
		Executor e = null;

		for (int i = 0; i < N; ++i) // create and start threads
			e.execute(new WorkerRunnable(doneSignal, i));

		doneSignal.await(); // wait for all to finish
	}
}

class WorkerRunnable implements Runnable {
	private final CountDownLatch doneSignal;
	private final int i;

	WorkerRunnable(CountDownLatch doneSignal, int i) {
		this.doneSignal = doneSignal;
		this.i = i;
	}

	public void run() {
		doWork(i);
		doneSignal.countDown();
	}

	void doWork(int i) {

	}
}
