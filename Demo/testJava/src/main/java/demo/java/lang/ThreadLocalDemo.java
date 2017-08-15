package demo.java.lang;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalDemo {

	public static void main(String[] args) {
		"aaaa".toCharArray();

		for (int i = 0; i < 10; i++) {
			LocalTester t1 = new LocalTester();
			t1.start();
		}

		LocalTester t2 = new LocalTester();

		t2.start();

	}

}

class LocalTester extends Thread {

	@Override
	public void run() {
		for (int i = 0; i < 3; i++) {
			System.out.println(getName() + " " + UniqueThreadIdGenerator.getCurrentThreadId());
		}
	}

}

class UniqueThreadIdGenerator {
	private static final AtomicInteger uniqueId = new AtomicInteger(0);
	
	private static final ThreadLocal<Integer> uniqueNum = new ThreadLocal<Integer>() {
		@Override
		protected Integer initialValue() {
			return uniqueId.getAndIncrement();
		}
	};

	public static int getCurrentThreadId() {
		return uniqueNum.get();
	}
} // UniqueThreadIdGenerator
