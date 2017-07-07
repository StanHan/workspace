package demo.webservice.cxf.demo3;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

public class SoapServer {

	public static void main(String[] args) {
		JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
		factory.setAddress("http://localhost:9000/HelloWorld");
		factory.setServiceClass(HelloService.class);
		HelloService helloServer = new HelloServiceImpl();
		factory.setServiceBean(helloServer);
		factory.getInInterceptors().add(new LoggingInInterceptor());
		factory.getOutInterceptors().add(new LoggingOutInterceptor());
		factory.create();
		System.out.println("http://localhost:9000/HelloWorld?wsdl");
	}

}
