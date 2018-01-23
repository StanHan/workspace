package demo.db.mabatis.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import demo.db.mabatis.dao.UserMapper;

@Service
public class UserService {

    @Autowired
    private UserMapper mapper;
    
}
