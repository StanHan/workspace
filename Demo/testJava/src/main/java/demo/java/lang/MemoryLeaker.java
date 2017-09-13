package demo.java.lang;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 内存泄漏
 *
 */
public class MemoryLeaker {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}

class MapLeaker {
    public ExecutorService exec = Executors.newFixedThreadPool(5);
    public Map<Task, TaskStatus> taskStatus = Collections.synchronizedMap(new HashMap<Task, TaskStatus>());
    private Random random = new Random();

    private enum TaskStatus {
        NOT_STARTED, STARTED, FINISHED
    };

    private class Task implements Runnable {
        private int[] numbers = new int[random.nextInt(200)];

        public void run() {
            int[] temp = new int[random.nextInt(10000)];
            taskStatus.put(this, TaskStatus.STARTED);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            taskStatus.put(this, TaskStatus.FINISHED);
        }
    }

    public Task newTask() {
        Task t = new Task();
        taskStatus.put(t, TaskStatus.NOT_STARTED);
        exec.execute(t);
        return t;
    }
}
