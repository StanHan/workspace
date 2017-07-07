package demo.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

public class Dom4J {

	public static void main(String[] args){
	}

	public static void testConverte() throws IOException, DocumentException {
		String path ="D:\\WorkSpace\\Test\\src\\test\\xml\\test.xml";
//		String path ="E:\\workspace\\BOSBigDataUDSPWebService\\pyzx\\com\\pactera\\pyzx\\util\\test.xml";
		
		FileInputStream fis = new FileInputStream(new File(path));
		Charset charset = Charset.forName("UTF-8");
		InputStreamReader inputStreamReader = new InputStreamReader(fis,charset);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		StringBuffer sb = new StringBuffer();
		String line ;
		while (( line = bufferedReader.readLine()) != null){
			sb.append(line);
		}
//		Document document =  DocumentHelper.parseText(text)
//		String aa = new String(sb.toString().getBytes(charset), charset);
//		System.out.println(aa);
		converte(sb.toString());
	}
	
	public static void converte(String text) throws DocumentException {
		if(text == null){
			text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><person> <name>James</name> </person>";
		}
		
		Document document = DocumentHelper.parseText(text);
		System.out.println(document.asXML());
	}

	public static String getValueFromXmlElement(String xml, String elementName) {
		int beginIndex = 0;
		int endIndex = 0;

		beginIndex = xml.indexOf("<" + elementName + ">");
		endIndex = xml.indexOf("</" + elementName + ">");
		String value = xml.substring(beginIndex + elementName.length() + 2, endIndex);
		return value;
	}

	public Document parse(URL url) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		return document;
	}

	/**
	 * 遍历
	 * 
	 * @param document
	 * @throws DocumentException
	 */
	public void iterator(Document document) throws DocumentException {

		Element root = document.getRootElement();

		// iterate through child elements of root
		for (Iterator i = root.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			// do something
		}

		// iterate through child elements of root with element name "foo"
		for (Iterator i = root.elementIterator("foo"); i.hasNext();) {
			Element foo = (Element) i.next();
			// do something
		}

		// iterate through attributes of root
		for (Iterator i = root.attributeIterator(); i.hasNext();) {
			Attribute attribute = (Attribute) i.next();
			// do something
		}
	}

	/**
	 * 获取单节点
	 * 
	 * @param document
	 */
	public void selectNode(Document document) {
		List list = document.selectNodes("//foo/bar");
		Node node = document.selectSingleNode("//foo/bar/author");
		String name = node.valueOf("@name");
	}

	public void findLinks(Document document) throws DocumentException {
		List list = document.selectNodes("//a/@href");
		for (Iterator iter = list.iterator(); iter.hasNext();) {
			Attribute attribute = (Attribute) iter.next();
			String url = attribute.getValue();
		}
	}

	public void treeWalk(Document document) {
		treeWalk(document.getRootElement());
	}

	public void treeWalk(Element element) {
		for (int i = 0, size = element.nodeCount(); i < size; i++) {
			Node node = element.node(i);
			if (node instanceof Element) {
				treeWalk((Element) node);
			} else {
				// do something....
			}
		}
	}

	public Document createDocument() {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("root");

		Element author1 = root.addElement("author").addAttribute("name", "James").addAttribute("location", "UK")
				.addText("James Strachan");

		Element author2 = root.addElement("author").addAttribute("name", "Bob").addAttribute("location", "US")
				.addText("Bob McWhirter");

		return document;
	}

	public void write(Document document) throws IOException {

		// lets write to a file
		XMLWriter writer = new XMLWriter(new FileWriter("output.xml"));
		writer.write(document);
		writer.close();

		// Pretty print the document to System.out
		OutputFormat format = OutputFormat.createPrettyPrint();
		writer = new XMLWriter(System.out, format);
		writer.write(document);

		// Compact format to System.out
		format = OutputFormat.createCompactFormat();
		writer = new XMLWriter(System.out, format);
		writer.write(document);
	}

	public Document styleDocument(Document document, String stylesheet) throws Exception {
		// load the transformer using JAXP
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(stylesheet));

		// now lets style the given document
		DocumentSource source = new DocumentSource(document);
		DocumentResult result = new DocumentResult();
		transformer.transform(source, result);

		// return the transformed document
		Document transformedDoc = result.getDocument();
		return transformedDoc;
	}

}
