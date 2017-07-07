package demo.webservice.jaxb;

import java.util.Map;

import javax.jws.WebService;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
@WebService
public interface HelloWorld {
	
	String sayHi(String text);
	
	String sayHiToUser(User user);
	
	@XmlJavaTypeAdapter(UserMapAdapter.class)
	Map<Integer,User> getUsers();
}
