package demo.java.util.concurrent.atomic;

import java.util.concurrent.atomic.AtomicLong;

/**
 * <h1>Atomic 关键字</h1> 可以使基本数据类型以原子的方式实现自增自减等操作。
 *
 */
public class AtomicDemo {

    private final AtomicLong count = new AtomicLong(0);

    public void testAtomicLong() {
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
