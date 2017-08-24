package demo.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import demo.util.DateUtil;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import jodd.util.MimeTypes;
import jodd.util.StringPool;

public class ChinaUnicom {
	/*
	 * String appKey = "5ac9eb8068b141a8bbd456e8bf308837"; String appSecret =
	 * "6d0584c7f6a64789"; String host = "aep.sdp.com"; String method = "POST";
	 * String contentType = "text/plain; charset=UTF-8"; String secretType =
	 * "SHA-1"; String connectType = "Keep-Alive"; int timeOut = 10000;
	 */

	private static final String contentType = "application/x-www-form-urlencoded";
	private static final String url_test = "http://10.240.97.198:20061/BigData/CreditChecking/GetUserCreditByKey/v1";
	private static final String appKey = "5ac9eb8068b141a8bbd456e8bf308837";
	private static final String appSecret = "6d0584c7f6a64789";
	private static final String REALM = "SDP";
	private static final String PROFILE = "UsernameToken";
	private static final String TYPE = "Appkey";
	
	public static Encoder encoder = Base64.getEncoder();
	public static Decoder decoder = Base64.getDecoder();

	String a = "WSSE realm=\"SDP\", profile=\"UsernameToken\", type=\"AppKey\"";

	public static void main(String[] args) throws Exception {
		String requestBody = buildRequest("13162415293,18516552831,13353739193");
		request(requestBody, url_test);
		System.out.println("==================================");
		send(url_test, requestBody, StandardCharsets.UTF_8.name());
	}

	public static String buildRequest(String telephoneNo) {
		return "Table=apiusercredit&KeyType=0&Keys=" + telephoneNo;
	}

	public static void testBase64() throws UnsupportedEncodingException {
		String base64String = "CkBmMjRhNmY1YzUwZDNlOTI3N2UyZTRkMTQwNjBjZjQ0ZmFjZTc3Y2NmMzNlNzE2ODNjODhmODlhMmYwMjY0ZDI3EiAwMDAwMDAyMzljYmZjMjJmYjM1YWE2Mzg1NDM1ZjlkYxgAIAIoYjABOMq/AUAASABQAFgAYAdoMXAKeCeAASqIAUY=";
		String a = new String(decoder.decode(base64String), StandardCharsets.UTF_8);
		System.out.println(a);
	}

	public static String request(String reqBody, String address) throws IOException, NoSuchAlgorithmException {
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
		// Nonce
		UUID uuid = UUID.randomUUID();
		String nonce = uuid.toString().replace("-", "");
		// Create,UTC时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String create = sdf.format(new Date());
		String ip = "http://10.240.97.198:20061/BigData/CreditChecking/GetUserCreditByKey/v1";
		// String ip =
		// "http://localhost:8888/BigData/CreditChecking/GetUserCreditByKey/v1";
		// 消息体

		// method
		String method = "POST";
		// HOST
		String host = "10.240.97.198:20061";

		String contentType = "application/x-www-form-urlencoded";
		// 加密方法
		String secretType = "SHA-1";
		// 计算passwordDigest
		byte[] passwd = (nonce + create + appSecret).getBytes();

		MessageDigest messageDigest = MessageDigest.getInstance(secretType);
		byte[] array = messageDigest.digest(passwd);
		byte[] passwordDigest = encoder.encode(array);
		// 设定连接的相关参数
		URL url = new URL(ip);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setReadTimeout(10000);
		connection.setRequestMethod(method);
		connection.setRequestProperty("Host", host);
		connection.setRequestProperty("Content-Type", contentType);
		connection.setRequestProperty("Authorization",
				"WSSE realm=\"SDP\", profile=\"UsernameToken\", type=\"Appkey\"");
		connection.setRequestProperty("X-WSSE", "UsernameToken Username=\"" + appKey + "\", PasswordDigest=\""
				+ passwordDigest + "\", Nonce=\"" + nonce + "\", Created=\"" + create + "\"");
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);

		out.write(reqBody);
		out.flush();
		out.close();

		// 获取服务端的反馈

