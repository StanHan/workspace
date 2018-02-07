package demo.javax.crypto;

import java.security.MessageDigest;

import demo.java.lang.HexDemo;

/**
 * <h1>安全散列算法SHA (Secure Hash Algorithm，SHA)</h1>
 * <p>
 * 主要适用于数字签名标准（Digital Signature Standard DSS）里面定义的数字签名算法（Digital Signature Algorithm DSA）。 对于长度小于 2^64 位的消息，SHA1 会产生一个
 * 160 位的消息摘要。
 * 
 * SHA 也是一个系列，它包括 SHA-1，SHA-224，SHA-256，SHA-384，和 SHA-512 等几种算法。 其中，SHA-1，SHA-224 和 SHA-256 适用于长度不超过 2^64 二进制位的消息。
 * SHA-384 和 SHA-512 适用于长度不超过 2^128 二进制位的消息。
 * <p>
 * <b>散列</b>，是信息的提炼，通常其长度要比信息小得多，且为一个固定长度。加密性强的散列一定是不可逆的，这就意味着通过散列结果，无法推出任何部分的原始信息。
 * <p>
 * 单向散列函数的安全性在于其产生散列值的操作过程具有较强的单向性。如果在输入序列中嵌入密码，那么任何人在不知道密码的情况下都不能产生正确的散列值，从而保证了其安全性。
 * 
 * SHA 将输入流按照每块 512 位（64 个字节）进行分块，并产生 20 个字节的被称为信息认证代码或信息摘要的输出。
 * 
 * 通过散列算法可实现数字签名实现，数字签名的原理是将要传送的明文通过一种函数运算（Hash）转换成报文摘要（不同的明文对应不同的报文摘要），报文摘要加密后与明文一起传送给接受方，
 * 接受方将接受的明文产生新的报文摘要与发送方的发来报文摘要解密比较，比较结果一致表示明文未被改动，如果不一致表示明文已被篡改。
 * 
 * <h2>SHA-1 与 MD5 的比较</h2>
 * <li>对强行攻击的安全性:最显著和最重要的区别是 SHA-1 摘要比 MD5 摘要长 32 位。使用强行技术，产生任何一个报文使其摘要等于给定报摘要的难度对 MD5 是 2^128 数量级的操作，而对 SHA-1 则是 2^160
 * 数量级的操作。这样，SHA-1 对强行攻击有更大的强度。
 * <li>对密码分析的安全性:由于 MD5 的设计，易受密码分析的攻击，SHA-1 显得不易受这样的攻击。
 * <li>速度:在相同的硬件上，SHA-1 的运行速度比 MD5 慢。
 */
public class SHA1 {

    // 测试

    public static void main(String[] args) throws Exception {
        String param = "StanHan";
        System.out.println("加密前：" + param);
        String digest = new SHA1().getDigestOfString(param.getBytes());
        System.out.println("加密后：" + digest);

        demo();
    }

    /**
     * 测试方法
     * 
     * @param args
     */
    static void demo() throws Exception {
        String key = "StanHan";
        System.out.println(encryptSHA(key));
    }

    /**
     * 定义加密方式
     */
    private final static String KEY_SHA = "SHA";
    private final static String KEY_SHA1 = "SHA-1";

    /**
     * SHA 加密
     * 
     * @param data
     *            需要加密的字节数组
     * @return 加密之后的字节数组
     * @throws Exception
     */
    public static byte[] encryptSHA(byte[] data) throws Exception {
        // 创建具有指定算法名称的信息摘要
        // MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA1);
        // 使用指定的字节数组对摘要进行最后更新
        sha.update(data);
        // 完成摘要计算并返回
        byte[] result = sha.digest();
        // 复位
        sha.reset();
        return result;
    }

    /**
     * SHA 加密
     * 
     * @param data
     *            需要加密的字符串
     * @return 加密之后的字符串
     * @throws Exception
     */
    public static String encryptSHA(String data) throws Exception {
        // 验证传入的字符串
        if (data == null || data.isEmpty()) {
            return "";
        }
        // 创建具有指定算法名称的信息摘要
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        // 使用指定的字节数组对摘要进行最后更新
        sha.update(data.getBytes());
        // 完成摘要计算
        byte[] bytes = sha.digest();
        // 将得到的字节数组变成字符串返回
        return HexDemo.byteArrayToHexString(bytes);
    }

    private final int[] abcde = { 0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476, 0xc3d2e1f0 };
    // 摘要数据存储数组
    private int[] digestInt = new int[5];
    // 计算过程中的临时数据存储数组
    private int[] tmpData = new int[80];

    // 计算sha-1摘要
    private int process_input_bytes(byte[] bytedata) {
        // 初试化常量
        System.arraycopy(abcde, 0, digestInt, 0, abcde.length);
        // 格式化输入字节数组，补10及长度数据
        byte[] newbyte = byteArrayFormatData(bytedata);
        // 获取数据摘要计算的数据单元个数
        int MCount = newbyte.length / 64;
        // 循环对每个数据单元进行摘要计算
        for (int pos = 0; pos < MCount; pos++) {
            // 将每个单元的数据转换成16个整型数据，并保存到tmpData的前16个数组元素中
            for (int j = 0; j < 16; j++) {
                tmpData[j] = byteArrayToInt(newbyte, (pos * 64) + (j * 4));
            }

            // 摘要计算函数

            encrypt();
        }
        return 20;
    }

