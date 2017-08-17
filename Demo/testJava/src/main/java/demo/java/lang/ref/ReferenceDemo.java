package demo.java.lang.ref;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.WeakHashMap;

public class ReferenceDemo {
    public static void main(String[] args) {
        weakReference();
    }

    /**
     * 如果一个对象具有强引用，那就 类似于必不可少的生活用品，垃圾回收器绝不会回收它。当内存空 间不足，Java虚拟机宁愿抛出OutOfMemoryError错误，使程序异常终止，也不会靠随意回收具有强引用的对象来解决内存不足问题。
     */
    static void 强引用() {
        // 在heap堆中创建新的Object对象通过o引用这个对象
        Object o = new Object();
        // 通过o建立o1到new Object()这个heap堆中的对象的引用
        Object o1 = o;
        // 这两个引用都是强引用.只要存在对heap中对象的引用，gc就不会收集该对象.

        // 如果显式地设置o和o1为null，或超出范围，则gc认为该对象不存在引用，这时就可以收集它了。可以收集并不等于就一会被收集，什么时候收集这要取决于gc的算法，
        o = null;
        o1 = null;

        // heap中对象有强可及对象、软可及对象、弱可及对象、虚可及对象和不可到达对象。应用的强弱顺序是强、软、弱、和虚。对于对象是属于哪种可及的对象，由他的最强的引用决定。
        String abc = new String("abc"); // 在heap中创建内容为“abc”的对象，并建立abc到该对象的强引用,该对象是强可及的。
        SoftReference<String> abcSoftRef = new SoftReference<String>(abc); // 建立对heap中对象的软引用，此时heap中的对象仍是强可及的。
        WeakReference<String> abcWeakRef = new WeakReference<String>(abc); // 建立对heap中对象的弱引用，此时heap中的对象仍是强可及的。
        abc = null; // heap中对象不再是强可及的，变成软可及的
        abcSoftRef.clear();// 之后变成弱可及的
    }

    /**
     * 如果一个对象只具有软引用，那就类似于可有可物的生活用品。如果内存空间足够，垃圾回收器就不会回收它，如果内存空间不足了，就会回收这些对象的内存。只要垃圾回收器没有回收它，该对象就可以被程序使用。
     * 软引用可以和一个引用队列（ReferenceQueue）联合使用，如果软引用所引用的对象被垃圾回收，Java虚拟机就会把这个软引用加入到与之关联的引用队列中。
     * 软引用主要用来实现内存敏感的高速缓存。在jvm报告内存不足之前会清除所有的软引用，这样以来gc就有可能收集软可及的对象，可能解决内存吃紧问题，避免内存溢出。什么时候会被收集取决于gc的算法和gc运行时可用内存的大小。
     * 当gc决定要收集软引用是执行以下过程:
     * <ol>
     * <li>1 首先将abcSoftRef的referent设置为null，不再引用heap中的new String(“abc”)对象。
     * <li>2 将heap中的new String(“abc”)对象设置为可结束的(finalizable)。
     * <li>3 当heap中的new String(“abc”)对象的finalize()方法被运行而且该对象占用的内存被释放， abcSoftRef被添加到它的ReferenceQueue中。
     * </ol>
     * 注:对ReferenceQueue软引用和弱引用可以有可无，但是虚引用必须有，参见：Reference(T paramT, ReferenceQueue<? super T> paramReferenceQueue)
     * 
     * 被 Soft Reference 指到的对象，即使没有任何 Direct Reference，也不会被清除。 一直要到 JVM 内存不足且 没有 Direct Reference 时才会清除，SoftReference
     * 是用来设计 object-cache 之用的。 如此一来 SoftReference 不但可以把对象 cache 起来，也不会造成内存不足的错误 （OutOfMemoryError）。
     */
    @SuppressWarnings("unused")
    static void softReference() {
        Object obj = new Object();
        Reference<Object> sr = new SoftReference<Object>(obj);

        // 引用时
        if (sr != null) {
            obj = sr.get();
        } else {
            obj = new Object();
            sr = new SoftReference<Object>(obj);
        }
    }

