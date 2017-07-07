package demo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class HttpRequest {
	private static String CHARSET = "utf-8";
	private static transient Log logger = LogFactory.getLog(HttpRequest.class);

	public static String METHOD_GET = "GET";
	public static String METHOD_POST = "POST";

	/**
	 * 请求http接口方法
	 * 
	 * @param url
	 * @param body
	 * @param method
	 * @return
	 * @throws IOException 
	 */
	public static String httpRequest(String url, String body, String method) throws IOException {
		Map<String,String> requestPropMap=new HashMap<String,String>();
		requestPropMap.put("Content-Type",  "text/html;charset=" + CHARSET);
		return httpRequest(url,body,method,requestPropMap);
	}

	
	/**
	 * 请求http接口方法
	 * 
	 * @param url
	 * @param body
	 * @param method
	 * @return
	 * @throws IOException 
	 */
	public static String httpRequest(String url, String body, String method,Map<String,String> requestPropMap) throws IOException {
		OutputStream outputStream=null;
		HttpURLConnection httpUrlConnection =null;
		InputStream inputStream =null;
		try {
			URL urlObject = new URL(url);
			httpUrlConnection = (HttpURLConnection) urlObject.openConnection();
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setConnectTimeout(30 * 1000);
			httpUrlConnection.setRequestMethod(method);
			httpUrlConnection.setUseCaches(false);
//			c.setRequestProperty("Content-Type", "text/html;charset=" + CHARSET);
//			c.setRequestProperty("Content-Type", "text/html;charset=GBK" );
			if(requestPropMap!=null && requestPropMap.size()>0){
				 Iterator<String> keyIt=requestPropMap.keySet().iterator();
				 while(keyIt.hasNext()){
					 String key=keyIt.next();
					 String value=requestPropMap.get(key);
					 httpUrlConnection.setRequestProperty(key, value);
				 }
			}
			httpUrlConnection.connect();
			
			if (method.toUpperCase() == "POST" || method.toUpperCase() == "PUT") {
				outputStream = httpUrlConnection.getOutputStream();
				outputStream.write(body.getBytes("UTF-8"));
				outputStream.flush();
			}
			if(outputStream != null){outputStream.close();}
			if (httpUrlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				inputStream = httpUrlConnection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, CHARSET));

				String line = null;
				StringBuilder sb = new StringBuilder();
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line);
				}
				if(bufferedReader!= null){
					bufferedReader.close();
				}
				if(inputStream!=null){
					inputStream.close();
				}
				if(httpUrlConnection!=null){
					httpUrlConnection.disconnect();
				}
				return sb.toString();
			}
		} catch (MalformedURLException e) {
			logger.error("连接错误url:" + url);
		} catch (UnsupportedEncodingException e) {
			logger.error("连接错误url:" + url);
		} catch (SocketTimeoutException e) {
			logger.error("连接超时url:" + url);
		} catch (IOException e) {
			logger.error("连接错误url:" + url);
		}finally{
			if(outputStream!=null ){
				outputStream.close();
			}
			if(inputStream!=null){
				inputStream.close();
			}
			if(httpUrlConnection!=null){
				httpUrlConnection.disconnect();
			}
		}
		return null;
	}
	
	
	/**
	 * 请求https接口方法
	 * 
	 * @param url
	 * @param body
	 * @param method
	 * @return
	 */
	public static String httpsRequest(String url, String body, String method) {
		System.out.println("请求URL:\t" + url);
		URL urlObject = null;
		HttpsURLConnection httpsUrlConnection = null;
		InputStreamReader inputStreamReader = null;
		try {
			// 创建URL对象
			urlObject = new URL(url);
			// 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
			httpsUrlConnection = (HttpsURLConnection) urlObject.openConnection();
			// 设置请求头自定义参数
			// sslConn.setRequestProperty("access_token", access_token);
			// 取得该连接的输入流，以读取响应内容
			inputStreamReader = new InputStreamReader(httpsUrlConnection.getInputStream());
			// 读取服务器的响应内容并显示
			int respInt = inputStreamReader.read();
			StringBuilder sb = new StringBuilder();
			while (respInt != -1) {
				sb.append((char) respInt);
				respInt = inputStreamReader.read();
			}
			if(inputStreamReader != null){inputStreamReader.close();}
			if(httpsUrlConnection != null){httpsUrlConnection.disconnect();;}
			return sb.toString();
		} catch (MalformedURLException e) {
			logger.error("连接错误url:" + url);
		} catch (UnsupportedEncodingException e) {
			logger.error("连接错误url:" + url);
		} catch (SocketTimeoutException e) {
			logger.error("连接超时url:" + url);
		} catch (IOException e) {
			logger.error("连接错误url:" + url);
		} finally {
			// 释放资源
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
					// 异常处理
				} catch (IOException e) {
					logger.error("IOException:InputStreamReader.close() error.");
				}
			}
			if (httpsUrlConnection != null) {
				httpsUrlConnection.disconnect();
			}
		}
		return "";
	}

	/**
	 * https请求
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String httpsRequestOrg(String url, String charset, NameValuePair... params) {
		String result = null;
		HttpClient client = new HttpClient();
		client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, charset);
		GetMethod method = new GetMethod(url);
		method.setQueryString(params);
		logger.debug(String.format("请求接口：%s, 请求参数: %s", url, method.getQueryString()));
		logger.debug(String.format("请求接口：%s, 请求参数: %s", url, method.getQueryString()));

		try {
			client.executeMethod(method);
			// 返回的json信息
			result = new String(method.getResponseBodyAsString());
			method.releaseConnection();
		} catch (Exception e) {
			throw new RuntimeException("网络连接失败！");
		}
		return result;
	}
	
	
	
	
}
