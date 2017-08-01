package demo.java.util.concurrent;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author hanjy
 *
 */
public class QueueDemo {

    
    
}

class Producer implements Runnable {
	private final BlockingQueue<Object> queue;

	Producer(BlockingQueue<Object> q) {
		queue = q;
	}

	public void run() {
		try {
			while (true) {
				queue.put(produce());
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	Object produce() {
		return new Object();
	}
}

class Consumer implements Runnable {
	private final BlockingQueue<Object> queue;

	Consumer(BlockingQueue<Object> q) {
		queue = q;
	}

	public void run() {
		try {
			while (true) {
				consume(queue.take());
			}
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	void consume(Object x) {
	}
}

class Setup {
	void main() {
		BlockingQueue<Object> blockingQueue = new ArrayBlockingQueue<Object>(2);
		Producer p = new Producer(blockingQueue);
		Consumer c1 = new Consumer(blockingQueue);
		Consumer c2 = new Consumer(blockingQueue);
		new Thread(p).start();
		new Thread(c1).start();
		new Thread(c2).start();
	}
}
