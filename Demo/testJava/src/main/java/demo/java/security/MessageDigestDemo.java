package demo.java.security;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.IntStream;

import demo.java.lang.HexDemo;

/**
 * <h1>消息摘要（Message Digest）</h1><br>
 * 又称为数字摘要(Digital Digest)。它是一个唯一对应一个消息或文本的固定长度的值，它由一个单向Hash加密函数对消息进行作用而产生。
 * HASH函数的抗冲突性使得如果一段明文稍有变化，哪怕只更改该段落的一个字母，通过哈希算法作用后都将产生不同的值。而HASH算法的单向性使得要找到到哈希值相同的两个不 同的输入消息，在计算上是不可能的。
 * 消息摘要算法的主要特征是加密过程不需要密钥，并且经过加密的数据无法被解密，只有输入相同的明文数据经过相同的消息摘要算法才能得到相同的密文。消息摘要算法不存在 密钥的管理与分发问题，适合于分布式网络相同上使用。
 * <p>
 * 
 *
 */
public class MessageDigestDemo {

    public static void main(String[] args) {
        MessageDigest md5 = getMd5Digest();
        IntStream.range(1, 4).forEach(e -> {
            System.out.println(HexDemo.bytes2Hex(md5.digest(Integer.valueOf(e).toString().getBytes())));
        });
    }

    static void demo() {
        MessageDigest messageDigest = getMd5Digest();
        System.out.println(HexDemo.byteArrayToHexString(messageDigest.digest("Stan".getBytes(StandardCharsets.UTF_8))));
        System.out.println(HexDemo.byteArrayToHexString(messageDigest.digest("Stan".getBytes(StandardCharsets.UTF_8))));
        System.out.println(HexDemo.byteArrayToHexString(messageDigest.digest("Stan".getBytes(StandardCharsets.UTF_8))));
        System.out.println(HexDemo.byteArrayToHexString(messageDigest.digest("Stan".getBytes(StandardCharsets.UTF_8))));
        messageDigest.update("Stan".getBytes(StandardCharsets.UTF_8));
        messageDigest.reset();
        System.out.println(HexDemo.byteArrayToHexString(messageDigest.digest()));
        messageDigest.update("Stan".getBytes(StandardCharsets.UTF_8));
        System.out.println(HexDemo.byteArrayToHexString(messageDigest.digest()));
        messageDigest.update("".getBytes(StandardCharsets.UTF_8));
        System.out.println(HexDemo.byteArrayToHexString(messageDigest.digest()));
        messageDigest.reset();
        System.out.println(HexDemo.byteArrayToHexString(messageDigest.digest()));
    }

    /**
     * 获取 MD5 消息摘要实例
     * 
     * @return MD5 消息摘要实例
     */
    public static MessageDigest getMd5Digest() {
        return getDigest("MD5");
    }

    /**
     * 获取 SHA-1 消息摘要实例
     * 
     * @return SHA-1 消息摘要实例
     */
    public static MessageDigest getShaDigest() {
        return getDigest("SHA");
    }

    /**
     * 获取 SHA-256 消息摘要实例
     * 
     * @return SHA-256 消息摘要实例
     */
    public static MessageDigest getSha256Digest() {
        return getDigest("SHA-256");
    }

    /**
     * 获取 SHA-384 消息摘要实例
     * 
     * @return SHA-384 消息摘要实例
     */
    public static MessageDigest getSha384Digest() {
        return getDigest("SHA-384");
    }

    /**
     * 获取 SHA-512 消息摘要实例
     * 
     * @return SHA-512 消息摘要实例
     */
    public static MessageDigest getSha512Digest() {
        return getDigest("SHA-512");
    }

    /**
     * 根据给定摘要算法创建一个消息摘要实例
     * 
     * @param algorithm
     *            摘要算法名
     * @return 消息摘要实例
     * @see MessageDigest#getInstance(String)
     * @throws RuntimeException
     *             当 {@link java.security.NoSuchAlgorithmException} 发生时
     */
    public static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 获取字符串UTF-8编码的MD5摘要信息，转成16进制的字符串
     * 
     * @param targetStr
     * @return
     */
    public static String getMd5DigestHexStr(String targetStr) {
        MessageDigest messageDigest = getMd5Digest();
        messageDigest.update(targetStr.getBytes(StandardCharsets.UTF_8));
        byte[] byteArray = messageDigest.digest();
        String md5HexStr = HexDemo.bytes2Hex(byteArray);
        return md5HexStr;

    }
}