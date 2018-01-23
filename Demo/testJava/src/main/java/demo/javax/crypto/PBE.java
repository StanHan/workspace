package demo.javax.crypto;

import java.security.Key;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import demo.java.lang.HexDemo;

/**
 * <h1>对称加密算法PBE</h1>
 * <p>
 * PBE是一种基于口令的加密算法，使用口令代替其他对称加密算法中的密钥，其特点在于口令由用户自己掌管，不借助任何物理媒体；采用随机数（这里我们叫做盐）杂凑多重加密等方法保证数据的安全性。
 * 
 * PBE算法是对称加密算法的综合算法，常见算法PBEWithMD5AndDES,使用MD5和DES算法构建了PBE算法。将盐附加在口令上，通过消息摘要算法经过迭代获得构建密钥的基本材料，构建密钥后使用对称加密算法进行加密解密。
 * 
 * JDK对DESede算法的支持
 * 
 * <li>算法/密钥长度/默认密钥长度：
 * <li>PBEWithMD5AndDES/56/56
 * <li>PBEWithMD5AndTripleDES/112,168/168
 * <li>PBEWithSHA1AndDESede/112,168/168
 * <li>PBEWithSHA1AndRC2_40/40 to 1024/128
 * <li>工作模式：CBC
 * <li>填充方式：PKCS5Padding
 */
public class PBE {

    public static void main(String[] args) throws Exception {
        byte[] salt = initSalt();
        System.out.println("salt：" + showByteArray(salt));
        // 这里的password需要是ASCII码，不然会报异常
        String password = "1111";
        System.out.println("口令：" + password);

        String data = "PBE数据";
        System.out.println("加密前数据: string:" + data);
        System.out.println("加密前数据: byte[]:" + showByteArray(data.getBytes()));
        System.out.println();
        byte[] encryptData = encrypt(data.getBytes(), password, salt);
        System.out.println("加密后数据: byte[]:" + showByteArray(encryptData));
        System.out.println("加密后数据: hexStr:" + HexDemo.encodeHexStr(encryptData));
        System.out.println();
        byte[] decryptData = decrypt(encryptData, password, salt);
        System.out.println("解密后数据: byte[]:" + showByteArray(decryptData));
        System.out.println("解密后数据: string:" + new String(decryptData));

    }

    public static final String ALGORITHM = "PBEWITHMD5andDES";

    public static final int ITERATION_COUNT = 100;

    /**
     * 初始盐<br/>
     * 盐的长度必须为8位
     * 
     * @return byte[] 盐
     * @throws Exception
     */
    public static byte[] initSalt() throws Exception {
        // 实例化安全随机数
        SecureRandom random = new SecureRandom();
        // 产出盐
        return random.generateSeed(8);
    }

    /**
     * 转换密钥
     * 
     * @param password
     *            密码
     * @return Key 密钥
     */
    private static Key toKey(String password) throws Exception {
        // 密钥材料
        PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray());
        // 实例化
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        // 生成密钥
        return keyFactory.generateSecret(keySpec);
    }

    /**
     * 加密
     * 
     * @param data
     *            待加密数据
     * @param key
     *            密钥
     * @param salt
     *            盐
     * @return byte[] 加密数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] data, String password, byte[] salt) throws Exception {
        // 转换密钥
        Key key = toKey(password);
        // 实例化PBE参数材料
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
        // 实例化
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 初始化
        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        // 执行操作
        return cipher.doFinal(data);
    }

    /**
     * 解密
     * 
     * @param data
     *            待机密数据
     * @param key
     *            密钥
     * @param salt
     *            盐
     * @return byte[] 解密数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] data, String password, byte[] salt) throws Exception {
        // 转换密钥
        Key key = toKey(password);
        // 实例化PBE参数材料
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATION_COUNT);
        // 实例化
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        // 初始化
        cipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        // 执行操作
        return cipher.doFinal(data);
    }

    private static String showByteArray(byte[] data) {
        if (null == data) {
            return null;
        }
        StringBuilder sb = new StringBuilder("{");
        for (byte b : data) {
            sb.append(b).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }

}
