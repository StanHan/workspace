package demo.java;

public class Simple {
	private int a;// 实例变量

	public int method() {
		int b = 0;// 局部变量
		a++;
		b = a;
		return b;
	}

	public static void main(String[] args) throws InterruptedException {
	   Integer i = null;
	   System.out.println(i == 9);
	}
	
	
}
