package demo.java.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;

import org.junit.Test;

/**
 * Set下有HashSet,LinkedHashSet,TreeSet。 List下有ArrayList,Vector,LinkedList。 两个接口都是继承自Collection.
 * 
 * <h2>List (inteface)</h2>
 * 
 * 次序是List 的最重要特点,它确保维护元素特定的顺序.
 * <li>－－ArrayList 允许对元素快速随机访问.
 * <li>－－LinkedList 对顺序访问进行优化,向List
 * 中间插入与移除的开销并不大，具有addFrist(),addLast(),getFirst,getLast,removeFirst和removeLast().这些方法使得LinkedList可当作堆栈／队列／双向队列.
 * 
 * 
 * <h2>Set (inteface)</h2>
 * 
 * 存入Set 的每个元素必须唯一，不保证维护元素的次序.加入Set 的Object必须定义equals()方法
 * <li>－－HashSet 为快速查找而设计的Set ，存入HashSet对象必须定义hashCode().
 * <li>－－TreeSet 保护次序的Set ，使用它可以从Set 中提取有序序列.
 * <li>－－LinkedHashSet 具有HashSet的查询速度，且内部使用链表维护元素的次序.
 * <p>
 * 它们之间的存储方式不一样：
 * <li>TreeSet采用红黑树的树据结构排序元素.
 * <li>HashSet采用散列函数，这是专门为快速查询而设计的.
 * <li>LinkedHashSet内部使用散列以加快查询速度，同时使用链表维护元素的次序.
 * <p>
 * 使用HashSet/TreeSet时，必须为类定义equals()；而HashCode()是针对HashSet，作为一种编程风格，当覆盖equals()的时候，就应该同时覆盖hashCode().
 * 
 * <p>
 * <li>ArrayXxx:底层数据结构是数组，查询快，增删慢
 * <li>LinkedXxx:底层数据结构是链表，查询慢，增删快
 * <li>HashXxx:底层数据结构是哈希表。依赖两个方法：hashCode()和equals()
 * <li>TreeXxx:底层数据结构是二叉树。两种方式排序：自然排序和比较器排序
 * 
 * <h2>Java 集合的快速失败机制 “fail-fast”</h2>
 * <li>动机： 在 Java Collection 中，为了防止在某个线程在对 Collection 进行迭代时，其他线程对该 Collection 进行结构上的修改。换句话说，迭代器的快速失败行为仅用于检测代码的 bug。
 * <li>本质： Fail-Fast 是 Java 集合的一种错误检测机制。
 * <li>作用场景： 在使用迭代器时，Collection 的结构发生变化，抛出 ConcurrentModificationException 。 当然，这并不能说明
 * Collection对象已经被不同线程并发修改，因为如果单线程违反了规则，同样也有会抛出该异常。
 * <li>原因： 迭代器在遍历时直接访问集合中的内容，并且在遍历过程中使用一个 modCount 变量。集合在被遍历期间如果内容发生变化，就会改变 modCount 的值。 每当迭代器使用 hashNext()/next()
 * 遍历下一个元素之前，都会检测 modCount 变量是否为 expectedmodCount 值，是的话就返回遍历；否则抛出异常，终止遍历。
 * <li>解决办法：在遍历过程中，所有涉及到改变 modCount 值得地方全部加上 synchronized；使用 CopyOnWriteArrayList 来替换 ArrayList。
 * <p>
 * 在面对并发的修改时，迭代器很快就会完全失败，而不是冒着在将来某个不确定时间发生任意不确定行为的风险。 我们知道 fail-fast 产生的原因就在于：程序在对 collection 进行迭代时，某个线程对该 collection
 * 在结构上对其做了修改。 要想进一步了解 fail-fast 机制，我们首先要对 ConcurrentModificationException 异常有所了解。 当方法检测到对象的并发修改，但不允许这种修改时就抛出该异常。
 * 同时需要注意的是，该异常不会始终指出对象已经由不同线程并发修改，如果单线程违反了规则，同样也有可能会抛出改异常。 诚然，迭代器的快速失败行为无法得到保证，它不能保证一定会出现该错误，但是快速失败操作会尽最大努力抛出
 * ConcurrentModificationException 异常， 所以，为提高此类操作的正确性而编写一个依赖于此异常的程序是错误的做法
 *
 */
public class CollectionDemo {

    @Test
    public void listToArray() {
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(null);
        Integer[] array = list.toArray(new Integer[list.size()]);
        for (Integer element : array) {
            System.out.println(element);
        }
    }

