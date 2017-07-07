package demo.java.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProducerConsumerExample {
	public static void main(String[] args) {
		test1();
		test2();
	}

	public static void test1() {
		Drop drop = new Drop();
		(new Thread(new Producer(drop))).start();
		(new Thread(new Consumer(drop))).start();
	}

	public static void test2() {
		List<Object> container = new ArrayList<Object>();
		new Thread(new Consume(container)).start();
		new Thread(new Product(container)).start();
		new Thread(new Consume(container)).start();
		new Thread(new Product(container)).start();

	}
}

class Producer implements Runnable {
	private Drop drop;

	public Producer(Drop drop) {
		this.drop = drop;
	}

	public void run() {
		String importantInfo[] = { "Mares eat oats", "Does eat oats", "Little lambs eat ivy",
				"A kid will eat ivy too" };
		Random random = new Random();

		for (int i = 0; i < importantInfo.length; i++) {
			drop.put(importantInfo[i]);
			try {
				Thread.sleep(random.nextInt(5000));
			} catch (InterruptedException e) {
			}
		}
		drop.put("DONE");
	}
}

class Consumer implements Runnable {
	private Drop drop;

	public Consumer(Drop drop) {
		this.drop = drop;
	}

	public void run() {
		Random random = new Random();
		for (String message = drop.take(); !message.equals("DONE"); message = drop.take()) {
			try {
				Thread.sleep(random.nextInt(5000));
			} catch (InterruptedException e) {
				return;
			}
			System.out.format("MESSAGE RECEIVED: %s%n", message);
		}
	}
}

class Drop {
	// Message sent from producer to consumer.
	private String message;
	// True if consumer should wait for producer to send message,
	// false if producer should wait for consumer to retrieve message.
	private boolean empty = true;

	public synchronized String take() {
		// Wait until message is available.
		while (empty) {
			try {
				wait();
			} catch (InterruptedException e) {
				return "";
			}
		}
		// Toggle status.
		empty = true;
		// Notify producer that status has changed.
		notifyAll();
		return message;
	}

	public synchronized void put(String message) {
		// Wait until message has been retrieved.
		while (!empty) {
			try {
				wait();
			} catch (InterruptedException e) {
				return;
			}
		}
		// Toggle status.
		empty = false;
		// Store message.
		this.message = message;
		// Notify consumer that status has changed.
		notifyAll();
	}
}

/**
 * 消费线程
 * 
 * @author Stan
 *
 */
class Consume implements Runnable {
	private List<Object> container = null;
	private int count;

	public Consume(List<Object> lst) {
		this.container = lst;
	}

	public void run() {
		while (true) {
			synchronized (container) {
				if (container.size() == 0) {
					try {
						System.out.println(Thread.currentThread().getName() + ": 空了，等待中");
						container.wait();// 放弃锁
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				container.remove(0);
				container.notify();
				System.out.println(Thread.currentThread().getName() + ": 我吃了" + (++count) + "个");
			}
		}
	}

}

/**
 * 生产线程
 * 
 * @author Stan
 *
 */
class Product implements Runnable {
	private List<Object> container = null;
	private int count;

	public Product(List<Object> lst) {
		this.container = lst;
	}

	public void run() {
		while (true) {
			synchronized (container) {
				if (container.size() > 5) {
					try {
						System.out.println(Thread.currentThread().getName() + ": 满了，等待中");
						container.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				container.add(new Object());
				container.notify();
				System.out.println(Thread.currentThread().getName() + ": 我生产了" + (++count) + "个");
			}
		}

	}

}