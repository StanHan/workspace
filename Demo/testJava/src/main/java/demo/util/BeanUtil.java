package demo.util;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
public class BeanUtil {

	public static void main(String[] args) {
		Map<String,String> map = new HashMap<String,String>();
		map.put("a", "aa");
		map.put("b", "bb");
		map.put("c", "cc");
		A bean = new A();
//		BeanUtils.copyProperties(map, bean);
		try {
//			BeanUtils.copyProperties(bean, map);
			BeanUtils.populate(bean, map);
		} catch (IllegalAccessException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*try {
			BeanUtils.describe(bean);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		System.out.println(bean.getA()+bean.getB()+bean.getC());
		
	}

	
}

class A{
	private String a;
	private String b;
	private String c;
	
	public String getA() {
		return a;
	}
	public void setA(String a) {
		this.a = a;
	}
	public String getB() {
		return b;
	}
	public void setB(String b) {
		this.b = b;
	}
	public String getC() {
		return c;
	}
	public void setC(String c) {
		this.c = c;
	}
}