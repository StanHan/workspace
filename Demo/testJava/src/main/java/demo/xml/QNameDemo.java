package demo.xml;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

public class QNameDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String a = buildPYrequest2("<conditions></conditions>","aaaaaa","bbbbbbbbbb","xml");
		System.out.println(a);
	}

	public static String buildPYrequest2(String condition,String userId,String password,String style){
		condition = (condition + "").replaceAll("<", "&lt;").replaceAll(">", "&gt;") ;
		
		Document document = DocumentHelper.createDocument();
		Namespace nsSoapenv = new Namespace("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
		QName soapenvEnvelope = new QName("Envelope", nsSoapenv);
		
		Element root = DocumentHelper.createElement(soapenvEnvelope);
//		Element root = DocumentHelper.createElement("soapenv:Envelope");
//		root.addAttribute("xmlns:soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
		root.addAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
		root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
		document.add(root);
		//生成root的一个接点  
//        Element soapBody = DocumentHelper.createElement("soapenv:Body");
		QName soapenvBody = new QName("Body", nsSoapenv);
		Element body = DocumentHelper.createElement(soapenvBody);
        root.add(body);
        Namespace nsPy = new Namespace("ns1", "http://batoffline.report.szpcs.scrc.com");
        QName queryReportQName = new QName("queryReport", nsPy);
        
        Element queryReport = DocumentHelper.createElement(queryReportQName);
        queryReport.addAttribute("soapenv:encodingStyle", "http://schemas.xmlsoap.org/soap/encoding/");
        
        body.add(queryReport);
        
        Namespace nsSoapenc = new Namespace("soapenc", "http://schemas.xmlsoap.org/soap/encoding/");
        QName userIDQName = new QName("userID",nsSoapenc);
        Element userID = DocumentHelper.createElement(userIDQName);
        userID.addAttribute("xsi:type", "string");
        userID.addText(userId);
        queryReport.add(userID);
        
        QName passwordQName = new QName("password",nsSoapenc);
        Element pw = DocumentHelper.createElement(passwordQName);
        pw.addAttribute("xsi:type", "string");
        pw.addText(password);
        queryReport.add(pw);
        
        QName queryConditionQName = new QName("queryCondition",nsSoapenc);
        Element queryCondition = DocumentHelper.createElement(queryConditionQName);
        queryCondition.addAttribute("xsi:type", "string");
        queryCondition.addText(condition);
        queryReport.add(queryCondition);
        
        QName outputStyleQName = new QName("outputStyle",nsSoapenc);
        Element outputStyle = DocumentHelper.createElement(outputStyleQName);
        outputStyle.addAttribute("xsi:type", "string");
        outputStyle.addText(style);
        queryReport.add(outputStyle);
        
        return document.asXML();
	}
}
