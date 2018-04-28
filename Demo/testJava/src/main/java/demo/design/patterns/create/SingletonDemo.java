package demo.design.patterns.create;

/**
 * <h1>设计模式之Singleton(单态/单例)</h1>
 * <h2>单态定义:</h2> Singleton模式主要作用是保证在Java应用程序中，一个类Class只有一个实例存在。
 * 
 * 在很多操作中，比如建立目录 数据库连接都需要这样的单线程操作。
 * 
 * 还有, singleton能够被状态化;
 * 这样，多个单态类在一起就可以作为一个状态仓库一样向外提供服务，比如，你要论坛中的帖子计数器，每次浏览一次需要计数，单态类能否保持住这个计数，并且能synchronize的安全自动加1，如果你要把这个数字永久保存到数据库，你可以在不修改单态接口的情况下方便的做到。
 * 
 * 另外方面，Singleton也能够被无状态化。提供工具性质的功能，
 * 
 * Singleton模式就为我们提供了这样实现的可能。使用Singleton的好处还在于可以节省内存，因为它限制了实例的个数，有利于Java垃圾回收（garbage collection）。
 * 
 * 我们常常看到工厂模式中类装入器(class loader)中也用Singleton模式实现的,因为被装入的类实际也属于资源。
 *
 * <h2>使用Singleton注意事项：</h2>
 * 有时在某些情况下，使用Singleton并不能达到Singleton的目的，如有多个Singleton对象同时被不同的类装入器装载；在EJB这样的分布式系统中使用也要注意这种情况，因为EJB是跨服务器，跨JVM的。
 * 
 * Singleton模式看起来简单，使用方法也很方便，但是真正用好，是非常不容易，需要对Java的类 线程 内存等概念有相当的了解。
 * 
 * 总之：如果你的应用基于容器，那么Singleton模式少用或者不用，可以使用相关替代技术。
 */
public class SingletonDemo {

}

/**
 * 
 *
 */
class Singleton1 {

    private Singleton1() {
    }

    // 在自己内部定义自己一个实例，是不是很奇怪？
    // 注意这是private 只供内部调用

    private static Singleton1 instance = new Singleton1();

    // 这里提供了一个供外部访问本class的静态方法，可以直接访问
    public static Singleton1 getInstance() {
        return instance;
    }
}

/**
 * 这种形式是lazy initialization，也就是说第一次调用时初始Singleton，以后就不用再生成了。
 * 
 * 注意到lazy initialization形式中的synchronized，这个synchronized很重要，如果没有synchronized，那么使用getInstance()是有可能得到多个Singleton实例。
 *
 */
class Singleton2 {
    private static Singleton2 instance = null;

    public static synchronized Singleton2 getInstance() {
        if (instance == null) {
            instance = new Singleton2();
        }
        return instance;
    }

}
