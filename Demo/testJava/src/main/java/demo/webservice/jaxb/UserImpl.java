package demo.webservice.jaxb;

import javax.xml.bind.annotation.XmlType;

@XmlType(name="User")
public class UserImpl implements User{
	
	String name;
	
	public UserImpl(){}
	
	public UserImpl(String n){
		this.name =n;
	}
	@Override
	public String getName() {
		return name;
	}
	public void setName(String n){
		this.name = n ;
	}
}
