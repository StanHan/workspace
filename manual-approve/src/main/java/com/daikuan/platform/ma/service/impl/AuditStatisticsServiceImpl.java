/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.service.impl;

import com.daikuan.platform.ma.common.vo.AuditStatisticsVO;
import com.daikuan.platform.ma.dao.AdminTaskMapper;
import com.daikuan.platform.ma.service.AuditStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author feizy
 * @version AuditStatisticsServiceImpl, v0.1 2017/3/27 16:08
 */
@Service
public class AuditStatisticsServiceImpl implements AuditStatisticsService {
    @Autowired
    private AdminTaskMapper adminTaskMapper;

    @Override
    public AuditStatisticsVO countEnterAuditPiece(Integer productId, long beginTime, long endTime) {
        // 已认领件数
        // 已审核件数（预审通过数量+预审拒绝数量）
        // 预审通过数量
        // 预审通过人数
        // 预审拒绝数量
        // 预审拒绝人数
        // 自动通过件数
        // 自动通过人数
        AuditStatisticsVO productApproveStatisticsVO = adminTaskMapper.queryProductApproveStatistics(productId,
                new Date(beginTime), new Date(endTime));
        if (productApproveStatisticsVO == null)
            return null;
        productApproveStatisticsVO.setApproveAmt(
                productApproveStatisticsVO.getAgreeTaskAmt() + productApproveStatisticsVO.getRejectTaskAmt());
        productApproveStatisticsVO
                .setClaimAmt(adminTaskMapper.queryClaimAmt(productId, new Date(beginTime), new Date(endTime)));
        productApproveStatisticsVO.setProductId(productId);
        return productApproveStatisticsVO;
    }
}
