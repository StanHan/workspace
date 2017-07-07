package demo.java.runtime;

import java.io.IOException;

public class RuntimeTestCase {

	public static void main(String[] args) throws InterruptedException, IOException {
		RuntimeTestCase.executeCMD(" java -version");
		
	}
	public static void executeCMD(String cmd){
		Runtime runtime = Runtime.getRuntime();
		Process process = null;
		try {
			process = runtime.exec("cmd /c " + cmd);
			System.out.println(process.toString());
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
