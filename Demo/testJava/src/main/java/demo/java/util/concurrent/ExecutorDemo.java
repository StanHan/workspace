package demo.java.util.concurrent;

import java.util.concurrent.Executor;

public class ExecutorDemo {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
}

/**
 * 直接执行
 *
 */
class DirectExecutor implements Executor {
    public void execute(Runnable r) {
        r.run();
    }
}

class ThreadPerTaskExecutor implements Executor {
    public void execute(Runnable r) {
        new Thread(r).start();
    }
}
