package demo.java.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.WeakHashMap;

import org.junit.Test;

/**
 * Map下有Hashtable,LinkedHashMap,HashMap,TreeMap
 * <li>HashMap：它根据键的hashCode值存储数据，大多数情况下可以直接定位到它的值，因而具有很快的访问速度，但遍历顺序却是不确定的。 HashMap最多只允许一条记录的键为null，允许多条记录的值为null。
 * HashMap非线程安全，即任一时刻可以有多个线程同时写HashMap，可能会导致数据的不一致。
 * 如果需要满足线程安全，可以用Collections的synchronizedMap方法使HashMap具有线程安全的能力，或者使用ConcurrentHashMap。
 * 
 * <li>Hashtable：Hashtable是遗留类，很多映射的常用功能与HashMap类似，不同的是它承自Dictionary类，
 * 并且是线程安全的，任一时间只有一个线程能写Hashtable，并发性不如ConcurrentHashMap，因为ConcurrentHashMap引入了分段锁。
 * Hashtable不建议在新代码中使用，不需要线程安全的场合可以用HashMap替换，需要线程安全的场合可以用ConcurrentHashMap替换。
 * 
 * <li>LinkedHashMap：LinkedHashMap是HashMap的一个子类，保存了记录的插入顺序，在用Iterator遍历LinkedHashMap时，先得到的记录肯定是先插入的，也可以在构造时带参数，按照访问次序排序。
 * 
 * <li>TreeMap：TreeMap实现SortedMap接口，能够把它保存的记录根据键排序，默认是按键值的升序排序，也可以指定排序的比较器，当用Iterator遍历TreeMap时，得到的记录是排过序的。
 * 如果使用排序的映射，建议使用TreeMap。在使用TreeMap时，key必须实现Comparable接口或者在构造TreeMap传入自定义的Comparator，否则会在运行时抛出java.lang.ClassCastException类型的异常。
 * <p>
 * 对于上述四种Map类型的类，要求映射中的key是不可变对象。不可变对象是该对象在创建后它的哈希值不会被改变。如果对象的哈希值发生变化，Map对象很可能就定位不到映射的位置了。
 * 
 *
 */
public class MapDemo {

