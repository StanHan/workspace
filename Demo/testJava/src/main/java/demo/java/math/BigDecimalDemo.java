package demo.java.math;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BigDecimalDemo {

    public static void main(String[] args) {
        double d = 3.1415926;
        String result = String.format("%.3f", d);
        System.out.println(result);
    }

    /**
     * 四舍五入
     */
    static void demo1() {
        double f = 3.1315;
        BigDecimal b = new BigDecimal(new Double(f).toString());
        double f1 = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(f1);
    }
    
    /**
     * DecimalFormat默认采用了RoundingMode.HALF_EVEN这种类型,而且format之后的结果是一个字符串类型String
     */
    static void demo2() {
        DecimalFormat df = new DecimalFormat("#.000");
        System.out.println(df.format(new BigDecimal(1.0145)));//1.014
        System.out.println(df.format(new BigDecimal(1.1315)));//1.132
    }

}
