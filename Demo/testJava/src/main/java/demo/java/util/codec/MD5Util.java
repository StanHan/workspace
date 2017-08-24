package demo.java.util.codec;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("all")
public class MD5Util{

    //MD5加密之后32位
    public static synchronized String md5(String str)throws Exception{
		String code =  code(code(str,"MD5").substring(6,28),"MD5");
		return code;
	}

	//SHA-256加密之后64位
	public static String SHA_256(String str)throws Exception{
		String code =  code(code(str,"SHA-256").substring(6,28),"SHA-256");
		return code;
	}

	//SHA-512加密之后128位
	public static String SHA_512(String str)throws Exception{
			String code =  code(code(str,"SHA-512").substring(6,28),"SHA-512");
			return code;
	}

    private static String code(String str1,String str2)throws Exception{
		MessageDigest md = MessageDigest.getInstance(str2);
		md.update(str1.getBytes());
		byte bt[] = md.digest();
		StringBuffer bf = new StringBuffer();
		for(int i=0;i<bt.length;i++){
			bf.append(Integer.toString((bt[i] & 0xff)+0x100,16).substring(1));
		}
		return bf.toString();
	}
    
    /**
     * 银联电子签名MD5方法
     * @param text
     * @return
     */
    public static String unionPaymd5(String text) {
		MessageDigest msgDigest = null;
		try {
			msgDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException(
					"System doesn't support MD5 algorithm.");
		}
		try {
			msgDigest.update(text.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		byte[] bytes = msgDigest.digest();

		byte tb;
		char low;
		char high;
		char tmpChar;

		String md5Str = new String();

		for (int i = 0; i < bytes.length; i++) {
			tb = bytes[i];
			tmpChar = (char) ((tb >>> 4) & 0x000f);

			if (tmpChar >= 10) {
				high = (char) (('a' + tmpChar) - 10);
			} else {
				high = (char) ('0' + tmpChar);
			}
			md5Str += high;
			tmpChar = (char) (tb & 0x000f);

			if (tmpChar >= 10) {
				low = (char) (('a' + tmpChar) - 10);
			} else {
				low = (char) ('0' + tmpChar);
			}
			md5Str += low;
		}

		return md5Str;
	}
    
    public static void main(String[] args) {
		try {
			System.out.println(new MD5Util().unionPaymd5(new String("112233helloword".getBytes("utf-8"), "utf-8")));
			System.out.println(new MD5Util().md5("112233helloword"));
			System.out.println(md5("udsp"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}