    /**
     * 弱引用与软引用的区别在于：只具有弱引用的对象拥有更短暂的生命周期。在垃圾回收器线程扫描它 所管辖的内存区域的过程中，一旦发现了只具有弱引用的对象，不管当前内存空间足够与否，都会回收它的内存。
     * 不过，由于垃圾回收器是一个优先级很低的线程， 因此不一定会很快发现那些只具有弱引用的对象。
     * 
     * 弱引用可以和一个引用队列（ReferenceQueue）联合使用，如果弱引用所引用的对象被垃圾回收，Java虚拟机就会把这个弱引用加入到与之关联的引用队列中。
     * 
     * 当gc碰到弱可及对象，并释放abcWeakRef的引用，收集该对象。但是gc可能需要对此运用才能找到该弱可及对象。
     * 
     * gc收集弱可及对象的执行过程和软可及一样，只是gc不会根据内存情况来决定是不是收集该对象。
     * 
     * 如果你希望能随时取得某对象的信息，但又不想影响此对象的垃圾收集，那么你应该用 Weak Reference 来记住此对象，而不是用一般的 reference。
     * 
     * 这类的技巧，在设计 Optimizer 或 Debugger 这类的程序时常会用到，因为这类程序需要取得某对象的信息，但是不可以 影响此对象的垃圾收集。
     */
    static void weakReference() {
        String abc = new String("abc");
        WeakReference<String> abcWeakRef = new WeakReference<String>(abc);
        abc = null;
        System.out.println("before gc: " + abcWeakRef.get());
        System.gc();
        System.out.println("after gc: " + abcWeakRef.get());

        System.out.println("------next demo------");
        Object obj = new Object();

        WeakReference<Object> wr = new WeakReference<Object>(obj);

        obj = null;

        // 等待一段时间，obj对象就会被垃圾回收
        if (wr.get() == null) {
            System.out.println("obj 已经被清除了 ");
        } else {
            System.out.println("obj 尚未被清除，其信息是 " + obj.toString());
        }
    }

    /**
     * 虚引用:与其他几种引用都不同，虚引用并不会决定对象的生命周期。如果一个对象仅持有虚引用，那么它就和没有任何引用一样，在任何时候都可能被垃圾回收。
     * 
     * 虚引用主要用来跟踪对象被垃圾回收的活动。虚引用与软引用和弱引用的一个区别在于：虚引用必须和引用队列（ReferenceQueue）联合使用。
     * 当垃圾回收器准备回收一个对象时，如果发现它还有虚引用，就会在回收对象的内存之前，把这个虚引用加入到与之关联的引用队列中。 程序可以通过判断引用队列中是否已经加入了虚引用，来了解被引用的对象是否将要被垃圾回收。
     * 程序如果发现某个虚引用已经被加入到引用队列，那么就可以在所引用的对象的内存被回收之前采取必要的行动。
     * 
     * 建立虚引用之后通过get方法返回结果始终为null,通过源代码你会发现,虚引用通向会把引用的对象写进referent,只是get方法返回结果为null.先看一下和gc交互的过程在说一下他的作用.
     * 
     * 1 不把referent设置为null, 直接把heap中的new String(“abc”)对象设置为可结束的(finalizable).
     * 
     * 2 与软引用和弱引用不同, 先把PhantomRefrence对象添加到它的ReferenceQueue中.然后在释放虚可及的对象. 你会发现在收集heap中的new
     * String(“abc”)对象之前,你就可以做一些其他的事情.
     */
    static void phantomReference() {

    }

}

