package demo.java.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.Test;

/**
 * <h2>List 结构图中所涉及到的类</h2>
 * <li>AbstractList 是一个抽象类，它实现List接口并继承于 AbstractCollection 。对于“按顺序遍历访问元素”的需求，使用List的Iterator
 * 即可以做到，抽象类AbstractList提供该实现；而访问特定位置的元素（也即按索引访问）、元素的增加和删除涉及到了List中各个元素的连接关系，并没有在AbstractList中提供实现（List
 * 的类型不同，其实现方式也随之不同，所以将具体实现放到子类）；
 * <li>AbstractSequentialList 是一个抽象类，它继承于 AbstractList。AbstractSequentialList 通过 ListIterator
 * 实现了“链表中，根据index索引值操作链表的全部函数”。此外，ArrayList 通过 System.arraycopy(完成元素的挪动) 实现了“顺序表中，根据index索引值操作顺序表的全部函数”；
 * <li>ArrayList, LinkedList, Vector, Stack 是 List 的 4 个实现类；
 * <li>ArrayList 是一个动态数组。它由数组实现，随机访问效率高，随机插入、随机删除效率低；
 * <li>LinkedList 是一个双向链表（顺序表）。LinkedList 随机访问效率低，但随机插入、随机删除效率高，。可以被当作堆栈、队列或双端队列进行操作；
 * <li>Vector 是矢量队列，和ArrayList一样，它也是一个动态数组，由数组实现。但ArrayList是非线程安全的，而Vector是线程安全的；
 * <li>Stack 是栈，它继承于Vector。它的特性是：先进后出(FILO, First In Last Out).
 * <p>
 * <h2>List 特性：</h2>
 * <li>Java 中的 List 是对数组的有效扩展，它是这样一种结构：如果不使用泛型，它可以容纳任何类型的元素，如果使用泛型，那么它只能容纳泛型指定的类型的元素。和数组（数组不支持泛型）相比，List 的容量是可以动态扩展的；
 * <li>List 中的元素是“有序”的。这里的“有序”，并不是排序的意思，而是说我们可以对某个元素在集合中的位置进行指定，包括对列表中每个元素的插入位置进行精确地控制、根据元素的整数索引（在列表中的位置）访问元素和搜索列表中的元素；
 * <li>List 中的元素是可以重复的，因为其为有序的数据结构；
 * <li>List中常用的集合对象包括：ArrayList、Vector和LinkedList，其中前两者是基于数组来进行存储，后者是基于链表进行存储。其中Vector是线程安全的，其余两个不是线程安全的；
 * <li>List中是可以包括 null 的，即使使用了泛型；
 * <li>List 接口提供了特殊的迭代器，称为 ListIterator，除了允许 Iterator 接口提供的正常操作外，该迭代器还允许元素插入和替换，以及双向访问。
 *
 */
public class ListDemo {

    /**
     * <li>ArrayList 实现了 List 中所有可选操作，并允许包括 NULL 在内的所有元素。
     * <li>ArrayList 是基于数组实现的，是一个动态数组，其容量能自动增长，并且用 size属性来标识该容器里的元素个数，而非这个被包装数组的大小。
     * 自动增长会带来数据向新数组的重新拷贝。因此，如果可预知数据量的多少，可在构造ArrayList时指定其容量 。
     * <li>ArrayList 实现了 RandomAccess 接口， 支持快速随机访问，实际上就是通过下标序号进行快速访问(与是否支持get(index)访问不同)。 RandomAccess 接口是 List
     * 实现所使用的标记接口，用来表明其支持快速（通常是固定时间）随机访问。
     * <li>ArrayList 实现了Cloneable接口，能被克隆。Cloneable 接口里面没有任何方法，只是起一个标记作用，表明当一个类实现了该接口时，该类可以通过调用clone()方法来克隆该类的实例。
     * <li>ArrayList不是线程安全的，只能用在单线程环境下。多线程环境下可以考虑用 Collections.synchronizedList(List l) 函数返回一个线程安全的ArrayList类，也可以使用
     * concurrent 并发包下的 CopyOnWriteArrayList 类。
     * <li>ArrayList 的源码看到，其包括 两个域 : transient Object[] elementData ： 支撑数组和int size ： 元素个数
     * <li>向 ArrayList 中增加元素时，都要去检查添加后元素的个数是否会超出当前数组的长度。如果超出，ArrayList 将会进行扩容，以满足添加数据的需求。数组扩容通过一个 public 方法
     * ensureCapacity(int minCapacity) 来实现 : 在实际添加大量元素前，我也可以使用 ensureCapacity 来手动增加 ArrayList 实例的容量，以减少递增式再分配的数量。
     * ArrayList 进行扩容时，会将老数组中的元素重新拷贝一份到新的数组中，每次数组容量的增长为其原容量的 1.5 倍 + 1。这种操作的代价是很高的，因此在实际使用时，我们应该尽量避免数组容量的扩张。
     * <li>ArrayList 的实现中大量地调用了Arrays.copyof() 和 System.arraycopy()方法 。
     */
    @Test
    public void arrayList() {
        ArrayList<Object> arrayList = new ArrayList<>();
        arrayList.clone();
        // 调整数组容量（减少容量）：将底层数组的容量调整为当前列表保存的实际元素的大小
        arrayList.trimToSize();
        // 数组扩容
        arrayList.ensureCapacity(10);
        // 用指定的元素替代此列表中指定位置上的元素，并返回以前位于该位置上的元素
        Object oldObject = arrayList.set(5, new Object());
        // 将指定的元素添加到此列表的尾部
        arrayList.add(new Object());
        // 将指定的元素插入此列表中的指定位置。 如果当前位置有元素，则向右移动当前位于该位置的元素以及所有后续元素（将其索引加1）。
        arrayList.add(3, new Object());
        // 按照指定collection的迭代器所返回的元素顺序，将该collection中的所有元素添加到此列表的尾部。
        arrayList.addAll(new ArrayList<>());
        // 从指定的位置开始，将指定collection中的所有元素插入到此列表中。
        arrayList.addAll(5, new ArrayList<>());
        // 返回此列表中指定位置上的元素。
        arrayList.get(0);
        // 移除此列表中指定位置上的元素
        Object removedObject = arrayList.remove(0);
        // 移除此列表中 “首次” 出现的指定元素（如果存在）。这是因为 ArrayList 中允许存放重复的元素。
        arrayList.remove(new Object());

    }

