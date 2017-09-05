package com.daikuan.platform.ma.service;

import com.daikuan.platform.ma.exception.dao.DaoException;

/**
 * Created by xiady on 2017/5/22.
 */
public interface AdminTaskService {
    Long queryApprovedAmount(Integer adminId) throws DaoException;
}
