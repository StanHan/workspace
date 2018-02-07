package demo.java.security;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import demo.java.util.Base64Demo;

/**
 * 签名类
 *
 */
public class SignatureDemo {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    /**
     * 校验数字签名
     * 
     * @param algorithm
     *            指定算法,如 :"DSA"
     * @param data
     *            加密数据
     * @param publicKeyStr
     * @param sign
     *            数字签名
     * @return
     * @throws Exception
     */
    public static boolean verifyWithPublicKey(String algorithm, byte[] data, PublicKey publicKey, String sign)
            throws Exception {
        Signature signature = Signature.getInstance(algorithm);
        // 用指定的公钥初始化
        signature.initVerify(publicKey);
        signature.update(data);
        // 验证签名是否有效
        return signature.verify(Base64Demo.decode(sign));
    }

    /**
     * 用私钥对信息进行数字签名
     * 
     * @param algorithm
     *            指定算法,如 :"DSA"
     * @param data
     *            加密数据
     * @param privateKeyStr
     *            私钥-base64加密的
     * @return
     * @throws Exception
     */
    public static String signToBase64Str(String algorithm, byte[] data, PrivateKey privateKey) throws Exception {
        // 返回一个指定算法的 Signature 对象
        Signature signature = Signature.getInstance(algorithm);
        // 用指定的私钥初始化
        signature.initSign(privateKey);
        // 添加要签名的信息
        signature.update(data);
        // 返回签名的数组
        byte[] sign = signature.sign();
        return Base64Demo.encode(sign);
    }

}
