package demo.java.designPattern;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 观察者模式
 *
 */
public class ObserverPattern {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

// 观察者，需要用到观察者模式的类需实现此接口
interface Observer {
	void update(Object... objs);
}

// 被观察者（一个抽象类，方便扩展）
abstract class Observable {

	public final ArrayList<Class<?>> obserList = new ArrayList<Class<?>>();

	/**
	 * AttachObserver（通过实例注册观察者） <b>Notice:</b>ob can't be null,or it will throw
	 * NullPointerException
	 **/
	public <T> void registerObserver(T ob) {
		if (ob == null)
			throw new NullPointerException();
		this.registerObserver(ob.getClass());
	}

	/**
	 * AttachObserver（通过Class注册观察者）
	 * 
	 * @paramcls
	 */
	public void registerObserver(Class<?> cls) {
		if (cls == null)
			throw new NullPointerException();
		synchronized (obserList) {
			if (!obserList.contains(cls)) {
				obserList.add(cls);
			}
		}
	}

	/**
	 * UnattachObserver（注销观察者） <b>Notice:</b>
	 * <b>ItreverseswithattachObserver()method</b>
	 **/
	public <T> void unRegisterObserver(T ob) {
		if (ob == null)
			throw new NullPointerException();
		this.unRegisterObserver(ob.getClass());
	}

	/**
	 * UnattachObserver（注销观察者，有时候在未获取到实例使用） <b>Notice:</b>
	 * <b>ItreverseswithattachObserver()method</b>
	 **/
	public void unRegisterObserver(Class<?> cls) {
		if (cls == null)
			throw new NullPointerException();
		synchronized (obserList) {
			Iterator<Class<?>> iterator = obserList.iterator();
			while (iterator.hasNext()) {
				if (iterator.next().getName().equals(cls.getName())) {
					iterator.remove();
					break;
				}
			}
		}
	}

	/** detach all observers */
	public void unRegisterAll() {
		synchronized (obserList) {
			obserList.clear();
		}
	}

	/** Ruturn the size of observers */
	public int countObservers() {
		synchronized (obserList) {
			return obserList.size();
		}
	}

	/**
	 * notify all observer（通知所有观察者，在子类中实现）
	 * 
	 * @paramobjs
	 */
	public abstract void notifyObservers(Object... objs);

	/**
	 * notify one certain observer（通知某一个确定的观察者）
	 * 
	 * @paramcls
	 * @paramobjs
	 */
	public abstract void notifyObserver(Class<?> cls, Object... objs);

	/**
	 * notifyonecertainobserver
	 * 
	 * @paramcls
	 * @paramobjs
	 */
	public abstract <T> void notifyObserver(T t, Object... objs);
}

// 目标被观察者
class ConcreteObservable extends Observable {

	private static ConcreteObservable instance = null;

	private ConcreteObservable() {
	}

	public static synchronized ConcreteObservable getInstance() {
		if (instance == null) {
			instance = new ConcreteObservable();
		}
		return instance;
	}

	@Override
	public <T> void notifyObserver(T t, Object... objs) {
		if (t == null)
			throw new NullPointerException();
		this.notifyObserver(t.getClass(), objs);
	}

	@Override
	public void notifyObservers(Object... objs) {
		for (Class<?> cls : obserList) {
			this.notifyObserver(cls, objs);
		}
	}

	// 通过java反射机制实现调用
	@Override
	public void notifyObserver(Class<?> cls, Object... objs) {
		if (cls == null)
			throw new NullPointerException();
		Method[] methods = cls.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals("update")) {
				try {
					method.invoke(cls, objs);
					break;
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
}


//使用(实现Observer接口）
class Text implements Observer{
  public void onCreate(){
      ConcreteObservable.getInstance().registerObserver(Text.class);
//      ....
  }

  //实现接口处理
  public void update(Object...objs){
      //做操作，比如更新数据，更新UI等
  }
}