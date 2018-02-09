package demo.javax.crypto;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.Security;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import com.sun.crypto.provider.SunJCE;

import demo.java.lang.HexDemo;

/**
 * 加密器/解密器
 * 
 * @author hanjy
 *
 */
public class CipherDemo {

    public static void main(String[] args) {
        
    }

    /**
     * 返回一个指定方法的 Cipher 对象,
     * 
     * @param transformation
     *            方法名 ( 可用 DES,DESede,Blowfish,CBC,PKCS5Padding)
     * @return
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     */
    public static Cipher getInstance(String transformation) throws NoSuchAlgorithmException, NoSuchPaddingException {
        return Cipher.getInstance(transformation);
    }

    /**
     * 用指定的密钥和模式初始化 Cipher 对象
     * 
     * @param cipher
     * @param opmode
     *            方式 (ENCRYPT_MODE, DECRYPT_MODE, WRAP_MODE,UNWRAP_MODE)
     * @param key
     * @throws InvalidKeyException
     */
    public static void init(Cipher cipher, int opmode, Key key) throws InvalidKeyException {
        cipher.init(opmode, key);
    }

    /**
     * 对 input 内的串 , 进行编码处理 , 返回处理后二进制串 , 是返回解密文还是加解文由 init 时的 opmode 决定。
     * 
     * 本方法的执行前如果有 update, 是对 updat 和本次 input 全部处理 , 否则是本 inout 的内容
     * 
     * @param cipher
     * @param input
     * @return
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static byte[] dofinal(Cipher cipher, byte[] input) throws IllegalBlockSizeException, BadPaddingException {
        return cipher.doFinal(input);
    }

}
