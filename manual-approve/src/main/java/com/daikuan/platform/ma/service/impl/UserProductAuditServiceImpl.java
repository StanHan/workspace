package com.daikuan.platform.ma.service.impl;

import java.nio.charset.StandardCharsets;
import java.util.*;

import com.daikuan.platform.ma.pojo.*;
import com.daikuan.platform.ma.service.IRejectReasonService;
import com.daikuan.platform.ma.vo.Reason;
import com.daikuan.platform.ma.vo.RejectReasonsVo;
import com.daikuan.platform.ma.web.RejectReasonController;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.daikuan.platform.ma.bean.ReasonCategoryVO;
import com.daikuan.platform.ma.common.response.ResponseDataVO;
import com.daikuan.platform.ma.constant.MAConstant;
import com.daikuan.platform.ma.constant.ServiceExceptionConstant;
import com.daikuan.platform.ma.dao.ARejectReasonMasterMapper;
import com.daikuan.platform.ma.dao.AUserProductAuditMapper;
import com.daikuan.platform.ma.dao.AUserProductAuditSendMapper;
import com.daikuan.platform.ma.dao.AUserProductAuditStatusMapper;
import com.daikuan.platform.ma.dao.AdminTaskMapper;
import com.daikuan.platform.ma.exception.AutoFilterException;
import com.daikuan.platform.ma.exception.MQException;
import com.daikuan.platform.ma.exception.ServiceException;
import com.daikuan.platform.ma.exception.dao.DaoException;
import com.daikuan.platform.ma.mq.DeviceProducer;
import com.daikuan.platform.ma.mq.Producer.MassageContent;
import com.daikuan.platform.ma.pojo.vo.AuditCallbackVO;
import com.daikuan.platform.ma.pojo.vo.UserProductAuditVO;
import com.daikuan.platform.ma.service.AutoDealFilterService;
import com.daikuan.platform.ma.service.IAuditCallback;
import com.daikuan.platform.ma.service.UserProductAuditService;
import com.daikuan.platform.ma.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.abel533.entity.Example;
import com.github.abel533.entity.Example.Criteria;

/**
 * 
 * @author yangqb
 * @date 2017年3月13日
 * @desc
 */
@Service
public class UserProductAuditServiceImpl implements UserProductAuditService {


    private static Logger logger = LoggerFactory.getLogger(UserProductAuditServiceImpl.class);

    // 重试次数
    private static ThreadLocal<Integer> SEQ = new ThreadLocal<Integer>() {

        @Override
        protected Integer initialValue() {
            return 0;
        }

    };
    
    @Autowired
    DeviceProducer deviceProducer;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private AUserProductAuditMapper userProductAuditMapper;

    @Autowired
    private AUserProductAuditStatusMapper userProductAuditStatusMapper;

    @Autowired
    private IAuditCallback auditCallback;

    @Autowired
    private AUserProductAuditSendMapper userProductAuditSendMapper;

    @Autowired
    private AutoDealFilterService autoDealFilterService;

    @Autowired
    private ARejectReasonMasterMapper rejectReasonMasterMapper;

    @Autowired
    private AdminTaskMapper adminTaskMapper;

    @Autowired
    private IRejectReasonService rejectReasonService;


    @Autowired
    private UserProductAuditService userProductAuditService;

    private static String allManualGreen;
    
