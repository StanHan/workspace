package com.daikuan.platform.ma.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.daikuan.platform.ma.constant.MAConstant;
import com.daikuan.platform.ma.exception.ErrorInfo;
import com.daikuan.platform.ma.exception.ServiceException;
import com.daikuan.platform.ma.exception.dao.DaoException;
import com.daikuan.platform.ma.pojo.AUserProductAuditStatusEntity;
import com.daikuan.platform.ma.pojo.WsResult;
import com.daikuan.platform.ma.pojo.vo.AuditCallbackVO;
import com.daikuan.platform.ma.redis.RedisUtil;
import com.daikuan.platform.ma.service.AdminTaskService;
import com.daikuan.platform.ma.service.ProductAudit;
import com.daikuan.platform.ma.service.UserProductAuditService;
import com.daikuan.platform.ma.util.Constants;
import com.daikuan.platform.ma.util.MAUtil;
import com.daikuan.platform.ma.util.RedisKey;
import com.daikuan.platform.ma.vo.AuditVo;
import com.daikuan.platform.ma.vo.CommitVO;

@RestController
@RequestMapping("/ma")
public class UserController {

    private static Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProductAudit productAudit;

    @Autowired
    private UserProductAuditService userProductAuditService;

    @Autowired
    private AdminTaskService adminTaskService;

    /**
     * 放弃审核操作
     *
     * @param adminId
     * @param applyId
     *            即a_user_product_audit_status的主键
     * @return
     */
    @RequestMapping(value = "/giveUp", method = RequestMethod.GET)
    @CrossOrigin(origins = "*")
    public Boolean giveUp(@RequestParam(value = "adminId") Integer adminId,
            @RequestParam(value = "adminName") String adminName, @RequestParam(value = "applyId") Long applyId,
            @RequestParam(value = "claimTime") long claimTime, @RequestParam(value = "displayTime") long displayTime,
            @RequestParam(value = "spendTime") long spendTime) {
        Boolean result = false;
        AuditVo vo = (AuditVo) redisUtil.get(RedisKey.KEY_LOADED_APP_ + applyId);
        System.out.println("vo:" + vo + "adminId:" + adminId + "applyId:" + applyId);
        if (vo == null) {
            log.error("get Object from Redis return null. key = " + RedisKey.KEY_LOADED_APP_ + applyId);
            return result;
        }
        try {
            productAudit.addAdminTaskRecord(adminId, adminName, vo, Constants.AUDIT_GIVE_UP, new Date(claimTime),
                    new Date(displayTime), spendTime);
            productAudit.resetRedisWhenGiveUp(adminId, applyId);
            result = true;
        } catch (Exception e) {
            log.error("giving up claimed audit accoured exception,amindId:{},applyId:{} ", adminId, applyId);
        }
        return result;

    }

    /**
     * 取消所有认领
     *
     * @param adminId
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/cancelClaimed", method = RequestMethod.GET)
    public Boolean cancelClaimed(@RequestParam(value = "adminId") Integer adminId) {
        Boolean result = false;
        try {
            productAudit.resetRedisWhenCancel(adminId);
            result = true;

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 初始化操作，用户进入页面时执行
     *
     * @param adminId
     */
    @CrossOrigin(origins = "*")
    @RequestMapping(value = "/init", method = RequestMethod.GET)
    public void init(@RequestParam(value = "adminId") long adminId, HttpServletRequest request) {
        log.info("init Redis Queue by admin:" + adminId);
        // 删除该审核员加载的申件ID集合
        stringRedisTemplate.delete(RedisKey.SET_LOADED_ + adminId);
        // 执行初始化任务
        productAudit.initData();

        request.getSession().setAttribute("adminId", adminId);// 有啥用？
    }

    /**
     * 获取一条审件
     *
     * @param adminId
     * @return
     */

