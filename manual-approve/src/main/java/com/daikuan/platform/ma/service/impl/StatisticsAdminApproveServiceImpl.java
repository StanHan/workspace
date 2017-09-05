/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.service.impl;

import com.daikuan.platform.ma.dao.AStatisticsAdminApproveMapper;
import com.daikuan.platform.ma.pojo.AStatisticsAdminApproveEntity;
import com.daikuan.platform.ma.service.StatisticsAdminApproveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author feizy
 * @version StatisticsAdminApproveServiceImpl, v0.1 2017/4/20 16:33
 */
@Service
public class StatisticsAdminApproveServiceImpl implements StatisticsAdminApproveService {

    @Autowired
    private AStatisticsAdminApproveMapper statisticsAdminApproveMapper;

    @Override
    public List<AStatisticsAdminApproveEntity> queryByProductIdAndType(Integer productId, int type, long auditDate, Long adminId) {
        return statisticsAdminApproveMapper.queryByProductIdAndType(productId, type, new Date(auditDate), adminId);
    }
}
