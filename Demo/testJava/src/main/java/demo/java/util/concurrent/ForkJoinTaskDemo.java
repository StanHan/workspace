package demo.java.util.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.IntStream;

/**
 * <h1>工作窃取算法</h1>
 * <p>
 * 工作窃取（work-stealing）算法是指某个线程从其他队列里窃取任务来执行。那么为什么需要使用工作窃取算法呢？
 * 假如我们需要做一个比较大的任务，我们可以把这个任务分割为若干互不依赖的子任务，为了减少线程间的竞争，于是把这些子任务分别放到不同的队列里，并为每个队列创建一个单独的线程来执行队列里的任务，线程和队列一一对应，
 * 比如A线程负责处理A队列里的任务。但是有的线程会先把自己队列里的任务干完，而其他线程对应的队列里还有任务等待处理。干完活的线程与其等着，不如去帮其他线程干活，于是它就去其他线程的队列里窃取一个任务来执行。
 * 而在这时它们会访问同一个队列，所以为了减少窃取任务线程和被窃取任务线程之间的竞争，通常会使用双端队列，被窃取任务线程永远从双端队列的头部拿任务执行，而窃取任务的线程永远从双端队列的尾部拿任务执行。
 * 
 * 工作窃取算法的优点是充分利用线程进行并行计算，并减少了线程间的竞争，其缺点是在某些情况下还是存在竞争，比如双端队列里只有一个任务时。并且消耗了更多的系统资源，比如创建多个线程和多个双端队列。
 * 
 * <h1>Java Fork Join框架 简介</h1>
 * <p>
 * Fork/Join并行方式是获取良好的并行计算性能的一种最简单同时也是最有效的设计技术。
 * <h1>Fork/Join框架</h1>
 * <h2>理解Fork/Join框架的设计。</h2>
 * <ol>
 * <li>第一步分割任务。首先我们需要有一个fork类来把大任务分割成子任务，有可能子任务还是很大，所以还需要不停的分割，直到分割出的子任务足够小。
 * <li>第二步执行任务并合并结果。分割的子任务分别放在双端队列里，然后几个启动线程分别从双端队列里获取任务执行。子任务执行完的结果都统一放在一个队列里，启动一个线程从队列里拿数据，然后合并这些数据。
 * </ol>
 * <p>
 * Fork/Join使用两个类来完成以上两件事情：
 * <li>ForkJoinTask：我们要使用ForkJoin框架，必须首先创建一个ForkJoin任务。它提供在任务中执行fork()和join()操作的机制，通常情况下我们不需要直接继承ForkJoinTask类，而只需要继承它的子类，
 * Fork/Join框架提供了以下两个子类： RecursiveAction：用于没有返回结果的任务。 RecursiveTask ：用于有返回结果的任务。
 * <li>ForkJoinPool：ForkJoinTask需要通过ForkJoinPool来执行，任务分割出的子任务会添加到当前工作线程所维护的双端队列中，进入队列的头部。
 * 当一个工作线程的队列里暂时没有任务时，它会随机从其他工作线程的队列的尾部获取一个任务。
 * <p>
 * <h2>使用Fork/Join框架</h2>
 * <p>
 * ForkJoinTask与一般的任务的主要区别在于它需要实现compute方法，在这个方法里，首先需要判断任务是否足够小，如果足够小就直接执行任务。
 * 如果不足够小，就必须分割成两个子任务，每个子任务在调用fork方法时，又会进入compute方法，看看当前子任务是否需要继续分割成孙任务，如果不需要继续分割，则执行当前子任务并返回结果。
 * 使用join方法会等待子任务执行完并得到其结果。
 * 
 * <h2>Fork/Join框架的异常处理</h2>
 * <p>
 * 在ForkJoinTask类的compute()方法中，你不能抛出任何已检查异常，因为在这个方法的实现中，它没有包含任何抛出（异常）声明。你必须包含必要的代码来处理异常。
 * 但是，你可以抛出（或者它可以被任何方法或使用内部方法的对象抛出）一个未检查异常。ForkJoinTask和ForkJoinPool类的行为与你可能的期望不同。
 * 程序不会结束执行，并且你将不会在控制台看到任何关于异常的信息。它只是被吞没，好像它没抛出（异常）。你可以使用ForkJoinTask类的一些方法，得知一个任务是否抛出异常及其异常种类。
 * ForkJoinTask在执行的时候可能会抛出异常，但是我们没办法在主线程里直接捕获异常，所以ForkJoinTask提供了isCompletedAbnormally()方法来检查任务是否已经抛出异常或已经被取消了，
 * 并且可以通过ForkJoinTask的getException方法获取异常。
 * 
 * <h2>Fork/Join框架的实现原理</h2>
 * <p>
 * ForkJoinPool由ForkJoinTask数组和ForkJoinWorkerThread数组组成，ForkJoinTask数组负责存放程序提交给ForkJoinPool的任务，而ForkJoinWorkerThread数组负责执行这些任务。
 * ForkJoinTask的fork方法实现原理。当我们调用ForkJoinTask的fork方法时，程序会调用ForkJoinWorkerThread的pushTask方法异步的执行这个任务，然后立即返回结果。
 * pushTask方法把当前任务存放在ForkJoinTask 数组queue里。然后再调用ForkJoinPool的signalWork()方法唤醒或创建一个工作线程来执行任务。
 * ForkJoinTask的join方法实现原理。Join方法的主要作用是阻塞当前线程并等待获取结果。首先，它调用了doJoin()方法，通过doJoin()方法得到当前任务的状态来判断返回什么结果，
 * 任务状态有四种：已完成（NORMAL），被取消（CANCELLED），信号（SIGNAL）和出现异常（EXCEPTIONAL）。
 * <li>如果任务状态是已完成，则直接返回任务结果。
 * <li>如果任务状态是被取消，则直接抛出CancellationException。
 * <li>如果任务状态是抛出异常，则直接抛出对应的异常。
 * <p>
 * 在doJoin()方法里，首先通过查看任务的状态，看任务是否已经执行完了，如果执行完了，则直接返回任务状态，
 * 如果没有执行完，则从任务数组里取出任务并执行。如果任务顺利执行完成了，则设置任务状态为NORMAL，如果出现异常，则纪录异常，并将任务状态设置为EXCEPTIONAL。
 */
