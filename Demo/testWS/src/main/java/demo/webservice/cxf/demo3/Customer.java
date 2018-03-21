package demo.webservice.cxf.demo3;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "Customer")//@XmlRootElement-指定XML根元素名称（可选）
@XmlAccessorType(XmlAccessType.FIELD)//@XmlAccessorType-控制属性或方法序列化 
@XmlType(propOrder={"name","age"})//@XmlType-映射一个类或一个枚举类型成一个XML Schema类型 
public class Customer{
	private int age;
	
	private String name;

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}