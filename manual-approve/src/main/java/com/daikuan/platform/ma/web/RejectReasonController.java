package com.daikuan.platform.ma.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daikuan.platform.ma.common.request.RejectReasonsRequest;
import com.daikuan.platform.ma.common.response.ResponseDataVO;
import com.daikuan.platform.ma.constant.MAConstant;
import com.daikuan.platform.ma.constant.ServiceExceptionConstant;
import com.daikuan.platform.ma.exception.ServiceException;
import com.daikuan.platform.ma.service.IRejectReasonService;
import com.daikuan.platform.ma.util.MAUtil;
import com.daikuan.platform.ma.vo.RejectReasonsVo;

/**
 * 
 * @author yangqb
 * @date 2017年3月24日
 * @desc 接收拒绝原因
 */
@RestController
@RequestMapping("/platform/reject")
public class RejectReasonController {

    private static Logger logger = LoggerFactory.getLogger(RejectReasonController.class);

    @Autowired
    private IRejectReasonService rejectReasonService;

    @PostMapping("/send")
    @CrossOrigin(origins = "*")
    public ResponseDataVO sendRejectReason(@RequestBody RejectReasonsRequest rejectReasonVO) {
        logger.info("sendRefuseReason==================" + rejectReasonVO);

        byte status = MAConstant.RES_RESULT_STATUS_SUC;
        String errCode = null;
        String message = null;

        try {
            rejectReasonService.save(rejectReasonVO);
        } catch (ServiceException e) {
            errCode = e.getErrCode();
            message = e.getMessage();
            status = MAConstant.RES_RESULT_STATUS_ERR;
            logger.error(message, e);
        } catch (Exception e) {
            errCode = ServiceExceptionConstant.OTHER_ERR_CODE;
            message = ServiceExceptionConstant.OTHER_ERR_MESSAGE;
            status = MAConstant.RES_RESULT_STATUS_ERR;
            logger.error(e.getMessage(), e);
        }

        ResponseDataVO rd = MAUtil.getResponseDataVO(status, errCode, message);
        return rd;
    }
    @CrossOrigin(origins = "*")
    @GetMapping("/all")
    public RejectReasonsVo  queryAll() {
        try {
            RejectReasonsVo vo = rejectReasonService.getRejectReasonByType();
            return vo;

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}
