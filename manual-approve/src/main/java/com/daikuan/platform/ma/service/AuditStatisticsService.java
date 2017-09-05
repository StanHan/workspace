/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.service;

import com.daikuan.platform.ma.common.vo.AuditStatisticsVO;

/**
 * @author feizy
 * @version AuditStatisticsService, v0.1 2017/3/27 16:06
 */
public interface AuditStatisticsService {
    /**
     * 产品审批统计
     * @param productId
     * @param beginTime
     * @param endTime
     * @return
     */
    AuditStatisticsVO countEnterAuditPiece(Integer productId, long beginTime, long endTime);
}
