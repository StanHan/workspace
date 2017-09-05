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
//        demoSearch(19);
        Collection collection = null;
        // testTreeSet();
        // testHashSet();
//        demoArrays(1, 2, 3);
//        java7Demo();
        listDemo();
    }
    
    static void demoArrays(int... ids) {
        System.out.println(ids);
        Arrays.stream(ids).forEach(System.out::println);
        
        List<int[]> list = Arrays.asList(ids);
        list.forEach(System.out::println);
        
        System.out.println("原型数据的数组，数组对象作为参数");
        int[] array2 = {1,2,3,4,5};
        List<int[]> list2 = Arrays.asList(array2);
        list2.forEach(System.out::println);
        System.out.println("包装类型的数组，数组的每个对象作为参数");
        Integer[] array3 = {1,2,3,4,5};
        List<Integer> list3 = Arrays.asList(array3);
        list3.forEach(System.out::println);
        
        List<Integer> list4 = Arrays.asList(1,2,3);
        list4.forEach(System.out::println);
    }

    static void listDemo(){
        List<String> list = null;
        for (String string : list) {
            System.out.println(string);
        }
    }

    static void demoSearch(int a) {
        Integer[] array1 = { 20, 42, 150, 19 };
        int idx = Arrays.binarySearch(array1, a);
        System.out.println(idx);
    }

    public static void testCollection() {
        ArrayList<String> arrayList = new ArrayList<String>();
        LinkedList<String> linkedList = new LinkedList<String>();
        Vector<String> vector = new Vector<String>();
        Stack<String> stack = new Stack<String>();

        ArrayDeque<String> arrayDeque = new ArrayDeque<String>();
        Map<String, Date> map = Collections.synchronizedMap(new HashMap<String, Date>());
    }

    public static void testTreeSet() {
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

    public static void testHashSet() {
        HashSet<String> set = new HashSet();
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

        Object[] array = set.toArray();
        for (Object string : array) {
            System.out.println(string);
        }
    }

    public static void testSort() {
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

    public static void testItorator() {
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

    public static void testDeque() {
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

    public void testQueue() {
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

}
