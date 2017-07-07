package demo.webservice.cxf.Demo.client;

import demo2.server.IMyservice;
import demo2.server.MyServiceImplService;

public class MyClient2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MyServiceImplService myServiceImplService = new MyServiceImplService();
		IMyservice myServer = myServiceImplService.getMyServiceImplPort();
		System.out.println(myServer.add(2, 9));
	}

}
