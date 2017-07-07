package demo.webservice;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

import org.w3c.dom.Document;

public class Test {

	public static void main(String[] args) throws Exception {
		Test.aa();
	}
	
	public static void aa() throws SOAPException, Exception, Exception{
		//1��������Ϣ����
		MessageFactory factory = MessageFactory.newInstance();
		//2�������Ϣ��������SoapMessage
		SOAPMessage message = factory.createMessage();
		//3������SOAPPart
		SOAPPart part = message.getSOAPPart();
		//4����ȡSOAPENvelope
		SOAPEnvelope envelope = part.getEnvelope();
		//5������ͨ��SoapEnvelope��Ч�Ļ�ȡ��Ӧ��Body��Header����Ϣ
		SOAPBody body = envelope.getBody();
		//6�����Qname������Ӧ�Ľڵ�(QName����һ����������ռ�Ľڵ�)
		//<ns:add xmlns="http://java.zttc.edu.cn/webservice"/>
		QName qname = new QName("http://java.zttc.edu.cn/webservice", "add","ns");
		//���ʹ�����·�ʽ�������ã����<>ת��Ϊ&lt;��&gt
		//body.addBodyElement(qname).setValue("<a>1</a><b>2</b>");
		SOAPBodyElement ele = body.addBodyElement(qname);
		ele.addChildElement("a").setValue("22"); 
		ele.addChildElement("b").setValue("33");
		//��ӡ��Ϣ��Ϣ
		message.writeTo(new FileOutputStream(new File("d:/test.xml")));
		System.out.println(message);
	}
	
	public void bb(String wsdlUrl,String ns) throws Exception{
		//1����������(Service)
		URL url = new URL(wsdlUrl);
		QName sname = new QName(ns,"MyServiceImplService");
		Service service = Service.create(url,sname);
		            
		//2������Dispatch
		Dispatch<SOAPMessage> dispatch = service.createDispatch(new QName(ns,"MyServiceImplPort"),SOAPMessage.class,Service.Mode.MESSAGE);
		            
		//3������SOAPMessage
		SOAPMessage msg = MessageFactory.newInstance().createMessage();
		SOAPEnvelope envelope = msg.getSOAPPart().getEnvelope();
		SOAPBody body = envelope.getBody();
		            
		//4������QName��ָ����Ϣ�д������
		QName ename = new QName(ns,"add","nn");//<nn:add xmlns="xx"/>
		SOAPBodyElement ele = body.addBodyElement(ename);
		ele.addChildElement("a").setValue("22");
		ele.addChildElement("b").setValue("33");
		msg.writeTo(System.out);
		System.out.println("\n invoking.....");
		                    
		//5��ͨ��Dispatch������Ϣ,�᷵����Ӧ��Ϣ
		SOAPMessage response = dispatch.invoke(msg);
		response.writeTo(System.out);
		System.out.println();
		            
		//6����Ӧ��Ϣ����,����Ӧ����Ϣת��Ϊdom����
		Document doc = response.getSOAPPart().getEnvelope().getBody().extractContentAsDocument();
		String str = doc.getElementsByTagName("addResult").item(0).getTextContent();
		System.out.println(str);
	}
}
