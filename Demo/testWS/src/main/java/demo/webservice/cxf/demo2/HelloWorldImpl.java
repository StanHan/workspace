package demo.webservice.cxf.demo2;

import javax.jws.WebService;

@WebService
public class HelloWorldImpl implements HelloWorld {

	@Override
	public String sayHello(String name) {
		System.out.println("sayHello����������");  
        return ("Hello"+name);  
	}

}
