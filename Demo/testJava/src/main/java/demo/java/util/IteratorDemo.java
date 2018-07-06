package demo.java.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

public class IteratorDemo {

    public static void main(String[] args) {
        // demoScanner();
        demoIterator();
    }

    /**
     * Iterator接口中包含一个方法，叫做remove()。该方法可以删除next最新返回的项。虽然Collection接口也包含一个remove方法，但是，使用Iterator的remove可能有更多的优点。”
     * 
     * 那么Collection接口中的remove()比Iterator接口中的remove()的差异性到底在哪呢？ Collection接口中的remove方法必须接受一个对象，即要从集合中移除的对像，并且返回boolean值。
     * 而Iterator接口中的remove方法，它通过迭代器，每次使用next后，让指针往后移一位后，再使用remove方法移除当前指针指向的对象 ，然后他的方法是void的，没有返回值的。
     * 
     * 当使用 fail-fast iterator 对 Collection 或 Map 进行迭代操作过程中尝试直接修改 Collection / Map 的内容时，即使是在单线程下运行，
     * java.util.ConcurrentModificationException 异常也将被抛出。 Iterator 是工作在一个独立的线程中，并且拥有一个 mutex 锁。 Iterator
     * 被创建之后会建立一个指向原来对象的单链索引表，当原来的对象数量发生变化时，这个索引表的内容不会同步改变，所以当索引指针往后移动的时候就找不到要迭代的对象，所以按照 fail-fast 原则 Iterator 会马上抛出
     * java.util.ConcurrentModificationException 异常。
     * 
     * 所以 Iterator 在工作的时候是不允许被迭代的对象被改变的。 但你可以使用 Iterator 本身的方法 remove() 来删除对象， Iterator.remove()
     * 方法会在删除当前迭代对象的同时维护索引的一致性。
     * 
     * 有意思的是如果你的 Collection / Map 对象实际只有一个元素的时候， ConcurrentModificationException 异常并不会被抛出。这也就是为什么在 javadoc 里面指出： it
     * would be wrong to write a program that depended on this exception for its correctness:
     * ConcurrentModificationException should be used only to detect bugs.
     * 
     * 
     */
    @Test
    public void testRemove() {

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