    @CrossOrigin
    @RequestMapping(value = "/getApplying", method = RequestMethod.GET)
    public AuditVo getApplying(@RequestParam(value = "adminId") int adminId) {

        log.info("getApplying ===领件  ===================================");
        String memberId = productAudit.fetchMember(adminId);
        log.info("admin {} fetch Member:{}.", adminId, memberId);
        // 一次补偿的机会,首次为空则初始化redis
        if (memberId == null) {
            productAudit.initData();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            memberId = productAudit.fetchMember(adminId);
            log.info("admin {} try to fetch member again, return Member:{}.", adminId, memberId);
            if (memberId == null) {
                return null;
            }
        }
        // else {
        AuditVo vo = (AuditVo) redisUtil.get(RedisKey.KEY_LOADED_APP_ + memberId);
        if (vo == null) {
            log.error("get vo from redis return null,key=" + RedisKey.KEY_LOADED_APP_ + memberId);
            return null;
        } else {
            // 推送时需要
            vo.setAppId(vo.getApplyId() + "");
            vo.setApplyId(Long.parseLong(memberId));
            Long approvedNum = 0L;
            try {
                approvedNum = adminTaskService.queryApprovedAmount(adminId);
                if (approvedNum == null) {
                    approvedNum = 0L;
                }
            } catch (DaoException e) {
                log.error("query approvedNum error,adminId:" + adminId);
            }
            vo.setApprovedNum(approvedNum);
            return vo;
        }
        // }
    }

