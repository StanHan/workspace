package demo.java.lang;

import java.util.ArrayList;
import java.util.List;

/**
 * 原始数据
 * <p>
 * 原码： byte是一个字节保存的，有8个位，即8个0、1。 8位的第一个位是符号位， 也就是说0000 0001代表的是数字1 ,1000 0000代表的就是-1 。
 * 
 * 所以正数最大位0111 1111，也就是数字127 ，负数最大为1111 1111，也就是数字-128。
 * <p>
 * 1、反码： 一个数如果是正，则它的反码与原码相同； 一个数如果是负，则符号位为1，其余各位是对原码取反；
 * <p>
 * 2、补码：利用溢出，我们可以将减法变成加法 对于十进制数，从9得到5可用减法： 9－4＝5,因为4+6＝10，我们可以将6作为4的补数。 改写为加法： 9+6＝15（去掉高位1，也就是减10）得到5。
 * 对于十六进制数，从c到5可用减法：c－7＝5，因为7+9＝16 将9作为7的补数。改写为加法：c+9＝21（去掉高位1，也就是减16）得到5。
 * 
 * 在计算机中，如果我们用1个字节表示一个数，一个字节有8位，超过8位就进1，在内存中情况为（100000000），进位1被丢弃。
 * 
 * <li>一个数为正，则它的原码、反码、补码相同
 * <li>一个数为负，刚符号位为1，其余各位是对原码取反，然后整个数加1
 * 
 * <li>- 1的原码为 10000001
 * <li>- 1的反码为 11111110
 * <li>+ 1
 * <li>- 1的补码为 11111111
 * 
 * <li>0的原码为 00000000
 * <li>0的反码为 11111111（正零和负零的反码相同）
 * <li>+1
 * <li>0的补码为 100000000（舍掉打头的1，正零和负零的补码相同）
 *
 */
public class PrimitiveData {

    public static void main(String[] args) {
        Integer a = 1;
        Integer b = null;
        System.out.println(a.compareTo(b));
    }

