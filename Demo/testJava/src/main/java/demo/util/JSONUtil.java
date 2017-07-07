package demo.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class JSONUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	static String string2Json(String s) {
		StringBuilder sb = new StringBuilder(s.length() + 20);
		sb.append('\"');
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\"':
				sb.append("\\\"");
				break;
			case '\\':
				sb.append("\\\\");
				break;
			case '/':
				sb.append("\\/");
				break;
			case '\b':
				sb.append("\\b");
				break;
			case '\f':
				sb.append("\\f");
				break;
			case '\n':
				sb.append("\\n");
				break;
			case '\r':
				sb.append("\\r");
				break;
			case '\t':
				sb.append("\\t");
				break;
			default:
				sb.append(c);
			}
		}
		sb.append('\"');
		return sb.toString();
	}

	static String number2Json(Number number) {
		return number.toString();
	}

	static String boolean2Json(Boolean bool) {
		return bool.toString();
	}

	static String array2Json(Object[] array) {
		if (array.length == 0)
			return "[]";
		StringBuilder sb = new StringBuilder(array.length << 4);
		sb.append('[');
		for (Object o : array) {
			sb.append(toJson(o));
			sb.append(',');
		}
		// 将最后添加的 ',' 变为 ']':
		sb.setCharAt(sb.length() - 1, ']');
		return sb.toString();
	}

	static String map2Json(Map<String, Object> map) {
		if (map.isEmpty())
			return "{}";
		StringBuilder sb = new StringBuilder(map.size() << 4);
		sb.append('{');
		Set<String> keys = map.keySet();
		for (String key : keys) {
			Object value = map.get(key);
			sb.append('\"');
			sb.append(key);
			sb.append('\"');
			sb.append(':');
			sb.append(toJson(value));
			sb.append(',');
		}
		// 将最后的 ',' 变为 '}':
		sb.setCharAt(sb.length() - 1, '}');
		return sb.toString();
	}

	public static String object2Json(Object o) {
		if (o == null) {
			return "null";
		} else {// javabean转换成json字符串
			JSONSerializer jsonSerializer = new JSONSerializer();
			jsonSerializer.exclude("*.class");
			return jsonSerializer.deepSerialize(o);
		}
	}
	
	public static String toJson(Object o) {
		if (o == null) {
			return "null";
		} else if (o instanceof String) {
			return string2Json((String) o);
		} else if (o instanceof Boolean) {
			return boolean2Json((Boolean) o);
		} else if (o instanceof Number) {
			return number2Json((Number) o);
		} else if (o instanceof Map) {
			return map2Json((Map<String, Object>) o);
		} else if (o instanceof Object[]) {
			return array2Json((Object[]) o);
		} 
		return null;
	}

	/**
	 * 对JSON字符串中的特殊字符进行转义
	 * 
	 * @param s
	 * @return
	 */
	public static String normalizeString(String s) {

		StringBuffer str = new StringBuffer();
		int len = (s != null) ? s.length() : 0;
		for (int i = 0; i < len; i++) {
			final char lChar = s.charAt(i);
			if (lChar == '\n')
				str.append("\\n");
			else if (lChar == '\r')
				str.append("\\r");
			else if (lChar == '\t')
				str.append("\\t");
			else if (lChar == '\b')
				str.append("\\b");
			else if (lChar == '\f')
				str.append("\\f");
			// else if(lChar == '/') lBuf.append("\\/");
			else if (lChar == '\'')
				str.append("\\'");
			else if (lChar == '\"')
				str.append("\\\"");
			else if (lChar == '\\')
				str.append("\\\\");
			else
				str.append(lChar);
		}
		return (str.toString());
	}

	/**
	 * 判断字符串是否是JS或JSON中的非法字符串
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isInvalidate(String s) {

		// 如果为空或空格，则为合法字符串
		if (s == null || "".equals(s.trim()))
			return false;

		// Javascript保留字列表
		if ("break".equals(s) || "delete".equals(s) || "function".equals(s) || "return".equals(s) || "typeof".equals(s)
				|| "case".equals(s) || "do".equals(s) || "if".equals(s) || "switch".equals(s) || "var".equals(s)
				|| "catch".equals(s) || "else".equals(s) || "in".equals(s) || "this".equals(s) || "void".equals(s)
				|| "continue".equals(s) || "false".equals(s) || "instanceof".equals(s) || "throw".equals(s)
				|| "while".equals(s) || "debugger".equals(s) || "finally".equals(s) || "new".equals(s)
				|| "true".equals(s) || "with".equals(s) || "default".equals(s) || "for".equals(s) || "null".equals(s)
				|| "try".equals(s) || "enum".equals(s) || "super".equals(s) || "export".equals(s) || "import".equals(s)
				|| "extends".equals(s) || "class".equals(s) || "const".equals(s))
			return true;

		// Javascript未来保留字(可以考虑在未来时进行判断)
		/*
		 * if ("abstract".equals(s) || "double".equals(s) || "goto".equals(s) ||
		 * "native".equals(s) || "static".equals(s) || "boolean".equals(s) ||
		 * "implements".equals(s) || "package".equals(s) || "byte".equals(s) ||
		 * "private".equals(s) || "synchronized".equals(s) || "char".equals(s)
		 * || "int".equals(s) || "protected".equals(s) || "throws".equals(s) ||
		 * "final".equals(s) || "interface".equals(s) || "public".equals(s) ||
		 * "transient".equals(s) || "float".equals(s) || "long".equals(s) ||
		 * "short".equals(s) || "volatile".equals(s)) return true;
		 */
		return false;
	}

	/**
	 * json数组转list对象
	 * 
	 * @param json
	 * @param c
	 * @return
	 */
	public static <T> List<T> toListObj(String json, Class<T> c) {
		JSONDeserializer<List<T>> jsonDeser = new JSONDeserializer<List<T>>();
		List<T> list = jsonDeser.deserialize(json);
		return list;
	}

	/**
	 * json转对象
	 * 
	 * @param json
	 * @param c
	 * @return
	 */
	public static <T> T toObj(String json, Class<T> c) {
		JSONDeserializer<T> jsonDeser = new JSONDeserializer<T>();
		T obj = jsonDeser.deserialize(json, c);
		return obj;
	}

	/*
	 * public static void main(String[] args) {
	 * 
	 * @SuppressWarnings("unchecked") Map<String, String> map2 = JSONUtil.toObj(
	 * "{\"ROWKEY\":\"201407AAAAA\",\"INFO:DIMENS\":\"123\",\"INFO:COUNT\":\"61\",\"INFO:NAME\":\"123\"}",
	 * HashMap.class); System.out.println(map2);
	 * 
	 * }
	 */
}
