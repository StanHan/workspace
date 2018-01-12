package demo.java.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import demo.vo.Bean;

public class SerializeDemo {

    public static void main(String[] args) throws Exception {
        String s = "1月7日，有网友称在上海中山公园龙之梦的负一层看见大量老鼠，不止一家店，整个负一层内，仅用肉眼就能看到四五只老鼠。记者在网友拍摄的视频中看到，老鼠比一个拳头还要大，不止一只二只，而是有很多只。";
        serialize(s, "D:/logs/aa.utf8");
        byte[] gbk = s.getBytes("gbk");
        System.out.println(Arrays.toString(gbk));
        byte[] utf8 = s.getBytes("utf8");
        System.out.println(Arrays.toString(utf8));
        deSerialize("D:/logs/aa.gbk");
    }
    static void byteToObjectTest() throws UnsupportedEncodingException{
        String h = "中国";
        System.out.println("源："+h);

        String g = new String(h.getBytes(Charset.forName("GBK")),"GBK");
        String u = new String(h.getBytes(Charset.forName("UTF-8")));
        System.out.println(g);
        System.out.println(u);

        byte[] bg = toByteArray(g);
        byte[] bu = toByteArray(u);

        System.out.println(Arrays.toString(bg));
        System.out.println(Arrays.toString(bu));

        String gb = (String)byteToObject(bg);
        String ub = (String)byteToObject(bu);

        System.out.println(gb);
        System.out.println(ub);
    }

    static void test() throws UnsupportedEncodingException {
        String str = "你好，我叫啦啦";
        byte[] array = toByteArray(str);
        System.out.println(Arrays.toString(array));
        byte[] array1 = str.getBytes("UTF8");
        System.out.println("UTF-8:" + Arrays.toString(array1));
        byte[] array2 = str.getBytes("GBK");
        System.out.println("GBK：" + Arrays.toString(array2));

        Object object = byteToObject(array);
        System.out.println((String) object);

        byte[] a = { -84, -19, 0, 5, 116, 0, 21, -28, -67, -96, -27, -91, -67, -17, -68, -116, -26, -120, -111, -27,
                -113, -85, -27, -107, -90, -27, -107, -90 };
        object = byteToObject(a);
        System.out.println((String) object);

        String s1 = new String(a);
        System.out.println(s1);

    }

    // 数组转对象
    public static Object byteToObject(byte[] bytes) {
        if (bytes.length < 1) {
            return null;
        }
        Object obj = null;
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream ois = new ObjectInputStream(bis);) {
            obj = ois.readObject();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return obj;
    }

    // 对象转数组
    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(bos);) {
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return bytes;
    }

    /**
     * 序列化对象到文件
     * 
     * @param t
     * @param path
     */
    public static <T> void serialize(T t, String path) {
        try (ObjectOutput objectOutput = new ObjectOutputStream(new FileOutputStream(path))) {
            objectOutput.writeObject(t);
            objectOutput.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文件反序列化为Java对象
     * 
     * @param path
     * @return
     */
    public static <T> T deSerialize(String path) {
        // Deserialize a string and date from a file.
        try (FileInputStream fileInputStream = new FileInputStream(path);
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {

            T t = (T) objectInputStream.readObject();
            System.out.println(t);
            return t;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 测试 transient关键字
     */
    static void testTransient() {
        Bean user = new Bean();
        user.setName("Alexia");
        user.setPasswd("123456");

        System.out.println("read before Serializable: ");
        System.out.println("username: " + user.getName());
        System.err.println("password: " + user.getPasswd());

        try {
            ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream("D:/cache/user.txt"));
            os.writeObject(user); // 将User对象写进文件
            os.flush();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ObjectInputStream is = new ObjectInputStream(new FileInputStream("D:/cache/user.txt"));
            user = (Bean) is.readObject(); // 从流中读取User的数据
            is.close();

            System.out.println("\nread after Serializable: ");
            System.out.println("username: " + user.getName());
            System.err.println("password: " + user.getPasswd());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void test2() {

        List<String> list = new ArrayList<String>();
        list.add("Stan");
        list.add("hello");
        list.add("world");
        Bean bean = new Bean();
        bean.setList(list);
        bean.setName("Stan");
        bean.setToday(new Date());
        bean.setAge(30);
        bean.setMoney(new Double(100000000000.00));
        String path = "d:/cache/today.txt";
        serialize(bean, path);
        Bean tmp_bean = deSerialize(path);
    }

    static void test1() {
        // Serialize today's date to a file.
        try (ObjectOutput objectOutput = new ObjectOutputStream(new FileOutputStream("d:/cache/today.txt"))) {
            objectOutput.writeObject("Today");
            objectOutput.writeObject(new Date());
            objectOutput.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Deserialize a string and date from a file.
        try (FileInputStream fileInputStream = new FileInputStream("d:/cache/today.txt");
                ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);) {

            String today = (String) objectInputStream.readObject();
            Date date = (Date) objectInputStream.readObject();
            System.out.println(today);
            System.out.println(date);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
