package demo.java.security;

/**
 * <h3>MD5Hash算法的特点：</h3>
 * <li>1：输入任意长度的信息，经过摘要处理，输出为128位的信息。（数字指纹）表示为16进制的字符串为128/4=32个字符。
 * <li>2：不同输入产生不同的结果，（唯一性）
 * <li>3：根据128位的输出结果不可能反推出输入的信息(不可逆)
 * <p>
 * MD5Hash算法作用：
 * <li>1：防止数据被篡改
 * <li>2：防止直接看到明文 ps:在密码存储中，即使采用md5存储密码也是有可能出现安全漏洞的，比如撞库的暴力破解
 * <li>3：数字签名
 * <p>
 * <h3>MD5 算法</h3>简要的叙述可以为：MD5 以 512 位分组来处理输入的信息，且每一分组又被划分为 16 个 32 位子分组，经过了一系列的处理后，算法的输出由四个 32 位分组组成，将这四个 32
 * 位分组级联后将生成一个 128 位散列值。
 * <li>填充
 * 
 * 在 MD5 算法中，首先需要对信息进行填充，使其位长对 512 求余的结果等于 448，并且填充必须进行，即使其位长对 512 求余的结果等于 448。因此，信息的位长（Bits Length）将被扩展至 N * 512 +
 * 448，N 为一个非负整数，N 可以是零。
 * 
 * 填充的方法如下： 1) 在信息的后面填充一个 1 和无数个 0，直到满足上面的条件时才停止用 0 对信息的填充。 2) 在这个结果后面附加一个以 64 位二进制表示的填充前信息长度（单位为Bit），如果二 进制表示的填充前信息长度超过
 * 64 位，则取低 64 位。
 * 
 * 经过这两步的处理，信息的位长 = N * 512 + 448 + 64 = （N + 1）* 512，即长度恰好是 512 的整数倍。这样做的原因是为满足后面处理中对信息长度的要求。
 * 
 * <li>初始化变量
 * 
 * 初始的 128 位值为初试链接变量，这些参数用于第一轮的运算，以大端字节序来表示，他们分别为： A = 0x01234567，B = 0x89ABCDEF，C = 0xFEDCBA98，D = 0x76543210。
 * 
 * （每一个变量给出的数值是高字节存于内存低地址，低字节存于内存高地址，即大端字节序。在程序中变量 A、B、C、D 的值分别为0x67452301，0xEFCDAB89，0x98BADCFE，0x10325476）
 * 
 * <li>处理分组数据
 * 
 * 每一分组的算法流程如下：
 * 
 * 第一分组需要将上面四个链接变量复制到另外四个变量中：A 到 a，B 到 b，C 到 c，D 到 d。从第二分组开始的变量为上一分组的运算结果，即 A = a， B = b， C = c， D = d。
 * 
 * 主循环有四轮，每轮循环都很相似。第一轮进行 16 次操作。每次操作对 a、b、c 和 d 中的其中三个作一次非线性函数运算，然后将所得结果加上第四个变量，文本的一个子分组和一个常数。再将所得结果向左环移一个不定的数，并加上 a、b、c
 * 或 d 中之一。最后用该结果取代 a、b、c 或 d 中之一。
 * 
 * <li>输出
 * 
 * 
 * 最后的输出是 a、b、c 和 d 的级联。
 *
 * 
 */
public class MD5Demo {

    /**
     * 测试方法
     * 
     * @param args
     */
    public static void main(String[] args) {
        String str = MD5Demo.getInstance().getMD5("");
        String str1 = MD5Demo.getInstance().getMD5("123");
        System.out.println(str);
        System.out.println("d41d8cd98f00b204e9800998ecf8427e");
        System.out.println(str1);
        System.out.println("202cb962ac59075b964b07152d234b70");
    }

    /**
     * 单例
     */
    private static MD5Demo instance;

    /**
     * 四个链接变量
     */
    private final int A = 0x67452301;
    private final int B = 0xefcdab89;
    private final int C = 0x98badcfe;
    private final int D = 0x10325476;

    /**
     * ABCD的临时变量
     */
    private int Atemp;
    private int Btemp;
    private int Ctemp;
    private int Dtemp;

