package demo.webservice.cxf.demo2;

import javax.jws.WebService;

@WebService
public interface HelloWorld {

	public String sayHello(String name); 
}
