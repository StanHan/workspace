package demo.java;

import java.util.ArrayList;
import java.util.List;

public class StaticParams {
    public static void main(String[] args) {  
        StaticParams.getInstance();  
    }  
    private static StaticParams sp = buildStaticParams();  
    
    private static int NUM_A = getA();  
    private static int NUM_B = getB();  
    private static List<String> LIST_A = getListA();  
  
    /** 
     * 私有化构造方法 
     */  
    private StaticParams() {  
        System.out.println("初始化构造方法");  
    }  
  
    /** 
     * sp的声明在其他几个静态变量之前 
     *  
     * @return 
     */  
    private static StaticParams buildStaticParams() {  
        if (null == sp) {  
            sp = new StaticParams();  
        }  
        int result = NUM_A * NUM_B; // 基本类型有默认值，此处不会报错，但结果不正确  
        System.out.println("result is :" + result);  
        LIST_A.add("abcd");     //此时LIST_A还未初始化，到此有异常  
        return sp;  
    }  
  
    /** 
     * 获取StaticParams实例 
     *  
     * @return 
     */  
    public static StaticParams getInstance() {  
        return sp;  
    }  
  
    private static int getA() {  
        System.out.println("初始化A");  
        return 10;  
    }  
  
    private static int getB() {  
        System.out.println("初始化B");  
        return 20;  
    }  
  
    private static List<String> getListA() {  
        System.out.println("初始化List");  
        return new ArrayList<String>();  
    }  
      
    
}
