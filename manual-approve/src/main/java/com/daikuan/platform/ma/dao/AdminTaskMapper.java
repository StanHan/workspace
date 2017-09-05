package com.daikuan.platform.ma.dao;

import java.util.Date;
import java.util.List;

import com.daikuan.platform.ma.common.vo.AuditStatisticsVO;
import com.daikuan.platform.ma.pojo.AAdminTaskEntity;
import org.apache.ibatis.annotations.Param;

public interface AdminTaskMapper extends BaseMapper<AAdminTaskEntity> {

    List<AAdminTaskEntity> select(AAdminTaskEntity vo);

    /**
     * 审核统计查询
     * 
     * @param productId
     * @param beginDate
     * @param endDate
     * @return
     */
    AuditStatisticsVO queryProductApproveStatistics(@Param("productId") final Integer productId,
                                                    final Date beginDate, final Date endDate);

    /**
     * 根据日期查询认领的数量
     * 
     * @param productId
     * @param beginDate
     * @param endDate
     * @return
     */
    long queryClaimAmt(@Param("productId") final Integer productId, final Date beginDate, final Date endDate);


    Long queryApprovedAmount(@Param("adminId") final Integer adminId);
}