/**
 * 软引用（soft reference）在强度上弱于强引用，通过类SoftReference来表示。它的作用是告诉垃圾回收器，程序中的哪些对象是不那么重要，
 * 当内存不足的时候是可以被暂时回收的。当JVM中的内存不足的时候，垃圾回收器会释放那些只被软引用所指向的对象。如果全部释放完这些对象之后，内存还不足， 才会抛出OutOfMemory错误。
 * 软引用非常适合于创建缓存。当系统内存不足的时候，缓存中的内容是可以被释放的。比如考虑一个图像编辑器的程序。
 * 该程序会把图像文件的全部内容都读取到内存中，以方便进行处理。而用户也可以同时打开多个文件。当同时打开的文件过多的时候，就可能造成内存不足。 如果使用软引用来指向图像文件内容的话，垃圾回收器就可以在必要的时候回收掉这些内存。
 *
 */
class ImageData {
    private String path;
    private SoftReference<byte[]> dataRef;

    public ImageData(String path) {
        this.path = path;
        dataRef = new SoftReference<byte[]>(new byte[0]);
    }

    private byte[] readImage() {
        return new byte[1024 * 1024]; // 省略了读取文件的操作
    }

    /**
     * 由于软引用所指向的对象可能被回收掉，在通过get方法来获取软引用所实际指向的对象的时候，总是要检查该对象是否还存活。
     * 
     * @return
     */
    public byte[] getData() {
        byte[] dataArray = dataRef.get();
        if (dataArray == null || dataArray.length == 0) {
            dataArray = readImage();
            dataRef = new SoftReference<byte[]>(dataArray);
        }
        return dataArray;
    }
}

/**
 * 弱引用（weak reference）在强度上弱于软引用，通过类WeakReference来表示。它的作用是引用一个对象，但是并不阻止该对象被回收。 如果使用一个强引用的话，只要该引用存在，那么被引用的对象是不能被回收的。
 * 弱引用则没有这个问题。在垃圾回收器运行的时候，如果一个对象的所有引用都是弱引用的话，该对象会被回收。 弱引用的作用在于解决强引用所带来的对象之间在存活时间上的耦合关系。
 *
 */
class WeakReferenceDemo {
    WeakHashMap<?, ?> weakHashMap = new WeakHashMap<Object, Object>();
    Object object = new Object();
    WeakReference<Object> wr = new WeakReference<Object>(object);
}

/**
 * 虚引用（PhantomReference）
 *
 */
class PhantomBuffer {
    private byte[] data = new byte[0];
    private ReferenceQueue<byte[]> queue = new ReferenceQueue<byte[]>();
    private PhantomReference<byte[]> ref = new PhantomReference<byte[]>(data, queue);

    public byte[] get(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Wrong buffer size");
        }
        if (data.length < size) {
            data = null;
            System.gc(); // 强制运行垃圾回收器
            try {
                queue.remove(); // 该方法会阻塞直到队列非空
                ref.clear(); // 幽灵引用不会自动清空，要手动运行
                ref = null;
                data = new byte[size];
                ref = new PhantomReference<byte[]>(data, queue);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return data;
    }
}

class PhantomReferenceDemo {
    public static boolean isRun = true;

    public static void main(String[] args) throws Exception {
        String abc = new String("abc");
        System.out.println(abc.getClass() + "@" + abc.hashCode());
        final ReferenceQueue<String> referenceQueue = new ReferenceQueue<String>();
        new Thread() {
            public void run() {
                while (isRun) {
                    Object o = referenceQueue.poll();
                    if (o != null) {
                        try {
                            Field field_rereferent = Reference.class.getDeclaredField("referent");
                            field_rereferent.setAccessible(true);
                            Object result = field_rereferent.get(o);
                            System.out.println("gc will collect:" + result.getClass() + "@" + result.hashCode());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
        PhantomReference<String> abcWeakRef = new PhantomReference<String>(abc, referenceQueue);
        abc = null;
        Thread.sleep(3000);
        System.gc();
        Thread.sleep(3000);
        isRun = false;
    }

}