    /**
     * 常量ti 公式:floor(abs(sin(i+1))×(2pow32)
     */
    private final int[] K = { 0xd76aa478, 0xe8c7b756, 0x242070db, 0xc1bdceee, 0xf57c0faf, 0x4787c62a, 0xa8304613,
            0xfd469501, 0x698098d8, 0x8b44f7af, 0xffff5bb1, 0x895cd7be, 0x6b901122, 0xfd987193, 0xa679438e, 0x49b40821,
            0xf61e2562, 0xc040b340, 0x265e5a51, 0xe9b6c7aa, 0xd62f105d, 0x02441453, 0xd8a1e681, 0xe7d3fbc8, 0x21e1cde6,
            0xc33707d6, 0xf4d50d87, 0x455a14ed, 0xa9e3e905, 0xfcefa3f8, 0x676f02d9, 0x8d2a4c8a, 0xfffa3942, 0x8771f681,
            0x6d9d6122, 0xfde5380c, 0xa4beea44, 0x4bdecfa9, 0xf6bb4b60, 0xbebfbc70, 0x289b7ec6, 0xeaa127fa, 0xd4ef3085,
            0x04881d05, 0xd9d4d039, 0xe6db99e5, 0x1fa27cf8, 0xc4ac5665, 0xf4292244, 0x432aff97, 0xab9423a7, 0xfc93a039,
            0x655b59c3, 0x8f0ccc92, 0xffeff47d, 0x85845dd1, 0x6fa87e4f, 0xfe2ce6e0, 0xa3014314, 0x4e0811a1, 0xf7537e82,
            0xbd3af235, 0x2ad7d2bb, 0xeb86d391 };

    /**
     * 向左位移数,计算方法未知
     */
    private final int[] s = { 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 7, 12, 17, 22, 5, 9, 14, 20, 5, 9, 14, 20, 5,
            9, 14, 20, 5, 9, 14, 20, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 4, 11, 16, 23, 6, 10, 15, 21, 6, 10,
            15, 21, 6, 10, 15, 21, 6, 10, 15, 21 };

    /**
     * 私有构造函数
     */
    private MD5Demo() {

    }

    /**
     * 单例模式
     * 
     * @return
     */
    public static MD5Demo getInstance() {
        if (instance == null) {
            instance = new MD5Demo();
        }
        return instance;
    }

    /**
     * 初始化函数
     */
    private void init() {
        Atemp = A;
        Btemp = B;
        Ctemp = C;
        Dtemp = D;
    }

    /**
     * 移动一定位数
     * 
     * @param a
     * @param s
     * @return
     */
    private int shift(int a, int s) {
        return (a << s) | (a >>> (32 - s)); // 右移的时候，高位一定要补零，而不是补充符号位
    }

    /**
     * 主循环
     * 
     * @param M
     */
    private void mainLoop(int[] M) {
        int F;
        int g;
        int a = Atemp;
        int b = Btemp;
        int c = Ctemp;
        int d = Dtemp;

        for (int i = 0; i < 64; i++) {
            if (i < 16) {
                F = (b & c) | ((~b) & d);
                g = i;
            } else if (i < 32) {
                F = (d & b) | ((~d) & c);
                g = (5 * i + 1) % 16;
            } else if (i < 48) {
                F = b ^ c ^ d;
                g = (3 * i + 5) % 16;
            } else {
                F = c ^ (b | (~d));
                g = (7 * i) % 16;
            }

            int tmp = d;
            d = c;
            c = b;
            b = b + shift(a + F + K[i] + M[g], s[i]);
            a = tmp;
        }

        Atemp += a;
        Btemp += b;
        Ctemp += c;
        Dtemp += d;
    }

    /**
     * 填充函数 处理后应满足bits≡448(mod512),字节就是bytes≡56（mode64) 填充方式为先加一个0,其它位补零 最后加上64位的原来长度
     * 
     * @param str
     * @return
     */
    private int[] add(String str) {
        int num = ((str.length() + 8) / 64) + 1; // 以512位，64个字节为一组
        int[] strByte = new int[num * 16]; // 64/4=16，所以有16个整数

        for (int i = 0; i < num * 16; i++) {
            // 全部初始化为0
            strByte[i] = 0;
        }

        int j;
        for (j = 0; j < str.length(); j++) {
            strByte[j >> 2] |= str.charAt(j) << ((j % 4) * 8); // 一个整数存储四个字节，小端序
        }
        strByte[j >> 2] |= 0x80 << ((j % 4) * 8); // 尾部添加1

        // 添加原长度，长度指位的长度，所以要乘8，然后是小端序，所以放在倒数第二个,这里长度只用了32位
        strByte[num * 16 - 2] = str.length() * 8;

        return strByte;
    }

    /**
     * 调用函数
     * 
     * @param source
     *            原始字符串
     * @return
     */
    public String getMD5(String source) {
        // 初始化
        init();
        int[] strByte = add(source);
        for (int i = 0; i < strByte.length / 16; i += 16) {
            int[] num = new int[16];
            for (int j = 0; j < 16; j++) {
                num[j] = strByte[i * 16 + j];
            }
            mainLoop(num);
        }
        return changeHex(Atemp) + changeHex(Btemp) + changeHex(Ctemp) + changeHex(Dtemp);
    }

    /**
     * 整数变成16进制字符串
     * 
     * @param a
     *            整数
     * @return
     */
    private String changeHex(int a) {
        String str = "";
        String tmp = "";
        for (int i = 0; i < 4; i++) {
            tmp = Integer.toHexString(((a >> i * 8) % (1 << 8)) & 0xff);
            if (tmp.length() < 2) {
                tmp = "0" + tmp;
            }
            str += tmp;
        }
        return str;
    }

}
