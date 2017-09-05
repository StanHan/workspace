/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.dao;

import com.daikuan.platform.ma.pojo.AStatisticsAdminApproveEntity;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @author feizy
 * @version AStatisticsAdminApproveMapper, v0.1 2017/3/29 15:30
 */
public interface AStatisticsAdminApproveMapper {
    /**
     * 根据a_admin_task信息产生审核员的统计表信息
     * @param statisticsDate
     */
    void statisticsAdminApprove(Date statisticsDate);

    /**
     * 查询审核的统计信息
     * @param productId
     * @param type
     * @param date
     * @param adminId
     * @return
     */
    List<AStatisticsAdminApproveEntity> queryByProductIdAndType(@Param("productId") Integer productId, int type, Date date, @Param("adminId") Long adminId);
}
