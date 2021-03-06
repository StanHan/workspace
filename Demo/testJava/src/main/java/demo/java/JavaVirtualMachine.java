package demo.java;

/**
 * <h1>JVM</h1> JVM内存永久区已经被metaspace替换（JEP 122）。JVM参数 -XX:PermSize 和 –XX:MaxPermSize被XX:MetaSpaceSize 和
 * -XX:MaxMetaspaceSize代替。
 * 
 * 
 * <h2>JVM 内存划分：</h2>
 * 
 * 
 * <h2>类似-Xms、-Xmn 这些参数的含义：</h2>
 * 
 * <h3>堆内存分配：</h3>
 * <li>JVM 初始分配的内存由-Xms 指定，默认是物理内存的 1/64；
 * <li>JVM 最大分配的内存由-Xmx 指定，默认是物理内存的 1/4；
 * <li>默认空余堆内存小于 40% 时，JVM 就会增大堆直到-Xmx 的最大限制；空余堆内存大于 70% 时，JVM 会减少堆直到 -Xms 的最小限制；
 * <li>因此服务器一般设置-Xms、-Xmx 相等以避免在每次 GC 后调整堆的大小。对象的堆内存由称为垃圾回收器的自动内存管理系统回收。
 * 
 * <h3>非堆内存分配：</h3>
 * <li>JVM 使用-XX:PermSize 设置非堆内存初始值，默认是物理内存的 1/64；
 * <li>由 XX:MaxPermSize 设置最大非堆内存的大小，默认是物理内存的 1/4；
 * <li>-Xmn2G：设置年轻代大小为 2G；
 * <li>-XX:SurvivorRatio，设置年轻代中 Eden 区与 Survivor 区的比值。
 *
 */
public interface JavaVirtualMachine {
    /**
     * <li>方法区（线程共享）：常量、静态变量、JIT(即时编译器) 编译后的代码也都在方法区；
     * <li>堆内存（线程共享）：垃圾回收的主要场所；
     * <li>程序计数器： 当前线程执行的字节码的位置指示器；
     * <li>虚拟机栈（栈内存）：保存局部变量、基本数据类型变量以及堆内存中某个对象的引用变量；
     * <li>本地方法栈 ：为 JVM 提供使用 native 方法的服务。
     */
    void 内存划分();

    /**
     * <li>引用计数 ：原理是此对象有一个引用，即增加一个计数，删除一个引用则减少一个计数。垃圾回收时，只用收集计数为 0 的对象。此算法最致命的是无法处理循环引用的问题；
     * 
     * <li>标记-清除 ：此算法执行分两阶段。第一阶段从引用根节点开始标记所有被引用的对象，第二阶段遍历整个堆，把未标记的对象清除； 此算法需要暂停整个应用，同时，会产生内存碎片；
     * 
     * <li>复制算法 ：此算法把内存空间划为两个相等的区域，每次只使用其中一个区域。垃圾回收时，遍历当前使用区域，把正在使用中的对象复制到另外一个区域中；
     * 此算法每次只处理正在使用中的对象，因此复制成本比较小，同时复制过去以后还能进行相应的内存整理，不会出现 “碎片” 问题。当然，此算法的缺点也是很明显的，就是需要两倍内存空间；
     * 
     * <li>标记-整理 ：此算法结合了 “标记-清除” 和 “复制” 两个算法的优点。也是分两阶段，第一阶段从根节点开始标记所有被引用对象，第二阶段遍历整个堆，把清除未标记对象并且把存活对象 “压缩” 到堆的其中一块，按顺序排放。
     * 此算法避免了 “标记-清除” 的碎片问题，同时也避免了 “复制” 算法的空间问题。
     */
    void 垃圾回收算法();
}
