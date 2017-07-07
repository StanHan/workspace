package demo.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.dom4j.Attribute;
import org.dom4j.Comment;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import test.java.io.IOUtil;
import test.java.util.StringUtil;

public class XMLTest {

	public static void main(String[] args) {
		String xml = "C:/Users/Stan/Desktop/人行解析/人行.xml";
		String xsd = "C:/Users/Stan/Desktop/人行解析/人行.xsd";
		File xmlFile = new File(xml);
		File xsdFile = new File(xsd);
		try {
			Document document_xml = reader(xmlFile);
			Element rootElement_xml = document_xml.getRootElement();

			Document document_xsd = reader(xsdFile);
			Element rootElement_xsd = document_xsd.getRootElement();

			Iterator<Node> iterator = rootElement_xsd.nodeIterator();
			Map<String, String> map = new HashMap<String, String>();
			// Map<String, Node> map2 = new HashMap<String, Node>();

			String comment = null;
			String name = null;
			// Node node_comment = null;
			while (iterator.hasNext()) {
				Node node = (Node) iterator.next();
				if (Node.COMMENT_NODE == node.getNodeType()) {// 注释信息
					// node_comment = node;
					comment = node.asXML();
					System.out.println(comment);
				}
				if (Node.ELEMENT_NODE == node.getNodeType()) {// 节点信息
					Element element = (Element) node;
					name = element.attributeValue("name");
					System.out.println("elememt_Name = " + name);
					map.put(name, comment);

					aa(element, map);
					// map2.put(name, node_comment);
				}
			}
			System.out.println(map);

			FileInputStream fileInputStream = new FileInputStream(xmlFile);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "GBK");
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			int i = 0;
			String line = null;
			while ((line = bufferedReader.readLine()) != null) {
				String key = StringUtil.getElementName(line);
//				System.out.println(key);
				String value = map.get(key);
				String tmp = (value == null?"":value);
				System.out.println(line + tmp);
			}

			bufferedReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void aa(Element element, Map<String, String> map) {
		String comment = null;
		String name = null;
		Element element_sequence = element.element("sequence");
		if(null == element_sequence){
			return;
		}
		Iterator<Node> iterator = element_sequence.nodeIterator();
		while (iterator.hasNext()) {
			Node node = iterator.next();
			if (Node.COMMENT_NODE == node.getNodeType()) {// 注释信息
				comment = node.asXML();
			}
			if (Node.ELEMENT_NODE == node.getNodeType()) {// 节点信息
				Element e = (Element) node;
				name = e.attributeValue("name");
				System.out.println("elememt_Name = " + name);
				map.put(name, comment);
			}
		}
	}

	/**
	 * 读取XML
	 * 
	 * @param file
	 */
	public static Document reader(File file) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(file);
		return document;
	}

}
