package demo.java.lang;

public class RuntimeDemo {

    /**
     * GC的基础算法：标记清除，标记整理，复制，分代。这些算法的第一步都是做的一件事: 标记(Mark)。JVM的标记算法采用了根搜索算法(Root Tracing)。
     * 
     * 根有几种：
     * <li>1. JVM栈的Frame里面的引用
     * <li>2. 静态类，常量的引用
     * <li>3. 本地栈中的引用
     * <li>4. 本地方法的引用
     * 
     * 一般我们能控制的就是JVM栈中的引用和静态类，常量的引用。标记也分为几个阶段，比如
     * <li>1. 标记直接和根引用的对象
     * <li>2. 标记间接和根引用的对象
     * <li>3. 由于分代算法，被老年代对象所引用的新生代的对象
     * 
     * 对于第三种，JVM采用了Card Marking(卡片标记)的方法，避免了在做Minor GC时需要对整个老年代扫描。具体的方法如下:
     * <li>1. 将老年代的内存分片，1个片默认是512byte
     * <li>2. 如果老年代的对象发生了修改，就把这个老年代对象所在的片标记为脏 dirty。或者老年代对象指向了新生代对象，那么它所在的片也会被标记为dirty
     * <li>3. 没有标记为脏的老年代片它没有指向新的新生代对象，所以可以不需要去扫描
     * <li>4. Minor GC扫描老年代空间时，只需要去扫描脏的卡片的对象，不需要扫描整个老年代空间
     * 
     * 所以做Minor GC时标记的时间 = T(stack_scan) + T(card_scan) + T(old_root_scan).
     * <li>T(stack_scan): 级联扫描在JVM栈里的根的时间
     * <li>T(card_scan): 级联扫描卡表中脏卡片的时间
     * <li>T(old_root_scan): 扫描在老年代中的直接的根的时间。
     * 
     * 注意是直接的根，不会去级联扫描老年代的对象。因为扫描都是从根开始的，一开始不知道根到底是在老年代还是新生代
     * 
     * 和Card Marking相关的一个重要的JVM参数是-XX:UseCondCardMark
     * 。使用这个参数的原因是在高并发的情况下，Card标记为脏的操作本身就存在着竞争，使用这个参数可以避免卡片被重复标记为脏，从而提高性能。
     * 
     * 分代算法是将对象分为新生代和老年代，然后使用不同的GC策略来进行回收，提高整体的效率。 由于新生代的大部分对象都会在一次Minor GC中死亡，存活的对象很少，所以新生代的GC收集器都采用了复制算法。 新生代分为Eden +
     * S0 + S1。 S0和S1就是用来实现复制的，在任何一次Minor GC后，S0和S1总是只有一个区域有数据，另一个区域为空，以便于下一次复制使用
     * 当新生代空间不能满足大对象分配时，老年代空间为它提供了分配担保，大对象可以直接进入老年代。
     * 
     * 有两个JVM参数可以控制新生代进入老年代的门槛:
     * <li>PretenureSizeThreshold: 单位是B，设置了对象大小的阀值
     * <li>MaxTenuringThreshold: 设置了进入老年代的年龄的阀值
     * 
     * 老年代对象一般都是存活时间久，老年代的空间本来就大，所以没有更多空间来提供分配担保，所以老年代一般采用标记--清理或者标记--整理算法。
     * 
     * <li>1. 新生代都采用复制算法
     * <li>2. CMS采用了标记--清除算法，由于标记清除算法会生成内存碎片，所以JVM提供了参数来使CMS可以在几次清除后作一次整理
     * -XX:CMSFullGCsBeforeCompaction：由于并发收集器不对内存空间进行压缩、整理，所以运行一段时间以后会产生“碎片”，使得运行效率降低。此值设置运行多少次GC以后对内存空间进行压缩、整理。
     * -XX:+UseCMSCompactAtFullCollection：打开对年老代的压缩。可能会影响性能，但是可以消除碎片
     * <li>3. Serial Old(MSC)和Parallel Old都采用标记整理算法
     * <li>4. UseSerialGC默认会在新生代使用Serial收集器，在老年代使用Serial Old收集器，这两个都是单线程的收集器
     * <li>5. UseConcMarkSweepGC默认会再新生代使用ParNew收集器，这是个并发的收集器。在老年代会使用CMS + Serial Old收集器，当CMS失败的时候，会启用Serial Old做FULL GC
     * <li>6. UseParallelOldGC默认会在新生的使用Parallel Scavenge收集器，在老年代使用Parallel
     * Old收集器。这两个收集器都是吞吐量优先，所谓吞吐量优先就是它可以严格控制GC的时间，从而保证吞吐量。但是吞吐量提高了，新生代和老年代的空间就是动态调整的，而不是按照初始配置的大小。因为单位时间清除的垃圾量近乎一个常量，既然要保证时间，那么必须保证垃圾总量，而垃圾总量可以通过新生代和老年代的大小来控制的
     * <li>7. 对于和用户有交互的应用，比如Web应用，一个重要的考量是系统的响应时间，要保证系统的响应时间就要保证由GC导致的stop the
     * world次数少，或者让用户线程和GC线程一起运行。所以Web应用是使用CMS收集器的一个重要场景。CMS减少了stop the world的次数，不可避免地让整体GC的时间拉长了。
     * <li>8. 对于计算密集型的应用可能会考虑计算的吞吐量，这时候可以使用Parallel Scavenge收集器来保证吞吐量
     * <li>9. Serial, ParNew, Parallel Scanvange, Parallel Old, Serial Old全程都会Stop the world，JVM这时候只运行GC线程，不运行用户线程
     * <li>10. CMS主要分为 initial Mark, Concurrent Mark, ReMark, Concurrent Sweep等阶段，initial Mark和Remark占整体的时间比较较小，它们会Stop
     * the world. Concurrent Mark和Concurrent Sweep会和用户线程一起运行。
     * 
     * 下面这张图对GC的日志信息做了说明:
     * 
     * 
     * 关于JVM调优的各种参数设置，网上一抓一大把，这里不多说了。有一个调优的整体的原则：
     * <li>1. 先做一个JVM的性能测试，了解当前的状态
     * <li>2. 明确调优的目标，比如减少FULL GC的次数，减少GC的总时间，提高吞吐量等
     * <li>3. 调整参数后再进行多次的测试，分析，最终达到一个较为理想的状态。各种参数要根据系统的自身情况来确定，没有统一的解决方案
     * 
     * 将各种工具的文章页很多，这里从解决问题的角度出发列出几个。
     * 
     * 查看JVM启动参数
     * <li>1. jps -v
     * <li>2. jinfo -flags pid
     * <li>3. jinfo pid -- 列出JVM启动参数和system.properties
     * <li>4. ps -ef | grep java
     * 
     * 查看当前堆的配置
     * <li>1. jstat -gc pid 1000 3 -- 列出堆的各个区域的大小
     * <li>2. jstat -gcutil pid 1000 3 -- 列出堆的各个区域使用的比例
     * <li>3. jmap -heap pid -- 列出当前使用的GC算法，堆的各个区域大小
     * 
     * 查看线程的堆栈信息
     * <li>1. jstack -l pid
     * 
     * dump堆内的对象
     * <li>1. jmap -dump:live,format=b,file=xxx pid
     * <li>2. -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=xxx -- 设置JVM参数，当JVM OOM时输出堆的dump
     * <li>3. ulimit -c unlimited -- 设置Linux
     * ulimit参数，可以产生coredump且不受大小限制。之前在线上遇到过一个极其诡异的问题，JVM整个进程突然挂了，这时候依靠JVM本身生成dump文件已经不行了，只有依赖Linux，让系统来生成进程挂掉的core
     * dump文件 使用jstack 可以来获得这个coredump的线程堆栈信息: jstack "$JAVA_HOME/bin/java" core.xxx > core.log
     * 
     * 获得当前系统占用CPU最高的10个进程，线程 ps Hh -eo pid,tid,pcpu,pmem | sort -nk3 |tail > temp.txt
     * 
     * 图形化界面
     * <li>1. jvisualvm 里面有很多插件，比如Visual GC，可以可视化地看到各个堆区域时候的状态，从而可以对整体GC的性能有整体的认识
     */
    static void gc() {
        Runtime.getRuntime().gc();
    }
}
