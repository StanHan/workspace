package demo.webservice.cxf.demo2;

import javax.xml.ws.Endpoint;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

public class ServerDemo {

	public static void main(String[] args) {

		//��һ�ַ�����ʽ��ͨ��CXF�ṩ��JaxWsServerFactoryBean������webservice  
		JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();  
		factory.setServiceClass(HelloWorldImpl.class);  
		factory.setAddress("http://localhost:8080/HelloWorld");  
		  
		Server server = factory.create();  
		server.start();  
		  
		//�ڶ��ַ�ʽ��ͨ��JAX-WS�ṩ��Endpoint������webservice  
		//���ȴ���webservice�����ṩ���ʵ��  
//		HelloWorldImpl implementor = new HelloWorldImpl();  
//		String address = "http://localhost:8080/HelloWorld";  
//		Endpoint.publish(address, implementor);  

	}

}
