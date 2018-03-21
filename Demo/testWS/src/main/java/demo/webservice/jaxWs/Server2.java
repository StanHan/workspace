package demo.webservice.jaxWs;

import org.apache.cxf.jaxws.JaxWsServerFactoryBean;

public class Server2 {

	private final static String ADDRESS = "http://localhost:8080/test.webservice.jaxWs/HelloWorld";
	
	public static void main(String[] args) {
		HelloWorld hw = new HelloWorldImpl();
		JaxWsServerFactoryBean jwsFactory = new JaxWsServerFactoryBean();
		jwsFactory.setAddress(ADDRESS);	//指定WebService的发布地址
		jwsFactory.setServiceClass(HelloWorld.class);//WebService对应的类型
		jwsFactory.setServiceBean(hw);//WebService对应的实现对象
		jwsFactory.create();
	}

}
