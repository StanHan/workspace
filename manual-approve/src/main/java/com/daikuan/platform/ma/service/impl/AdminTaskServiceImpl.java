package com.daikuan.platform.ma.service.impl;

import com.daikuan.platform.ma.constant.ServiceExceptionConstant;
import com.daikuan.platform.ma.dao.AdminTaskMapper;
import com.daikuan.platform.ma.exception.ServiceException;
import com.daikuan.platform.ma.exception.dao.DaoException;
import com.daikuan.platform.ma.service.AdminTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by xiady on 2017/5/22.
 */

@Service
public class AdminTaskServiceImpl implements AdminTaskService{

    @Autowired
    private AdminTaskMapper adminTaskMapper;
    @Override
    public Long queryApprovedAmount(Integer adminId) throws DaoException{
        try {
            return adminTaskMapper.queryApprovedAmount(adminId);
        }catch (Exception e){
            throw new DaoException("审批-查询审核员审件数量出现异常, adminId=" + adminId, e);
        }

    }
}
