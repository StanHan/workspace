package com.daikuan.platform.ma.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daikuan.platform.ma.dao.AdminTaskMapper;
import com.daikuan.platform.ma.pojo.AAdminTaskEntity;
import com.daikuan.platform.ma.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private static Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    AdminTaskMapper adminMapper;

    @Override
    public AAdminTaskEntity findById(Long id) {
        return adminMapper.selectByPrimaryKey(id);
    }

}
