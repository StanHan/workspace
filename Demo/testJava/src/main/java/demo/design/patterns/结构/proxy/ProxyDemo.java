package demo.design.patterns.结构.proxy;

import org.junit.Test;

import demo.java.lang.reflect.JDKProxyFactory;
import demo.spring.service.IActions;
import demo.vo.People;

/**
 * <h1>设计模式之Proxy(代理)</h1> 设计模式中定义: 为其他对象提供一种代理以控制对这个对象的访问.
 * 
 * <h2>为什么要使用Proxy?</h2>
 * <li>1.授权机制:
 * 不同级别的用户对同一对象拥有不同的访问权利,如论坛系统中,就使用Proxy进行授权机制控制,访问论坛有两种人:注册用户和游客(未注册用户),Jive中就通过类似ForumProxy这样的代理来控制这两种用户对论坛的访问权限.
 * <li>2.某个客户端不能直接操作到某个对象,但又必须和那个对象有所互动. 举例两个具体情况:
 * (1)如果那个对象是一个是很大的图片,需要花费很长时间才能显示出来,那么当这个图片包含在文档中时,使用编辑器或浏览器打开这个文档,打开文档必须很迅速,不能等待大图片处理完成,这时需要做个图片Proxy来代替真正的图片.
 * (2)如果那个对象在Internet的某个远端服务器上,直接操作这个对象因为网络速度原因可能比较慢,那我们可以先用Proxy来代替那个对象.
 * 总之原则是,对于开销很大的对象,只有在使用它时才创建,这个原则可以为我们节省很多宝贵的Java内存. 所以,有些人认为Java耗费资源内存,我以为这和程序编制思路也有一定的关系.
 * 
 * <h2>如何使用Proxy?</h2>
 * 
 *
 */
public class ProxyDemo {

    public static void main(String[] args) {
    }

    @Test
    public void proxyByJDK() {

        JDKProxyFactory factory = new JDKProxyFactory();

        People people = new People();
        people.setId(0);
        people.setName("Stan");

        IActions service = (IActions) factory.createProxyIntance(people);
        service.say("666!");
    }

    @Test
    public void proxyByCGlib() {
        People people = new People();
        people.setId(0);
        people.setName("Stan");

        CGlibProxyFactory factory = new CGlibProxyFactory();

        People service = (People) factory.createProxyIntance(people);

        service.say("888");

    }

}
