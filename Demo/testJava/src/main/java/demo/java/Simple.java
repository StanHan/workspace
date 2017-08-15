package demo.java;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Simple {
	private int a;// 实例变量

	public int method() {
		int b = 0;// 局部变量
		a++;
		b = a;
		return b;
	}

	public static void main(String[] args) throws InterruptedException {
	    int i = 23600;
	    Long a = (long) i;
	    float b = (float) (a/1000.0);
	    System.out.println(b);
	}
	
}




class SubClass extends SurperClass{
    @Override
    public void test(){
        System.out.println("SubClass.test()");
    }

    @Override
    public void test1() {
        // TODO Auto-generated method stub
        
    }
}

class SubClass2 extends SurperClass{

    @Override
    public void test1() {
        // TODO Auto-generated method stub
        
    }
}

abstract class SurperClass{
    public void test(){
        System.out.println("SurperClass.test()");
    }
    
    public abstract void test1();
}
