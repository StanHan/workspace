package demo.java.util.codec;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Enumeration;
import java.util.Vector;

public class Base64Demo{
    public static void main(String[] args) {
        demo1();
        demoJava8();
    }
    
    static void demoJava8(){
        Encoder encoder = Base64.getEncoder();
        Decoder decoder = Base64.getDecoder();
        System.out.println(encoder.encodeToString("a".getBytes(StandardCharsets.UTF_8)));
        byte[] array = decoder.decode("YQ==");
        System.out.println(new String(array, StandardCharsets.UTF_8));
    }
    
    static void demo1(){
        IBase64 base64 = new MyBase64Encoder();
        String encode = base64.encode("a".getBytes(StandardCharsets.UTF_8));
        System.out.println(encode);
        String decode = base64.backEncode("YQ==".getBytes(StandardCharsets.UTF_8));
        System.out.println(decode);
    }
}

class MyBase64Encoder implements IBase64 {
    /**
     * base64码表
     */
    private static final byte[] base = { 0x41, 0x42, 0x43, 0x44, 0x45, 0x46, 0x47, 0x48, 0x49, 0x4a, 0x4b, 0x4c, 0x4d,
            0x4e, 0x4f, 0x50, 0x51, 0x52, 0x53, 0x54, 0x55, 0x56, 0x57, 0x58, 0x59, 0x5a, 0x61, 0x62, 0x63, 0x64, 0x65,
            0x66, 0x67, 0x68, 0x69, 0x6a, 0x6b, 0x6c, 0x6d, 0x6e, 0x6f, 0x70, 0x71, 0x72, 0x73, 0x74, 0x75, 0x76, 0x77,
            0x78, 0x79, 0x7a, 0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x2b, 0x2f };

    @Override
    public byte baseIndex(byte b) {
        for (int i = 0; i < base.length; i++) {
            if (base[i] == b) {
                return (byte) i;
            }
        }
        return -1;
    }

    @Override
    public String backEncode(byte[] b) {
        StringBuffer sb = new StringBuffer();
        Vector<Byte> list = new Vector<Byte>();
        int real_len = b.length;
        int len = real_len - 2;
        int more_len = len & 3;
        int use_len = len - more_len;
        for (int i = 0; i < use_len; i += 4) {
            list.add(backFirst(baseIndex(b[i]), baseIndex(b[i + 1])));
            list.add(backSecond(baseIndex(b[i + 1]), baseIndex(b[i + 2])));
            list.add(backThird(baseIndex(b[i + 2]), baseIndex(b[i + 3])));
        }
        Enumeration e = list.elements();
        byte s[] = new byte[list.size()];
        int k = -1;
        while (e.hasMoreElements()) {
            s[++k] = (Byte) e.nextElement();
        }
        sb.append(new String(s));
        if (more_len == 2) {
            byte b_1[] = new byte[1];
            b_1[0] = backLastOne(baseIndex(b[len - 2]), baseIndex(b[len - 1]), 2, 6);
            sb.append(new String(b_1));
        }
        if (more_len == 3) {
            byte b_2[] = new byte[2];
            b_2[0] = backFirst(baseIndex(b[len - 3]), baseIndex(b[len - 2]));
            b_2[1] = backLastOne(baseIndex(b[len - 2]), baseIndex(b[len - 1]), 4, 4);
            sb.append(new String(b_2));
        }
        return sb.toString();
    }

    @Override
    public byte lastOneByte(byte b, int move) {
        int r_b = b & 0xff;
        r_b = r_b << move;
        r_b = r_b >>> 2;
        return (byte) (r_b & 0x3f);
    }

    @Override
    public byte backLastOne(byte last_b, byte next_b, int move_l, int move_b) {
        int r_l = last_b & 0xff;
        int r_n = next_b & 0xff;
        r_l = r_l << move_l;
        r_n = r_n << move_b;
        r_n = r_n >>> move_b;
        return (byte) ((r_l | r_n) & 0xff);
    }

