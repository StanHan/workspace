package demo.spring;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeanUtils;

public class Test {

	public static void main(String[] args) {
		A a = new A();
		a.setaBC("aaaaaaaa");
		B b = new B();
		BeanUtils.copyProperties(a, b);
		System.out.println(b.getAbc());
		
		Map<String,String> map = new HashMap<String,String>();
		map.put("A", "A");
		map.put("a", "a");
		System.out.println(map.get("A"));
		System.out.println("a".equals(null));
		System.out.println("a" != null);
		System.out.println("a" == new String("a"));
		String string = "a";
		System.out.println("a" == string);
	}

}

class A{
	private String a;
	private String aBC;

	public String getaBC() {
		return aBC;
	}

	public void setaBC(String aBC) {
		this.aBC = aBC;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}
}

class B{
	private Integer a;
	private String abc;
	

	public String getAbc() {
		return abc;
	}

	public void setAbc(String abc) {
		this.abc = abc;
	}

	public Integer getA() {
		return a;
	}

	public void setA(Integer a) {
		this.a = a;
	}
}