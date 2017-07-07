package demo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CustomizedXmlConfigurer{

	private Map<String, String> resourceMap = new HashMap<String, String>();
	private final String MAP_KEY = "name";
	private final String MAP_VALUE = "value";
	private Document document;
	private List<String> keys = new ArrayList<String>();

	public List<String> getKeys() {
		return keys;
	}

	public void setKeys(List<String> keys) {
		this.keys = keys;
	}

	public CustomizedXmlConfigurer(Document document) {
		this.document = document;
	}

	public CustomizedXmlConfigurer() {
		super();
	}

	public void addResource(File resource) throws ParserConfigurationException, SAXException, IOException {
		load(resource);
	}

	public void addResource(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		load(is);
	}

	public void addResource(String fileName) throws ParserConfigurationException, SAXException, IOException {
		load(fileName);
	}

	private void load(Object resource) throws ParserConfigurationException, SAXException, IOException {

		if (document == null) {
			DocumentBuilderFactory domfactory = DocumentBuilderFactory.newInstance();
			InputStream is = null;
			DocumentBuilder builder = domfactory.newDocumentBuilder();
			if (resource instanceof File) {
				is = new FileInputStream((File) resource);
			} else if (resource instanceof String) {
				is = this.getClass().getClassLoader().getResourceAsStream((String) resource);
			} else if (resource instanceof InputStream) {
				is = (InputStream) resource;
			}

			document = builder.parse(is);
		}

		Element root = document.getDocumentElement();
		NodeList nodeList = root.getChildNodes();
		for (int i = 0; i < nodeList.getLength() - 1; i = i + 2) {
			Node proNode = nodeList.item(i + 1);

			String key = null;
			String value = null;

			for (Node node = proNode.getFirstChild(); node != null; node = node.getNextSibling()) {
				// System.out.println("node:"+node);
				// System.out.println(node.getNodeName());
				if ("property".equals(node.getNodeName())) {
					// for(Node
					// no=node.getFirstChild();no!=null;no=no.getNextSibling()){
					// if (no.getNodeType() == no.ELEMENT_NODE) {
					// if (no.getNodeName().equals(MAP_KEY)) {
					// key = no.getTextContent().trim();
					// } else if (no.getNodeName().equals(MAP_VALUE)) {
					// value = no.getTextContent().trim();
					// }
					// }
					// if (key != null)
					// resourceMap.put(key, value);
					// System.out.println(key);
					// keys.add(key);
					// }
					for (Node no = node.getFirstChild(); no != null; no = no.getNextSibling()) {
						if (MAP_KEY.equals(no.getNodeName())) {
							key = no.getTextContent().trim();
						} else if (MAP_VALUE.equals(no.getNodeName())) {
							value = no.getTextContent().trim();
						}
					}
					if (key != null) {
						resourceMap.put(key, value);
						keys.add(key);
						key = null;
					}
					// System.out.println(node.getFirstChild().getNextSibling());
				}
			}
		}

	}

	public String getString(String name) {
		return resourceMap.get(name);
	}

	public String getString(String name, String defaultValue) {
		String result = resourceMap.get(name);
		if (result == null || result.trim().length() == 0) {
			return defaultValue;
		}
		return result;
	}

	public int getInt(String name) {
		String result = resourceMap.get(name);
		return Integer.parseInt(result);
	}

	public int getInt(String name, int defaultValue) {
		String result = resourceMap.get(name);
		if (result == null || result.trim().length() == 0) {
			return defaultValue;
		}
		return Integer.parseInt(result);
	}

	public float getFloat(String name) {
		String result = resourceMap.get(name);
		return Float.parseFloat(result);
	}

	public float getFloat(String name, float defaultValue) {
		String result = resourceMap.get(name);
		if (result == null || result.trim().length() == 0) {
			return defaultValue;
		}
		return Float.parseFloat(result);
	}

	public Map<String, String> getResourceMap() {
		return resourceMap;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) throws ParserConfigurationException, SAXException, IOException {
		this.document = document;
		this.load(null);
	}

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		CustomizedXmlConfigurer c = new CustomizedXmlConfigurer();
		c.addResource(new File("src/config.xml"));
		List<String> keys = c.getKeys();
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			System.out.println(key.trim() + ":" + c.getString(key).trim());
		}
		c.addResource("etc/core-site.xml");
		System.out.println(c.getString("jxhk.req.file.path"));
	}

}