    @Override
    public byte backFirst(byte first, byte second) {
        int r_f = first & 0xff;
        int r_s = second & 0xff;
        r_f = r_f << 2;
        r_s = r_s >>> 4;
        return (byte) ((r_f | r_s) & 0xff);
    }

    @Override
    public byte backSecond(byte second, byte third) {
        int r_s = second & 0xff;
        int r_t = third & 0xff;
        r_s = r_s << 4;
        r_t = r_t >>> 2;
        return (byte) ((r_s | r_t) & 0xff);
    }

    @Override
    public byte backThird(byte third, byte fourth) {
        int r_t = third & 0xff;
        int r_f = fourth & 0xff;
        r_t = r_t << 6;
        return (byte) ((r_t | r_f) & 0xff);
    }

    @Override
    public String encode(byte[] b) {
        StringBuffer sb = new StringBuffer();
        int len = b.length;
        int more_len = len % 3;
        int use_len = len - more_len;
        byte[] s = new byte[4];
        for (int i = 0; i < use_len; i += 3) {
            s[0] = base[firstByte(b[i])];
            s[1] = base[secondByte(b[i], b[i + 1])];
            s[2] = base[thirdByte(b[i + 1], b[i + 2])];
            s[3] = base[fourthByte(b[i + 2])];
            sb.append(new String(s));
        }
        if (more_len == 1) {
            byte b_2[] = new byte[2];
            b_2[0] = base[firstByte(b[len - 1])];
            b_2[1] = base[lastOneByte(b[len - 1], 6)];
            sb.append(new String(b_2));
            return sb.append("==").toString();
        } else if (more_len == 2) {
            byte b_3[] = new byte[3];
            b_3[0] = base[firstByte(b[len - 2])];
            b_3[1] = base[secondByte(b[len - 2], b[len - 1])];
            b_3[2] = base[lastOneByte(b[len - 1], 4)];
            sb.append(new String(b_3));
            return sb.append("=").toString();
        }
        return sb.toString();
    }

    @Override
    public byte firstByte(byte b) {
        // 00000000000000000000000001010011
        // 01010011
        int r_f = b & 0xff;
        r_f = r_f >>> 2;
        return (byte) (r_f & 0x3f);
    }

    @Override
    public byte secondByte(byte last_b, byte next_b) {
        int r_l = last_b & 0xff;
        int r_n = next_b & 0xff;
        r_l = r_l << 6;
        r_l = r_l >>> 2;
        r_n = r_n >>> 4;
        return (byte) ((r_l | r_n) & 0x3f);
    }

    @Override
    public byte thirdByte(byte last_b, byte next_b) {
        int r_l = last_b & 0xff;
        int r_n = next_b & 0xff;
        r_l = r_l << 4;
        r_l = r_l >>> 2;
        r_n = r_n >>> 6;
        return (byte) ((r_l | r_n) & 0x3f);
    }

    @Override
    public byte fourthByte(byte b) {
        int r_b = b & 0xff;
        r_b = r_b << 2;
        r_b = r_b >>> 2;
        return (byte) (r_b & 0x3f);
    }
}

//模板类模板类写好了再按思路写个实现就可以了
interface IBase64 {
  /**
   * 根据传进来的字符的字节码，查询base64码表的索引，并返回所查到的索引
   * 
   * @paramb一个编码后的字节码
   * @return返回base64码表的索引
   */
  public abstract byte baseIndex(byte b);

  /**
   * 解码的方法 传进来的是编码后的base64字符的字节码 解析时是4个一组进行解析
   * 
   * @paramb编码后的字符的字节码数组
   * @return返回原来的字符串
   */
  public abstract String backEncode(byte[] b);

  /**
   * 解码 将4个字节码中的第1个的后6位（00xxxxxx）和第2个 字节的前4位的后2位（00yy0000） 还原为原来的字节码（xxxxxxyy）
   * 
   * @paramfirst4个字节码中的第1个
   * @paramsecond4个字节码中的第2个
   * @return原来的字符的字节码
   */
  public abstract byte backFirst(byte first, byte second);