public class ForkJoinTaskDemo {

    public static void main(String[] args) {
        IntStream.range(0, 10).forEach(e -> demoFibonacci(e));

    }

    static void demoSum() {
        ForkJoinPool forkJoinPool = new ForkJoinPool();

        // 生成一个计算任务，负责计算1+2+3+4+5
        SumTask task = new SumTask(1, 5);

        // 执行一个任务
        ForkJoinTask<Integer> result = forkJoinPool.submit(task);

        try {
            System.out.println(result.get());
        } catch (InterruptedException e) {

        } catch (ExecutionException e) {

        }
    }

    static int demoFibonacci(int n) {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        FibonacciTask task = new FibonacciTask(n);
        ForkJoinTask<Integer> result = forkJoinPool.submit(task);
        try {
            System.out.println(result.get());
            return result.get();
        } catch (InterruptedException e) {

        } catch (ExecutionException e) {

        }
        // 检查任务是否已经抛出异常或已经被取消了
        if (task.isCompletedAbnormally()) {
            System.out.println(task.getException());
        }
        return 0;
    }
}

/**
 * 斐波那契数列（Fibonacci sequence），又称黄金分割数列、因数学家列昂纳多·斐波那契（Leonardoda Fibonacci）以兔子繁殖为例子而引入，故又称为“兔子数列”，
 * 指的是这样一个数列：1、1、2、3、5、8、13、21、34、……在数学上，斐波纳契数列以如下被以递归的方法定义：F(0)=0，F(1)=1,F(n)=F(n-1)+F(n-2)（n>=2，n∈N*）
 * 在现代物理、准晶体结构、化学等领域，斐波纳契数列都有直接的应用，为此，美国数学会从1963年起出版了以《斐波纳契数列季刊》为名的一份数学杂志，用于专门刊载这方面的研究成果。
 *
 */
class FibonacciTask extends RecursiveTask<Integer> {
    private static final long serialVersionUID = 1L;

    final int n;

    FibonacciTask(int n) {
        this.n = n;
    }

    protected Integer compute() {
        if (n <= 1) {
            return n;
        }

        FibonacciTask f1 = new FibonacciTask(n - 1);
        f1.fork();
        FibonacciTask f2 = new FibonacciTask(n - 2);
        return f2.compute() + f1.join();
    }
}

class SumTask extends RecursiveTask<Integer> {
    private static final long serialVersionUID = 1L;

    private static final int THRESHOLD = 2;// 阈值

    private int start;
    private int end;

    public SumTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        int sum = 0;

        // 如果任务足够小就计算任务
        boolean canCompute = (end - start) <= THRESHOLD;

        if (canCompute) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            // 如果任务大于阀值，就分裂成两个子任务计算
            int middle = (start + end) / 2;
            SumTask leftTask = new SumTask(start, middle);
            SumTask rightTask = new SumTask(middle + 1, end);

            // 执行子任务
            leftTask.fork();
            rightTask.fork();

            // 等待子任务执行完，并得到其结果
            int leftResult = leftTask.join();
            int rightResult = rightTask.join();
            // 合并子任务
            sum = leftResult + rightResult;
        }
        return sum;
    }

}
