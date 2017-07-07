package demo.java.lang.reflect;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

public class ProxyTest {

	public static void main(String[] args) {
		testDynamicProxy();
		System.out.println("----------------");
		testStaticProxy();
	}

	/**
	 * 测试动态代理
	 */
	@Test
	public static void testDynamicProxy() {
		IUserDAO userDAO = new UserDAOImpl();
		InvocationHandler handler = new DAOLogHandler(userDAO);
		// 动态创建代理对象，用于代理一个AbstractUserDAO类型的真实主题对象
		IUserDAO proxy = (IUserDAO) Proxy.newProxyInstance(IUserDAO.class.getClassLoader(),
				new Class[] { IUserDAO.class }, handler);
		proxy.findUserById("张无忌"); // 调用代理对象的业务方法
	}

	/**
	 * 测试静态代理
	 */
	@Test
	public static void testStaticProxy() {
		IUserDAO userDAO = new UserDAOImpl();
		IUserDAO staticProxy = new StaticProxy(userDAO);
		staticProxy.findUserById("张无忌");
	}

	public static void testCglibProxy(){
		UserDAOImpl dao = new UserDAOImpl();
        CglibProxy cglibProxy=new CglibProxy();
        UserDAOImpl hw=(UserDAOImpl)cglibProxy.createProxy(dao);
        hw.findUserById("张无忌");
	}
	
}

// 抽象UserDAO：抽象主题角色
interface IUserDAO {
	public Boolean findUserById(String userId);
}

// 具体UserDAO类：真实主题角色
class UserDAOImpl implements IUserDAO {
	public Boolean findUserById(String userId) {
		if (userId.equalsIgnoreCase("张无忌")) {
			System.out.println("查询ID为" + userId + "的用户信息成功！");
			return true;
		} else {
			System.out.println("查询ID为" + userId + "的用户信息失败！");
			return false;
		}
	}
}

/**
 * 静态代理
 *
 */
class StaticProxy implements IUserDAO {
	private Calendar calendar;
	public IUserDAO iUserDao;

	public StaticProxy(IUserDAO userDao) {
		this.iUserDao = userDao;
	}

	@Override
	public Boolean findUserById(String userId) {
		beforeInvoke();
		boolean result = iUserDao.findUserById(userId);
		afterInvoke();
		return result;
	}

	// 记录方法调用时间
	public void beforeInvoke() {
		calendar = new GregorianCalendar();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		String time = hour + ":" + minute + ":" + second;
		System.out.println("调用时间：" + time);
	}

	public void afterInvoke() {
		System.out.println("方法调用结束！");
	}
}

/**
 * 自定义请求处理程序类. 动态代理类只能代理接口，代理类都需要实现InvocationHandler类，实现invoke方法。
 * 该invoke方法就是调用被代理接口的所有方法时需要调用的，该invoke方法返回的值是被代理接口的一个实现类
 *
 */
class DAOLogHandler implements InvocationHandler {
	private Calendar calendar;
	private Object object;

	public DAOLogHandler() {
	}

	// 自定义有参构造函数，用于注入一个需要提供代理的真实主题对象
	public DAOLogHandler(Object object) {
		this.object = object;
	}

	// 实现invoke()方法，调用在真实主题类中定义的方法
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		beforeInvoke();
		Object result = method.invoke(object, args); // 转发调用
		afterInvoke();
		return result;
	}

	// 记录方法调用时间
	public void beforeInvoke() {
		calendar = new GregorianCalendar();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		String time = hour + ":" + minute + ":" + second;
		System.out.println("调用时间：" + time);
	}

	public void afterInvoke() {
		System.out.println("方法调用结束！");
	}
}


class CglibProxy implements MethodInterceptor {
    //要代理的原始对象
    private Object obj;
    
    public Object createProxy(Object target) {
        this.obj = target;
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(this.obj.getClass());// 设置代理目标
        enhancer.setCallback(this);// 设置回调
        enhancer.setClassLoader(target.getClass().getClassLoader());
        return enhancer.create();
    }


    /**
     * 在代理实例上处理方法调用并返回结果
     * 
     * @param proxy
     *            代理类
     * @param method
     *            被代理的方法
     * @param params
     *            该方法的参数数组
     * @param methodProxy
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] params,
            MethodProxy methodProxy) throws Throwable {
        Object result = null;
        // 调用之前
        doBefore();
        // 调用原始对象的方法
        result = methodProxy.invokeSuper(proxy, params);
        // 调用之后
        doAfter();
        return result;
    }

    private void doBefore() {
        System.out.println("before method invoke");
    }

    private void doAfter() {
        System.out.println("after method invoke");
    }



}