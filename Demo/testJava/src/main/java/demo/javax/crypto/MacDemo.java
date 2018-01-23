package demo.javax.crypto;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import demo.java.lang.HexDemo;
import demo.java.security.MessageDigestDemo;

/**
 * 消息认证码算法（Message Authentication Code，MAC）,是经过特定算法后产生的一小段信息，检查某段消息的完整性，以及作身份验证。
 * 
 * 在Java中支持的消息摘要算法有：HmacMD5、HmacSHA1、HmacSHA224、HmacSHA256、HmacSHA384、HmacSHA512等。
 * 
 * 消息认证码的算法中，通常会使用使用带密钥的散列函数，或者块密码的带认证工作模式（如CBC）。 信息鉴别码不能提供对信息的保密，若要同时实现保密认证，同时需要对信息进行加密。
 *
 * HMAC-MD5就可以用一把发送方和接收方都有的key进行计算，而没有这把key的第三方是无法计算出正确的散列值的，这样就可以防止数据被篡改。
 * 
 * HMAC(Hash-based Message Authentication Code)是密钥相关的哈希运算消息认证码，HMAC运算利用哈希算法，以一个密钥和一个消息为输入，生成一个消息摘要作为输出。
 * <h2>HMAC的应用</h2>
 * <p>
 * HMAC的一个典型应用是用在“质疑/应答”（Challenge/Response）身份认证中。它的使用方法是这样的：
 * <li>(1) 客户端发出登录请求（假设是浏览器的GET请求）
 * <li>(2) 服务器返回一个随机值，并在会话中记录这个随机值
 * <li>(3) 客户端将该随机值作为密钥，用户密码进行hmac运算，然后提交给服务器
 * <li>(4) 服务器读取用户数据库中的用户密码和步骤2中发送的随机值做与客户端一样的hmac运算，然后与用户发送的结果比较，如果结果一致则验证用户合法
 * <p>
 * 这么做有什么好处呢？ 如果我们在登录的过程中，黑客截获了我们发送的数据，他也只能得到 hmac 加密过后的结果，由于不知道密钥，根本不可能获取到用户密码，从而保证了安全性。
 * 
 */
public class MacDemo {

    public static void main(String[] args) throws Exception {
        demoMacDigestStr("");
        System.out.println(MessageDigestDemo.getMd5DigestHexStr(""));
    }

    /**
     * 打印不同算法获取的摘要信息
     * 
     * @throws Exception
     */
    static void demoMacDigestStr(String str) throws Exception {
        byte[] data = str.getBytes(StandardCharsets.UTF_8);
        HMacAlgorithm[] algorithms = HMacAlgorithm.values();
        for (HMacAlgorithm algorithm : algorithms) {
            byte[] digest = encrypt(algorithm.getName(), data, initHmacKey(algorithm.getName()));
            String digestStr = HexDemo.bytes2Hex(digest);
            System.out.println(algorithm + ":" + digestStr);
        }
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
     * JDK 中自带了一个密钥生成器 KeyGenerator 用于帮助我们生成密钥
     * 
     * @param algorithm
     *            密钥算法 algorithm HmacMD5/HmacSHA/HmacSHA256/HmacSHA384/HmacSHA512
     * @return 密钥
     */
    public static byte[] initHmacKey(String algorithm) {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    /**
     * 获取 HmaMD5的密钥
     * 
     * @return HmaMD5的密钥
     * @throws RuntimeException
     *             当 {@link java.security.NoSuchAlgorithmException} 发生时
     */
    public static byte[] getHmaMD5key() {
        return initHmacKey("HmacMD5");
    }

    /**
     * 获取 HmaSHA的密钥
     * 
     * @return HmaSHA的密钥
     */
    public static byte[] getHmaSHAkey() {
        return initHmacKey("HmacSHA1");
    }

    /**
     * 获取 HmaSHA256的密钥
     * 
     * @return HmaSHA256的密钥
     */
    public static byte[] getHmaSHA256key() {
        return initHmacKey("HmacSHA256");
    }

    /**
     * 获取 HmaSHA384的密钥
     * 
     * @return HmaSHA384的密钥
     */
    public static byte[] getHmaSHA384key() {
        return initHmacKey("HmacSHA384");
    }

    /**
     * 获取 HmaSHA512的密钥
     * 
     * @return HmaSHA384的密钥
     */
    public static byte[] getHmaSHA512key() {
        return initHmacKey("HmacSHA512");
    }

    /**
     * HMAC算法(Hash-based Message Authentication Code)。
     * 
     * 在Java中支持的消息摘要算法有：HmacMD5、HmacSHA1、HmacSHA224、HmacSHA256、HmacSHA384、HmacSHA512等。
     * <li>算法种类
     * <li>HmacMD5， 摘要长度128
     * <li>HmacSHA1， 摘要长度160
     * <li>HmacSHA256， 摘要长度256
     * <li>HmacSHA384， 摘要长度384
     * <li>HmacSHA512， 摘要长度512
     * 
     * 
     */
    public static enum HMacAlgorithm {

        HmacMD5("HmacMD5"),

        HmacSHA1("HmacSHA1"),

        HmacSHA224("HmacSHA224"),

        HmacSHA256("HmacSHA256"),

        HmacSHA384("HmacSHA384"),

        HmacSHA512("HmacSHA512");

        private String name;

        private HMacAlgorithm(String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
