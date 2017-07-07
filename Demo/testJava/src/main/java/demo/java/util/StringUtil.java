package demo.java.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.hadoop.hbase.util.Bytes;

public class StringUtil {

	public static void main(String[] args) {
		String s = "a,b,c,";
		System.out.println(s.substring(0,s.length() -1));
		String a = null;
//		a.getClass();
		Number n = 0; 
		Class<? extends Number> c = n.getClass(); 
		System.out.println(c);
		System.out.println(a.matches("\\d"));
//		JPasswordField a = null;
//		a.getPassword();
		
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
	 * 将2维数组LIST转为String数组List
	 * 
	 * @param byteList
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> parseByteArrayToList(List<byte[][]> byteList) {
		if (byteList == null || byteList.size() == 0) {
			return null;
		}
		List<String[]> list = new LinkedList<String[]>();

		byte[][] tmpArray = byteList.get(0);
		String[] array_string = new String[tmpArray.length];

		for (int i = 0; i < byteList.size(); i++) {

			byte[][] byteArray = byteList.get(i);

			for (int j = 0; j < byteArray.length; j++) {

				byte[] colomnByte = byteArray[j];

				String string = (String) convert(colomnByte, String.class);
				array_string[j] = string;
			}
			list.add(array_string);
		}
		return list;
	}

	/**
	 * 
	 * @Description: 将byte类型转为String类型，并返回json字符串 @Version1.0 2014年8月26日
	 *               下午4:50:09 by zhoujunyang
	 * @param byteList
	 *            需要转换的列表 第一行为列名，第二行以后则为相应数据byte[][]，第一维为相应的列，第二维为相应列的值
	 * @param typeMap
	 *            byteList对应每列的类型 key为列名 value为列相应的类型
	 * @return String List<byte[][]> byteList将其中byte结果转为字符串后的json格式字符
	 */
	public static List<String[]> changeByteToString(List<byte[][]> byteList, Map<String, Class> typeMap)
			throws Exception {

		if (byteList == null || byteList.size() == 0) {
			return null;
		}

		List<String[]> resList = new LinkedList<String[]>();
		@SuppressWarnings("rawtypes")
		Map<Integer, Class> clomnTypemap = new HashMap<Integer, Class>();// 存放每列的类型
		String[] clomnVal = null;

		for (int i = 0; i < byteList.size(); i++) {

			byte[][] byteArray = byteList.get(i);
			clomnVal = new String[typeMap.size()];

			for (int j = 0; j < byteArray.length; j++) {

				byte[] colomnByte = byteArray[j];
				if (i == 0) {// 第一行列名,根据第一行取得相应每一列的类型
					Object columTemp = convert(colomnByte, String.class);
					clomnVal[j] = String.valueOf(columTemp);
					clomnTypemap.put(j, typeMap.get(clomnVal[j]));
				} else {
					Object columTemp = convert(colomnByte, clomnTypemap.get(j));
					clomnVal[j] = String.valueOf(columTemp);
				}
			}
			resList.add(clomnVal);
		}
		// if(resList!=null && resList.size()>0){
		// return JSONUtil.toJson(resList);
		// }
		return resList;
	}

	/**
	 * 
	 * @Description: 将byte类型转为相应的类型 @Version1.0 2014年8月26日 下午4:50:09 by
	 *               zhoujunyang
	 * @param src
	 *            需要转换的byte数组
	 * @param tagCls
	 *            需要转的类型
	 * @return
	 */
	public static Object convert(byte[] src, Class tagCls) {
		if (String.class.isAssignableFrom(tagCls)) {
			return src == null ? null : Bytes.toString(src, 0, src.length);
		}
		if (BigDecimal.class.isAssignableFrom(tagCls)) {
			return src == null ? null : Bytes.toBigDecimal(src);
		}
		if (Boolean.class.isAssignableFrom(tagCls) || boolean.class.isAssignableFrom(tagCls)) {
			return src == null ? null : Bytes.toBoolean(src);
		}
		if (Double.class.isAssignableFrom(tagCls) || double.class.isAssignableFrom(tagCls)) {
			return src == null ? null : Bytes.toDouble(src);
		}
		if (Float.class.isAssignableFrom(tagCls) || float.class.isAssignableFrom(tagCls)) {
			return src == null ? null : Bytes.toFloat(src);
		}
		if (Integer.class.isAssignableFrom(tagCls) || int.class.isAssignableFrom(tagCls)) {
			return (src == null || src.length <= 0) ? null : Bytes.toInt(src);
		}
		if (Long.class.isAssignableFrom(tagCls) || long.class.isAssignableFrom(tagCls)) {
			return src == null ? null : Bytes.toLong(src);
		}
		if (Short.class.isAssignableFrom(tagCls) || short.class.isAssignableFrom(tagCls)) {
			return src == null ? null : Bytes.toShort(src);
		}
		return null;
	}

	/*
	 * public static void main(String[] args) throws
	 * UnsupportedEncodingException{ String s = "315566788";
	 * System.out.println(Bytes.toInt(convertStrToByteOfType(Integer.class,
	 * s))); }
	 */

	/**
	 * 
	 * @Description: 转字节数组 @Version1.0 2015年7月16日 上午11:36:35 by
	 *               张仁华（renhua.zhang@pactera.com）创建
	 * @param value
	 * @return
	 */
	public static byte[] toBytes(String value) {
		if (value != null) {
			return Bytes.toBytes(value);
		}
		return null;
	}

	public static byte[] toBytes(Integer value) {
		if (value != null) {
			return Bytes.toBytes(value);
		}
		return null;
	}

	public static byte[] toBytes(Long value) {
		if (value != null) {
			return Bytes.toBytes(value);
		}
		return null;
	}

	public static byte[] toBytes(Double value) {
		if (value != null) {
			return Bytes.toBytes(value);
		}
		return null;
	}

	public static byte[] toBytes(Float value) {
		if (value != null) {
			return Bytes.toBytes(value);
		}
		return null;
	}

	/**
	 * @Description:将字符串转成对应类型的字节数组
	 * @param cls
	 * @param str
	 * @return
	 */
	public static <T> byte[] convertStrToByteOfType(Class<T> cls, String str) {
		byte[] b = null;
		if (Integer.class.isAssignableFrom(cls)) {
			if (str != null && str.length() > 0 && str.matches("[0-9]*")) {
				b = Bytes.toBytes(Integer.parseInt(str));
			}
		}
		return b;
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
