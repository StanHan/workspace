package demo.webservice.jaxb;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.jws.WebService;
@WebService(endpointInterface="com.syc.jaxb.HelloWorld",
			serviceName="HelloWorld")
public class HelloWorldImpl implements HelloWorld {
	
	Map<Integer,User> users = new LinkedHashMap<Integer, User>();
	
	@Override
	public String sayHi(String text) {
		System.out.println("sayHello called...");
		return "Hello " +text;
	}

	@Override
	public String sayHiToUser(User user) {
		System.out.println("sayUserHello called...");
		users.put(users.size()+1, user);
		return "Hello"+ user.getName();
	}

	@Override
	public Map<Integer, User> getUsers() {
		System.out.println("getMapUsers called...");
		return users;
	}

}
