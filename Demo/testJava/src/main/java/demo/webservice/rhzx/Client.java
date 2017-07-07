package demo.webservice.rhzx;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import com.icfcc.prefint.cums.htmlparse.webservice.services.GeneralXmlServiceImpDelegate;
import com.icfcc.prefint.cums.htmlparse.webservice.services.GeneralXmlServiceImpService;
import com.icfcc.prefint.cums.htmlparse.webservice.services.HtmlParseRequest;
import com.icfcc.prefint.cums.htmlparse.webservice.services.HtmlParseResponse;

public class Client {

	public static void main(String[] args) {
		test2();
	}

	/**
	 * 人行新版
	 */
	public static void test2(){
		String url = "http://10.240.91.32:7001/icrqs/CreditReportService";
		//http://10.240.91.32:7001/icrqs/CreditReportService?wsdl
		QName qName = new QName("http://webservice.icrqs.cfcc.com/","CreditReportService");
		
		ObjectFactory objectFactory = new ObjectFactory();
		try {
			URL wsdl = new URL(url);
			CreditReportService creditReportService = new CreditReportService(wsdl,qName);
			CreditReportServiceDelegate delegate = creditReportService.getCreditReportServicePort();
			
			CuSingleRequest cuSingleRequest = objectFactory.createCuSingleRequest();
			cuSingleRequest.setCertno("370521196810170213");//证件号
			cuSingleRequest.setCerttype("0");//证件类型
			cuSingleRequest.setChannelid("00");
			cuSingleRequest.setClientip("10.240.139.82");
			cuSingleRequest.setCstmsysid("00");
			cuSingleRequest.setName("张晓轩");
			cuSingleRequest.setQryreason("02");
			cuSingleRequest.setQrytype("1");//查询类型    目前只能是 1 人行版信用报告
			//查询要求时限   可以填整数包括负数 
			//填写正数时代表 查询信用报告时，从今天开始向前正数的天数内的本地信用报告也是有效报告，存在本地直接查本地，不存在本地查人行 
			//填写0 代表不查询本地报告  
			//填写 负数代表 查询信用报告时，只查询从今天开始向前负数的绝对值的天数内的本地信用报告， 若不存在不查询人行
			cuSingleRequest.setQtimelimit("3");
			cuSingleRequest.setQuerymode("0");//查询人行方式   0-webservice  1-mq
			cuSingleRequest.setResulttype("0");//结果类型      0-xml    1-html   2-xml&html
			cuSingleRequest.setUserid("zhangzhy");
			
			System.out.println(cuSingleRequest);
			
			CuResult cuResult = delegate.sendCuRequest(cuSingleRequest);
			System.out.println(cuResult);
			
			CuGetResult cuGetResult = objectFactory.createCuGetResult();
			cuGetResult.setFlowid(cuResult.getFlowid());
			cuGetResult.setUserid(cuSingleRequest.getUserid());
			System.out.println(cuGetResult);
			
			CuSingleResult cuSingleResult = delegate.getCuResult(cuGetResult);
			System.out.println(cuSingleResult);
		} catch (Exception e) {
			e.printStackTrace();
		}

		
	}

	/**
	 * 人行旧版
	 */
	public static void test1() throws MalformedURLException {
		HtmlParseRequest vo = new HtmlParseRequest();
		vo.setQuerierName("苏冬");
		vo.setQuerierCertype("0");// 身份证
		vo.setQuerierCertno("220284199001124620");
		vo.setQueryFormat("30");
		vo.setQueryType("03");
		vo.setSysCode("cbk");
		vo.setOtherSysUserid("zhunan01");
		vo.setQueryReason("03");
		vo.setValidDays(30);

		// URL wsdlLocation = new
		// URL("http://localhost:7777/WebRoot/GeneralXmlServiceImpPort?wsdl");
		URL wsdlLocation = new URL("http://172.111.60.71:7020/WebRoot/GeneralXmlServiceImpPort?wsdl");
		// GeneralXmlServiceImpService service = new
		// GeneralXmlServiceImpService(wsdlLocation);
		GeneralXmlServiceImpService service = new GeneralXmlServiceImpService();
		GeneralXmlServiceImpDelegate delegate = service.getGeneralXmlServiceImpPort();

		HtmlParseResponse htmlParseResponse = delegate.getGeneralXml(vo);
		System.out.println("ErrorMessage:" + htmlParseResponse.getErrorMessage());
		System.out.println("ResultCode:" + htmlParseResponse.getResultCode());
		System.out.println(htmlParseResponse.getHtmlStr());
		System.out.println(htmlParseResponse.getBs());
		List<String> stringList = htmlParseResponse.getResultLineList();
		for (String string : stringList) {
			System.out.println(string);
		}
	}

}
