/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.service;

import com.daikuan.platform.ma.pojo.AStatisticsAdminApproveEntity;

import java.util.List;

/**
 * @author feizy
 * @version StatisticsAdminApproveService, v0.1 2017/4/20 16:33
 */
public interface StatisticsAdminApproveService {

    /**
     * 查询审核员的统计信息
     * @param productId
     * @param type
     * @param auditDate
     * @param adminId
     * @return
     */
    List<AStatisticsAdminApproveEntity> queryByProductIdAndType(Integer productId, int type, long auditDate, Long adminId);
}