    /**
     * 完成一笔审批后提交
     *
     * @param commitVO
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    @CrossOrigin
    @ResponseBody
    public WsResult<Byte> sumbit(@RequestBody CommitVO commitVO) {
        log.info("submit ===提交审核 ===================================");
        WsResult<Byte> wr = new WsResult<Byte>();
        AUserProductAuditStatusEntity productAuditStatus = null;
        try {
            productAuditStatus = productAudit
                    .selectAUserProductAuditStatusByApplyId(Long.valueOf(commitVO.getApplyId()));
        } catch (ServiceException e1) {
            // 已被审核过
            wr.setResult(MAConstant.RES_RESULT_STATUS_ERR);
            wr.setError(new ErrorInfo("", "查询该件数据是否已被审核失败"));
            return wr;
        }

        Byte auditStatus = (productAuditStatus != null) ? productAuditStatus.getAuditStatus() : null;

        if (auditStatus == Constants.AUDIT_REFUSE || auditStatus == Constants.AUDIT_PASS) {
            // 已被审核过
            productAudit.resetRedisWhenSubmit(commitVO.getAdminId().toString(), commitVO.getApplyId().toString());
            wr.setResult(MAConstant.RES_RESULT_STATUS_ERR);
            wr.setError(new ErrorInfo("", "已被审核员" + "过, 不能再次审核!"));
            return wr;
        }

        AuditVo vo = (AuditVo) redisUtil.get(RedisKey.KEY_LOADED_APP_ + commitVO.getApplyId());

        log.info("vo:" + vo + ", comimtVO:" + commitVO.getApplyId());
        productAudit.resetRedisWhenSubmit(commitVO.getAdminId().toString(), commitVO.getApplyId().toString());

        productAudit.addAdminTaskRecord(commitVO.getAdminId(), commitVO.getAdminName(), vo, commitVO.getAuditStatus(),
                commitVO.getClaimTime(), commitVO.getDisplayTime(), commitVO.getSpendTime());

        productAudit.updateAuditStatus(commitVO);

        List<Integer> list = commitVO.getReasonIds();
        StringBuilder sb = new StringBuilder();
        if (list != null && !list.isEmpty()) {
            for (Integer integer : list) {
                log.info("reason id : {}", integer);
                sb.append(integer + ",");
            }
        }
        AuditCallbackVO auditCallbackVO = new AuditCallbackVO();
        auditCallbackVO.setApplyId(Long.valueOf(commitVO.getAppId()));
        auditCallbackVO.setReasonIds(sb.toString());
        auditCallbackVO.setStatus(commitVO.getAuditStatus());
        auditCallbackVO.setUserId(Long.valueOf(commitVO.getUserId()));
        auditCallbackVO.setProductId(commitVO.getProductId());

        try {
            // 调用MQ发送
            log.info("send message to ao,userId:{},applyId:{},", auditCallbackVO.getUserId(),
                    auditCallbackVO.getApplyId());
            userProductAuditService.sendMQ(auditCallbackVO);

        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        // 成功
        wr.setResult(MAConstant.RES_RESULT_STATUS_SUC);
        return wr;
    }

    // 指定认领
    @GetMapping("/claim")
    @CrossOrigin(origins = "*")
    public WsResult<Integer> claimTask(@RequestParam("adminId") Integer adminId, @RequestParam("userId") Long userId) {

        WsResult<Integer> wsResult = new WsResult<Integer>();
        log.info("{} claim user:{}.", adminId, userId);
        if (adminId == null || userId == null) {
            return getErrorResult(wsResult, "参数有误!");
        }

        AUserProductAuditStatusEntity productAuditStatus = productAudit.getProductAuditStatusByUserId(userId);
        if (productAuditStatus == null || productAuditStatus.getId() == null) {
            return getErrorResult(wsResult, "认领失败!");
        }
        String claimResult = null;
        try {
            claimResult = productAudit.claim(adminId, String.valueOf(productAuditStatus.getId()));
        } catch (ServiceException e) {
            String msg = e.getMessage();
            String code = e.getErrCode();
            wsResult.setError(new ErrorInfo(code, msg));
            if (Constants.CLAIM_HAS_EXIST.equals(code)) {
                wsResult.setResult(-3);
            }
            if (Constants.CLAIM_APPROVED.equals(code)) {
                wsResult.setResult(-2);
            }
            return wsResult;
        }
        if (claimResult == null) {
            return getErrorResult(wsResult, "认领失败!");
        } else {
            try {
                Long claim = Long.parseLong(claimResult);
                wsResult.setResult(1);
                return wsResult;

            } catch (Exception ex) {
                return getErrorResult(wsResult, "认领失败!");
            }
        }
    }

    /**
     * 查询当前产品分流比例信息
     * 
     * @return
     */
    @GetMapping("/product/proportion/current")
    @CrossOrigin(origins = "*")
    public WsResult<String> getCurrentProductProportion() {
        WsResult<String> wsResult = new WsResult<String>();
        StringRedisTemplate stringRedisTemplate = redisUtil.getStringRedisTemplate();
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        String value = valueOps.get(RedisKey.KEY_PRODUCT_PROPORTION);
        if (value == null) {
            value = Constants.DEFAULT_PRODUCTS_PROPORTION;
        }
        wsResult.setResult(value);
        return wsResult;
    }

    /**
     * 设置产品分流比例
     * 
     * @param proportion
     * @return
     */
    @PostMapping("/product/proportion")
    @CrossOrigin(origins = "*")
    public WsResult<Boolean> setProductProportion(@RequestParam("proportion") String proportion) {
        log.info("try to setProductProportion:" + proportion);
        Boolean result;
        if (MAUtil.checkProportion(proportion)) {
            StringRedisTemplate stringRedisTemplate = redisUtil.getStringRedisTemplate();
            ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
            valueOps.set(RedisKey.KEY_PRODUCT_PROPORTION, proportion);
            result = true;
        } else {
            result = false;
        }
        WsResult<Boolean> wsResult = new WsResult<Boolean>();
        wsResult.setResult(result);
        return wsResult;
    }

    private WsResult<Integer> getErrorResult(WsResult<Integer> result, String message) {
        ErrorInfo error = new ErrorInfo();
        error.setErrCode("99");
        error.setMessage(message);
        result.setError(error);
        result.setResult(2);

        return result;
    }

}