  /**
   * 解码 将4个字节码中的第2个的后4位（0000xxxx）和第3个 字节的前6位的后4位（00yyyy00） 还原为原来的字节码（xxxxyyyy）
   * 
   * @paramsecond4个字节码中的第2个
   * @paramthird4个字节码中的第3个
   * @return原来的字符的字节码
   */
  public abstract byte backSecond(byte second, byte third);

  /**
   * 解码 将4个字节码中的第3个的后2位（000000xx）和第4个 字节的后6位（00yyyyyy） 还原为原来的字节码（xxyyyyyy）
   * 
   * @paramthird传进来的第3个字符
   * @paramfourth传进来的第4个字符
   * @return原来的字符的字节码
   */
  public abstract byte backThird(byte third, byte fourth);

  /**
   * 解码 将编码后的字符串数组的最后2个字节码还原为原来的字节码 假如数组末尾剩下2个字节： 将倒数第2个字节的前后6位(00xxxxxx)
   * 和倒数第一个字节的后2位(000000yy) 还原为原来的编码（xxxxxxyy） 假如数组末尾剩下3个字节：
   * 将倒数第2个字节的前后4位(0000xxxx) 和倒数第一个字节的后4位(0000yyyy) 还原为原来的编码（xxxxyyyy）
   * 
   * @paramlast_b倒数第2个字节
   * @paramnext_b倒数第1个字节
   * @parammove_l倒数第2个字节移动位数的参数
   * @parammove_b倒数第1个字节移动位数的参数
   * @return原来的字符的字节码
   */
  public byte backLastOne(byte last_b, byte next_b, int move_l, int move_b);

  /**
   * 编码 将传进来的字符编码为base64，返回一个base64的字符串 编码时3个字节一组进行编码，传进来的是要进行编码的字符串数组
   * 
   * @paramb要进行编码的字符串数组
   * @return编码后的字符串
   */
  public abstract String encode(byte[] b);

  /**
   * 假如字符长度%3！=0，使用此方法编码末尾字符 假如b=xxxxyyyy 假如末尾字节个数等于1：
   * 将这个字节的前6位作为一个字节(00xxxxyy) 将这个字节的后6位作为一个字节(00xxyyyy) 假如末尾字节个数等于2：
   * 将这个字节的后6位作为一个字节(00xxyyyy)
   * 
   * @paramb末尾的字符的字节码
   * @parammove末尾的字符的字节码要移动的位数的参数
   * @return编码后的字节码
   */
  public abstract byte lastOneByte(byte b, int move);

  /**
   * 编码 假如b=xxxxyyyy 将第1个字节的前6位编码为base64 将3个字节中的第1个子节码转为（00xxxxyy）
   * 
   * @paramb3个字节中的第1个字节
   * @return编码后的字节码
   */
  public abstract byte firstByte(byte b);

  /**
   * 编码 假如last_b=xxxxyyyynext_b=kkkkffff 将3个字节中的第1个字节的最后2位（000000yy）
   * 和第2个字节的前4位（kkkk0000）编码为（00yykkkk）
   *
   * @paramlast_b3个字节中的第1个字节
   * @paramnext_b3个字节中的第2个字节
   * @return编码后的字节码
   */
  public abstract byte secondByte(byte last_b, byte next_b);

  /**
   * 编码 假如last_b=xxxxyyyynext_b=kkkkffff 将3个字节中的第2个字节的最后4位（0000yyyy）
   * 和第4个字节的前2位（kk000000）编码为（00yyyykk）
   *
   *
   * @paramlast_b3个字节中的第2个字节
   * @paramnext_b3个字节中的第3个字节
   * @return编码后的字节码
   */
  public abstract byte thirdByte(byte last_b, byte next_b);

  /**
   * 编码 假如b=xxxxyyyy 将3个字节中的第3个字节的最后6位（00xxyyyy） 转码为（00xxyyyy）
   * 
   * @paramb3个字节中的第3个字节
   * @return编码后的字节码
   */
  public abstract byte fourthByte(byte b);
}
