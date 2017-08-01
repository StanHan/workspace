package demo.java.runtime;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class RuntimeTestCase {

	public static void main(String[] args) throws InterruptedException, IOException {
		executeCMD(" java -version");
		
	}
	public static void executeCMD(String cmd) throws InterruptedException{
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = runtime.exec("cmd /c " + cmd);
			process.waitFor();
			OutputStream out = process.getOutputStream();
			PrintWriter printWriter = new PrintWriter(out);
			printWriter.println("a");
			System.out.println(process.toString());
			int ret = process.exitValue();
	        System.out.println(ret);
	        System.out.println("availableProcessors = "+runtime.availableProcessors());
	        System.out.println("maxMemory="+runtime.maxMemory());
	        System.out.println("freeMemory="+runtime.freeMemory());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void connectMysql(){
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = runtime.exec("cmd /c mysql -uroot -p123456 ");
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			// 等待编译结束
			process.waitFor();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 检查返回码，看编译是否出错。
		System.out.println("process="+process.toString());
		int ret = process.exitValue();
		System.out.println(ret);
		System.out.println("availableProcessors = "+runtime.availableProcessors());
		System.out.println("maxMemory="+runtime.maxMemory());
		System.out.println("freeMemory="+runtime.freeMemory());
	}
	
}
