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
		String a = "aa;bb;cc";
		int idx = a.indexOf(";");
		System.out.println(idx);
		String b = a.substring(0, idx);
		String c = a.substring(idx);
		System.out.println(b);
		System.out.println(c);
	}
	
	
}