    /**
     * 从结构实现来讲，HashMap是数组+链表+红黑树（JDK1.8增加了红黑树部分）实现的。
     * <h3>数据底层具体存储的是什么？这样的存储方式有什么优点呢？</h3>
     * <li>(1) 从源码可知，HashMap类中有一个非常重要的字段，就是 Node[] table，即哈希桶数组，明显它是一个Node的数组。
     * Node是HashMap的一个内部类，实现了Map.Entry接口，本质是就是一个映射(键值对)
     * <li>(2)HashMap就是使用哈希表来存储的。哈希表为解决冲突，可以采用开放地址法和链地址法等来解决问题，Java中HashMap采用了链地址法。
     * 链地址法，简单来说，就是数组加链表的结合。在每个数组元素上都一个链表结构，当数据被Hash后，得到数组下标，把数据放在对应下标元素的链表上。
     * <p>
     * 根据key的hashCode()方法得到其hashCode 值（该方法适用于每个Java对象），然后再通过Hash算法的后两步运算（高位运算和取模运算，下文有介绍）来定位该键值对的存储位置，
     * 有时两个key会定位到相同的位置，表示发生了Hash碰撞。当然Hash算法计算结果越分散均匀，Hash碰撞的概率就越小，map的存取效率就会越高。
     * 
     * 如果哈希桶数组很大，即使较差的Hash算法也会比较分散，如果哈希桶数组数组很小，即使好的Hash算法也会出现较多碰撞，
     * 所以就需要在空间成本和时间成本之间权衡，其实就是在根据实际情况确定哈希桶数组的大小，并在此基础上设计好的hash算法减少Hash碰撞。
     * 
     * <h3>那么通过什么方式来控制map使得Hash碰撞的概率又小，哈希桶数组（Node[]table）占用空间又少呢？</h3>答案就是好的Hash算法和扩容机制。
     * 在理解Hash和扩容流程之前，我们得先了解下HashMap的几个字段。
     * <li>int threshold; // 所能容纳的key-value对极限
     * <li>final float loadFactor; // 负载因子
     * <li>int modCount;
     * <li>int size;
     * <p>
     * Node[] table的初始化长度length(默认值是16)，Load factor为负载因子(默认值是0.75)，threshold是HashMap所能容纳的最大数据量的Node(键值对)个数。
     * threshold=length*loadFactor。也就是说，在数组定义好长度之后，负载因子越大，所能容纳的键值对个数越多。
     * 结合负载因子的定义公式可知，threshold就是在此loadFactor和length(数组长度)对应下允许的最大元素数目，超过这个数目就重新resize(扩容)，扩容后的HashMap容量是之前容量的两倍。
     * 默认的负载因子0.75是对空间和时间效率的一个平衡选择，建议大家不要修改，除非在时间和空间比较特殊的情况下，如果内存空间很多而又对时间效率要求很高，可以降低负载因子Load factor的值；
     * 相反，如果内存空间紧张而对时间效率要求不高，可以增加负载因子loadFactor的值，这个值可以大于1。
     * size这个字段其实很好理解，就是HashMap中实际存在的键值对数量。注意和table的长度length、容纳最大键值对数量threshold的区别。
     * 而modCount字段主要用来记录HashMap内部结构发生变化的次数，主要用于迭代的快速失败。强调一点，内部结构发生变化指的是结构发生变化，例如put新键值对，但是某个key对应的value值被覆盖不属于结构变化。
     * 这里存在一个问题，即使负载因子和Hash算法设计的再合理，也免不了会出现拉链过长的情况，一旦出现拉链过长，则会严重影响HashMap的性能。于是，在JDK1.8版本中，对数据结构做了进一步的优化，引入了红黑树。
     * 而当链表长度太长（默认超过8）时，链表就转换为红黑树，利用红黑树快速增删改查的特点提高HashMap的性能，其中会用到红黑树的插入、删除、查找等算法。
     * <h3>HashMap的put方法</h3>
     * <li>①.判断键值对数组table[i]是否为空或为null，否则执行resize()进行扩容；
     * <li>②.根据键值key计算hash值得到插入的数组索引i，如果table[i]==null，直接新建节点添加，转向⑥，如果table[i]不为空，转向③；
     * <li>③.判断table[i]的首个元素是否和key一样，如果相同直接覆盖value，否则转向④，这里的相同指的是hashCode以及equals；
     * <li>④.判断table[i] 是否为treeNode，即table[i] 是否是红黑树，如果是红黑树，则直接在树中插入键值对，否则转向⑤；
     * <li>⑤.遍历table[i]，判断链表长度是否大于8，大于8的话把链表转换为红黑树，在红黑树中执行插入操作，否则进行链表的插入操作；遍历过程中若发现key已经存在直接覆盖value即可；
     * <li>⑥.插入成功后，判断实际存在的键值对数量size是否超多了最大容量threshold，如果超过，进行扩容。
     * 
     * <h3>扩容机制</h3> 扩容(resize)就是重新计算容量，向HashMap对象里不停的添加元素，而HashMap对象内部的数组无法装载更多的元素时，对象就需要扩大数组的长度，以便能装入更多的元素。
     * 当然Java里的数组是无法自动扩容的，方法是使用一个新的数组代替已有的容量小的数组。
     * 
     * <h3>小结</h3>
     * <li>(1) 扩容是一个特别耗性能的操作，所以当程序员在使用HashMap的时候，估算map的大小，初始化的时候给一个大致的数值，避免map进行频繁的扩容。
     * <li>(2) 负载因子是可以修改的，也可以大于1，但是建议不要轻易修改，除非情况非常特殊。
     * <li>(3) HashMap是线程不安全的，不要在并发的环境中同时操作HashMap，建议使用ConcurrentHashMap。
     * <li>(4) JDK1.8引入红黑树大程度优化了HashMap的性能。
     * <li>(5) 还没升级JDK1.8的，现在开始升级吧。HashMap的性能提升仅仅是JDK1.8的冰山一角。
     */
    public void hashMap() {
        HashMap<String, Object> map = new HashMap<>(8);
        map.put("a", "a");
    }

