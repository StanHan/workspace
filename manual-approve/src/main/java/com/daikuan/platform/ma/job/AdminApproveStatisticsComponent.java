/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.job;

import com.daikuan.platform.ma.dao.AStatisticsAdminApproveMapper;
import com.daikuan.platform.ma.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author feizy
 * @version AdminApproveStatisticsComponent, v0.1 2017/3/29 11:18
 */
@Component
public class AdminApproveStatisticsComponent {
    @Autowired
    private AStatisticsAdminApproveMapper statisticsAdminApprove;

    private Logger LOGGER = LoggerFactory.getLogger(AdminApproveStatisticsComponent.class);


    @Transactional(rollbackFor = Exception.class)
    public void handle(Date date) {
        LOGGER.info("客户审批统计【{}】开始...", CommonUtils.dateToString(date, "yyyy-MM-dd"));
        statisticsAdminApprove.statisticsAdminApprove(date);
        LOGGER.info("客户审批统计【{}】结束...", CommonUtils.dateToString(date, "yyyy-MM-dd"));
    }

    @Transactional(rollbackFor = Exception.class)
    public void handle(Date start, Date end) {
        start = CommonUtils.convertSpecialTimeDate(start, 1);
        end = CommonUtils.convertSpecialTimeDate(end, 1);
        LOGGER.info("客户审批统计循环【{}】开始...", CommonUtils.dateToString(start, "yyyy-MM-dd"));
        while (start.getTime() <= end.getTime()) {
            LOGGER.info("客户审批统计【{}】开始...", CommonUtils.dateToString(start, "yyyy-MM-dd"));
            statisticsAdminApprove.statisticsAdminApprove(start);
            LOGGER.info("客户审批统计【{}】结束...", CommonUtils.dateToString(start, "yyyy-MM-dd"));
            start = CommonUtils.getNextDay(start);
        }
        LOGGER.info("客户审批统计循环【{}】结束...", CommonUtils.dateToString(end, "yyyy-MM-dd"));
    }
}
