package demo.webservice.pyCredit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;

import org.codehaus.xfire.client.Client;
import org.w3c.dom.*;



public class Test {
	
	public static String soapUrl = "";
    private static String userName = "";
	private static String passWord = "";
	
	public static void main(String args[]) throws Exception{
	
		Test test =new Test();
		test.testwsQuery();
	}
	

	private void testwsQuery()
	{
	try{

             String queryInfo = "";
             BufferedReader br = new BufferedReader(new FileReader("c:/test/wsquery1.xml"));
             String str = br.readLine();
             while(str!=null){
               queryInfo+=str;
               str = br.readLine();

            }

            br.close();

            Client client = new Client(new URL("http://www.pycredit.com:9001/services/WebServiceSingleQuery?wsdl"));
            Object [] results = client.invoke("queryReport",new Object[]{"查询账户","账户密文",queryInfo,"xml"});
        
            if(results[0] instanceof String){
                //返回字符串，解析处理字符串内容 
            	System.out.println("resut:"+results[0].toString());
            }else if (results[0] instanceof org.w3c.dom.Document) {
               //返回字符串Document，解析处理Document内容
                org.w3c.dom.Document doc = (org.w3c.dom.Document)results[0];
     
               Element element = doc.getDocumentElement();
               NodeList children = element.getChildNodes();
               Node node = children.item(0);
               System.out.println("result content:"+node.getNodeValue());
            } 

            }catch(Exception e){
                 System.out.println("(Exception :"+e.getMessage());
        }
		
	}
}
