package demo.java.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CopyOnWriteArraySet;

import org.junit.Test;

/**
 * 
 * @author hanjy
 *
 */
public class SetDemo {

    /**
     * 
     */
    @Test
    public void testCopyOnWriteArraySet() {
        CopyOnWriteArraySet<String> tt = new CopyOnWriteArraySet<>();
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
