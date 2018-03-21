package demo.webservice.cxf.demo3;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

public class SoapClient2 {

	public static void main(String[] args) throws Exception {
//		 ObjectFactory factory = new ObjectFactory();  
	     Customer customer = new Customer();  
	     customer.setAge(20);  
	     customer.setName("Josen");  

	      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();  
	      documentBuilderFactory.setNamespaceAware(true);  
	      DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();  
	      Document document = documentBuilder.newDocument();  

	      JAXBContext jaxbContext = JAXBContext.newInstance(Customer.class);  
	      //Java����ת����XML  
	      Marshaller marshaller = jaxbContext.createMarshaller();  
	      marshaller.marshal(customer, document);  
	        
	      DOMSource domSource = new DOMSource(document);  
	      StringWriter stringWriter = new StringWriter();  
	      StreamResult streamResult = new StreamResult(stringWriter);  
	      TransformerFactory transformerFactory = TransformerFactory.newInstance();  
	      Transformer transformer = transformerFactory.newTransformer();  
	      transformer.transform(domSource, streamResult);  
	      String xmlString = stringWriter.toString();  
	      System.out.println(xmlString);
	      //XMLת����Java����  
	      Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();  
	      StringReader reader = new StringReader(xmlString);  
	      Customer cus = (Customer)unmarshaller.unmarshal(reader);  
	      System.out.println("Age:"+cus.getAge());  
	      System.out.println("Name:"+cus.getName());  

	}

}
