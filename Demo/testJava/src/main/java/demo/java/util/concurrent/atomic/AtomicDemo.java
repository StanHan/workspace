package demo.java.util.concurrent.atomic;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicDemo {

	private final AtomicLong count = new AtomicLong(0);
	
	public void testAtomicLong(){
		long l = count.incrementAndGet();
		System.err.println(l);
		System.out.println(count.get());
		count.addAndGet(107);
		System.out.println(count.get());
		count.compareAndSet(107, 100);
		System.out.println(count.get());
		count.decrementAndGet();
		System.out.println(count.get());
		l = count.getAndIncrement();
		System.err.println(l);
		System.out.println(count);
	}
	
}