    static void testArray(int... ids) {
        System.out.println(ids);
        System.out.println(Arrays.toString(ids));
        List<int[]> list = Arrays.asList(ids);
        list.forEach(System.out::println);

        System.out.println("原型数据的数组，数组对象作为参数");
        int[] array2 = { 1, 2, 3, 4, 5 };
        List<int[]> list2 = Arrays.asList(array2);
        list2.forEach(System.out::println);
        System.out.println("包装类型的数组，数组的每个对象作为参数");

        Integer[] array3 = { 1, 2, 3, 4, 5 };
        List<Integer> list3 = Arrays.asList(array3);
        String tmp = Arrays.toString(array3);
        System.out.println(tmp);
        System.out.println(tmp.substring(1, tmp.length() - 1));

        List<Integer> list4 = Arrays.asList(1, 2, 3);
        list4.forEach(System.out::println);
    }

    @Test
    public void demoSearch(int a) {
        Integer[] array1 = { 20, 42, 150, 19 };
        int idx = Arrays.binarySearch(array1, a);
        System.out.println(idx);
    }

    @Test
    public void testCollection() {
        ArrayList<String> arrayList = new ArrayList<String>();
        LinkedList<String> linkedList = new LinkedList<String>();
        Vector<String> vector = new Vector<String>();
        Stack<String> stack = new Stack<String>();

        ArrayDeque<String> arrayDeque = new ArrayDeque<String>();
        Map<String, Date> map = Collections.synchronizedMap(new HashMap<String, Date>());
    }

    @Test
    public void testSort() {
        List<Double> list = new ArrayList<Double>();
        list.add(7.5);
        list.add(8.5);
        list.add(6.5);
        for (Double string : list) {
            System.out.print(string + " ");
        }
        Collections.sort(list);
        System.out.println();
        for (Double string : list) {
            System.err.print(string + " ");
        }
        Collections.reverse(list);
        System.out.println();
        for (Double string : list) {
            System.err.print(string + " ");
        }
    }

    @Test
    public void testItorator() {
        List<String> list = new ArrayList<String>();
        list.add("6");
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        for (String string : list) {
            System.out.print(string + " ");
        }
        System.out.println();
        for (Iterator<String> iterator = list.iterator(); iterator.hasNext();) {
            String string = iterator.next();
            System.err.print(string + " ");
        }

    }

    @Test
    public void testDeque() {
        Deque<String> deque = new ArrayDeque<String>();
        deque.addFirst("Fist1");
        deque.addFirst("Fist2");
        deque.addLast("Last1");
        deque.addLast("Last2");
        deque.offer("offer1");
        while (!deque.isEmpty()) {
            String tmpFirst = deque.poll();
            System.out.println(tmpFirst);
        }

    }

    void testQueue() {
        Queue<String> queue;
    }

    @Test
    public void arrayDemo() {
        byte[] anArrayOfBytes;
        short[] anArrayOfShorts;
        long[] anArrayOfLongs;
        float[] anArrayOfFloats;
        // this form is discouraged
        float anArrayOfFloats2[];
        double[] anArrayOfDoubles;
        boolean[] anArrayOfBooleans;
        char[] anArrayOfChars;
        String[] anArrayOfStrings;
        int[] anotherArray = { 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000 };

        // declares an array of integers
        int[] anArray;

        // allocates memory for 10 integers
        anArray = new int[10];

        // initialize first element
        anArray[0] = 100;
        // initialize second element
        anArray[1] = 200;
        // and so forth
        anArray[2] = 300;
        anArray[3] = 400;
        anArray[4] = 500;
        anArray[5] = 600;
        anArray[6] = 700;
        anArray[7] = 800;
        anArray[8] = 900;
        anArray[9] = 1000;

        System.out.println("Element at index 0: " + anArray[0]);
        System.out.println("Element at index 1: " + anArray[1]);
        System.out.println("Element at index 2: " + anArray[2]);
        System.out.println("Element at index 3: " + anArray[3]);
        System.out.println("Element at index 4: " + anArray[4]);
        System.out.println("Element at index 5: " + anArray[5]);
        System.out.println("Element at index 6: " + anArray[6]);
        System.out.println("Element at index 7: " + anArray[7]);
        System.out.println("Element at index 8: " + anArray[8]);
        System.out.println("Element at index 9: " + anArray[9]);

        System.out.println(anArray.length);
    }

    @Test
    public void multiDimArrayDemo() {
        String[][] names = { { "Mr. ", "Mrs. ", "Ms. " }, { "Smith", "Jones" } };
        // Mr. Smith
        System.out.println(names[0][0] + names[1][0]);
        // Ms. Jones
        System.out.println(names[0][2] + names[1][1]);
    }

    @Test
    public void arrayCopyDemo() {
        char[] copyFrom = { 'd', 'e', 'c', 'a', 'f', 'f', 'e', 'i', 'n', 'a', 't', 'e', 'd' };
        char[] copyTo = new char[7];

        System.arraycopy(copyFrom, 2, copyTo, 0, 7);
        System.out.println(new String(copyTo));

        char[] copyTo2 = java.util.Arrays.copyOfRange(copyFrom, 2, 9);
        System.out.println(new String(copyTo2));
    }

}
