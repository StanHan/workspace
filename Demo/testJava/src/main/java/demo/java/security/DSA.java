package demo.java.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import demo.java.io.IoDemo;
import demo.java.util.Base64Demo;

/**
 * <h1>数字签名</h1>
 * <p>
 * 所谓数字签名就是信息发送者用其私钥对从所传报文中提取出的特征数据（或称数字指纹）进行 RSA 算法操作，以保证发信人无法抵赖曾发过该信息（即不可抵赖性），同时也确保信息报文在经签名后末被篡改（即完整性）。
 * 当信息接收者收到报文后，就可以用发送者的公钥对数字签名进行验证。 在数字签名中有重要作用的数字指纹是通过一类特殊的散列函数（HASH 函数）生成的，对这些 HASH 函数的特殊要求是：
 * <li>接受的输入报文数据没有长度限制；
 * <li>对任何输入报文数据生成固定长度的摘要（数字指纹）输出
 * <li>从报文能方便地算出摘要；
 * <li>难以对指定的摘要生成一个报文，而由该报文反推算出该指定的摘要；
 * <li>两个不同的报文难以生成相同的摘要
 * 
 * 
 * <h1>DSA-Digital Signature Algorithm</h1>
 * <p>
 * 是Schnorr和ElGamal签名算法的变种，被美国NIST作为DSS(DigitalSignature Standard)。
 * 简单的说，这是一种更高级的验证方式，用作数字签名。不单单只有公钥、私钥，还有数字签名。私钥加密生成数字签名，公钥验证数据及签名。 如果数据和签名不匹配则认为验证失败！ 即 传输中的数据
 * 可以不再加密，接收方获得数据后，拿到公钥与签名验证数据是否有效
 * 
 */
public class DSA {
    // 不仅可以使用DSA算法，同样也可以使用RSA算法做数字签名
    /*
     * public static final String KEY_ALGORITHM = "RSA"; public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
     */
    public static void main(String[] args) throws Exception {
        demo();
    }

    static void demo() throws Exception {
        String dataStr = "hello world.";
        byte[] data = dataStr.getBytes(StandardCharsets.UTF_8);

        KeyPair keyPair = initKey("$%^*%^()(HJG8awfjas7");// 构建密钥

        // 产生签名
        String sign = signToBase64Str(data, keyPair.getPrivate());
        System.out.println("基于私钥对数据进行签名：" + sign);

        // 验证签名
        boolean verify1 = verifyWithPublicKey("aaa".getBytes(StandardCharsets.UTF_8), keyPair.getPublic(), sign);
        System.err.println("经验证 数据和签名匹配:" + verify1);

        boolean verify = verifyWithPublicKey(data, keyPair.getPublic(), sign);
        System.err.println("经验证 数据和签名匹配:" + verify);
    }

    static void demo3() throws Exception {
        for (int i = 0; i < 10; i++) {
            System.out.println("---------------非随机---------------");
            initKey(null);
            System.out.println("-----------------随机生成器-------------");
            initKey("Stan");
        }
    }

    /**
     * 生成他的密钥对 , 并且分别保存。 分别保存在 myprikey.dat 和 mypubkey.dat 中 , 以便下次不在生成，因为生成密钥对的时间比较长
     * 
     * @throws IOException
     */
    public static void saveKeyPair(KeyPair keyPair) throws IOException {
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();
        IoDemo.saveObjectToFile(publicKey, "D:/logs/myprikey.dat");
        IoDemo.saveObjectToFile(privateKey, "D:/logs/mypubkey.dat");
    }

    /**
     * 读取私钥
     * 
     * @param filePath
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static PrivateKey readPrivateKeyFromFile(String filePath) throws ClassNotFoundException, IOException {
        Object object = IoDemo.readObjectFromFile(filePath);
        return (PrivateKey) object;
    }

    /**
     * 读取公钥
     * 
     * @param filePath
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static PublicKey readPublicKeyFromFile(String filePath) throws ClassNotFoundException, IOException {
        Object object = IoDemo.readObjectFromFile(filePath);
        return (PublicKey) object;
    }

    /**
     * 生成密钥
     * 
     * @param seed
     *            种子
     * @return 密钥对象
     * @throws Exception
     */
    public static KeyPair initKey(String seed) throws Exception {
        return KeyPairDemo.initKey("DSA", 512, seed);
    }

    /**
     * 取得私钥，并用BASE64算法编码
     * 
     * @param keyPair
     * @return
     * @throws Exception
     */
    public static String base64EncodePrivateKey(KeyPair keyPair) {
        PrivateKey privateKey = keyPair.getPrivate();
        return Base64Demo.encode(privateKey.getEncoded()); // base64加密私钥
    }

    /**
     * 取得公钥，并用BASE64算法编码
     * 
     * @param keyPair
     * @return
     * @throws Exception
     */
    public static String base64EncodePublicKey(KeyPair keyPair) {
        PublicKey publicKey = keyPair.getPublic();
        return Base64Demo.encode(publicKey.getEncoded()); // base64加密公钥
    }

    /**
     * 用他私人密钥 (prikey) 对他所确认的信息 (info) 进行数字签名产生一个签名数组
     * 
     * @param myinfo
     * @throws SignatureException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws ClassNotFoundException
     */
    static byte[] signWithPrivateKey(String myinfo, PrivateKey privateKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signet = Signature.getInstance("DSA");
        signet.initSign(privateKey);
        signet.update(myinfo.getBytes(StandardCharsets.UTF_8));
        byte[] signed = signet.sign();
        return signed;
    }

    /**
     * 校验数字签名
     * 
     * @param data
     *            加密数据
     * @param publicKeyStr
     * @param sign
     *            数字签名
     * @return
     * @throws Exception
     */
    public static boolean verifyWithPublicKey(byte[] data, PublicKey publicKey, String sign) throws Exception {
        return SignatureDemo.verifyWithPublicKey("DSA", data, publicKey, sign);
    }

    /**
     * 用私钥对信息进行数字签名
     * 
     * @param data
     *            加密数据
     * @param privateKeyStr
     *            私钥-base64加密的
     * @return
     * @throws Exception
     */
    public static String signToBase64Str(byte[] data, PrivateKey privateKey) throws Exception {
        return SignatureDemo.signToBase64Str("DSA", data, privateKey);
    }

}
