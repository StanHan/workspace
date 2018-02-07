package demo.java.util;

import java.util.AbstractCollection;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

/**
 * @see Set
 * @see List
 * @see Map
 * @see SortedSet
 * @see SortedMap
 * @see HashSet
 * @see TreeSet
 * @see ArrayList
 * @see LinkedList
 * @see Vector
 * @see Collections
 * @see Arrays
 * @see AbstractCollection
 *
 */
public class CollectionDemo {

    public static void main(String[] args) {
        testSet();
    }

    static void testRemove() {
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

    static void listToArray() {
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

    static void testList() {
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

    static void demoSearch(int a) {
        Integer[] array1 = { 20, 42, 150, 19 };
        int idx = Arrays.binarySearch(array1, a);
        System.out.println(idx);
    }

    static void testCollection() {
        ArrayList<String> arrayList = new ArrayList<String>();
        LinkedList<String> linkedList = new LinkedList<String>();
        Vector<String> vector = new Vector<String>();
        Stack<String> stack = new Stack<String>();

        ArrayDeque<String> arrayDeque = new ArrayDeque<String>();
        Map<String, Date> map = Collections.synchronizedMap(new HashMap<String, Date>());
    }

    static void testTreeSet() {
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
    static void testSet() {
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

    static void testHashSet() {
        HashSet<String> set = new HashSet<>();
        set.add("han");
        set.add("jun");
        set.add("ying");
        set.add("Stan");
        set.add("abc");

        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String string = (String) iterator.next();
            System.out.println(string);
        }
        System.out.println("------------------");
        Object[] array = set.toArray();
        for (Object string : array) {
            System.out.println(string);
        }
    }

    static void testSort() {
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

    static void testItorator() {
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

    static void testDeque() {
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

    static void arrayDemo() {
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

    static void multiDimArrayDemo() {
        String[][] names = { { "Mr. ", "Mrs. ", "Ms. " }, { "Smith", "Jones" } };
        // Mr. Smith
        System.out.println(names[0][0] + names[1][0]);
        // Ms. Jones
        System.out.println(names[0][2] + names[1][1]);
    }

    static void arrayCopyDemo() {
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
