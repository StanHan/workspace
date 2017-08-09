package demo.java.util;

public interface Java命令 {

    /**
     * jstack用于打印出给定的java进程ID或core file或远程调试服务的Java堆栈信息，如果是在64位机器上，需要指定选项"-J-d64"，Windows的jstack使用方式只支持以下的这种方式： jstack
     * [-l] pid 。如果java程序崩溃生成core文件，jstack工具可以用来获得core文件的java stack和native stack的信息，从而可以轻松地知道java程序是如何崩溃和在程序何处发生问题。
     * 另外，jstack工具还可以附属到正在运行的java程序中，看到当时运行的java程序的java stack和native stack的信息,如果现在运行的java程序呈现hung的状态，jstack是非常有用的。
     * 
     * 命令格式:
     * <ul>
     * <li>jstack [ option ] pid。
     * <ul>
     * <li>-F当’jstack [-l] pid’没有相应的时候强制打印栈信息
     * <li>-l长列表. 打印关于锁的附加信息,例如属于java.util.concurrent的ownable synchronizers列表.
     * <li>-m打印java和native c/c++框架的所有栈信息.
     * <li>-h | -help打印帮助信息
     * <li>pid 需要被打印配置信息的java进程id,可以用jps查询.
     * </ul>
     * <li>jstack [ option ] executable core 。core 将被打印信息的core dump文件
     * <li>jstack [ option ] [server-id@]remote-hostname-or-IP 。remote-hostname-or-IP 远程debug服务的主机名或ip
     * </ul>
     */
    void jstack();

    /**
     * javah是用于根据JAVA本地方法，生成对应的c语言头文件及相应的stub文件的命令。 JNI（Java Native
     * Interface）是Java本地方法调用接口，从Java1.1开始，(JNI)标准就成为java平台的一部分，它允许Java代码和其他语言写的代码进行交互。
     * JNI一开始是为了本地已编译语言，尤其是C和C++而设计的，但是它并不妨碍你使用其他语言，只要调用约定受支持就可以了。
     */
    void javah();

    /**
     * jps命令(Java Virtual Machine Process Status Tool)
     * <p>
     * 用来查看基于HotSpot的JVM里面中，所有具有访问权限的Java进程的具体状态, 包括进程ID，进程启动的路径及启动参数等等，与unix上的ps类似，只不过jps是用来显示java进程，可以把jps理解为ps的一个子集。
     * 使用jps时，如果没有指定hostid，它只会显示本地环境中所有的Java进程；如果指定了hostid，它就会显示指定hostid上面的java进程，不过这需要远程服务上开启了jstatd服务，可以参看前面的jstatd章节来启动jstad服务。
     * <li>jps [ options ] [ hostid ]
     * <li>-q 忽略输出的类名、Jar名以及传递给main方法的参数，只输出pid。
     * <li>-m 输出传递给main方法的参数，如果是内嵌的JVM则输出为null。
     * <li>-l 输出应用程序主类的完整包名，或者是应用程序JAR文件的完整路径。
     * <li>-v 输出传给JVM的参数。
     * <li>-V 输出通过标记的文件传递给JVM的参数（.hotspotrc文件，或者是通过参数-XX:Flags=<filename>指定的文件）。
     * <li>-J 用于传递jvm选项到由javac调用的java加载器中，例如，“-J-Xms48m”将把启动内存设置为48M，使用-J选项可以非常方便的向基于Java的开发的底层虚拟机应用程序传递参数。
     * <li>hostid指定了目标的服务器，它的语法如下：[protocol:][[//]hostname][:port][/servername]
     * <li>protocol - 如果protocol及hostname都没有指定，那表示的是与当前环境相关的本地协议，如果指定了hostname却没有指定protocol，那么protocol的默认就是rmi。
     * <li>hostname - 服务器的IP或者名称，没有指定则表示本机。
     * <li>port - 远程rmi的端口，如果没有指定则默认为1099。
     * <li>Servername - 注册到RMI注册中心中的jstatd的名称。
     */
    void jps();

    /**
     * jstat命令(Java Virtual Machine Statistics Monitoring Tool)
     * <p>
     * Jstat用于监控基于HotSpot的JVM，对其堆的使用情况进行实时的命令行的统计，使用jstat我们可以对指定的JVM做如下监控：
     * <li>类的加载及卸载情况
     * <li>查看新生代、老生代及持久代的容量及使用情况
     * <li>查看新生代、老生代及持久代的垃圾收集情况，包括垃圾回收的次数及垃圾回收所占用的时间
     * <li>查看新生代中Eden区及Survior区中容量及分配情况等 jstat工具特别强大，它有众多的可选项，通过提供多种不同的监控维度，使我们可以从不同的维度来了解到当前JVM堆的使用情况。
     * <p>
     * 
     */
    void jstat();

    /**
     * jinfo命令(Java Configuration Info)
     * <p>
     * jinfo可以输出并修改运行时的java 进程的opts。用处比较简单，用于输出JAVA系统参数及命令行参数。 用法是jinfo -opt pid 如：查看2788的MaxPerm大小可以用 jinfo -flag
     * MaxPermSize 2788。
     */
    void jinfo();

    /**
     * jmap命令(Java Memory Map)
     * <p>
     * 打印出某个java进程（使用pid）内存内的，所有‘对象’的情况（如：产生那些对象，及其数量）。可以输出所有内存中对象的工具，甚至可以将VM 中的heap，以二进制输出成文本。使用方法 jmap -histo
     * pid。如果连用SHELL jmap -histo pid>a.log可以将其保存到文本中去，在一段时间后，使用文本对比工具，可以对比出GC回收了哪些对象。jmap -dump:format=b,file=outfile
     * 3024可以将3024进程的内存heap输出出来到outfile文件里，再配合MAT（内存分析工具(Memory Analysis
     * Tool），使用参见：http://blog.csdn.net/fenglibing/archive/2011/04/02/6298326.aspx）或与jhat (Java Heap Analysis
     * Tool)一起使用，能够以图像的形式直观的展示当前内存是否有问题。 64位机上使用需要使用如下方式： jmap -J-d64 -heap pid
     * <p>
     * <li>jmap [ option ] pid
     * <li>jmap [ option ] executable core
     * <li>jmap [ option ] [server-id@]remote-hostname-or-IP
     * <p>
     * 基本参数：
     * <li>-dump:[live,]format=b,file=<filename> 使用hprof二进制形式,输出jvm的heap内容到文件=. live子选项是可选的，假如指定live选项,那么只输出活的对象到文件.
     * <li>-finalizerinfo 打印正等候回收的对象的信息.
     * <li>-heap 打印heap的概要信息，GC使用的算法，heap的配置及wise heap的使用情况.
     * <li>-histo[:live] 打印每个class的实例数目,内存占用,类全名信息. VM的内部类名字开头会加上前缀”*”. 如果live子参数加上后,只统计活的对象数量.
     * <li>-permstat 打印classload和jvm heap长久层的信息. 包含每个classloader的名字,活泼性,地址,父classloader和加载的class数量.
     * 另外,内部String的数量和占用内存数也会打印出来.
     * <li>-F 强迫.在pid没有相应的时候使用-dump或者-histo参数. 在这个模式下,live子参数无效.
     * <li>-h | -help 打印辅助信息
     * <li>-J 传递参数给jmap启动的jvm.
     * <li>pid 需要被打印配相信息的java进程id,创业与打工的区别 - 博文预览,可以用jps查问.
     */
    void jmap();

}