    /**
     * CopyOnWriteArrayList是ArrayList 的一个线程安全的变体，其中所有可变操作（add、set 等等）都是通过对底层数组进行一次新的复制来实现的。 该类产生的开销比较大，但是在两种情况下，它非常适合使用。
     * <li>1：在不能或不想进行同步遍历，但又需要从并发线程中排除冲突时。
     * <li>2：当遍历操作的数量大大超过可变操作的数量时。遇到这两种情况使用CopyOnWriteArrayList来替代ArrayList再适合不过了。
     * <p>
     * 那么为什么CopyOnWriterArrayList可以替代ArrayList呢？
     * 
     * <li>第一、CopyOnWriterArrayList的无论是从数据结构、定义都和ArrayList一样。它和ArrayList一样，同样是实现List接口，底层使用数组实现。在方法上也包含add、remove、clear、iterator等方法。
     * <li>第二、CopyOnWriterArrayList根本就不会产生ConcurrentModificationException异常，也就是它使用迭代器完全不会产生fail-fast机制。
     * <p>
     * CopyOnWriterArrayList的add方法与ArrayList的add方法有一个最大的不同点就在于，下面三句代码：
     * 
     * <pre>
     * Object[] arrayOfObject2 = Arrays.copyOf(arrayOfObject1, i + 1);
     * arrayOfObject2[i] = paramE;
     * setArray(arrayOfObject2);
     * </pre>
     * 
     * 他们所展现的魅力就在于copy原来的array，再在copy数组上进行add操作，这样做就完全不会影响COWIterator中的array了。
     * CopyOnWriterArrayList所代表的核心概念就是：任何对array在结构上有所改变的操作（add、remove、clear等），CopyOnWriterArrayList都会copy现有的数据，再在copy的数据上修改，
     * 这样就不会影响COWIterator中的数据了，修改完成之后改变原有数据的引用即可。同时这样造成的代价就是产生大量的对象，同时数组的copy也是相当有损耗的。
     */
    public void copyOnWriteArrayList() {
        CopyOnWriteArrayList<Object> copyOnWriteArrayList = new CopyOnWriteArrayList<>();
    }

    /**
     * LinkedList 是 List接口的双向链表实现。LinkedList 实现了 List 中所有可选操作，并且允许所有元素（包括 null）。除了实现 List 接口外，LinkedList
     * 为在列表的开头及结尾进行获取(get)、删除(remove)和插入(insert)元素提供了统一的访问操作，而这些操作允许LinkedList
     * 作为Stack(栈)、Queue(队列)或Deque(双端队列：double-ended queue)进行使用。
     * <li>LinkedList 是一个继承于AbstractSequentialList的双向链表。它也可以被当作堆栈、队列或双端队列进行操作；
     * <li>LinkedList 实现 List 接口，具有 List 的所有功能；
     * <li>LinkedList 实现 Deque 接口，即能将LinkedList当作双端队列使用；
     * <li>LinkedList 实现了Cloneable接口，即覆盖了函数clone()，能克隆；
     * <li>LinkedList 实现java.io.Serializable接口，这意味着LinkedList支持序列化，能通过序列化去传输；
     * <li>与 ArrayList 不同，LinkedList 没有实现 RandomAccess 接口，不支持快速随机访问。 相对于ArrayList，LinkedList有更好的增删效率，更差的随机访问效率；
     * <p>
     * LinkedList底层的数据结构是基于双向链表的，且头结点中不存放数据,既然是双向链表，那么必定存在一种数据结构——我们可以称之为节点，节点实例保存业务数据，前一个节点的位置信息和后一个节点位置信息，
     */
    @Test
    public void linkedList() {
        LinkedList<Object> linkedList = new LinkedList<>();
        List<Object> synchronizedList = Collections.synchronizedList(linkedList);

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
    public void testRemove() {
        List<Integer> list = Arrays.asList(1, 2, 3);

        for (Integer integer : list) {
            System.out.println(integer);
            /** remove 时报错 */
            try {
                list.remove(integer);
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
            }
        }

    }

    @Test
    public void listIterator() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        ListIterator<Integer> listIterator = list.listIterator();
        while (listIterator.hasNext()) {
            Integer i = listIterator.next();
            System.out.println(i);
        }
    }
}
