package demo.java.lang.thread;

import java.text.SimpleDateFormat;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 在JDK的API文档中ThreadLocal的定义第一句道出：This class provides thread-local variables. 好，这个类提供了线程本地的变量。
 */
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
