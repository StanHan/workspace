/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.job;

import com.daikuan.platform.ma.util.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author feizy
 * @version AdminApproveStatisticsJob, v0.1 2017/3/29 9:55
 */
@Component
public class AdminApproveStatisticsJob {
    @Value("${job.statistics.admin.approve.start.date}")
    String startDate;
    @Autowired
    AdminApproveStatisticsComponent adminApproveStatisticsComponent;
    private Logger LOGGER = LoggerFactory.getLogger(AdminApproveStatisticsJob.class);

    @Scheduled(cron = "${job.statistics.admin.approve.cron}")
    public void execute() {
        LOGGER.info("管理员审批统计：开始。。。。");
        Date now = new Date();
        if (StringUtils.hasText(startDate)) {
            Date start = CommonUtils.stringToDate(startDate, "yyyy-MM-dd");
            adminApproveStatisticsComponent.handle(start, now);
            startDate = null;
        } else {
            adminApproveStatisticsComponent.handle(new Date());
        }
        LOGGER.info("管理员审批统计：完成");
    }
}
