package demo.java.util.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

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

    /**
     * 命名线程工厂
     *
     */
    public static class NamedThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        NamedThreadFactory(String name) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            if (null == name || name.isEmpty()) {
                name = "pool";
            }

            namePrefix = name + "-" + poolNumber.getAndIncrement() + "-thread-";
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
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
