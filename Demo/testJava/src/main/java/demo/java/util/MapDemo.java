package demo.java.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.WeakHashMap;

public class MapDemo {

    public static void main(String[] args) {
        testCanKeyNull();
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
