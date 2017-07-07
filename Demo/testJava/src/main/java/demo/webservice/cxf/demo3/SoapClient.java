package demo.webservice.cxf.demo3;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

public class SoapClient {

	public static void main(String[] args) {
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
		factory.setAddress("http://localhost:9000/HelloWorld");
		factory.setServiceClass(HelloService.class);
		factory.getInInterceptors().add(new LoggingInInterceptor());
		HelloService helloService = (HelloService) factory.create();
		
		Customer customer1 = new Customer();
		customer1.setAge(99);
		customer1.setName("Stan1");
		
		Customer customer2 = new Customer();
		customer2.setAge(199);
		customer2.setName("Stan2");
		
		helloService.save(customer1, customer2);
		helloService.test("i love u");
		helloService.get(8000);
	}

}