		StringBuilder strResponse = new StringBuilder();
		try {
			Map<String, List<String>> rspHeaders = connection.getHeaderFields();

			Set<String> rspHeadNames = rspHeaders.keySet();
			for (String key : rspHeadNames) {
				if (null == key) {
					strResponse.append(rspHeaders.get(key).get(0));
				} else {
					strResponse.append(key + " " + rspHeaders.get(key).get(0));
				}
			}
			System.out.println(strResponse.toString());
			System.out.println("------------------");
			
			int code = connection.getResponseCode();
			String message = connection.getResponseMessage();
			System.out.println(message + " " + code);
			InputStream in;

			if (code == HttpURLConnection.HTTP_OK) {
				in = connection.getInputStream();
			} else {
				in = connection.getErrorStream();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String strLine = "";
			StringBuilder strResponse2 = new StringBuilder();
			while ((strLine = reader.readLine()) != null) {
				strResponse2.append(strLine);
			}

			System.out.println(strResponse2);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		System.out.println("------------------------------");

		return strResponse.toString();
	}

	/**
	 * 发送信息
	 * 
	 * @param url
	 * @param xml
	 * @param encoding
	 * @return
	 * @throws Exception
	 */
	public static String send(String url, String xml, String encoding) throws Exception {
		UUID uuid = UUID.randomUUID();
		String nonce = uuid.toString().replace("-", "");
		// Create,UTC时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String createdTime = sdf.format(new Date());
		String passwordDigest = encrypt(nonce, createdTime, appSecret);

		HttpRequest httpRequest = HttpRequest.post(url)
//		.contentType(contentType)
		.header("Authorization", "WSSE realm=\"SDP\", profile=\"UsernameToken\", type=\"AppKey\"")
		.header("X-WSSE", "UsernameToken Username=\"" + appKey + "\", PasswordDigest=\"" + passwordDigest + "\", Nonce=\""+ nonce +"\", Created=\"" + createdTime + "\"")
//		.bodyText(xml,MimeTypes.MIME_APPLICATION_XML,StringPool.UTF_8)
		.bodyText(xml,contentType,StringPool.UTF_8)
		.connectionKeepAlive(true)
		.accept(MimeTypes.MIME_TEXT_HTML);
		
		System.out.println("httpRequest");
		System.out.println(httpRequest.toString());
		
		HttpResponse response = httpRequest.send();
		System.out.println("HttpResponse");
		System.out.println(response.toString());
		
		/*HttpResponse response = HttpRequest.post(url).charset(encoding)
				.header("Authorization", "WSSE realm=\"SDP\", profile=\"UsernameToken\", type=\"AppKey\"")
				.header("X-WSSE",
						"UsernameToken Username=\"" + appKey + "\", PasswordDigest=\"" + passwordDigest + "\", Nonce=\""
								+ nonce + "\", Created=\"" + createdTime + "\"")
				.body(xml.getBytes(encoding), contentType).charset(encoding).send();*/

		System.out.println("response: " + response.toString());

		int statusCode = response.statusCode();
		System.out.println("statusCode:" + statusCode);
		String responseBody = response.bodyText();
		System.out.println(responseBody);

		if (statusCode == 200) {
			return responseBody;
		} else {
			throw new Exception("返回信息：" + statusCode);
		}
	}

	public static void parseResponse() throws UnsupportedEncodingException {
		String response = "CkBjZTBmMTY5ODk1NmEwMzJhMzhiNzU4M2JlYjVmNzNlN2FmODkyNjZjZGYxNGI4Y2VjYWQ4ZjkzNzcwODkyZDFkEiA1OWUxYmI5MTdiMmQxZTE0MjYzZTA5N2M4OTkxNGUyORoDMjAwIAEoCjABONEcQABIAFAAWABgEWgAcAB6RuS4iua1t+S4iua1t+W4gua1puS4nOaWsOWMuua9jeWdiui3rzIwMOWPt++8iOS4iua1t+mTtuihjOe9keeCuTPmpbzvvImAAQKIAQCQAQCYAUGgARCoATmwAQC4ASA=";
		// BASE64Encoder encoder = new BASE64Encoder();
		byte[] byte_result = decoder.decode(response.getBytes(StandardCharsets.UTF_8));
		String result = new String(byte_result, StandardCharsets.UTF_8);
		System.out.println(result);
	}

	/**
	 * 摘要算法如下PasswordDigest = Base64 (SHA-1 (nonce + created + password)).
	 * 
	 * @param password
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String encrypt(String nonce, String createTime, String password) throws NoSuchAlgorithmException {
		// 加密方法
		String secretType = "SHA-1";
		// 计算passwordDigest
		byte[] passwd = (nonce + createTime + password).getBytes();

		MessageDigest alga = MessageDigest.getInstance(secretType);
		byte[] passwordDigest = encoder.encode(alga.digest(passwd));
		System.out.println("passwordDigest :" + passwordDigest);
		return new String(passwordDigest);
	}

	/**
	 * 获取随机数
	 * 
	 * @return
	 */
	public static String getNonce() {
		UUID uuid = UUID.randomUUID();
		String nonce = uuid.toString().replace("-", "");
		System.out.println("nonce:" + nonce);
		return nonce;
	}

	/**
	 * 获取创建时间
	 * 
	 * @return
	 */
	public static String getCreatedTime() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String create = sdf.format(date);
		System.out.println("create:" + create);

		String datetime = DateUtil.dateToStr(date, DateUtil.TZ);// 创建时间
		System.out.println("CreatedTime :" + datetime);

		System.out.println(create.equals(datetime));
		return datetime;
	}
}
