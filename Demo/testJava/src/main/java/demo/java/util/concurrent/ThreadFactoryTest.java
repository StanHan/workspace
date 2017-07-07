package demo.java.util.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class ThreadFactoryTest {

	public static void main(String[] args) {
		ThreadFactory threadFactory = Executors.defaultThreadFactory();
	}

}

class SimpleThreadFactory implements ThreadFactory {
	public Thread newThread(Runnable r) {
		return new Thread(r);
	}
}