    @Test
    public void demoHash() {
        int hashCode = "Stan".hashCode();
        System.out.println("hashCode:      " + Integer.toBinaryString(hashCode));
        System.out.println("hashCode>>>16: " + Integer.toBinaryString(hashCode >>> 16));
        System.out.println("h^(h>>>16):    " + Integer.toBinaryString(hashCode ^ (hashCode >>> 16)));
        System.out.println("(n-1)&hash:    " + Integer.toBinaryString((16 - 1) & (hashCode ^ (hashCode >>> 16))));
        System.out.println((16 - 1) & (hashCode ^ (hashCode >>> 16)));

        System.out.println(indexFor(hash("Stan"), 16));
    }

    /**
     * <h2>确定哈希桶数组索引位置</h2> JDK1.8优化了高位运算的算法，通过hashCode()的高16位异或低16位实现的，
     * 主要是从速度、功效、质量来考虑的，这么做可以在数组table的length比较小的时候，也能保证考虑到高低Bit都参与到Hash的计算中，同时不会有太大的开销。
     */
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    /**
     * <h2>确定哈希桶数组索引位置</h2>这里的Hash算法本质上就是三步：取key的hashCode值、高位运算、取模运算。
     * 这个方法非常巧妙，它通过h&(table.length-1)来得到该对象的保存位，而HashMap底层数组的长度总是2的n次方，这是HashMap在速度上的优化。
     * 当length总是2的n次方时，h&(length-1)运算等价于对length取模，也就是h%length，但是&比%具有更高的效率。
     */
    static int indexFor(int h, int length) { // jdk1.7的源码，jdk1.8没有这个方法，但是实现原理一样的
        return h & (length - 1); // 第三步 取模运算
    }

    public void treeMap() {
        TreeMap<String, Object> treeMap = new TreeMap<>();
    }

    /**
     * LinkedHashMap 在不对HashMap做任何改变的基础上，给HashMap的任意两个节点间加了两条连线(before指针和after指针)，使这些节点形成一个双向链表。
     * 
     * <pre>
     *     void foo(Map m) {
     *         Map copy = new LinkedHashMap(m);
     *         ...
     *     }
     * </pre>
     */
    public void linkedHashMap() {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();
    }

    public static final Map<String, String> CLIENTS;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("CDW", "qazwsx");
        CLIENTS = Collections.unmodifiableMap(map);
    }

    /**
     * 不可变的Map（原理：使用内部静态类）
     */
    @Test
    public void unmodifiableMap() {
        Map<Integer, StringBuilder> map = new HashMap<>();
        map.put(1, new StringBuilder("aa"));
        map.put(2, new StringBuilder("bb"));
        System.out.println(map);
        Map<Integer, StringBuilder> unmodifiableMap = Collections.unmodifiableMap(map);
        try {
            // 抛出 java.lang.UnsupportedOperationException
            unmodifiableMap.put(3, new StringBuilder("cc"));
            System.out.println(unmodifiableMap);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        }
        StringBuilder key2 = unmodifiableMap.get(2);
        key2.append("222222");
        System.out.println(unmodifiableMap.get(2));
    }

    /**
     * 测试Map的KEY能否为null,结果是可以
     */
    static void testCanKeyNull() {
        Map<String, String> map = new HashMap<>();
        // Map<String, String> map = new LinkedHashMap<>();
        map.put("1", "Han");
        map.put(null, null);
        System.out.println(map);
        map.put(null, "Stan");
        System.out.println(map.get(null));
    }

    static void testMap() {
        /*
         * HashMap 也用到了哈希码的算法，以便快速查找一个键
         */
        HashMap<String, String> hashMap = new HashMap<String, String>();

        /* TreeMap 是对键按序存放,因此它便有一些扩展的方法，比如 firstKey(),lastKey() 等，你还可以从 TreeMap 中指定一个范围以取得其子 Map */
        TreeMap<String, String> treeMap = new TreeMap<String, String>();
        Hashtable<String, String> hashTable = new Hashtable<String, String>();
        WeakHashMap<String, String> weakHashMap = new WeakHashMap<String, String>();

    }

    static void testProperties() throws IOException {
        Properties properties = new Properties();

        InputStream is = MapDemo.class.getResourceAsStream("d:/test/aaa.properties");
        properties.load(is);

        Set<Object> set = properties.keySet();
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            Object key = (Object) iterator.next();
            System.out.println(key.toString() + " = " + properties.getProperty((String) key));
        }

        properties = System.getProperties();
    }
}
