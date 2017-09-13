package demo.java.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class StringUtil {

	public static void main(String[] args) {
	    String refuseId = "47,53,55,56,112,";
	    String[] array = refuseId.split(",");
        List<Integer> refuseIdList = new ArrayList<>(array.length);
        for (String string : array) {
            refuseIdList.add(Integer.valueOf(string));
        }
        System.out.println(Arrays.toString(array));
        refuseIdList.forEach(System.out::print);
	}

	/**
	 * 获取<aaa>中的aaa
	 * 
	 * @param string
	 * @return
	 */
	public static String getElementName(String s) {
		if (s == null || "".equals(s)) {
			return "";
		}
		int begin = s.indexOf("<");
		int tmp = s.indexOf("</");
		if (begin == tmp) {
			return "";
		}
		int end = s.indexOf(">");
		String result = s.substring(begin + 1, end);
		return result;
	}

	/**
	 * true:null or "" or "     "
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str == null || "".equals(str.trim())) {
			return true;
		}
		return false;
	}

	/**
	 * 首字母大写
	 * 
	 * @param string
	 * @return
	 */
	public static String upperCase1stLetter(String string) {
		if (null == string) {
			return "";
		}
		// string = string.substring(0, 1).toUpperCase() + string.substring(1);
		// return string;
		char[] cs = string.toCharArray();
		if (97 <= cs[0] && cs[0] <= 122) {// 小写字母ASCII码区间
			cs[0] -= 32;
			return String.valueOf(cs);
		}
		return string;
	}

	/**
	 * 首字母小写
	 * 
	 * @param string
	 * @return
	 */
	public static String lowerCase1stLetter(String string) {
		if (null == string) {
			return "";
		}
		char[] cs = string.toCharArray();
		if (65 <= cs[0] && cs[0] <= 90) {// 大写字母ASCII码区间
			cs[0] += 32;
			return String.valueOf(cs);
		}
		return string;
	}

	public static String getDefineString(String type, String property) {
		StringBuffer buffer = new StringBuffer("");
		buffer.append("private " + type + " " + property + ";").append("\n");

		return buffer.toString();
	}

	/**
	 * 生成set方法
	 * 
	 * @param type
	 * @param property
	 * @return
	 */
	public static String generateSetter(String type, String property) {
		StringBuffer buffer = new StringBuffer("");
		String fcUpperProperty = upperCase1stLetter(property);

		buffer.append("public void set" + fcUpperProperty + "(" + type + " " + property + ") {").append("\n")
				.append("this." + property + "=" + property + ";").append("\n").append("}").append("\n");

		return buffer.toString();
	}

	/**
	 * 生成get方法
	 * 
	 * @param type
	 * @param property
	 * @return
	 */
	public static String generateGetter(String type, String property) {
		StringBuffer buffer = new StringBuffer("");
		String fcUpperProperty = upperCase1stLetter(property);

		buffer.append("public " + type + " get" + fcUpperProperty + "() {").append("\n")
				.append("return " + property + ";").append("\n").append("}").append("\n");

		return buffer.toString();
	}

	/**
	 * 生成get\set方法
	 * 
	 * @param type
	 * @param property
	 * @return
	 */
	public static String generateGetSet(String type, String property) {
		return generateGetter(type, property) + generateSetter(type, property);
	}

	/**
	 * 多个对象拼接成字符串
	 * 
	 * @param objs
	 *            对象数组
	 * @return
	 */
	public static String append(Object... objects) {
		StringBuilder sb = new StringBuilder();
		for (Object obj : objects) {
			sb.append(obj);
		}
		return sb.toString();
	}

	/**
	 * 
	 * @Description: 将日期转换为字符串匹配格式 @Version1.0 2014年7月23日 下午4:50:09 by
	 *               张仁华（renhua.zhang@pactera.com）创建
	 * @param filePath
	 * @param dateTime
	 * @return
	 */
	public static String parseDateToStr(String regex, Date dateTime) {

		// 根据时间获取日历
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);

		// 获取该日历中对应的 年月日时分秒
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.format("%02d", calendar.get(Calendar.MONTH) + 1);
		String day = String.format("%02d", calendar.get(Calendar.DATE));
		String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
		String minute = String.format("%02d", calendar.get(Calendar.MINUTE));
		String second = String.format("%02d", calendar.get(Calendar.SECOND));
		String milltsecond = String.valueOf(calendar.get(Calendar.MILLISECOND));

		// 将字符串中的%表达式替换成对应的日期
		return regex.replace("%yyyy", year).replace("%MM", month).replace("%dd", day).replace("%HH", hour)
				.replace("%mm", minute).replace("%ss", second).replace("%S", milltsecond);
	}

	public static String getCurrentTime(String regex) {

		SimpleDateFormat sp = new SimpleDateFormat(regex);
		String currentTime = sp.format(new Date());
		return currentTime;
	}

	/**
	 * 
	 * @Description:Map 深copy @Version1.0 2015年8月3日 下午3:30:28 by
	 *                  张仁华（renhua.zhang@pactera.com）创建
	 * @param map
	 * @return
	 */
	public static <K, Y> HashMap<K, Y> clone(Map<K, Y> map) {

		HashMap<K, Y> result = null;
		if (map != null) {
			result = new HashMap<>();
			Iterator<Entry<K, Y>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<K, Y> next = iterator.next();
				result.put(next.getKey(), next.getValue());
			}
		}
		return result;
	}

	
}
