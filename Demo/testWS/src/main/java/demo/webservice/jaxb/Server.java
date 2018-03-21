package demo.webservice.jaxb;

import javax.xml.ws.Endpoint;

public class Server {
	public static void main(String[] args) {
		System.out.println("Starting Server... ");
		String address = "http://localhost:8899/hello";
		HelloWorldImpl implementor = new HelloWorldImpl();
		Endpoint.publish(address, implementor);
		System.out.println("http://localhost:8899/hello?wsdl");
		try {
			Thread.sleep(5*60*1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("Exiting Server... ");
		System.exit(0);
	}
}
