package com.daikuan.platform.ma.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daikuan.platform.ma.common.response.ResponseDataVO;
import com.daikuan.platform.ma.constant.MAConstant;
import com.daikuan.platform.ma.constant.ServiceExceptionConstant;
import com.daikuan.platform.ma.exception.ServiceException;
import com.daikuan.platform.ma.pojo.vo.UserProductAuditVO;
import com.daikuan.platform.ma.service.UserProductAuditService;
import com.daikuan.platform.ma.util.Constants;
import com.daikuan.platform.ma.util.MAUtil;

/**
 * 
 * @author yangqb
 * @date 2017年3月13日
 * @desc 进件
 */

@RestController
@RequestMapping("/platform/audit")
public class UserProductAuditController {

    private static Logger logger = LoggerFactory.getLogger(UserProductAuditController.class);

    @Autowired
    private UserProductAuditService userProductAuditService;



    // 进件
    @PostMapping("/entry")
    @CrossOrigin(origins = "*")
    public ResponseDataVO sendUserProductAudit(@RequestBody UserProductAuditVO userProductAuditVO) {

        logger.info("saveUserProductAudit==================" + userProductAuditVO);

        byte status = MAConstant.RES_RESULT_STATUS_SUC;
        String errCode = null;
        String message = null;
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            userProductAuditService.save(userProductAuditVO, map);
        } catch (ServiceException e) {
            errCode = e.getErrCode();
            message = e.getMessage();
            status = MAConstant.RES_RESULT_STATUS_ERR;
            logger.error(message, e);
            // 给一次回滚机会
            if (map.get("applyId") != null) {
                Long applyId = map.get("applyId") == null ? null : (long) map.get("applyId");
                Long adminTaskId = map.get("adminTaskId") == null ? null : (long) map.get("adminTaskId");
                userProductAuditService.rollback(applyId, adminTaskId);
            }
        } catch (Exception e) {
            errCode = ServiceExceptionConstant.OTHER_ERR_CODE;
            message = ServiceExceptionConstant.OTHER_ERR_MESSAGE;
            status = MAConstant.RES_RESULT_STATUS_ERR;
            logger.error(e.getMessage(), e);
            // 给一次回滚机会
            if (map.get("applyId") != null) {
                Long applyId = map.get("applyId") == null ? null : (long) map.get("applyId");
                Long adminTaskId = map.get("adminTaskId") == null ? null : (long) map.get("adminTaskId");
                userProductAuditService.rollback(applyId, adminTaskId);
            }
        }

        if (map.get("auditStatus") == Constants.AUDIT_PASS) {
            // 自动化直接过了
            status = MAConstant.RES_RESULT_STATUS_AUDIT_SUC;
        }
        ResponseDataVO rd = MAUtil.getResponseDataVO(status, errCode, message);
        return rd;
    }

}


