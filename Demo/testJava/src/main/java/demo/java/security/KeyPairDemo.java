package demo.java.security;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import demo.java.util.Base64Demo;

public class KeyPairDemo {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    /**
     * 密钥生成器，以指定的长度初始化 KeyPairGenerator 对象 , 如果没有初始化系统以 1024 长度默认设置
     * 
     * @param algorithm
     *            algorithm 算法名 . 如 :"DSA","RSA"
     * @param keysize
     * 
     *            算法位长 . 其范围必须在 512 到 1024 之间，且必须为 64 的倍数
     * @param seed
     *            种子
     * @return
     * @throws Exception
     */
    public static KeyPair initKey(String algorithm, int keysize, String seed) throws Exception {
        if (keysize < 512) {
            keysize = 1024;
        }
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        if (seed == null) {
            keyPairGenerator.initialize(keysize);
        } else {
            // 设定随机产生器
            SecureRandom secureRandom = new SecureRandom();
            secureRandom.setSeed(seed.getBytes(StandardCharsets.UTF_8));
            keyPairGenerator.initialize(keysize, secureRandom);
        }
        // 产生新密钥对
        KeyPair keyPair = keyPairGenerator.genKeyPair();
        printKeyPair(keyPair);
        return keyPair;
    }

    /**
     * 打印密钥对
     */
    public static void printKeyPair(KeyPair keyPair) {
        // 返回公钥
        PublicKey publicKey = keyPair.getPublic();
        printPublicKey(publicKey);
        // 返回私钥
        PrivateKey privateKey = keyPair.getPrivate();
        printPrivateKey(privateKey);
    }

    /**
     * 打印公钥
     * 
     * @param publicKey
     */
    public static void printPublicKey(PublicKey publicKey) {
        System.out.printf("公钥 Algorithm: %s,format：%s,长度：%d %n", publicKey.getAlgorithm(), publicKey.getFormat(),
                publicKey.getEncoded().length);
        System.out.println("公钥base64 encoded:" + Base64Demo.encode(publicKey.getEncoded()));
    }

    /**
     * 打印私钥
     * 
     * @param privateKey
     */
    public static void printPrivateKey(PrivateKey privateKey) {
        System.out.printf("私钥 Algorithm: %s,format：%s,长度：%d %n", privateKey.getAlgorithm(), privateKey.getFormat(),
                privateKey.getEncoded().length);
        System.out.println("私钥base64 encoded:" + Base64Demo.encode(privateKey.getEncoded()));
    }

    /**
     * public key 是用 X.509 编码的
     * 
     * @param keys
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey buildPublicKey(String algorithm, byte[] keys)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        X509EncodedKeySpec bobPubKeySpec = new X509EncodedKeySpec(keys);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PublicKey bobPubKey = keyFactory.generatePublic(bobPubKeySpec);
        printPublicKey(bobPubKey);
        return bobPubKey;
    }

    /**
     * Private key 是用 PKCS#8 编码
     * 
     * @param keys
     * @return
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PrivateKey buildPrivateKey(String algorithm, byte[] keys)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(keys);
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        PrivateKey privateKey = keyFactory.generatePrivate(priPKCS8);
        printPrivateKey(privateKey);
        return privateKey;
    }

}
