package demo.webservice.jaxWs;

import javax.xml.ws.Endpoint;

public class Server1 {
	
	private final static String ADDRESS = "http://localhost:8080/test.webservice.jaxWs/HelloWorld";
	
	public static void main(String[] args) {
		HelloWorld hw = new HelloWorldImpl();
	    Endpoint.publish(ADDRESS, hw);
	}

}
