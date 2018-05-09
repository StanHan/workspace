package demo.java.util.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import demo.java.lang.ThreadDemo;
import demo.java.lang.ThreadDemo.UncaughtExceptionHandlerDemo;

public class ThreadFactoryDemo {

    public static void main(String[] args) {
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
    }

    /**
     * 需要指定线程异常处理器的线程工厂
     * 
     */
    public static class SpecialThreadFactory implements ThreadFactory {

        private Thread.UncaughtExceptionHandler handler;

        public SpecialThreadFactory(Thread.UncaughtExceptionHandler handler) {
            super();
            this.handler = handler;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setUncaughtExceptionHandler(handler);
            return t;
        }

    }

}

class SimpleThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r);
    }
}