    @Override
    public void save(UserProductAuditVO userProductAuditVO, Map<String, Object> map)
            throws DaoException, InterruptedException, ServiceException {

        // 校验入参是否合法
        Long applyId = checkParams(userProductAuditVO);

        // 查看是否已传过
        AUserProductAuditEntity userProductAudit = queryUserProductAudit(applyId);
        if (userProductAudit != null) {
            // 告知推送方已接收, 不要再推送
            throw new ServiceException(ServiceExceptionConstant.PRODUCT_REPEAT_SEND,
                    "进件-重复推送,applyId=" + userProductAuditVO.getApplyId());
        }

        AUserProductAuditEntity vo2Entity = vo2Entity(userProductAuditVO);

        // 获取自动化飘绿结果
        Map<String, ReasonCategoryVO> enterAutoDealService = getAutoDealResult(vo2Entity);

        // 保存到进件表
        insert2AUserProductAudit(map, vo2Entity, applyId);

        // 获取自动化飘绿结果是否直接通过了
        Byte auditStatus = getAutoDealAuditStatus(map, userProductAuditVO, enterAutoDealService);

        // 获取原因信息版本号
        String version = getCurrentVersion();

        // 保存到审核结果状态表
        AUserProductAuditStatusEntity userProductAuditStatus = insert2UserProductAuditStatus(vo2Entity,
                enterAutoDealService, auditStatus, version);

        // 如果是直接通过了, 则直接向上游报告审核结果, 审核员操作日志表也要加一条记录, 记录为自动过
        if (auditStatus == Constants.AUDIT_PASS) {
            AuditCallbackVO auditCallbackVO = new AuditCallbackVO();
            auditCallbackVO.setApplyId(applyId);
            auditCallbackVO.setStatus(Constants.AUDIT_PASS);
            auditCallbackVO.setUserId(userProductAuditVO.getUserId());
            auditCallbackVO.setProductId(userProductAuditVO.getProductId());

            try {
                //调用MQ发送
                logger.info("send message to ao,mqaddress:{},userId:{},applyId:{},",auditCallbackVO.getUserId(),auditCallbackVO.getApplyId());
                userProductAuditService.sendMQ(auditCallbackVO);

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            if(allManualGreen == null || allManualGreen.trim().equals("")){
                allManualGreen = manualGreen();
            }
            userProductAuditStatus.setManualGreen(allManualGreen);
            userProductAuditStatusMapper.updateManualGreenByApplyId(userProductAuditStatus);
            AAdminTaskEntity adminTask = insert2AAdminTask(vo2Entity, userProductAuditStatus.getApproveTime(),
                    MAConstant.AUTO_PASS_A, Constants.AUDIT_PASS);
            map.put("adminTaskId", adminTask == null ? null : adminTask.getId());
            insert2AUserProductAuditSend(userProductAuditStatus, Constants.AUDIT_PASS, null, null, applyId);
        }

    }

    private AUserProductAuditEntity vo2Entity(UserProductAuditVO userProductAuditVO) throws ServiceException {
        AUserProductAuditEntity vo2Entity = new AUserProductAuditEntity();
        try {
            BeanUtils.copyProperties(userProductAuditVO, vo2Entity);
            vo2Entity.setCreateAt(new Date());
        } catch (BeansException e) {
            throw new ServiceException(ServiceExceptionConstant.PRODUCT_REPEAT_SEND,
                    "进件-VO到entity转换异常,applyId=" + userProductAuditVO.getApplyId(), e);
        }
        return vo2Entity;
    }

    private AAdminTaskEntity insert2AAdminTask(AUserProductAuditEntity userProductAudit, Date approveTime,
            Byte autoPass, Byte claimStatus) throws DaoException {
        AAdminTaskEntity adminTask = new AAdminTaskEntity();
        adminTask.setAdminId(Integer.valueOf(MAConstant.AUTO_ADMIN_ID));
        adminTask.setApplyId(userProductAudit.getApplyId());
        adminTask.setApproveTime(approveTime);
        adminTask.setAutoPass(autoPass);
        adminTask.setClaimStatus(claimStatus);
        adminTask.setClaimTime(null);
        adminTask.setCreateAt(new Date());
        adminTask.setDisplayTime(null);
        adminTask.setProductAuditId(userProductAudit.getId());
        adminTask.setSpendTime(null);
        adminTask.setProductId(userProductAudit.getProductId());
        adminTask.setUserId(userProductAudit.getUserId());
        try {
            adminTaskMapper.insert(adminTask);
        } catch (Exception e) {
            throw new DaoException("进件-自动化审核通过后, 插入审核员操作日志表失败", e);
        }
        return adminTask;
    }

    private String getCurrentVersion() throws DaoException {
        String version = null;
        try {
            version = rejectReasonMasterMapper.queryVersion();
        } catch (Exception e) {
            throw new DaoException("进件-查询拒绝原因当前版本号失败", e);
        }
        return version;
    }

    private Byte getAutoDealAuditStatus(Map<String, Object> map, UserProductAuditVO userProductAuditVO,
            Map<String, ReasonCategoryVO> enterAutoDealService) throws ServiceException {
        Byte auditStatus = Constants.AUDIT_PASS;
        if (enterAutoDealService == null) {
            map.put("auditStatus", auditStatus);
            throw new ServiceException("", "自动化飘绿异常applyId=" + userProductAuditVO.getApplyId() + ",飘绿返回结果为空");
        }
        
        if (enterAutoDealService != null) {
            for (Map.Entry<String, ReasonCategoryVO> item : enterAutoDealService.entrySet()) {
                if (item.getKey() == null) {
                    map.put("auditStatus", auditStatus);
                    throw new ServiceException("",
                            "自动化飘绿异常applyId=" + userProductAuditVO.getApplyId() + ", 飘绿结果" + item.getKey() + "中含有空KEY");
                }

                if (!StringUtils.isBlank(userProductAuditVO.getPhotoRealTime())
                        && !StringUtils.isBlank(userProductAuditVO.getPhotoBack())) {

                }

                if (item.getValue() == null && !specialFlag(userProductAuditVO, item.getKey())) {
                    map.put("auditStatus", auditStatus);
                    throw new ServiceException("", "自动化飘绿异常applyId=" + userProductAuditVO.getApplyId() + ", 飘绿结果"
                            + item.getKey() + "中含有空VALUE对象");
                }
                
                // 有非全绿的, 则状态置为待认领
                if ((item.getValue() == null ) ||(item.getValue().getWholeGreenFlag() == null ) || (item.getValue().getWholeGreenFlag() != MAConstant.WHOLE_GREEN_FLAG_T)) {
                    auditStatus = Constants.AUDIT_UNDECIDED;
                }
            }
        }

        return auditStatus;
    }

    // 实时头像为空的及身份证国徽面为空的, 飘绿结果value可以为空
    private boolean specialFlag(UserProductAuditVO userProductAuditVO, String mapKey) {
        return (StringUtils.isBlank(userProductAuditVO.getPhotoRealTime()) && Constants.KEY_HEAD_ICON.equals(mapKey))
                || (StringUtils.isBlank(userProductAuditVO.getPhotoBack()) && Constants.KEY_BACK_IDCARD.equals(mapKey));
    }

    private Map<String, ReasonCategoryVO> getAutoDealResult(AUserProductAuditEntity userProductAudit)
            throws ServiceException {
        Map<String, ReasonCategoryVO> enterAutoDealService = null;
        try {
            enterAutoDealService = autoDealFilterService.enterAutoDealService(userProductAudit);
        } catch (AutoFilterException e) {
            throw new ServiceException("", "自动化飘绿异常applyId=" + userProductAudit.getApplyId(), e);
        }
        return enterAutoDealService;
    }

    private AUserProductAuditStatusEntity insert2UserProductAuditStatus(AUserProductAuditEntity vo2Entity,
            Map<String, ReasonCategoryVO> enterAutoDealService, Byte auditStatus, String version) throws DaoException {
        AUserProductAuditStatusEntity userProductAuditStatus = new AUserProductAuditStatusEntity();
        userProductAuditStatus.setApplyId(vo2Entity.getApplyId());
        userProductAuditStatus.setAuditStatus(auditStatus);
        try {
           String  json = objectMapper.writeValueAsString(enterAutoDealService);
           userProductAuditStatus.setAutoGreen(json);
        } catch (JsonProcessingException e) {

        }
        userProductAuditStatus.setCreateAt(new Date());
        userProductAuditStatus.setProductAuditId(vo2Entity.getId());
        userProductAuditStatus.setProductId(vo2Entity.getProductId());
        userProductAuditStatus.setResultType(MAConstant.RESULT_TYPE_AUTO);
        userProductAuditStatus.setUserId(vo2Entity.getUserId());
        userProductAuditStatus.setReasonVersion(version);
        userProductAuditStatus.setApproveTime(new Date());
        try {
            userProductAuditStatusMapper.insert(userProductAuditStatus);
        } catch (Exception e) {
            throw new DaoException("插入审核结果状态表失败applyId=" + vo2Entity.getApplyId(), e);
        }
        return userProductAuditStatus;
    }

    private void insert2AUserProductAudit(Map<String, Object> map, AUserProductAuditEntity userProductAuditEntity, Long applyId)
            throws DaoException {
        try {
            // 入进件表
            userProductAuditMapper.insert(userProductAuditEntity);
        } catch (Exception e) {
            map.put("applyId", applyId);
            throw new DaoException("进件-保存审批数据失败, applyId=" + applyId, e);
        }
    }

    private AUserProductAuditEntity queryUserProductAudit(Long applyId) throws DaoException {
        AUserProductAuditEntity userProductAudit = null;
        try {
            userProductAudit = userProductAuditMapper.queryByApplyId(applyId);
        } catch (Exception e) {
            throw new DaoException("进件-查询进件表是否已存在该条数据失败, applyId=" + applyId, e);
        }
        return userProductAudit;
    }

    private Long checkParams(UserProductAuditVO userProductAuditVO) throws ServiceException {
        if (userProductAuditVO == null) {
            throw new ServiceException(ServiceExceptionConstant.IN_PARAM_NULL, "进件-调用方传送数据包为空");
        }

        Long applyId = userProductAuditVO.getApplyId();
        if (userProductAuditVO.getApplyId() == null) {
            throw new ServiceException(ServiceExceptionConstant.APPLY_ID_NULL, "进件-申请ID为空");
        }
        return applyId;
    }

    @Override
    public boolean sendMQ(AuditCallbackVO auditCallbackVO) throws MQException{

        String json = JSON.toJSONString(auditCallbackVO);
        logger.info("sendMQ:"+json);
        
        MassageContent messageContent = new MassageContent("CustomerServiceAuditResult","Result","",json.getBytes(StandardCharsets.UTF_8));
        return deviceProducer.messageSend(messageContent);
    }
    
    @Override
    public void send(AUserProductAuditStatusEntity userProductAuditStatus)
            throws InterruptedException, ServiceException, DaoException {
        if (userProductAuditStatus == null) {
            throw new ServiceException("", "审核回调-向业务发送审核结果, 所传参数为空, userProductAuditStatus=" + userProductAuditStatus);
        }

        AuditCallbackVO auditCallbackVO = getAuditCallbackVO(userProductAuditStatus);
        ResponseDataVO callback = null;
        boolean callFail = false;
        try {
            callback = auditCallback.callbackAudit(auditCallbackVO);
        } catch (Exception e) {
            callFail = true;
        }

        // 重试N次
        Integer seq = SEQ.get();
        Exception exception = null;
        while (callFail && seq < MAConstant.CALLBACK_RETRY_FREQUENCY) {
            try {
                callback = auditCallback.callbackAudit(auditCallbackVO);
                callFail = false;
            } catch (Exception e) {
                callFail = true;
                SEQ.set(SEQ.get() + 1);
                seq = SEQ.get();
                exception = e;
            }
        }

        if (callFail) {
            // 重试N次后, 还是失败
            throw new ServiceException(ServiceExceptionConstant.CALLBACK_RETRY_FAIL,
                    "审核回调-审批完成后回调业务接口, 重试 " + MAConstant.CALLBACK_RETRY_FREQUENCY + "次后仍异常userId="
                            + auditCallbackVO.getUserId() + ",applyId=" + auditCallbackVO.getApplyId(),
                    exception);
        }

        Byte status = (null != callback) && (null != callback.getResult()) ? callback.getResult().getStatus() : null;
        String errCode = (null != callback) && (null != callback.getError()) ? callback.getError().getErrCode() : null;
        String message = (null != callback) && (null != callback.getError()) ? callback.getError().getMessage() : null;

        // 这是个发送状态历史表, 只添加, 审批结果回调如果一直异常, 我们记录回调状态, 可供业务方查询
        insert2AUserProductAuditSend(userProductAuditStatus, status, errCode, message, auditCallbackVO.getApplyId());

    }

    private void insert2AUserProductAuditSend(AUserProductAuditStatusEntity userProductAuditStatus, Byte status,
            String errCode, String message, Long applyId) throws DaoException {
        AUserProductAuditSendEntity userProductAuditSend = new AUserProductAuditSendEntity();
        userProductAuditSend.setProductAuditId(userProductAuditStatus.getProductAuditId());
        userProductAuditSend.setApproveTime(userProductAuditStatus.getApproveTime());
        userProductAuditSend.setStatus(status);
        userProductAuditSend.setErrorcode(errCode);
        userProductAuditSend.setMessage(message);
        userProductAuditSend.setCreateAt(new Date());
        userProductAuditSend.setApplyId(applyId);
        try {
            userProductAuditSendMapper.insert(userProductAuditSend);
        } catch (Exception e) {
            throw new DaoException(
                    "审核完成后, 已审核记录插入a_user_product_audit_send失败,插入实体为userProductAuditSend=" + userProductAuditSend, e);
        }
    }

    private AuditCallbackVO getAuditCallbackVO(AUserProductAuditStatusEntity userProductAuditStatus) {
        AuditCallbackVO auditCallbackVO = new AuditCallbackVO();
        auditCallbackVO.setUserId(userProductAuditStatus.getUserId());
        auditCallbackVO.setApplyId(userProductAuditStatus.getApplyId());
        auditCallbackVO.setStatus(userProductAuditStatus.getAuditStatus());
        auditCallbackVO.setReasonIds(userProductAuditStatus.getReasonIds());
        return auditCallbackVO;
    }

    @Override
    public void rollback(Long applyId, Long adminTaskId) {
        // 进件时, 如果异常, 则给一次回滚机会
        if (applyId != null) {
            userProductAuditMapper.deleteByApplyId(applyId);
            userProductAuditStatusMapper.deleteByApplyId(applyId);
            userProductAuditSendMapper.deleteByExample(getApplyIdExample(applyId));
        }

        if (adminTaskId != null) {
            adminTaskMapper.deleteByPrimaryKey(adminTaskId);
        }
    }

    private Example getApplyIdExample(Long applyId) {
        Example example = new Example(AUserProductAuditSendEntity.class);
        Criteria criteria = example.createCriteria();
        criteria.andEqualTo("applyId", applyId);
        return example;
    }

   private String manualGreen()  throws DaoException{
        Map<Integer, ARejectReasonMasterEntity> allPhotoFrontMap = new TreeMap<Integer, ARejectReasonMasterEntity>();
        Map<Integer, ARejectReasonMasterEntity> allPhotoBackMap = new TreeMap<Integer, ARejectReasonMasterEntity>();
        Map<Integer, ARejectReasonMasterEntity> allPhotoVideoMap = new TreeMap<Integer, ARejectReasonMasterEntity>();
        Map<Integer, ARejectReasonMasterEntity> allPhotoRealTimeMap = new TreeMap<Integer, ARejectReasonMasterEntity>();
        RejectReasonsVo rejectReasonsVo = rejectReasonService.getRejectReasonByType();
        allPhotoBackMap = buildMap(rejectReasonsVo.getPhotoBack());
        allPhotoFrontMap = buildMap(rejectReasonsVo.getPhotoFront());
        allPhotoVideoMap = buildMap(rejectReasonsVo.getVideo());
        allPhotoRealTimeMap = buildMap(rejectReasonsVo.getPhotoRealtime());
        Reason rela = rejectReasonsVo.getRela();
        Reason unitAddr = rejectReasonsVo.getUnitAddr();
        Reason homeAddr = rejectReasonsVo.getHomeAddr();
        Reason jobUnit = rejectReasonsVo.getJobUnit();
        Map<Integer, ARejectReasonMasterEntity> photoFrontRed = new TreeMap<Integer, ARejectReasonMasterEntity>();
        Map<Integer, ARejectReasonMasterEntity> photoBackRed =  new TreeMap<Integer, ARejectReasonMasterEntity>();
        Map<Integer, ARejectReasonMasterEntity> realTimeRed =  new TreeMap<Integer, ARejectReasonMasterEntity>();
        Map<Integer, ARejectReasonMasterEntity> videoRed =  new TreeMap<Integer, ARejectReasonMasterEntity>();
        Boolean relaRed = false;
        Boolean unitAddrRed = false;
        Boolean homeAddrRed = false;
        Boolean jogUnitRed = false;
        Map<String, ReasonCategoryVO> manualGreen = new TreeMap<String, ReasonCategoryVO>();
        ReasonCategoryVO  back = new ReasonCategoryVO();
        ReasonCategoryVO  front = new ReasonCategoryVO();
        ReasonCategoryVO  real = new ReasonCategoryVO();
        ReasonCategoryVO  video = new ReasonCategoryVO();
        ReasonCategoryVO  home = new ReasonCategoryVO();
        ReasonCategoryVO  unitAddress = new ReasonCategoryVO();
        ReasonCategoryVO  jobUnitName = new ReasonCategoryVO();
        ReasonCategoryVO  relative = new ReasonCategoryVO();
        back.setType(11);
        back.setWholeGreenFlag(1);
        front.setType(6);
        front.setWholeGreenFlag(1);
        video.setType(8);
        video.setWholeGreenFlag(1);
        real.setType(7);
        real.setWholeGreenFlag(1);
        relative.setWholeGreenFlag(1);
        relative.setType(5);
        unitAddress.setWholeGreenFlag(1);
        unitAddress.setType(2);
        home.setWholeGreenFlag(1);
        home.setType(4);
        jobUnitName.setWholeGreenFlag(1);
        jobUnitName.setType(3);

        StringBuffer backGreen = new StringBuffer();
        StringBuffer frontGreen = new StringBuffer();
        StringBuffer videoGreen = new StringBuffer();
        StringBuffer realGreen = new StringBuffer();
        for(Map.Entry<Integer, ARejectReasonMasterEntity> entry : allPhotoRealTimeMap.entrySet()) {
            realGreen.append(entry.getKey() + ",");
        }
        //real.setGreenIds(realGreen.toString());
        for(Map.Entry<Integer, ARejectReasonMasterEntity> entry : allPhotoFrontMap.entrySet()) {
            frontGreen.append(entry.getKey() + ",");
        }
        //front.setGreenIds(frontGreen.toString());

        for(Map.Entry<Integer, ARejectReasonMasterEntity> entry : allPhotoVideoMap.entrySet()) {
            videoGreen.append(entry.getKey() + ",");
        }
        //video.setGreenIds(videoGreen.toString());

        for(Map.Entry<Integer, ARejectReasonMasterEntity> entry : allPhotoBackMap.entrySet()) {
            backGreen.append(entry.getKey() + ",");
        }
        relative.setGreenIds(rela.getCode() + "");
        unitAddress.setGreenIds(unitAddr.getCode() + "");
        home.setGreenIds(homeAddr.getCode() + "");
        jobUnitName.setGreenIds(jobUnit.getCode() + "");
        front.setGreenIds(frontGreen.toString());
        back.setGreenIds(backGreen.toString());
        real.setGreenIds(realGreen.toString());
        video.setGreenIds(videoGreen.toString());
        manualGreen.put(Constants.KEY_CONTACT, relative);
        manualGreen.put(Constants.KEY_UNIT_NAME,jobUnitName);
        manualGreen.put(Constants.KEY_FAMILY_ADDR, home);
        manualGreen.put(Constants.KEY_UNIT_ADDR, unitAddress);
        manualGreen.put(Constants.KEY_FRONT_IDCARD, front);
        manualGreen.put(Constants.KEY_BACK_IDCARD, back);
        manualGreen.put(Constants.KEY_HEAD_ICON, real);
        manualGreen.put(Constants.KEY_VIDEO, video);
        String jsonStr = null;
        try {
            jsonStr = objectMapper.writeValueAsString(manualGreen);
        } catch (JsonProcessingException e) {
        }
        return jsonStr;
    }


    public  Map<Integer, ARejectReasonMasterEntity>  buildMap(List<Reason> vo){
        Map<Integer, ARejectReasonMasterEntity> allReasonMap = new HashMap<Integer, ARejectReasonMasterEntity>();
        for(Reason rea: vo){
            ARejectReasonMasterEntity entity = new ARejectReasonMasterEntity();
            entity.setCauseMessage(rea.getMessage());
            entity.setJoinRefuse(rea.getChange());
            entity.setReasonId(rea.getCode());
            entity.setType(rea.getType().byteValue());
            allReasonMap.put(rea.getCode(), entity);
        }
        return allReasonMap;

    }


}