    // 格式化输入字节数组格式
    private byte[] byteArrayFormatData(byte[] bytedata) {
        // 补0数量
        int zeros = 0;
        // 补位后总位数
        int size = 0;
        // 原始数据长度
        int n = bytedata.length;
        // 模64后的剩余位数
        int m = n % 64;
        // 计算添加0的个数以及添加10后的总长度
        if (m < 56) {
            zeros = 55 - m;
            size = n - m + 64;
        } else if (m == 56) {
            zeros = 63;
            size = n + 8 + 64;
        } else {
            zeros = 63 - m + 56;
            size = (n + 64) - m + 64;
        }
        // 补位后生成的新数组内容
        byte[] newbyte = new byte[size];
        // 复制数组的前面部分
        System.arraycopy(bytedata, 0, newbyte, 0, n);
        // 获得数组Append数据元素的位置
        int l = n;
        // 补1操作
        newbyte[l++] = (byte) 0x80;
        // 补0操作
        for (int i = 0; i < zeros; i++) {
            newbyte[l++] = (byte) 0x00;
        }
        // 计算数据长度，补数据长度位共8字节，长整型
        long N = (long) n * 8;
        byte h8 = (byte) (N & 0xFF);
        byte h7 = (byte) ((N >> 8) & 0xFF);
        byte h6 = (byte) ((N >> 16) & 0xFF);
        byte h5 = (byte) ((N >> 24) & 0xFF);
        byte h4 = (byte) ((N >> 32) & 0xFF);
        byte h3 = (byte) ((N >> 40) & 0xFF);
        byte h2 = (byte) ((N >> 48) & 0xFF);
        byte h1 = (byte) (N >> 56);
        newbyte[l++] = h1;
        newbyte[l++] = h2;
        newbyte[l++] = h3;
        newbyte[l++] = h4;
        newbyte[l++] = h5;
        newbyte[l++] = h6;
        newbyte[l++] = h7;
        newbyte[l++] = h8;
        return newbyte;
    }

    private int f1(int x, int y, int z) {
        return (x & y) | (~x & z);
    }

    private int f2(int x, int y, int z) {
        return x ^ y ^ z;
    }

    private int f3(int x, int y, int z) {
        return (x & y) | (x & z) | (y & z);
    }

    private int f4(int x, int y) {
        return (x << y) | x >>> (32 - y);
    }
    // 单元摘要计算函数

    private void encrypt() {
        for (int i = 16; i <= 79; i++) {
            tmpData[i] = f4(tmpData[i - 3] ^ tmpData[i - 8] ^ tmpData[i - 14] ^ tmpData[i - 16], 1);
        }
        int[] tmpabcde = new int[5];
        for (int i1 = 0; i1 < tmpabcde.length; i1++) {
            tmpabcde[i1] = digestInt[i1];
        }
        for (int j = 0; j <= 19; j++) {
            int tmp = f4(tmpabcde[0], 5) + f1(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + tmpData[j]
                    + 0x5a827999;
            tmpabcde[4] = tmpabcde[3];
            tmpabcde[3] = tmpabcde[2];
            tmpabcde[2] = f4(tmpabcde[1], 30);
            tmpabcde[1] = tmpabcde[0];
            tmpabcde[0] = tmp;
        }
        for (int k = 20; k <= 39; k++) {
            int tmp = f4(tmpabcde[0], 5) + f2(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + tmpData[k]
                    + 0x6ed9eba1;
            tmpabcde[4] = tmpabcde[3];
            tmpabcde[3] = tmpabcde[2];
            tmpabcde[2] = f4(tmpabcde[1], 30);
            tmpabcde[1] = tmpabcde[0];
            tmpabcde[0] = tmp;
        }
        for (int l = 40; l <= 59; l++) {
            int tmp = f4(tmpabcde[0], 5) + f3(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + tmpData[l]
                    + 0x8f1bbcdc;
            tmpabcde[4] = tmpabcde[3];
            tmpabcde[3] = tmpabcde[2];
            tmpabcde[2] = f4(tmpabcde[1], 30);
            tmpabcde[1] = tmpabcde[0];
            tmpabcde[0] = tmp;
        }
        for (int m = 60; m <= 79; m++) {
            int tmp = f4(tmpabcde[0], 5) + f2(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + tmpData[m]
                    + 0xca62c1d6;
            tmpabcde[4] = tmpabcde[3];
            tmpabcde[3] = tmpabcde[2];
            tmpabcde[2] = f4(tmpabcde[1], 30);
            tmpabcde[1] = tmpabcde[0];
            tmpabcde[0] = tmp;
        }
        for (int i2 = 0; i2 < tmpabcde.length; i2++) {
            digestInt[i2] = digestInt[i2] + tmpabcde[i2];
        }
        for (int n = 0; n < tmpData.length; n++) {
            tmpData[n] = 0;
        }
    }

    // 4字节数组转换为整数
    private int byteArrayToInt(byte[] bytedata, int i) {
        return ((bytedata[i] & 0xff) << 24) | ((bytedata[i + 1] & 0xff) << 16) | ((bytedata[i + 2] & 0xff) << 8)
                | (bytedata[i + 3] & 0xff);
    }

    // 整数转换为4字节数组
    private void intToByteArray(int intValue, byte[] byteData, int i) {
        byteData[i] = (byte) (intValue >>> 24);
        byteData[i + 1] = (byte) (intValue >>> 16);
        byteData[i + 2] = (byte) (intValue >>> 8);
        byteData[i + 3] = (byte) intValue;
    }

    // 计算sha-1摘要，返回相应的字节数组
    public byte[] getDigestOfBytes(byte[] byteData) {
        process_input_bytes(byteData);
        byte[] digest = new byte[20];
        for (int i = 0; i < digestInt.length; i++) {
            intToByteArray(digestInt[i], digest, i * 4);
        }
        return digest;
    }

    // 计算sha-1摘要，返回相应的十六进制字符串
    public String getDigestOfString(byte[] byteData) {
        return HexDemo.byteArrayToHexString(getDigestOfBytes(byteData));
    }

}
