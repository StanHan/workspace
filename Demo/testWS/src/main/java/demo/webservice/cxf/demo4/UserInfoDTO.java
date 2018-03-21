package demo.webservice.cxf.demo4;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="ServerUserInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserInfoDTO implements Serializable {

	private static final long serialVersionUID = 1222334L;

	private Integer id;
	private String name;
	private Integer age;
	private Integer address;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public Integer getAddress() {
		return address;
	}
	public void setAddress(Integer address) {
		this.address = address;
	}
}
