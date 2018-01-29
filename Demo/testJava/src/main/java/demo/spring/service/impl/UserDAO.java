package demo.spring.service.impl;

import demo.spring.service.IUserDAO;

public class UserDAO implements IUserDAO {

    @Override
    public Boolean findUserById(String userId) {
        System.out.println("业务逻辑：根据" + userId + "查找。");
        return true;
    }

}
