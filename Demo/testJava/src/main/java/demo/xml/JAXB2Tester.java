package demo.xml;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;

public class JAXB2Tester {

	public static void main(String[] args) {
		testWrite();
		String path = "D:/SVN/hadoop/0100 项目明细/010013双网双卡-信用卡决策/001 需求文档/关于影像流人行以卡办卡补充需求-附件：人行专业版报告补充解析逻辑/李小萌.xml";
		String serializedFile = "d:/cache/serialized.txt";
//		ReportMessage reportMessage = new ReportMessage();
		
//		Header header = new Header();
//		MessageHeader messageHeader = new MessageHeader();
//		messageHeader.setQueryTime("2011.05.17 07:49:38");
//		messageHeader.setReportCreateTime("2011.05.17 07:49:38");
//		messageHeader.setReportSN("2011051600000000108911");
//		header.setMessageHeader(messageHeader);
//		
//		reportMessage.setHeader(header);
		
//		write(ReportMessage, path);
		/*ReportMessage object = null;
		{
			Date start = new Date();
			object = read(path, ReportMessage.class);
			Date end = new Date();
			System.out.println("解析XML为BEAN耗时：" +(end.getTime() - start.getTime())+" 毫秒");
		}
		
		{
			Date start = new Date();
			String json = JSONUtil.object2Json(object);
			System.out.println(json);
			Date end = new Date();
			System.out.println("BEAN转JSON耗时：" +(end.getTime() - start.getTime())+" 毫秒");
		}
		
		{
			Date start = new Date();
			SerializeUtil.serialize(object, serializedFile);
			Date end = new Date();
			System.out.println("BEAN序列化耗时：" +(end.getTime() - start.getTime())+" 毫秒");
		}
		
		{
			Date start = new Date();
			ReportMessage reportMessage = SerializeUtil.deSerialize(serializedFile);
			Date end = new Date();
			System.out.println("BEAN反序列化耗时：" +(end.getTime() - start.getTime())+" 毫秒");
			write(reportMessage, "D:\\reportMessage.XML");
		}
		byte[] tmp = null;
		{
			Date start = new Date();
			tmp = ProtoStuffSerializerUtil.serialize(object);
			Date end = new Date();
			System.out.println("BEAN序列化_protoStuff耗时：" +(end.getTime() - start.getTime())+" 毫秒");
		}
		
		{
			Date start = new Date();
			ReportMessage reportMessage = ProtoStuffSerializerUtil.deserialize(tmp,ReportMessage.class);
			Date end = new Date();
			System.out.println("BEAN反序列化_protoStuff耗时：" +(end.getTime() - start.getTime())+" 毫秒");
			write(reportMessage, "D:\\reportMessage2.XML");
		}*/
//		String path = "D:/SVN/hadoop/0100 项目明细/010013双网双卡-信用卡决策/001 需求文档/ReportMessage.xml";
//		ReportMessage reportMessage = read(path, ReportMessage.class);
//		System.out.println(reportMessage);
		
	}

	public static void testRead() {
		Person person2 = read("d:\\cache\\person.xml", Person.class);
		System.out.println(person2);
//		System.out.println(JSONUtil.object2Json(person2));
	}

	public static void testWrite() {
		Address address = new Address();

		// write(address, "d:\\cache\\test.xml");

		// Address p = read("d:\\cache\\test.xml", Address.class);
		// System.out.println(p);
		// System.out.println(JSONUtil.object2Json(p));
		Person person = new Person();
		person.setName("Stan");
		person.setAddress(address);
		person.setGender(Gender.MALE);

		List<Job> jobs = new ArrayList<Job>();
		Job job = new Job();
		job.setComputer("MingLuck");
		job.setDesc("java programmer");
		jobs.add(job);
		Job job2 = new Job();
		job2.setComputer("Pectera");
		job2.setDesc("big data");
		jobs.add(job2);

		person.setJobs(jobs);

		write(person, "d:\\cache\\person.xml");
	}

	/**
	 * 对象转xml
	 * 
	 * @param clas
	 */
	public static <T> void write(T t, String filePath) {
		try {
			JAXBContext context = JAXBContext.newInstance(t.getClass());
			Marshaller marshaller = context.createMarshaller();
			FileWriter fileWriter = new FileWriter(filePath);
			marshaller.marshal(t, System.out);
			System.out.println();
			// 是否格式化生成的xml串 
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

			marshaller.setProperty(Marshaller.JAXB_ENCODING, "GBK");// 编码格式
			// 我在实际开发中重新封装了JAXB基本类，这里就使用到了该属性。不过默认的编码格式UTF-8
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			// 是否省略xml头信息(<?xml version="1.0" encoding="gb2312"
			// standalone="yes"?>)
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);

//			marshaller.marshal(t, System.out);
			marshaller.marshal(t, fileWriter);
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 下面代码演示将上面生成的xml转换为对象
	 * 
	 */
	public static <T> T read(String xmlFilePath, Class<T> clas) {
		T t = null;
		try {
			JAXBContext context = JAXBContext.newInstance(clas);
//			FileReader fileReader = new FileReader(xmlFilePath);
			String xml = FileUtils.readFileToString(new File(xmlFilePath),"UTF-8");
			System.out.println(xml);
			System.out.println("------------------------------------");
			Unmarshaller unmarshaller = context.createUnmarshaller();
			
//			t = (T) unmarshaller.unmarshal(new FileReader(new File(xmlFilePath)));
			t = (T) unmarshaller.unmarshal(new StringReader(xml));
			
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			marshaller.marshal(t, System.out);
			System.out.println();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JAXBException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return t;
	}
}

@XmlRootElement // 表示person是一个根元素
@XmlAccessorType(XmlAccessType.FIELD)
class Person implements Serializable {
	private static final long serialVersionUID = -1613717988236764406L;
	private String name;
	private Address address;
	private Gender gender;

	@XmlElementWrapper(name = "works")
	@XmlElement(name = "job")
	private List<Job> jobs;

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}
}

enum Gender {
	MALE(true), FEMALE(false);

	private boolean value;

	Gender(boolean _value) {
		this.value = _value;
	}
}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class Address {
	private String country = "中国";
	private String state = "上海";
	private String city = "上海浦东";
	private String street = "川沙";
	private String zipcode = "200000";

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

}

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
class Job {
	private String computer;
	private String desc;

	public String getComputer() {
		return computer;
	}

	public void setComputer(String computer) {
		this.computer = computer;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
