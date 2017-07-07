package demo.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.UUID;

import com.google.protobuf.InvalidProtocolBufferException;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import test.ProtocolBuffer.CustomerCreditDataProto.CustomerCreditData;

public class SendPostRequest {
	private static final String appKey = "5ac9eb8068b141a8bbd456e8bf308837";
	private static final String appSecret = "6d0584c7f6a64789";
	
	public static void main(String[] args) throws Exception {
		/*System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
		// Nonce
		UUID uuid = UUID.randomUUID();
		String nonce = uuid.toString().replace("-", "");
		// Create,UTC时间
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		String create = sdf.format(new Date());
		String ip = "http://10.240.97.198:20061/BigData/CreditChecking/GetUserCreditByKey/v1";
//		String ip = "http://localhost:8888/BigData/CreditChecking/GetUserCreditByKey/v1";
		// 消息体
//		String reqBody = "Table=ApiUserCredit&Keys=18516552831&KeyType=0";
//		String reqBody = "Table=ApiUserCredit&Keys=13162415293&KeyType=0";
		String reqBody = "Table=apiusercredit&Keys=18516552831&KeyType=0";
		
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
		BASE64Encoder base64Encoder = new BASE64Encoder();
		String passwordDigest = base64Encoder.encode(messageDigest.digest(passwd));
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
		OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");

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
					strResponse.append(rspHeaders.get(key).get(0) + "\n");
				} else {
					strResponse.append(key + ": " + rspHeaders.get(key).get(0) + "\n");
				}
			}

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
			while ((strLine = reader.readLine()) != null) {
				strResponse.append("\n" + strLine);
			}

			System.out.println(strResponse);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
		System.out.println("------------------------------");*/
		parseCustomerCreditData();
	}
	
	public static void parseCustomerCreditData() throws Base64DecodingException, IOException{
//		String response="CkBjMzY4ZmExODZhODg4ZjFjYjg2MDM4ZjI1NTQ2MjBiMjE0OGI4YzgyNjA5NTU0MjQ1NWExY2RjYzdiNDEwMTk3EiA4ZTFkODFjYjhkMzcyN2YxNzMyMjRjYTgzNTNmN2I1ZhoDMTAwIAEoDzABOPUPQABIAFAAWABgBWgAcAB6LeeZveWfjuW4gua0ruWMl+WMuuaYjuS7geihl+mBk+WNgeWFreWnlOWbm+e7hIABAogBAJABAJgBQaABF6gBN7ABALgBGg==";
//		String response="ChIyMjA4MDIxOTg1MDQyMDkwMTMSCzEzMTYyNDE1MjkzGgMxMDAgASgPMAE49Q9AAEgAUABYAGAFaABwAHot55m95Z+O5biC5rSu5YyX5Yy65piO5LuB6KGX6YGT5Y2B5YWt5aeU5Zub57uEgAECiAEAkAEAmAFBoAEXqAE3sAEAuAEa";
//		String response="CkBjZTBmMTY5ODk1NmEwMzJhMzhiNzU4M2JlYjVmNzNlN2FmODkyNjZjZGYxNGI4Y2VjYWQ4ZjkzNzcwODkyZDFkEiA1OWUxYmI5MTdiMmQxZTE0MjYzZTA5N2M4OTkxNGUyORoDMjAwIAEoCjABONEcQABIAFAAWABgEWgAcAB6RuS4iua1t+S4iua1t+W4gua1puS4nOaWsOWMuua9jeWdiui3rzIwMOWPt++8iOS4iua1t+mTtuihjOe9keeCuTPmpbzvvImAAQKIAQCQAQCYAUGgARCoATmwAQC4ASA=";
		String response="ChIzMjExODExOTg4MTIwODYwMTASCzE4NTE2NTUyODMxGgMyMDAgASgKMAE40RxAAEgAUABYAGARaABwAHpG5LiK5rW35LiK5rW35biC5rWm5Lic5paw5Yy65r2N5Z2K6LevMjAw5Y+377yI5LiK5rW36ZO26KGM572R54K5M+alvO+8iYABAogBAJABAJgBQaABEKgBObABALgBIA==";
		byte[] byte_result = Base64.decode(response.getBytes());
		CustomerCreditData bean = CustomerCreditData.parseFrom(byte_result);
		BASE64Decoder base64Decoder = new BASE64Decoder();
		byte[] buffer = base64Decoder.decodeBuffer(response);
		CustomerCreditData bean2 = CustomerCreditData.parseFrom(buffer);
		System.out.println(bean.toString());
		System.out.println("-------------------------------");
		System.out.println(bean2.toString());
	}
}