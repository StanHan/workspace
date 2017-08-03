package demo.log.logback;

/**
 * Logback是由log4j创始人Ceki Gülcü设计的又一个开源日志组件。logback当前分成三个模块：logback-core,logback- classic和logback-access。
 * 
 * Logback的核心对象：Logger、Appender、Layout。
 * 
 * <li>Logger:日志的记录器，把它关联到应用的对应的context上后，主要用于存放日志对象，也可以定义日志类型、级别。
 * <li>Appender:用于指定日志输出的目的地，目的地可以是控制台、文件、远程套接字服务器、 MySQL、 PostreSQL、Oracle和其他数据库、 JMS和远程UNIX Syslog守护进程等。
 * <li>Layout:负责把事件转换成字符串，格式化的日志信息的输出。具体的Layout通配符，可以直接查看帮助文档。
 * 
 * Level 有效级别：
 * 
 * Logger可以被分配级别。级别包括：TRACE、DEBUG、INFO、WARN和ERROR，定义于ch.qos.logback.classic.Level类。程序会打印高于或等于所设置级别的日志，设置的日志等级越高，打印出来的日志就越少。如果设置级别为INFO，则优先级高于等于INFO级别（如：INFO、
 * WARN、ERROR）的日志信息将可以被输出,小于该级别的如DEBUG将不会被输出。为确保所有logger都能够最终继承一个级别，根logger总是有级别，默认情况下，这个级别是DEBUG。
 * 
 * 三值逻辑
 * 
 * Logback的过滤器基于三值逻辑（ternary logic），允许把它们组装或成链，从而组成任意的复合过滤策略。
 * 过滤器很大程度上受到Linux的iptables启发。这里的所谓三值逻辑是说，过滤器的返回值只能是ACCEPT、DENY和NEUTRAL的其中一个。
 * 
 * <li>如果返回DENY，那么记录事件立即被抛弃，不再经过剩余过滤器；
 * <li>如果返回NEUTRAL，那么有序列表里的下一个过滤器会接着处理记录事件；
 * <li>如果返回ACCEPT，那么记录事件被立即处理，不再经过剩余过滤器。
 * 
 * Filter 过滤器
 * 
 * Logback-classic提供两种类型的过滤器：常规过滤器和TuroboFilter过滤器。Logback整体流程：Logger
 * 产生日志信息；Layout修饰这条msg的显示格式；Filter过滤显示的内容；Appender具体的显示，即保存这日志信息的地方。
 * 
 * @author hanjy
 *
 */
public class LogbackDemo {

    public static void main(String[] args) {

    }

}
