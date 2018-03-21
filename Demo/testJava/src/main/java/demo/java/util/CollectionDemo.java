package demo.java.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

import org.junit.Test;

/**
 * Set下有HashSet,LinkedHashSet,TreeSet。 List下有ArrayList,Vector,LinkedList。
 * 两个接口都是继承自Collection. 

List (inteface) 

次序是List 的最重要特点,它确保维护元素特定的顺序. 
－－ArrayList 允许对元素快速随机访问. 
－－LinkedList 对顺序访问进行优化,向List 中间插入与移除的开销并不大，具有addFrist(),addLast(),getFirst,getLast,removeFirst和removeLast().这些方法使得LinkedList可当作堆栈／队列／双向队列. 


Set (inteface) 

存入Set 的每个元素必须唯一，不保证维护元素的次序.加入Set 的Object必须定义equals()方法 
－－HashSet　为快速查找而设计的Set ，存入HashSet对象必须定义hashCode(). 
－－TreeSet  保护次序的Set ，使用它可以从Set 中提取有序序列. 
－－LinkedHashSet　　具有HashSet的查询速度，且内部使用链表维护元素的次序. 
它们之间的存储方式不一样： 
TreeSet采用红黑树的树据结构排序元素. 
HashSet采用散列函数，这是专门为快速查询而设计的. 
LinkedHashSet内部使用散列以加快查询速度，同时使用链表维护元素的次序. 

使用HashSet/TreeSet时，必须为类定义equals()；而HashCode()是针对HashSet，作为一种编程风格，当覆盖equals()的时候，就应该同时覆盖hashCode().
 * 
 * 
 * <li>ArrayXxx:底层数据结构是数组，查询快，增删慢
 * <li>LinkedXxx:底层数据结构是链表，查询慢，增删快
 * <li>HashXxx:底层数据结构是哈希表。依赖两个方法：hashCode()和equals()
 * <li>TreeXxx:底层数据结构是二叉树。两种方式排序：自然排序和比较器排序
 *
 */
public class CollectionDemo {

    @Test
    public void testRemove() {
        List<Integer> list = Arrays.asList(1, 2, 3);

        for (Integer integer : list) {
            System.out.println(integer);
            /** remove 时报错 */
            try {
                list.remove(integer);
            } catch (UnsupportedOperationException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println("------------------------------");
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            Integer i = iterator.next();
            System.out.println(i);
            /** remove 时报错 */
            iterator.remove();
        }
    }

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
    public void testList() {
        List<Integer> list = null;
        try {
            System.out.println("如果list为null,foreach 操作会抛空指针");
            for (Integer string : list) {
                System.out.println(string);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        Integer[] array3 = { 1, 2, 3, 4, 5 };
        list = Arrays.asList(array3);

        Integer[] emptyArray = {};
        Integer[] arrayNull = null;
        Integer[] biggerArray = new Integer[10];

        try {
            System.out.println("List.toArray(...) 方法参数为null,报空指针");
            emptyArray = list.toArray(arrayNull);
            System.out.println(Arrays.toString(arrayNull));
            System.out.println(Arrays.toString(emptyArray));
        } catch (Exception e) {
            System.out.println(e);
        }

        {
            System.out.println("List.toArray(...) 参数长度大于list size，全部填充，其他填充为null.");
            arrayNull = list.toArray(biggerArray);
            System.out.println(Arrays.toString(arrayNull));
            System.out.println(Arrays.toString(biggerArray));
        }

        {
            System.out.println("List.toArray(...) 参数长度小于于list size，创建一个新的数组填充，参数数组不变。让人误解的逻辑！！");
            arrayNull = list.toArray(emptyArray);
            System.out.println(Arrays.toString(arrayNull));
            System.out.println(Arrays.toString(emptyArray));
        }

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
    public void testTreeSet() {
        TreeSet<String> treeSet = new TreeSet<String>();
        treeSet.add("han");
        treeSet.add("jun");
        treeSet.add("ying");
        treeSet.add("Stan");
        treeSet.add("abc");

        Iterator<String> iterator = treeSet.iterator();
        while (iterator.hasNext()) {
            String string = (String) iterator.next();
            System.out.println(string);
        }

        /*
         * Object[] array = treeSet.toArray(); for (Object string : array) { System.out.println(string); }
         */
    }

    /**
     * 测试集合交集、并集、差集等操作
     */
    @Test
    public void testSet() {
        Set<Integer> result = new HashSet<Integer>();

        System.out.println("set1=" + set1);
        System.out.println("set2=" + set2);
        System.out.println("emptySet=" + emptySet);
        System.out.println("nullSet=" + nullSet);

        result.clear();
        System.out.println(result.addAll(set1));
        System.out.println(result.retainAll(set2));
        System.out.println("set1 与 set2 交集：" + result);

        result.clear();
        System.out.println(result.addAll(set1));
        System.out.println(result.removeAll(set2));
        System.out.println("set1 与 set2 差集：" + result);

        result.clear();
        System.out.println(result.addAll(set1));
        System.out.println(result.addAll(set2));
        System.out.println("set1 与 set2 并集：" + result);

        System.out.println(set1.removeAll(emptySet));
        System.out.println("set1=" + set1);
        System.out.println("emptySet=" + emptySet);

        result.clear();
        System.out.println(result.addAll(set1));
        System.out.println(result.addAll(nullSet));
        System.out.println("set1 与 nullSet 并集：" + result);
    }

    @Test
    public void testHashSet() {
        HashSet<String> set = new HashSet<>();
        set.add("b20");
        set.add("c3");
        set.add("D4");
        set.add("e5");
        set.add("a1");

        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " , ");
        }
        System.out.println();
        for (String string : set) {
            System.out.print(string + " , ");
        }
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

    static Set<Integer> set1 = new HashSet<Integer>() {
        {
            add(1);
            add(2);
        }
    };

    static Set<Integer> set2 = new HashSet<Integer>() {
        {
            add(1);
            add(3);
        }
    };

    static Set<Integer> emptySet = new HashSet<Integer>();
    static Set<Integer> nullSet = null;

}
