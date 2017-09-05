/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.web;

import com.daikuan.platform.ma.bean.ResponseVO;
import com.daikuan.platform.ma.common.request.AdminApproveStatisticsQueryReq;
import com.daikuan.platform.ma.common.response.AdminApproveStatisticsResponse;
import com.daikuan.platform.ma.common.response.AuditStatisticsResponse;
import com.daikuan.platform.ma.common.response.Response;
import com.daikuan.platform.ma.common.vo.AdminApproveStatisticsVO;
import com.daikuan.platform.ma.common.vo.AuditStatisticsVO;
import com.daikuan.platform.ma.pojo.AStatisticsAdminApproveEntity;
import com.daikuan.platform.ma.service.AuditStatisticsService;
import com.daikuan.platform.ma.service.StatisticsAdminApproveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author feizy
 * @version AuditStatisticsController, v0.1 2017/3/27 15:05
 */
@RestController
@RequestMapping("/ma/audit/statistics")
public class AuditStatisticsController {
    @Autowired
    private AuditStatisticsService auditStatisticsService;
    @Autowired
    private StatisticsAdminApproveService statisticsAdminApproveService;
    private static final Logger LOGGER = LoggerFactory.getLogger(AuditStatisticsController.class);

    /**
     * 审核查询：进件统计
     * 
     * @param productId
     * @param beginTime
     * @param endTime
     * @return
     */
    @RequestMapping("/query/{productId}/{beginTime}/{endTime}")
    public AuditStatisticsResponse countEnterAuditPiece(
            @PathVariable("productId") String productId,
            @PathVariable("beginTime") long beginTime,
            @PathVariable("endTime") long endTime) {
        AuditStatisticsResponse responseVO = null;
        responseVO = new AuditStatisticsResponse();
        try {
            AuditStatisticsVO statisticsResult = auditStatisticsService.countEnterAuditPiece("null".equals(productId) ? null : Integer.valueOf(productId),
                    beginTime, endTime);
            if (statisticsResult == null) {
                responseVO.setRtn(Response.FAIL);
                responseVO.setMessage(Response.FAIL_MSG);
                return responseVO;
            }
            responseVO.setResult(statisticsResult);
        } catch (Exception e) {
            LOGGER.error(
                    "product approve statistics happened exception, the productId is {}, beginTime is {}, endTime is {}",
                    productId, beginTime, endTime, e);
            responseVO.setRtn(Response.FAIL);
            responseVO.setMessage(Response.FAIL_MSG);
        }
        return responseVO;
    }

    @RequestMapping("/admin/approve")
    public AdminApproveStatisticsResponse adminApproveStatistics(@RequestBody AdminApproveStatisticsQueryReq requestBody) {
        AdminApproveStatisticsResponse responseVO = null;
        try {
            List<AStatisticsAdminApproveEntity> results = statisticsAdminApproveService.queryByProductIdAndType(requestBody.getProductId(), requestBody.getType(), requestBody.getAuditDate(), requestBody.getAdminId());
            List<AdminApproveStatisticsVO> adminStatisticsVOList = new ArrayList<>();
            for (AStatisticsAdminApproveEntity temp:
                 results) {
                AdminApproveStatisticsVO adminApproveStatisticsVO = new AdminApproveStatisticsVO();
                adminApproveStatisticsVO.setAdminId(temp.getAdminId());
                adminApproveStatisticsVO.setAdminName(temp.getAdminName());
                adminApproveStatisticsVO.setH09(temp.getH09());
                adminApproveStatisticsVO.setH10(temp.getH10());
                adminApproveStatisticsVO.setH11(temp.getH11());
                adminApproveStatisticsVO.setH12(temp.getH12());
                adminApproveStatisticsVO.setH13(temp.getH13());
                adminApproveStatisticsVO.setH14(temp.getH14());
                adminApproveStatisticsVO.setH15(temp.getH15());
                adminApproveStatisticsVO.setH16(temp.getH16());
                adminApproveStatisticsVO.setH17(temp.getH17());
                adminApproveStatisticsVO.setH18(temp.getH18());
                adminApproveStatisticsVO.setH19(temp.getH19());
                adminApproveStatisticsVO.setH20(temp.getH20());
                adminApproveStatisticsVO.setId(temp.getId());
                adminApproveStatisticsVO.setLastApproveTime(temp.getLastApproveTime());
                adminApproveStatisticsVO.setProductId(temp.getProductId());
                adminApproveStatisticsVO.setStatisticsTime(temp.getStatisticsTime());
                adminApproveStatisticsVO.setSubavg(temp.getSubavg());
                adminApproveStatisticsVO.setSubtotal(temp.getSubtotal());
                adminApproveStatisticsVO.setType(temp.getType());
                adminStatisticsVOList.add(adminApproveStatisticsVO);
            }
            responseVO = new AdminApproveStatisticsResponse();
            responseVO.setResultList(adminStatisticsVOList);
        } catch (Exception e) {
            LOGGER.error(
                    "query admin approve statistics happened exception, the param is {}",
                   requestBody, e);
            responseVO = new AdminApproveStatisticsResponse();
            responseVO.setRtn(Response.FAIL);
            responseVO.setMessage(Response.FAIL_MSG);
        }
        return responseVO;
    }
}
