package demo.java.designPattern.proxy;

import demo.spring.service.IActions;
import demo.vo.People;

public class ProxyDemo {

    public static void main(String[] args) {
        proxyTest();
        proxyTest2();
    }

    static void proxyTest() {

        JDKProxyFactory factory = new JDKProxyFactory();

        People people = new People();
        people.setId(0);
        people.setName("Stan");
        
        IActions service = (IActions) factory.createProxyIntance(people);
        service.say("666!");
    }

    static void proxyTest2() {
        People people = new People();
        people.setId(0);
        people.setName("Stan");

        CGlibProxyFactory factory = new CGlibProxyFactory();
        

        People service = (People) factory.createProxyIntance(people);

        service.say("888");

    }

}
