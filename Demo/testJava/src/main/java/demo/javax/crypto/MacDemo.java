package demo.javax.crypto;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import demo.java.lang.HexDemo;
import demo.java.security.MD5Util;

/**
 * 消息认证码算法（Message Authentication Code，MAC）,是经过特定算法后产生的一小段信息，检查某段消息的完整性，以及作身份验证。
 * 
 * 在Java中支持的消息摘要算法有：HmacMD5、HmacSHA1、HmacSHA224、HmacSHA256、HmacSHA384、HmacSHA512等。
 * 
 * 消息认证码的算法中，通常会使用使用带密钥的散列函数，或者块密码的带认证工作模式（如CBC）。 信息鉴别码不能提供对信息的保密，若要同时实现保密认证，同时需要对信息进行加密。
 *
 */
public class MacDemo {

    public static void main(String[] args) throws Exception {
        // demoMacDigestStr();
        System.err.println(MD5Util.getMd5Str("按422828199001243938" + "20160622"));
        // Mac md5 = buildMac(HQMacAlgorithm.HmacMD5.getName(), null);
        // md5.update("42282819900124393820160622".getBytes());
        // byte[] digest = md5.doFinal();
        // System.err.println(HexDemo.bytes2Hex(digest));

        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("按42282819900124393820160622".getBytes(StandardCharsets.UTF_8));
        byte[] byteArray = messageDigest.digest();
        System.err.println(HexDemo.byteArrayToHexString(byteArray));
        
        messageDigest.update("按42282819900124393820160622".getBytes("GBK"));
        byteArray = messageDigest.digest();
        System.err.println(HexDemo.byteArrayToHexString(byteArray));
    }

    /**
     * 打印不同算法获取的摘要信息
     * 
     * @throws Exception
     */
    static void demoMacDigestStr() throws Exception {
        byte[] data = "StanHan".getBytes();
        System.out.println(HexDemo.bytes2Hex(data));
        HQMacAlgorithm[] algorithms = HQMacAlgorithm.values();
        for (HQMacAlgorithm algorithm : algorithms) {
            byte[] key = initKey(algorithm.getName());
            byte[] digest = encrypt(algorithm.getName(), data, key);
            String digestStr = HexDemo.bytes2Hex(digest);
            System.err.println(algorithm + ":" + digestStr);
        }
        System.out.println(MD5Util.md5("StanHan"));
    }

    /**
     * 根据算法和key值构建MAC
     * 
     * @param algorithm
     * @param key
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static Mac buildMac(String algorithm, byte[] key) throws NoSuchAlgorithmException {
        Mac mac = Mac.getInstance(algorithm);
        if (key != null) {
            try {
                SecretKey secretKey = new SecretKeySpec(key, algorithm);
                mac.init(secretKey);
            } catch (InvalidKeyException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return mac;
    }

    /**
     * 加密
     * 
     * @param algorithm
     * @param data
     * @param key
     * @return
     */
    public static byte[] encrypt(String algorithm, byte[] data, byte[] key) throws Exception {
        Mac mac = buildMac(algorithm, key);
        return mac.doFinal(data);
    }

    /**
     * 初始化密钥
     * 
     * @param algorithm
     * @return
     */
    public static byte[] initKey(String algorithm) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * Mac算法。在Java中支持的消息摘要算法有：HmacMD5、HmacSHA1、HmacSHA224、HmacSHA256、HmacSHA384、HmacSHA512等。
     * 
     */
    public static enum HQMacAlgorithm {

        HmacMD5("HmacMD5"),

        HmacSHA1("HmacSHA1"),

        HmacSHA224("HmacSHA224"),

        HmacSHA256("HmacSHA256"),

        HmacSHA384("HmacSHA384"),

        HmacSHA512("HmacSHA512");

        private String name;

        private HQMacAlgorithm(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
