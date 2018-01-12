package demo.java.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class IteratorDemo {

    public static void main(String[] args) {
        // demoScanner();
        demoIterator();
    }

    static void demoIterator() {
        String s = "a,b,c,d,e,f";
        String[] array = s.split(",");
        List<String> list = Arrays.asList(array);
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            String t = iterator.next();
            System.err.println(t);
        }

        while (iterator.hasNext()) {
            String t = iterator.next();
            System.out.println(t);
        }
    }

    /**
     * 
     * java.util.Scanner是Java5的新特征，主要功能是简化文本扫描。这个类最实用的地方表现在获取控制台输入，其他的功能都很鸡肋，尽管Java API文档中列举了大量的API方法，但是都不怎么地。
     * 如果说Scanner使用简便，不如说Scanner的构造器支持多种方式，构建Scanner的对象很方便。
     * 
     * 可以从字符串（Readable）、输入流、文件等等来直接构建Scanner对象，有了Scanner了，就可以逐段（根据正则分隔式）来扫描整个文本，并对扫描后的结果做想要的处理。
     *
     */
    static void demoScanner() {
        try (Scanner scanner = new Scanner(System.in);) {
            System.out.println("请输入：");
            while (true) {
                String line = scanner.nextLine();
                System.out.println(line);
                if (line.equalsIgnoreCase("QUIT")) {
                    break;
                }
            }
            String line = scanner.nextLine();
            System.out.println(line);
            System.out.println("Bye bye!");
        }
    }

    /**
     * 逐行扫描文件，并逐行输出
     */
    static void demoInputStream2() throws FileNotFoundException {
        InputStream in = new FileInputStream(new File("D:\\logs/call_log.sql"));
        Scanner s = new Scanner(in);
        while (s.hasNextLine()) {
            System.out.println(s.nextLine());
        }
        s.close();
    }

    /**
     * 获取控制台输入
     */
    static void demoInputStream() {
        try (Scanner scanner = new Scanner(System.in);) {
            System.out.println("请输入字符串：");
            while (true) {
                String line = scanner.nextLine();
                if (line.equals("exit")) {
                    break;
                }
                System.out.println(">>>" + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Scanner默认使用空格作为分割符来分隔文本，但允许你指定新的分隔符
     */
    static void demoString() {
        Scanner s = new Scanner("123 asdf sd 45 789 sdf asdfl,sdf.sdfl,asdf    ......asdfkl    las");
        // s.useDelimiter(" |,|\\.");
        while (s.hasNext()) {
            System.out.println(s.next());
        }
        s.close();
    }

}
