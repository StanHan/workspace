package demo.java.lang.reflect;

import demo.spring.service.IUserDAO;

/**
 * 静态代理
 *
 */
public class UserDaoStaticProxyDemo implements IUserDAO {

    public IUserDAO userDao;

    public UserDaoStaticProxyDemo(IUserDAO userDao) {
        this.userDao = userDao;
    }

    @Override
    public Boolean findUserById(String userId) {
        beforeInvoke();
        boolean result = userDao.findUserById(userId);
        afterInvoke();
        return result;
    }

    // 记录方法调用时间
    public void beforeInvoke() {
        System.out.println("调用时间：" + System.currentTimeMillis());
    }

    public void afterInvoke() {
        System.out.println("结束时间：" + System.currentTimeMillis());
    }
}
