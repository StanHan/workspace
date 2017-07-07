package demo.java.util.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class CallableDemo {

	public static void main(String[] args) {
		// testCallableAndFuture();
		// testFutureTask();
		testCompletionService();
	}

	/**使用任务集中的第一个非 null 结果，而忽略任何遇到异常的任务，并且在第一个任务就绪时取消其他所有任务
	 * @param excutor
	 * @param solvers
	 * @throws InterruptedException
	 */
	public void solve2(Executor excutor, Collection<Callable<String>> solvers) throws InterruptedException {

		CompletionService<String> completionService = new ExecutorCompletionService<String>(excutor);

		int n = solvers.size();

		List<Future<String>> futureList = new ArrayList<Future<String>>(n);
		String result = null;
		try {
			for (Callable<String> s : solvers) {
				futureList.add(completionService.submit(s));
			}
			for (int i = 0; i < n; ++i) {
				try {
					String r = completionService.take().get();
					if (r != null) {
						result = r;
						break;
					}
				} catch (ExecutionException ignore) {
				}
			}
		} finally {
			for (Future<String> future : futureList){
				future.cancel(true);
			}
		}

		if (result != null){
			System.out.println(result);
		}
		
			
	}

	public void solve1(Executor executor, Collection<Callable<String>> callables)
			throws InterruptedException, ExecutionException {

		CompletionService<String> completionService = new ExecutorCompletionService<String>(executor);

		for (Callable<String> callable : callables) {
			completionService.submit(callable);
		}

		int n = callables.size();
		for (int i = 0; i < n; ++i) {
			Future<String> future = completionService.take();
			String result = future.get();
			if (result != null) {
				System.out.println(result);
			}
		}
	}

	public static void testCompletionService() {

		ExecutorService executorService = Executors.newCachedThreadPool();
		/**提交到CompletionService中的Future是按照完成的顺序排列的*/
		CompletionService<Integer> completionService = new ExecutorCompletionService<Integer>(executorService);

		for (int i = 1; i < 5; i++) {
			final int taskID = i;
			completionService.submit(new Callable<Integer>() {
				public Integer call() throws Exception {
					return taskID;
				}
			});
		}
		// 可能做一些事情
		for (int i = 1; i < 5; i++) {
			try {
				Future<Integer> future = completionService.take();
				System.out.println(future.get());
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	public static void testCallableAndFuture() {

		ExecutorService executorService = Executors.newSingleThreadExecutor();

		Callable<String> callable = new Callable<String>() {
			public String call() throws Exception {
				Random random = new Random();
				String threadName = Thread.currentThread().getName();
				return threadName + " : " + random.nextInt(100);
			}
		};

		Future<String> future = executorService.submit(callable);

		try {
			Thread.sleep(2000);// 可能做一些事情
			System.out.println(future.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	public static void testFutureTask() {

		Callable<Integer> callable = new Callable<Integer>() {
			public Integer call() throws Exception {
				return new Random().nextInt(100);
			}
		};

		FutureTask<Integer> future = new FutureTask<Integer>(callable);

		new Thread(future).start();

		try {
			Thread.sleep(2000);// 可能做一些事情
			System.out.println(future.get());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}
}
