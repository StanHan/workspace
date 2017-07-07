package demo.webservice.cxf.Demo.client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;

import demo2.server.IMyservice;

public class MyClient {

	public static void main(String[] args) {  
		  
        try {  
            URL url = new URL("http://localhost:7777/tudou?wsdl");  
            QName qname=new QName("http://server.demo2/","MyServiceImplService"); 
            
            Service service=Service.create(url, qname);  
            IMyservice ms=service.getPort(IMyservice.class);  
            System.out.println(ms.add(1, 4) ); 
            System.out.println(ms.minus(1, 4));
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        }  
    } 
}
