package demo.java.lang;

import java.math.BigDecimal;

public class MathDemo {

    public static void main(String[] args) {
        Integer a = 0;
        demoRint();
    }
    
    static void rounding() {
        double f = 3.1315;
        BigDecimal b = new BigDecimal(new Double(f).toString());
        double f1 = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.println(f1);
    }

    /**
     * rint 四舍五入，返回double值 注意.5的时候会取偶数
     */
    static void demoRint() {
        System.out.println(Math.rint(10.1)); // 10.0
        System.out.println(Math.rint(10.7)); // 11.0
        System.out.println(Math.rint(11.5)); // 12.0
        System.out.println(Math.rint(10.5)); // 10.0
        System.out.println(Math.rint(10.51)); // 11.0
        System.out.println(Math.rint(-10.5)); // -10.0
        System.out.println(Math.rint(-11.5)); // -12.0
        System.out.println(Math.rint(-10.51)); // -11.0
        System.out.println(Math.rint(-10.6)); // -11.0
        System.out.println(Math.rint(-10.2)); // -10.0
    }

    /**
     * round 四舍五入，float时返回int值，double时返回long值
     */
    public static void round() {
        System.out.println(Math.round(10.1)); // 10
        System.out.println(Math.round(10.7)); // 11
        System.out.println(Math.round(10.5)); // 11
        System.out.println(Math.round(10.51)); // 11
        System.out.println(Math.round(-10.5)); // -10
        System.out.println(Math.round(-10.51)); // -11
        System.out.println(Math.round(-10.6)); // -11
        System.out.println(Math.round(-10.2)); // -10
    }

    /**
     * abs求绝对值
     */
    static void demoAbs() {
        System.out.println(Math.abs(-10.4)); // 10.4
        System.out.println(Math.abs(10.1)); // 10.1
    }

    /**
     * ceil天花板的意思，就是返回大的值，注意一些特殊值
     */
    static void demoCeil() {
        System.out.println(Math.ceil(-10.1)); // -10.0
        System.out.println(Math.ceil(10.7)); // 11.0
        System.out.println(Math.ceil(-0.7)); // -0.0
        System.out.println(Math.ceil(0.0)); // 0.0
        System.out.println(Math.ceil(-0.0)); // -0.0

    }

    /**
     * floor地板的意思，就是返回小的值
     */
    static void floor() {
        System.out.println(Math.floor(-10.1)); // -11.0
        System.out.println(Math.floor(10.7)); // 10.0
        System.out.println(Math.floor(-0.7)); // -1.0
        System.out.println(Math.floor(0.0)); // 0.0
        System.out.println(Math.floor(-0.0)); // -0.0
    }

    /**
     * max 两个中返回大的值,min和它相反，就不举例了
     */
    static void max() {
        System.out.println(Math.max(-10.1, -10)); // -10.0
        System.out.println(Math.max(10.7, 10)); // 10.7
        System.out.println(Math.max(0.0, -0.0)); // 0.0
    }

    /**
     * random 取得一个大于或者等于0.0小于不等于1.0的随机数
     */
    static void demoRandom() {
        System.out.println(Math.random()); // 0.08417657924317234
        System.out.println(Math.random()); // 0.43527904004403717
    }

    /**
     * Double类型求和
     * 
     * @param d
     * @return
     */
    public static Double sum(Double... d) {
        Double sum = 0.0;
        for (int i = 0; i < d.length; i++) {
            if (d[i] == null) {
                sum += 0.0;
            } else {
                sum += d[i];
            }
        }
        return sum;
    }
}
