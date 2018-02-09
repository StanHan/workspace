package demo.util;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MacUtils {


    public static final MessageDigest md5 = getDigest("MD5");

    public static String buildSign(String idNo, String name, String phone, String path,String password) {
        StringBuilder sb = new StringBuilder();
        sb.append(idNo).append(phone).append(name).append(path).append(password);
        byte[] data = sb.toString().getBytes(StandardCharsets.UTF_8);
        byte[] digest = md5.digest(data);
        return bytes2Hex(digest);
    }

    public static String bytes2Hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String tmp = null;
        for (byte b : bytes) {
            tmp = Integer.toHexString(0xFF & b);
            if (tmp.length() == 1) {
                tmp = "0" + tmp;
            }
            sb.append(tmp);
        }
        return sb.toString();
    }

    public static MessageDigest getDigest(String algorithm) {
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
