package demo.xml;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class XMLSchema2JavaClass {

	public static void main(String[] args) {
		String fileName = "d:\\SVN\\hadoop\\0100 项目明细\\010013双网双卡-信用卡决策\\001 需求文档\\人行.xml";
		File schemaFile = new File(fileName);
		String fileSavePath = "d:\\cache\\ppc2\\";
		try {
			Document document = reader(schemaFile);
			Element rootElement = document.getRootElement();
			
			List<Element> elements = rootElement.elements();
			int i = 0;
			Iterator<Node> iterator = rootElement.nodeIterator();
			String classComment = null;
			while (iterator.hasNext()) {
				Node node = (Node) iterator.next();
				if (Node.COMMENT_NODE == node.getNodeType()) {// 注释信息
					String comment = node.asXML();
					classComment = comment.replaceAll("<!--", "/**").replaceAll("-->", "*/");
					System.out.println(classComment);
				}
				if (Node.ELEMENT_NODE == node.getNodeType()) {// 节点信息
					Element element = (Element)node;
					String elementName = element.getNamespacePrefix() + ":" + element.getName();
					if ("xs:element".equalsIgnoreCase(elementName)) {
						continue;
					}
					System.out.println("+" + ++i + "+");

					String name = element.attributeValue("name");
					System.out.println("elememt_Name = " + name);
					System.out.println("******************");

					String javaContent = parseElement2JavaFile(element,"package com.pactera.nppc.vo;",classComment);
					String javaFileName = fileSavePath + name + ".java";

					demo.java.io.IOUtil.writeFile(javaFileName, javaContent, "UTF-8");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**将该XML Element 描述的数据结构转为java bean
	 * @param element
	 * @return
	 */
	public static String parseElement2JavaFile(Element element,String packageInfo,String classComment) {
		StringBuilder javaContent = new StringBuilder();
		StringBuilder getSetStringBuilder = new StringBuilder();
		String className = element.attributeValue("name");
		javaContent.append(packageInfo + "\n");
		
		javaContent.append("import javax.xml.bind.annotation.XmlAccessType;\n");
		javaContent.append("import javax.xml.bind.annotation.XmlAccessorType;\n");
		javaContent.append("import javax.xml.bind.annotation.XmlRootElement;\n");
		javaContent.append("import javax.xml.bind.annotation.XmlType;\n");
		
		javaContent.append(classComment+ "\n");
		
		javaContent.append("@XmlAccessorType(XmlAccessType.FIELD)\n");
		javaContent.append("@XmlRootElement\n");
//		javaContent.append("@XmlType\n");
		javaContent.append("public class " + className + "{\n");
		Element element_sequence = element.element("sequence");
		System.out.println(element_sequence.asXML());
		boolean isInsert = false;

		Iterator<Node> iterator = element_sequence.nodeIterator();
		while (iterator.hasNext()) {
			Node node = iterator.next();
			if (Node.COMMENT_NODE == node.getNodeType()) {// 注释信息
				String comment = node.asXML();
				javaContent.append(comment.replaceAll("<!--", "/**").replaceAll("-->", "*/"));
				javaContent.append("\n");
			}
			if (Node.ELEMENT_NODE == node.getNodeType()) {// 节点信息
				System.out.println(node.asXML());
				Element tmpElement = (Element) node;
				String type = tmpElement.attribute("type").getStringValue();
				String name = tmpElement.attribute("name").getStringValue();
				type = changeSchemaDataType(type);

				Attribute maxOccursAttribute = tmpElement.attribute("maxOccurs");
				String maxOccurs = null;
				if (maxOccursAttribute != null) {
					maxOccurs = maxOccursAttribute.getStringValue();
				}

				if (maxOccurs != null && "unbounded".equals(maxOccurs)) {
					isInsert = true;
					
//					javaContent.append("@XmlElementWrapper(name=\""+name+"\")\n");
//					javaContent.append("@XmlElement(name=\""+type+"\")\n");
					
					type = "List<" + type + ">";
					javaContent.append("private " + type + " " + name + ";\n");
				}else {
					javaContent.append("private " + type + " " + name + ";\n");
				}

				String getset = generateGetSet(type, name);
				getSetStringBuilder.append(getset);
			}
		}
		
		javaContent.append(getSetStringBuilder);
		
		javaContent.append("}");
		if (isInsert) {
			javaContent.insert(packageInfo.length()+1, "import java.util.List;\n");
//			javaContent.insert(packageInfo.length()+1, "import java.util.List;\nimport javax.xml.bind.annotation.XmlElement;\nimport javax.xml.bind.annotation.XmlElementWrapper;");
		}
		System.out.println(javaContent.toString());
		return javaContent.toString();
	}
	
	static String generateGetSet(String type,String name) {
	    return "";
	}

	/**
	 * 将Schema中的数据类型转为java类型
	 * 
	 * @param type
	 * @return
	 */
	public static String changeSchemaDataType(String type) {
		String javaType = null;
		if ("xs:string".equalsIgnoreCase(type)) {
			javaType = "String";
		} else if ("xs:decimal".equalsIgnoreCase(type)) {
			javaType = "Double";
		} else if ("xs:integer".equalsIgnoreCase(type)) {
			javaType = "Integer";
		} else {
			javaType = type;
		}
		return javaType;
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
