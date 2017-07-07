package demo.webservice.cxf.Demo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.QName;
import org.dom4j.io.SAXReader;

public class Demo1 {

	public static void main(String[] args) throws Exception {
		Document document = DocumentHelper.createDocument();
        Element root = DocumentHelper.createElement("soap:Envelope");
        root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.addAttribute("xmlns:xsd", "http://www.w3.org/2001/XMLSchema");
        root.addAttribute("xmlns:soap", "http://schemas.xmlsoap.org/soap/envelope/");
        document.add(root);
        //生成root的一个接点  
        Element category = DocumentHelper.createElement("soap:Body"); 
        root.add(category);
        //生产category的一个接点  
        Element method = category.addElement("SendNotify","http://tempuri.org/");
        method.addElement("title").addText("aaaaaaaaaaaaaaaaaaaa");
        method.addElement("content").addText("bbbbbbbbbbbbbbbbbbbbbb");
        System.out.println(document.asXML());
//        URL url = new URL("http://*.*.*.*/****");
//        URLConnection connection = url.openConnection();
//        HttpURLConnection httpConn = (HttpURLConnection)connection;
//        byte[] b = document.asXML().getBytes();
//        // Set the appropriate HTTP parameters.
//        httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
//        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
//        httpConn.setRequestProperty("SOAPAction", "http://tempuri.org/SendNotify");
//        httpConn.setRequestMethod("POST");
//        httpConn.setDoOutput(true);
//        httpConn.setDoInput(true);
//        OutputStream out = httpConn.getOutputStream();
//        out.write(b);
//        out.close();
//        // Read the response and write it to standard out.
//        InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
//        BufferedReader in = new BufferedReader(isr);
//        SAXReader reader = new SAXReader();
//        Document resultDoc = reader.read(in);
//        Element resultRoot = resultDoc.getRootElement();
//        List rootList = resultRoot.selectNodes("/your xml node patht']"); 
//        Element element = null;  
//        String resu=null;
//        // 循环此节点,并取出对应的文本信息  
//        for (Object obj : rootList) {  
//            element = (Element) obj;  
//            resu = element.getTextTrim();  
//        } 
//        System.out.println(resu);
	}
	
}
