package demo.java.codec;

import java.io.UnsupportedEncodingException;

public class Test {

	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		Base64 base64 = new MyBase64Encoder();
		String encode = base64.encode("a".getBytes("UTF-8"));
		System.out.println(encode);
		String decode = base64.backEncode("YQ==".getBytes("UTF-8"));
		System.out.println(decode);
		
		
	}

}
