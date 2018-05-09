package demo.java.lang;

public class ThreadGroupDemo {

}

class ThreadGroupDemo1 extends ThreadGroup {

    public ThreadGroupDemo1(String name) {
        super(name);
    }

    public ThreadGroupDemo1() {
        super("ThreadGroup1");
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println(getName() + " catch a exception from " + t.getName());
        super.uncaughtException(t, e);
    }

}
