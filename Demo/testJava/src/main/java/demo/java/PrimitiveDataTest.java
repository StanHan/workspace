package demo.java;

import java.util.ArrayList;
import java.util.List;

public class PrimitiveDataTest {

	public static void main(String[] args) {
		// Unicode for uppercase Greek omega character
		char uniChar = '\u03A9';
		System.out.println(uniChar);
		
		unboxing();
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

	public static void bitDemo() {
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

	public static void prePostDemo() {
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
