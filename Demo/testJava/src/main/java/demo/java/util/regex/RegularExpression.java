package demo.java.util.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegularExpression {

	public static void main(String[] args) {
		test4();
	}
	
	public static void test4(){
		String a = "a;b;z；d;e；y";
		String[] array = a.split(";|；");
		for (String string : array) {
			System.out.println(string);
		}
	}
	
	/**
	 * | 将两个匹配条件进行逻辑“或”（Or）运算。
	 */
	public static void test2(){
		System.out.println("|DF|A3".split("|").length);
		String[] array = "|DF|A3".split("");
		for (String string : array) {
			System.out.printf("%s ,", string);
		}
		System.out.println("-------------------------");
		String[] array2 = "|DF|A3".split("|");
		for (String string : array2) {
			System.out.printf("%s ,", string);
		}
		
		System.out.println("-------------------------");
		String[] array3 = "abcdefghijklmnopqrstuvwxyyz0123456789".split("");
		for (String string : array3) {
			System.out.printf("%s ,", string);
		}
		System.out.println("-------------------------");
		System.out.println("|DF|A3".split("\\|").length);
	}
	
	public static void test3() {
		//* 匹配前面的子表达式零次或多次
		System.out.println("ac.matches(a*b*c) = "+"ac".matches("a*b*c"));
		System.out.println("ac.matches(a*b*c) = "+"ac".matches("a*b*c"));
		System.out.println("a*b*c.matches(a*b*c) = "+"a*b*c".matches("a*b*c"));
		System.out.println("abc.matches(a*b*c) = "+"abc".matches("a*b*c"));
		System.out.println("bbc.matches(a*b*c) = "+"bbc".matches("a*b*c"));
		System.out.println("zbc.matches(a*b*c) = "+"zbc".matches("a*b*c"));
		System.out.println("aaaaaaa.pro".matches(".*pro$"));
		System.out.println("He's a weasel".matches(".*weasel$"));
		System.out.println("z".matches("zo*"));
		System.out.println("zo".matches("zo*"));
		System.out.println("zoo".matches("zo*"));
	}
	
	public static void test1(){
		String string = "33,你好,88";
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(string);
		
		while (matcher.find()) {
			String tmp = matcher.group(0);
			System.out.println(tmp);
		}
		//
		boolean isMatched = matcher.matches();
		System.out.println("是否全部匹配："+isMatched);
		boolean isLooked = matcher.lookingAt();
		System.out.println("是否开头匹配："+isLooked);
		
		int groupCount = matcher.groupCount();
		System.out.println(groupCount);
	}
	
	// 判断一个字符串是否都为数字
	public static boolean isDigit2(String strNum) {
		Pattern pattern = Pattern.compile("[0-9]{1,}");
		Matcher matcher = pattern.matcher(strNum);
		return matcher.matches();
	}

	/**
	 * 截取所有数字，并拼接
	 * 
	 * @param content
	 * @return
	 */
	public static String getAllNumbers(String content) {
		StringBuilder stringBuilder = new StringBuilder();
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			System.out.println(matcher.group(0));
			stringBuilder.append(matcher.group(0));
		}
		return stringBuilder.toString();
	}

	/**
	 * 截取第一次匹配到的数字
	 * 
	 * @param content
	 * @return
	 */
	public static String getFirstNumbers(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			System.out.println(matcher.group());
			return matcher.group();
		}
		return "";
	}

	// 截取非数字
	public static String splitNotNumber(String content) {
		Pattern pattern = Pattern.compile("\\D+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}
	// 判断一个字符串是否含有数字

	public static boolean hasDigit(String content) {
		boolean flag = false;
		Pattern p = Pattern.compile(".*\\d+.*");
		Matcher m = p.matcher(content);
		if (m.matches()) {
			flag = true;
		}
		return flag;
	}

	/**
	 * 用ascii码判断是否为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			int chr = str.charAt(i);
			if (chr < 48 || chr > 57) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 用正则表达式
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric2(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 用JAVA自带的函数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric3(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	// 判断一个字符串是否都为数字
	public static boolean isDigit(String strNum) {
		return strNum.matches("[0-9]{1,}");
	}
}
