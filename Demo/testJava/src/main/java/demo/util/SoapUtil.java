package demo.util;

import java.util.ArrayList;  
import java.util.List;  
  
import javax.xml.namespace.QName;  
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.soap.MessageFactory;  
import javax.xml.soap.SOAPBody;  
import javax.xml.soap.SOAPElement;  
import javax.xml.soap.SOAPEnvelope;  
import javax.xml.soap.SOAPException;  
import javax.xml.soap.SOAPFactory;  
import javax.xml.soap.SOAPHeader;  
import javax.xml.soap.SOAPMessage;  
import javax.xml.soap.SOAPPart;  
import javax.xml.transform.OutputKeys;  
import javax.xml.transform.Source;  
import javax.xml.transform.Transformer;  
import javax.xml.transform.TransformerFactory;  
import javax.xml.transform.dom.DOMSource;  
import javax.xml.transform.sax.SAXSource;  
import javax.xml.transform.stream.StreamResult;  
  
import org.w3c.dom.Document;  
import org.w3c.dom.Node;  
import org.xml.sax.InputSource;  
/** 
 *  
 * @author 汪心利 
 * Date 2010-3-30下午02:51:30 
 * (c)CopyRight seahigh 2010 
 */  
public class SoapUtil {  
	
	public static void main(String[] args) throws Exception {  
        SoapUtil util = new SoapUtil();  
        SOAPPart part = util.initSoapPart();  
        Source inform = util.getParameterValues(part, util.getTestnames());  
        util.soap2String(inform);  
    }  
	
    public SOAPPart initSoapPart() throws SOAPException {  
  
        SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();  
  
        SOAPPart soapPart = soapMessage.getSOAPPart();  
  
        SOAPEnvelope soapEnvelope = soapPart.getEnvelope();  
        SOAPHeader soapHeader = soapEnvelope.getHeader();  
        SOAPElement cwmp = soapEnvelope.addNamespaceDeclaration("cwmp","urn:dslforum-org:cwmp-1-0");  
        SOAPElement xsi = soapEnvelope.addNamespaceDeclaration("xsi","http://www.w3.org/2001/XMLSchema-instance");  
        SOAPElement xsd = soapEnvelope.addNamespaceDeclaration("xsd","http://www.w3.org/2001/XMLSchema");    
        SOAPElement enc = soapEnvelope.addNamespaceDeclaration("SOAP-ENC","http://schemas.xmlsoap.org/soap/encoding/");  
  
        SOAPElement id = soapHeader.addChildElement("ID", "cwmp");  
        id.setTextContent("1");  
        return soapPart;  
    }  
  
    public void soap2String(Source source) throws Exception {
        if (source != null) {  
            Node root = null;  
            if (source instanceof DOMSource) {  
                root = ((DOMSource) source).getNode();  
            } else if (source instanceof SAXSource) {  
                InputSource inSource = ((SAXSource) source).getInputSource();  
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();  
                dbf.setNamespaceAware(true);  
                Document doc = dbf.newDocumentBuilder().parse(inSource);  
                root = (Node) doc.getDocumentElement();  
            }  
            Transformer transformer = TransformerFactory.newInstance().newTransformer();  
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");  
            transformer.transform(new DOMSource(root), new StreamResult(System.out));  
        }  
    }  
  
    public Source informResponse(SOAPPart part) throws Exception {  
        SOAPEnvelope soapEnvelope = part.getEnvelope();  
        SOAPBody soapBody = soapEnvelope.getBody();  
        SOAPElement informRes = soapBody.addChildElement("InformResponse",  "cwmp");  
        SOAPElement max = SOAPFactory.newInstance().createElement("MaxEnvelopes", "", "");  
        max.setTextContent("1");  
        informRes.addChildElement(max);  
        return part.getContent();  
    }  
  
    public Source getParameterValues(SOAPPart part, List<String> list) throws Exception {  
        SOAPEnvelope soapEnvelope = part.getEnvelope();  
        SOAPBody soapBody = soapEnvelope.getBody();  
        SOAPElement informRes = soapBody.addChildElement("GetParameterValues","cwmp");  
        SOAPFactory soapFactory = SOAPFactory.newInstance();  
        SOAPElement names = soapFactory.createElement("ParameterNames", "", "");  
        names.addAttribute(new QName("SOAP-ENC:arrayType"), "xsd:string[" + list.size() + "]");  
        
        SOAPElement nameElement = null;  
        for (int i = 0; i < list.size(); i++) {  
            nameElement = soapFactory.createElement("string", "", "");  
            nameElement.setTextContent((String) list.get(i));  
            names.addChildElement(nameElement);  
        }  
        informRes.addChildElement(names);  
        return part.getContent();  
    }  
  
    public List<String> getTestnames() {  
        List<String> list = new ArrayList<String>();  
        list.add("InternetGatewayDevice.DeviceInfo.X_CT-COM_CPU");  
        list.add("InternetGatewayDevice.DeviceInfo.X_CT-COM_WorkTime");  
        list.add("InternetGatewayDevice.DeviceInfo.SoftwareVersion");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.SSID");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_ReceiveNoise");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_LAN-TotalBytesReceived");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_LAN-TotalBytesSent");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_WAN-TotalBytesReceived");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_WAN-TotalBytesSent");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_LAN-TotalPacketsReceived");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_LAN-TotalPacketsSent");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_WAN-TotalPacketsReceived");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_WAN-TotalPacketsSent");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_Stat.ResponsePass");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_Stat.AskPass");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_Stat.SuccessPass");  
        list.add("InternetGatewayDevice.DeviceInfo.X_CT-COM_Temp");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_Stat.LAN-PacketsError");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_Stat.LAN-TotalBytesReceived");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_Stat.LAN-TotalBytesSent");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_Stat.WAN-TotalBytesReceived");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_Stat.WAN-PacketsError");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.X_CT-COM_Stat.WAN-TotalBytesSent");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.AssociatedDevice.1.X_CT-COM_ReceiveRate");  
        list.add("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.AssociatedDevice.1.X_CT-COM_SendRate");  
        list.add("InternetGatewayDevice.DeviceInfo.SerialNumber");  
        list.add("InternetGatewayDevice.DeviceInfo.ManufacturerOUI");  
        return list;  
    }  
} 