    /**
     * 常规的大字节序表示一个数的话，用高字节位的存放数字的低位，比较符合人的习惯。而小字节序和大字节序正好相反，用高字节位存放数字的高位。
     * <p>
     * 将int转成4字节的小字节序字节数组
     */
    public static byte[] toLittleEndian(int i) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) i;
        bytes[1] = (byte) (i >>> 8);
        bytes[2] = (byte) (i >>> 16);
        bytes[3] = (byte) (i >>> 24);
        return bytes;
    }

    /**
     * 常规的大字节序表示一个数的话，用高字节位的存放数字的低位，比较符合人的习惯。而小字节序和大字节序正好相反，用高字节位存放数字的高位。
     * <p>
     * 将小字节序的4字节的字节数组转成int
     */
    public static int getLittleEndianInt(byte[] bytes) {
        int b0 = bytes[0] & 0xFF;
        int b1 = bytes[1] & 0xFF;
        int b2 = bytes[2] & 0xFF;
        int b3 = bytes[3] & 0xFF;
        return b0 + (b1 << 8) + (b2 << 16) + (b3 << 24);
    }

    static void testChar() {
        // Unicode for uppercase Greek omega character 'Ω'
        char uniChar = '\u03A9';
        System.out.println(uniChar);
        Character omega = 'Ω';
        System.out.println(omega);
    }

    static void testInt() {
        // 左移0位（不变）
        final int OP_READ = 1 << 0;
        System.out.println(OP_READ);
        // 左移2位（*4）
        final int OP_WRITE = 1 << 2;
        System.out.println(OP_WRITE);
        // 左移3位（*8）
        final int OP_CONNECT = 1 << 3;
        System.out.println(OP_CONNECT);
        // 左移4位（*16）
        final int OP_ACCEPT = 1 << 4;
        System.out.println(OP_ACCEPT);
        // -128 到 127 是Java常量池的常量，所以是一个对象
        Integer a = -128;
        Integer b = -128;
        System.out.println(a == b);
        // 超过这个范围就不是常量池的数据了，需要重新分配一个地址存放
        Integer c = 128;
        Integer d = 128;
        System.out.println(c == d);
    }

    static void testByte() {
        System.out.println(Byte.MAX_VALUE);
        System.out.println(Byte.MIN_VALUE);
        Byte a = 12;
        Byte b = 12;
        System.out.println(a == b);
        byte c = 12;
        System.out.println(c == a);
        Byte d = Byte.valueOf("12");
        System.out.println(a == d);
    }

    public static void unboxing() {

        Integer i = new Integer(-8);

        // 1. Unboxing through method invocation
        int absVal = absoluteValue(i);
        System.out.println("absolute value of " + i + " = " + absVal);

        List<Double> ld = new ArrayList<>();
        ld.add(3.1416); // Π is autoboxed through method invocation.

        // 2. Unboxing through assignment
        double pi = ld.get(0);
        System.out.println("pi = " + pi);

    }

    public static int absoluteValue(int i) {
        return (i < 0) ? -i : i;
    }

    static void bitDemo() {
        // The number 26, in binary
        int binVal = 0b10000000000000000000000000011010;
        System.out.println("binVal = " + Integer.toBinaryString(binVal));
        System.out.println("按位取反：" + "~binVal = " + Integer.toBinaryString(~binVal));
        System.out.println("左移：" + "(binVal<<2) = " + Integer.toBinaryString((binVal << 2)));
        System.out.println("右移：" + "(binVal>>2) = " + Integer.toBinaryString((binVal >> 2)));
        System.out.println("无符号右移：" + "(binVal>>>1) = " + Integer.toBinaryString((binVal >>> 1)));

        int bitmask2 = 0b100011;
        int bitmask1 = 0b111010;
        System.out.println("按位与：" + Integer.toBinaryString(bitmask2) + " & " + Integer.toBinaryString(bitmask1) + " = "
                + Integer.toBinaryString(bitmask2 & bitmask1));
        System.out.println("按位或：" + Integer.toBinaryString(bitmask2) + " | " + Integer.toBinaryString(bitmask1) + " = "
                + Integer.toBinaryString(bitmask2 | bitmask1));
        System.out.println("按位异或：" + Integer.toBinaryString(bitmask2) + " ^ " + Integer.toBinaryString(bitmask1) + " = "
                + Integer.toBinaryString(bitmask2 ^ bitmask1));
        System.out.println(Byte.toString((byte) 0b00010001));
        ;
    }

    static void prePostDemo() {
        int i = 3;
        i++;

        System.out.println(i);// prints 4
        ++i;

        System.out.println(i);// prints 5

        System.out.println(++i);// prints 6

        System.out.println(i++);// prints 6

        System.out.println(i);// prints 7

        int j = 0;
        j = (i++);// 相当于 a = i; i=i+1;
        j = i++;// 相当于 a = i; i=i+1;
        j = ++i;// 相当于 i=i+1; a = i; （先i = i + 1,再使用i的值）

        System.out.println(i);
        System.out.println(j);

        int n = i++ % 5;// n = 0
        System.out.println(n);

        double aValue = 8933.234;
        aValue++;
        System.out.println(aValue);
    }

    public static void demo() {
        boolean result = true;
        char capitalC = 'C';
        // Unicode for uppercase Greek omega character
        char uniChar = '\u03A9';// Ω
        byte b = 100;
        short s = 10000;
        int i = 100000;
        // The number 26, in decimal
        int decVal = 26;
        // The number 26, in hexadecimal
        int hexVal = 0x1a;
        // The number 26, in binary
        int binVal = 0b11010;

        double d1 = 123.4;
        // same value as d1, but in scientific notation
        double d2 = 1.234e2;
        double d3 = 123.4D;
        float f1 = 123.4F;

        long creditCardNumber = 1234_5678_9012_3456L;
        long socialSecurityNumber = 999_99_9999L;
        float pi = 3.14_15F;
        long hexBytes = 0xFF_EC_DE_5E;
        long hexWords = 0xCAFE_BABE;
        long maxLong = 0x7fff_ffff_ffff_ffffL;
        byte nybbles = 0b0010_0101;
        long bytes = 0b11010010_01101001_10010100_10010010;

        // Invalid: cannot put underscores adjacent to a decimal point
        /* float pi1=3_.1415F; */
        // Invalid: cannot put underscores adjacent to a decimal point
        /* float pi2=3._ 1415F; */
        // Invalid: cannot put underscores prior to an L suffix
        /* long socialSecurityNumber1=999_99_9999_ L; */

        // OK (decimal literal)
        int x1 = 5_2;
        // Invalid: cannot put underscores At the end of a literal
        // int x2=52_;
        // OK (decimal literal)
        int x3 = 5_______2;

        // Invalid: cannot put underscores in the 0x radix prefix
        /* int x4=0_ x52; */
        // Invalid: cannot put underscores at the beginning of a number
        /* int x5=0x_ 52; */
        // OK (hexadecimal literal)
        int x6 = 0x5_2;
        // Invalid: cannot put underscores at the end of a number
        /* int x7=0x52_; */
    }

    public static void arithmeticDemo() {

        int result = 1 + 2;
        // result is now 3
        System.out.println("1 + 2 = " + result);
        int original_result = result;

        result = result - 1;
        // result is now 2
        System.out.println(original_result + " - 1 = " + result);
        original_result = result;

        result = result * 2;
        // result is now 4
        System.out.println(original_result + " * 2 = " + result);
        original_result = result;

        result = result / 2;
        // result is now 2
        System.out.println(original_result + " / 2 = " + result);
        original_result = result;

        result = result + 8;
        // result is now 10
        System.out.println(original_result + " + 8 = " + result);
        original_result = result;

        result = result % 7;
        // result is now 3
        System.out.println(original_result + " % 7 = " + result);
    }
}
