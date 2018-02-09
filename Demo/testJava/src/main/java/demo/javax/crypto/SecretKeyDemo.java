package demo.javax.crypto;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class SecretKeyDemo {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

    /**
     * 相对来说对称密钥的使用是很简单的 , 对于 JCE 来讲支技DES,DESede,Blowfish,HmacMD5,HmacSHA1
     * 
     * @param algorithm
     * @throws NoSuchAlgorithmException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     */
    public static void test(String algorithm) throws NoSuchAlgorithmException, IllegalBlockSizeException,
            BadPaddingException, NoSuchPaddingException, InvalidKeyException {
        // 生成密钥
        KeyGenerator keygen = KeyGenerator.getInstance(algorithm);
        SecretKey deskey = keygen.generateKey();
        // 用密钥加密明文 (myinfo), 生成密文 (cipherByte)
        Cipher c1 = Cipher.getInstance(algorithm);
        c1.init(Cipher.ENCRYPT_MODE, deskey);
        byte[] cipherByte = c1.doFinal("Stan".getBytes());

        // 用密钥解密密文
        c1 = Cipher.getInstance(algorithm);
        c1.init(Cipher.DECRYPT_MODE, deskey);
        byte[] clearByte = c1.doFinal(cipherByte);
    }

    /**
     * 相对来说对称密钥的使用是很简单的 , 对于 JCE 来讲支技 DES,DESede,Blowfish 三种加密术
     * 
     * @throws NoSuchAlgorithmException
     */
    static void test2(String algorithm) throws NoSuchAlgorithmException {
        // 生成密钥
        KeyGenerator keygen = KeyGenerator.getInstance(algorithm);
        SecretKey deskey = keygen.generateKey();
        byte[] desEncode = deskey.getEncoded();
        SecretKeySpec destmp = new SecretKeySpec(desEncode, algorithm);
        SecretKey mydeskey = destmp;
    }

}
