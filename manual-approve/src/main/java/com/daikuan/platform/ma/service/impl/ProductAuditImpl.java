package com.daikuan.platform.ma.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.daikuan.platform.ma.bean.ReasonCategoryVO;
import com.daikuan.platform.ma.constant.ServiceExceptionConstant;
import com.daikuan.platform.ma.dao.ARejectReasonMasterMapper;
import com.daikuan.platform.ma.dao.AUserProductAuditMapper;
import com.daikuan.platform.ma.dao.AUserProductAuditStatusMapper;
import com.daikuan.platform.ma.dao.AdminTaskMapper;
import com.daikuan.platform.ma.exception.ServiceException;
import com.daikuan.platform.ma.pojo.AAdminTaskEntity;
import com.daikuan.platform.ma.pojo.ARejectReasonMasterEntity;
import com.daikuan.platform.ma.pojo.AUserProductAuditEntity;
import com.daikuan.platform.ma.pojo.AUserProductAuditStatusEntity;
import com.daikuan.platform.ma.redis.RedisUtil;
import com.daikuan.platform.ma.service.ProductAudit;
import com.daikuan.platform.ma.task.InitTask;
import com.daikuan.platform.ma.util.CommonUtils;
import com.daikuan.platform.ma.util.Constants;
import com.daikuan.platform.ma.util.RedisKey;
import com.daikuan.platform.ma.vo.AuditVo;
import com.daikuan.platform.ma.vo.CommitVO;
import com.daikuan.platform.ma.vo.HomeAddr;
import com.daikuan.platform.ma.vo.Info;
import com.daikuan.platform.ma.vo.JobUnit;
import com.daikuan.platform.ma.vo.PhotoBack;
import com.daikuan.platform.ma.vo.PhotoFront;
import com.daikuan.platform.ma.vo.PhotoRealtime;
import com.daikuan.platform.ma.vo.Reason;
import com.daikuan.platform.ma.vo.RejectReasonsVo;
import com.daikuan.platform.ma.vo.Rela;
import com.daikuan.platform.ma.vo.UnitAddr;
import com.daikuan.platform.ma.vo.Video;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ProductAuditImpl implements ProductAudit {

    private static Logger log = LoggerFactory.getLogger(ProductAuditImpl.class);

    public static ExecutorService executorService = Executors.newSingleThreadExecutor();

    private static final int GET_FILE_TO_FASTDFS_FLAG = 100;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private AdminTaskMapper adminTaskMapper;

    @Autowired
    private AUserProductAuditMapper userProductAuditMapper;

    @Autowired
    private AUserProductAuditStatusMapper userProductAuditStatusMapper;

    @Autowired
    private ARejectReasonMasterMapper rejectReasonMapper;

    @PostConstruct
    private void initRedisOps() {
        this.listOperations = stringRedisTemplate.opsForList();
        this.setOperations = stringRedisTemplate.opsForSet();
    }

    private ListOperations<String, String> listOperations;
    private SetOperations<String, String> setOperations;
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 指定认领申件，如果applyId为空则为随机认领
     * 
     * @param adminId
     *            申件人ID
     * @param applyId
     *            申件ID
     * @return 申件ID，null:指定认领失败
     */
    public String claim(int adminId, String applyId) throws ServiceException {
        if (applyId == null || applyId.isEmpty()) {// 随机拿件
            return claim(adminId);
        }
        // 指定认领
        String loadedObject = RedisKey.KEY_LOADED_APP_ + applyId;// 缓存的对象
        if (redisUtil.exist(loadedObject)) {// 被认领过
            int tmp = findWhoClaimIt(applyId);
            log.info("admin {} 认领 {} 失败,已被 admin {} 认领.", adminId, applyId, tmp);
            throw new ServiceException(Constants.CLAIM_HAS_EXIST, "该件已被 admin " + tmp + " 认领");
        } else {// 未被认领
            AUserProductAuditStatusEntity vo = userProductAuditStatusMapper.selectByPrimaryKey(Long.valueOf(applyId));
            if (vo == null) {
                log.error("指定认领失败,userProductAuditMapper.selectByPrimaryKey return null,id=" + applyId);
                return null;
            }
            if (vo.getAuditStatus() == Constants.AUDIT_UNDECIDED) {// 未处理
                AuditVo auditVo = buildAuditVo(vo);
                redisUtil.set(loadedObject, auditVo);
            } else {// 已处理
                log.info("指定认领失败,已被审批,adminId=" + adminId + ", applyId=" + applyId + ", status=" + vo.getAuditStatus());
                throw new ServiceException(Constants.CLAIM_APPROVED, "指定认领失败,已被审批");
            }
        }
        String claimedList = RedisKey.LIST_CLAIMED_ + adminId;// 某审核员申领的申件ID

        listOperations.leftPush(claimedList, applyId);
        return applyId;
    }

    /**
     * 查找哪个admin id 认领了该件
     * <p>
     * 方法很笨，但是急于上线不好大动代码，暂时先这样吧。后面有时间再优化吧（采用hashMap形式，key为applyId，value为adminID）。
     * 
     * @param applyId
     * @return
     */
    private int findWhoClaimIt(String applyId) {
        for (int i = 1; i < 1000; i++) {
            String key = RedisKey.LIST_CLAIMED_ + i;
            if (redisUtil.exist(key)) {
                List<String> list = listOperations.range(key, 0, -1);
                if (list.contains(applyId)) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 随机认领申件
     * 
     * @param adminId
     *            申件人ID
     * @return 申件ID，null:暂无申件可以领取
     */
    public String claim(int adminId) {

        AUserProductAuditStatusEntity statusVo = null;
        do {
            // 从混合集合中随机pop一条数据
            String id = setOperations.pop(RedisKey.SET_MIXED);
            log.info("pop member from {} return {}.", RedisKey.SET_MIXED, id);
            if (id == null) {// 无申件可认领
                log.info("暂无申件可以领取.");
                return null;
            } else {
                String loadedObject = RedisKey.KEY_LOADED_APP_ + id;// 缓存的对象
                if (redisUtil.exist(loadedObject)) {// 被认领过
                    log.info("该件已被认领，id=" + id);
                    continue;
                }
                statusVo = userProductAuditStatusMapper.selectByPrimaryKey(Long.valueOf(id));
                if (statusVo == null) {
                    log.error("userProductAuditMapper.selectByPrimaryKey return null,id=" + id);
                    continue;
                }
                if (statusVo.getAuditStatus() != Constants.AUDIT_UNDECIDED) {// 已审批
                    log.info("Record:{} status=" + statusVo.getAuditStatus(), id);
                    continue;
                }
            }
        } while (statusVo == null || isGiveUp(adminId, statusVo.getProductAuditId()));

        String id = String.valueOf(statusVo.getId());

        // 构建页面对象
        AuditVo vo = buildAuditVo(statusVo);

        // 缓存对象
        redisUtil.set(RedisKey.KEY_LOADED_APP_ + id, vo);

        String claimedList = RedisKey.LIST_CLAIMED_ + adminId;// 某审核员申领的申件ID
        listOperations.remove(claimedList, 0, id);// 删除所有并添加，这样做的目的是保证claimedList里只有一个
        listOperations.rightPush(claimedList, id);// 向队尾插数据
        return id;
    }

    /**
     * 判断该applyId是否被adminId放弃过
     * 
     * @param adminId
     * @param productAuditId
     *            对应a_user_product_audit的id product_audit_id
     * @return
     */
    private boolean isGiveUp(int adminId, long productAuditId) {
        // 判断放弃集合中是否存在
        String key = RedisKey.SET_GIVE_UP_ + productAuditId;
        if (setOperations.isMember(key, String.valueOf(adminId))) {
            log.info("admin:{} had gived up member:{}.", adminId, productAuditId);
            return true;
        }
        // 判断操作记录里是否存在
        AAdminTaskEntity adminTask = new AAdminTaskEntity();
        adminTask.setAdminId(adminId);
        adminTask.setProductAuditId(productAuditId);
        adminTask.setClaimStatus(Constants.AUDIT_GIVE_UP);
        List<AAdminTaskEntity> list = adminTaskMapper.select(adminTask);
        if (list != null && list.size() > 0) {
            log.info("admin:{} had gived up member:{}.", adminId, productAuditId);
            return true;
        }
        return false;
    }

    /**
     * 从已认领的队列里拿件,如果已认领的队列里无件可用，则重新申领一件
     * 
     * @param adminId
     * @return
     */
    public String fetchMember(int adminId) {
        String statusId = null;
        String claimedList = RedisKey.LIST_CLAIMED_ + adminId;// 某审核员申领的申件ID
        String adminLoadedSet = RedisKey.SET_LOADED_ + adminId;// 某审核员加载的申件ID
        do {
            long index = setOperations.size(adminLoadedSet);
            log.info("admin:{} has loaded {} records.", adminId, index);
            statusId = listOperations.index(claimedList, index);
            log.info("admin:{} has claimed {} records:", adminId, listOperations.size(claimedList));
            if (statusId == null || statusId.isEmpty()) {
                String newStatusId;
                do {
                    newStatusId = claim(adminId);
                    if (newStatusId == null) {// 认领不到申件
                        return null;
                    }
                    // 放入admin已加载的set里
                } while (setOperations.add(adminLoadedSet, newStatusId) == 0);

                return newStatusId;
            }
            // 放入admin已加载的set里
        } while (setOperations.add(adminLoadedSet, statusId) == 0);

        return statusId;
    }

    /**
     * 当放弃操作时重置REDIS状态
     * 
     * @param adminId
     * @param applyId
     *            ,即a_user_product_audit_status的主键
     */
    public void resetRedisWhenGiveUp(Integer adminId, Long applyId) {

        RedisCallback<List<Object>> callback = new RedisCallback<List<Object>>() {
            final byte[] admin = adminId.toString().getBytes(StandardCharsets.UTF_8);
            final byte[] apply = applyId.toString().getBytes(StandardCharsets.UTF_8);

            @Override
            public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                // 加入到放弃集合中
                connection.sAdd((RedisKey.SET_GIVE_UP_ + applyId).getBytes(StandardCharsets.UTF_8), admin);
                // 设置有效时间24小时
                connection.expire((RedisKey.SET_GIVE_UP_ + applyId).getBytes(StandardCharsets.UTF_8), 24 * 60 * 60);
                // 从已加载集合中删除
                connection.sRem((RedisKey.SET_LOADED_ + adminId).getBytes(StandardCharsets.UTF_8), apply);
                // 从已认领队列删除
                connection.lRem((RedisKey.LIST_CLAIMED_ + adminId).getBytes(StandardCharsets.UTF_8), 0, apply);
                // 删除缓存的对象
                connection.del((RedisKey.KEY_LOADED_APP_ + applyId).getBytes(StandardCharsets.UTF_8));
                // 加入到混合集合中
                connection.sAdd(RedisKey.SET_MIXED.getBytes(StandardCharsets.UTF_8), apply);
                return connection.closePipeline();
            }
        };

        List<Object> executeResult = stringRedisTemplate.execute(callback);
        for (Object object : executeResult) {
            log.info(object.toString());
        }
    }

    /**
     * 当取消所有认领时重置REDIS状态
     * 
     * 
     * @param adminId
     */
    public void resetRedisWhenCancel(Integer adminId) {
        List<String> list = listOperations.range(RedisKey.LIST_CLAIMED_ + adminId, 0, -1);

        RedisCallback<List<Object>> callback = new RedisCallback<List<Object>>() {
            @Override
            public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                if (list != null && list.size() > 0) {
                    // 删除申领队列
                    connection.del((RedisKey.LIST_CLAIMED_ + adminId).getBytes(StandardCharsets.UTF_8));
                    for (String string : list) {
                        // 删除缓存的对象
                        connection.del((RedisKey.KEY_LOADED_APP_ + string).getBytes(StandardCharsets.UTF_8));
                    }
                    // 加入到混合集合中
                    String[] strings = new String[list.size()];
                    list.toArray(strings);
                    byte[][] array = CommonUtils.toArray(strings);
                    connection.sAdd(RedisKey.SET_MIXED.getBytes(), array);
                }

                // 删除已加载集合
                connection.del((RedisKey.SET_LOADED_ + adminId).getBytes(StandardCharsets.UTF_8));
                return connection.closePipeline();
            }
        };
        List<Object> executeResult = stringRedisTemplate.execute(callback);
        for (Object object : executeResult) {
            log.info(object.toString());
        }
    }

    @Override
    public void resetRedisWhenSubmit(String adminId, String applyId) {

        RedisCallback<List<Object>> callback = new RedisCallback<List<Object>>() {
            @Override
            public List<Object> doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                // 从已加载集合中删除
                connection.sRem((RedisKey.SET_LOADED_ + adminId).getBytes(StandardCharsets.UTF_8),
                        applyId.getBytes(StandardCharsets.UTF_8));
                // 从已认领队列删除
                connection.lRem((RedisKey.LIST_CLAIMED_ + adminId).getBytes(StandardCharsets.UTF_8), 0,
                        applyId.getBytes(StandardCharsets.UTF_8));
                // 删除缓存的对象
                connection.del((RedisKey.KEY_LOADED_APP_ + applyId).getBytes(StandardCharsets.UTF_8));
                // 删除放弃集合
                connection.del((RedisKey.SET_GIVE_UP_ + applyId).getBytes(StandardCharsets.UTF_8));
                return connection.closePipeline();
            }
        };
        List<Object> executeResult = stringRedisTemplate.execute(callback);
        for (Object object : executeResult) {
            log.info(object.toString());
        }
    }

    /**
     * 增加操作日志
     * 
     * @param adminId
     * @param applyId
     * @param action：放弃,拒绝,通过
     * @return
     */
    public int addAdminTaskRecord(Integer adminId, String adminName, AuditVo vo, Byte action, Date claimTime,
            Date displayTime, long spendTime) {
        Date now = new Date();
        AAdminTaskEntity record = new AAdminTaskEntity();
        record.setAdminId(adminId);
        record.setAdminName(adminName);
        record.setApplyId(Long.valueOf(vo.getApplyId()));
        record.setApproveTime(now);
        record.setClaimStatus(action);
        record.setClaimTime(claimTime);
        record.setCreateAt(now);
        record.setDisplayTime(displayTime);
        record.setProductAuditId(vo.getProductAuditId());
        record.setProductId(vo.getProductId());
        record.setSpendTime(spendTime);
        record.setUpdateAt(now);
        record.setUserId(Long.valueOf(vo.getUserId()));
        return adminTaskMapper.insert(record);
    }

    public void initData() {
        executorService.execute(new InitTask(userProductAuditStatusMapper, redisUtil));
    }

    @Override
    public int updateAuditStatus(CommitVO commitVO) {
        Map<Integer, ARejectReasonMasterEntity> rejectMap = loadAllRejectReason();
        AUserProductAuditStatusEntity record = new AUserProductAuditStatusEntity();
        record.setId(Long.valueOf(commitVO.getApplyId()));
        record.setAuditStatus(commitVO.getAuditStatus());
        record.setAdminId(commitVO.getAdminId());
        record.setApproveTime(commitVO.getApproveTime());
        record.setAuditRemarks(commitVO.getRemark());
        String json;
        try {
            json = objectMapper.writeValueAsString(commitVO.getSnapshot());
        } catch (JsonProcessingException e) {
            log.error("parse Object to json error." + commitVO.getSnapshot());
            json = JSON.toJSONString(commitVO.getSnapshot());
        }
        record.setManualGreen(json);
        List<Integer> reasonIds = commitVO.getReasonIds();
        StringBuilder reason = new StringBuilder();
        StringBuilder reasonIDs = new StringBuilder();
        String reasonVersion = null;
        RejectReasonsVo rejectReasonsVo = commitVO.getRejectReasonsVo();
        Map<Integer, ARejectReasonMasterEntity> allPhotoFrontMap = new TreeMap<Integer, ARejectReasonMasterEntity>();
        Map<Integer, ARejectReasonMasterEntity> allPhotoBackMap = new TreeMap<Integer, ARejectReasonMasterEntity>();
        Map<Integer, ARejectReasonMasterEntity> allPhotoVideoMap = new TreeMap<Integer, ARejectReasonMasterEntity>();
        Map<Integer, ARejectReasonMasterEntity> allPhotoRealTimeMap = new TreeMap<Integer, ARejectReasonMasterEntity>();
        allPhotoBackMap = buildMap(rejectReasonsVo.getPhotoBack());
        allPhotoFrontMap = buildMap(rejectReasonsVo.getPhotoFront());
        allPhotoVideoMap = buildMap(rejectReasonsVo.getVideo());
        allPhotoRealTimeMap = buildMap(rejectReasonsVo.getPhotoRealtime());
        Reason rela = rejectReasonsVo.getRela();
        Reason unitAddr = rejectReasonsVo.getUnitAddr();
        Reason homeAddr = rejectReasonsVo.getHomeAddr();
        Reason jobUnit = rejectReasonsVo.getJobUnit();
        Map<Integer, ARejectReasonMasterEntity> photoFrontRed = new TreeMap<Integer, ARejectReasonMasterEntity>();
        Map<Integer, ARejectReasonMasterEntity> photoBackRed = new TreeMap<Integer, ARejectReasonMasterEntity>();
        Map<Integer, ARejectReasonMasterEntity> realTimeRed = new TreeMap<Integer, ARejectReasonMasterEntity>();
        Map<Integer, ARejectReasonMasterEntity> videoRed = new TreeMap<Integer, ARejectReasonMasterEntity>();
        Boolean relaRed = false;
        Boolean unitAddrRed = false;
        Boolean homeAddrRed = false;
        Boolean jogUnitRed = false;
        Map<String, ReasonCategoryVO> manualGreen = new TreeMap<String, ReasonCategoryVO>();
        ReasonCategoryVO back = new ReasonCategoryVO();
        ReasonCategoryVO front = new ReasonCategoryVO();
        ReasonCategoryVO real = new ReasonCategoryVO();
        ReasonCategoryVO video = new ReasonCategoryVO();
        ReasonCategoryVO home = new ReasonCategoryVO();
        ReasonCategoryVO unitAddress = new ReasonCategoryVO();
        ReasonCategoryVO jobUnitName = new ReasonCategoryVO();
        ReasonCategoryVO relative = new ReasonCategoryVO();
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
        if (reasonIds != null && reasonIds.size() > 0) {
            Collections.sort(reasonIds);
            for (Integer reasonId : reasonIds) {
                reasonIDs.append(reasonId + ",");
                ARejectReasonMasterEntity vo = rejectMap.get(reasonId);
                if (vo == null) {
                    reason.append(reasonId + ",");
                } else {
                    reason.append(vo.getCauseMessage() + ",");
                    reasonVersion = vo.getVersion();
                }
                if (allPhotoBackMap.containsKey(reasonId)) {
                    photoBackRed.put(reasonId, allPhotoBackMap.get(reasonId));
                    allPhotoBackMap.remove(reasonId);
                    continue;
                }
                if (allPhotoFrontMap.containsKey(reasonId)) {
                    photoFrontRed.put(reasonId, allPhotoFrontMap.get(reasonId));
                    allPhotoFrontMap.remove(reasonId);
                    continue;
                }
                if (allPhotoVideoMap.containsKey(reasonId)) {
                    videoRed.put(reasonId, allPhotoVideoMap.get(reasonId));
                    allPhotoVideoMap.remove(reasonId);
                    continue;
                }
                if (allPhotoRealTimeMap.containsKey(reasonId)) {
                    realTimeRed.put(reasonId, allPhotoRealTimeMap.get(reasonId));
                    allPhotoRealTimeMap.remove(reasonId);
                    continue;
                }
                if (reasonId == rela.getCode()) {
                    relaRed = true;
                    continue;
                }
                if (reasonId == homeAddr.getCode()) {
                    homeAddrRed = true;
                    continue;
                }
                if (reasonId == jobUnit.getCode()) {
                    jogUnitRed = true;
                    continue;
                }
                if (reasonId == unitAddr.getCode()) {
                    unitAddrRed = true;
                }

            }

            if (photoBackRed.isEmpty()) {
                for (Map.Entry<Integer, ARejectReasonMasterEntity> entry : allPhotoBackMap.entrySet()) {
                    backGreen.append(entry.getKey() + ",");
                }
            } else {
                back.setType(11);
                StringBuffer backRed = new StringBuffer();
                StringBuffer backMarkIds = new StringBuffer();
                for (Map.Entry<Integer, ARejectReasonMasterEntity> entry : allPhotoBackMap.entrySet()) {

                    backGreen.append(entry.getKey() + ",");
                }
                for (Map.Entry<Integer, ARejectReasonMasterEntity> entry : photoBackRed.entrySet()) {
                    if (entry.getValue().getJoinRefuse() == 1) {
                        backRed.append(entry.getKey() + ",");
                    } else {
                        backMarkIds.append(entry.getKey() + ",");
                    }
                }
                // back.setGreenIds(backGreen.toString());
                back.setRedIds(backRed.toString());
                back.setMarkIds(backMarkIds.toString());
                back.setWholeGreenFlag(null);
            }

            if (photoFrontRed.isEmpty()) {
                for (Map.Entry<Integer, ARejectReasonMasterEntity> entry : allPhotoFrontMap.entrySet()) {
                    frontGreen.append(entry.getKey() + ",");

                }
            } else {
                front.setType(6);
                StringBuffer frontRed = new StringBuffer();
                StringBuffer frontMarkIds = new StringBuffer();
                for (Map.Entry<Integer, ARejectReasonMasterEntity> entry : allPhotoFrontMap.entrySet()) {
                    frontGreen.append(entry.getKey() + ",");

                }
                for (Map.Entry<Integer, ARejectReasonMasterEntity> entry : photoFrontRed.entrySet()) {
                    if (entry.getValue().getJoinRefuse() == 1) {
                        frontRed.append(entry.getKey() + ",");
                    } else {
                        frontMarkIds.append(entry.getKey() + ",");
                    }
                }
                // front.setGreenIds(frontGreen.toString());
                front.setRedIds(frontRed.toString());
                front.setMarkIds(frontMarkIds.toString());
                front.setWholeGreenFlag(null);
            }

            if (videoRed.isEmpty()) {
                for (Map.Entry<Integer, ARejectReasonMasterEntity> entry : allPhotoVideoMap.entrySet()) {
                    videoGreen.append(entry.getKey() + ",");
                }
            } else {
                back.setType(8);
                StringBuffer videoRedStr = new StringBuffer();
                StringBuffer videoMarkIds = new StringBuffer();
                for (Map.Entry<Integer, ARejectReasonMasterEntity> entry : allPhotoVideoMap.entrySet()) {
                    videoGreen.append(entry.getKey() + ",");
                }
                for (Map.Entry<Integer, ARejectReasonMasterEntity> entry : videoRed.entrySet()) {
                    if (entry.getValue().getJoinRefuse() == 1) {
                        videoRedStr.append(entry.getKey() + ",");
                    } else {
                        videoMarkIds.append(entry.getKey() + ",");
                    }
                }
                // video.setGreenIds(videoGreen.toString());
                video.setRedIds(videoRedStr.toString());
                video.setMarkIds(videoMarkIds.toString());
                video.setWholeGreenFlag(null);
            }

            if (realTimeRed.isEmpty()) {
                for (Map.Entry<Integer, ARejectReasonMasterEntity> entry : allPhotoRealTimeMap.entrySet()) {
                    realGreen.append(entry.getKey() + ",");
                }
            } else {
                real.setType(7);
                StringBuffer realRed = new StringBuffer();
                StringBuffer realMarkIds = new StringBuffer();
                for (Map.Entry<Integer, ARejectReasonMasterEntity> entry : allPhotoRealTimeMap.entrySet()) {
                    realGreen.append(entry.getKey() + ",");

                }
                for (Map.Entry<Integer, ARejectReasonMasterEntity> entry : realTimeRed.entrySet()) {
                    if (entry.getValue().getJoinRefuse() == 1) {
                        realRed.append(entry.getKey() + ",");
                    } else {
                        realMarkIds.append(entry.getKey() + ",");
                    }
                }
                // real.setGreenIds(realGreen.toString());
                real.setRedIds(realRed.toString());
                real.setMarkIds(realMarkIds.toString());
                video.setWholeGreenFlag(null);

            }
            if (relaRed) {
                relative.setWholeGreenFlag(null);
                relative.setType(5);
                relative.setRedIds(rela.getCode() + "");
            } else {
                relative.setGreenIds(rela.getCode() + "");
            }

            if (unitAddrRed) {
                unitAddress.setWholeGreenFlag(null);
                unitAddress.setType(2);
                unitAddress.setRedIds(unitAddr.getCode() + "");
            } else {
                unitAddress.setGreenIds(unitAddr.getCode() + "");
            }
            if (homeAddrRed) {
                home.setWholeGreenFlag(null);
                home.setType(4);
                home.setRedIds(homeAddr.getCode() + "");
            } else {
                home.setGreenIds(homeAddr.getCode() + "");
            }
            if (jogUnitRed) {
                jobUnitName.setWholeGreenFlag(null);
                jobUnitName.setType(3);
                jobUnitName.setRedIds(jobUnit.getCode() + "");
            } else {
                jobUnitName.setGreenIds(jobUnit.getCode() + "");
            }
        } else {
            for (Map.Entry<Integer, ARejectReasonMasterEntity> entry : allPhotoRealTimeMap.entrySet()) {
                realGreen.append(entry.getKey() + ",");
            }
            // real.setGreenIds(realGreen.toString());
            for (Map.Entry<Integer, ARejectReasonMasterEntity> entry : allPhotoFrontMap.entrySet()) {
                frontGreen.append(entry.getKey() + ",");
            }
            // front.setGreenIds(frontGreen.toString());

            for (Map.Entry<Integer, ARejectReasonMasterEntity> entry : allPhotoVideoMap.entrySet()) {
                videoGreen.append(entry.getKey() + ",");
            }
            // video.setGreenIds(videoGreen.toString());

            for (Map.Entry<Integer, ARejectReasonMasterEntity> entry : allPhotoBackMap.entrySet()) {
                backGreen.append(entry.getKey() + ",");
            }
            relative.setGreenIds(rela.getCode() + "");
            unitAddress.setGreenIds(unitAddr.getCode() + "");
            home.setGreenIds(homeAddr.getCode() + "");
            jobUnitName.setGreenIds(jobUnit.getCode() + "");
            // back.setGreenIds(backGreen.toString());
        }
        real.setGreenIds(realGreen.toString());
        front.setGreenIds(frontGreen.toString());
        back.setGreenIds(backGreen.toString());
        video.setGreenIds(videoGreen.toString());
        manualGreen.put(Constants.KEY_CONTACT, relative);
        manualGreen.put(Constants.KEY_UNIT_NAME, jobUnitName);
        manualGreen.put(Constants.KEY_FAMILY_ADDR, home);
        manualGreen.put(Constants.KEY_UNIT_ADDR, unitAddress);
        manualGreen.put(Constants.KEY_FRONT_IDCARD, front);
        manualGreen.put(Constants.KEY_BACK_IDCARD, back);
        manualGreen.put(Constants.KEY_HEAD_ICON, real);
        manualGreen.put(Constants.KEY_VIDEO, video);
        try {
            String jsonStr = objectMapper.writeValueAsString(manualGreen);
            record.setManualGreen(jsonStr);
        } catch (JsonProcessingException e) {

        }
        record.setReason(reason.toString());
        record.setReasonIds(reasonIDs.toString());
        record.setReasonVersion(reasonVersion);
        record.setResultType(Constants.AUDIT_MANUAL);
        record.setUpdateAt(new Date());
        return userProductAuditStatusMapper.updateByPrimaryKeySelective(record);
    }

    public Map<Integer, ARejectReasonMasterEntity> buildMap(List<Reason> vo) {
        Map<Integer, ARejectReasonMasterEntity> allReasonMap = new HashMap<Integer, ARejectReasonMasterEntity>();
        for (Reason rea : vo) {
            ARejectReasonMasterEntity entity = new ARejectReasonMasterEntity();
            entity.setCauseMessage(rea.getMessage());
            entity.setJoinRefuse(rea.getChange());
            entity.setReasonId(rea.getCode());
            entity.setType(rea.getType().byteValue());
            allReasonMap.put(rea.getCode(), entity);
        }
        return allReasonMap;

    }

    @Override
    public AUserProductAuditStatusEntity selectAUserProductAuditStatusByApplyId(Long applyId) throws ServiceException {
        if (applyId == null) {
            throw new ServiceException(ServiceExceptionConstant.APPLY_ID_NULL, "");
        }
        AUserProductAuditStatusEntity userProductAuditStatus = userProductAuditStatusMapper.selectByPrimaryKey(applyId);
        return userProductAuditStatus;
    }

    /**
     * 加载所有的拒绝原因
     * 
     * @return key:reasonId ;value ARejectReasonMasterEntity
     */
    public Map<Integer, ARejectReasonMasterEntity> loadAllRejectReason() {
        List<ARejectReasonMasterEntity> list = rejectReasonMapper.selectAll();
        if (list != null && list.size() > 0) {
            Map<Integer, ARejectReasonMasterEntity> map = new HashMap<Integer, ARejectReasonMasterEntity>(list.size());
            for (ARejectReasonMasterEntity vo : list) {
                map.put(vo.getReasonId(), vo);
            }
            return map;
        }
        return null;
    }

    public Map<Integer, Reason> loadRejectReasonByType(Integer type) {
        List<ARejectReasonMasterEntity> list = rejectReasonMapper.getRejectReasonByType(type);
        if (list != null && list.size() > 0) {
            Map<Integer, Reason> map = new HashMap<Integer, Reason>(list.size());
            for (ARejectReasonMasterEntity vo : list) {
                Reason reason = new Reason();
                reason.setType(type);
                reason.setChange(vo.getJoinRefuse());
                reason.setCode(vo.getReasonId());
                reason.setMessage(vo.getCauseMessage());
                map.put(vo.getReasonId(), reason);
            }
            return map;
        }
        return null;
    }

    @Override
    public AuditVo buildAuditVo(AUserProductAuditStatusEntity vo) {

        Map<Integer, ARejectReasonMasterEntity> rejectMap = loadAllRejectReason();

        AUserProductAuditEntity bo = userProductAuditMapper.selectByPrimaryKey(vo.getProductAuditId());

        String autoJson = vo.getAutoGreen();

        // Map<String,Object> map = JSON.parseObject(autoJson);
        Map<String, ReasonCategoryVO> map = null;
        try {
            map = objectMapper.readValue(autoJson,
                    objectMapper.getTypeFactory().constructMapType(Map.class, String.class, ReasonCategoryVO.class));
        } catch (IOException e) {
            log.error("parse json to Object error.");
        }
        ReasonCategoryVO contact = map.get(ReasonCategoryVO.KEY_CONTACT);
        ReasonCategoryVO unitname = map.get(ReasonCategoryVO.KEY_UNIT_NAME);
        ReasonCategoryVO familyAddr = map.get(ReasonCategoryVO.KEY_FAMILY_ADDR);
        ReasonCategoryVO unitAddr = map.get(ReasonCategoryVO.KEY_UNIT_ADDR);
        ReasonCategoryVO frontIdCard = map.get(ReasonCategoryVO.KEY_FRONT_IDCARD);
        ReasonCategoryVO backIdCard = map.get(ReasonCategoryVO.KEY_BACK_IDCARD);
        ReasonCategoryVO icon = map.get(ReasonCategoryVO.KEY_HEAD_ICON);
        ReasonCategoryVO video = map.get(ReasonCategoryVO.KEY_VIDEO);
        try {
            AuditVo auditVo = new AuditVo();
            auditVo.setApplyId(vo.getApplyId());
            auditVo.setUserId(vo.getUserId() + "");
            auditVo.setProductAuditId(vo.getProductAuditId());
            auditVo.setProductId(vo.getProductId());
            if (bo != null) {
                auditVo.setUserName(bo.getCustomerName());
                auditVo.setMobileNo(bo.getMobileNo());
                auditVo.setApplyAt(bo.getApplyAt());
                auditVo.setApplyTimes(bo.getApplyTimes().intValue());
                auditVo.setSendTimes(bo.getSendTimes().intValue());
                auditVo.setProductName(bo.getProductId().toString());
                auditVo.setHomeAddr(buildHomeAddr(familyAddr, rejectMap, bo));
                auditVo.setUnitAddr(buildUnitAddr(unitAddr, rejectMap, bo));
                auditVo.setJobUnit(buildJobUnit(unitname, rejectMap, bo));
                auditVo.setRela(buildRela(contact, rejectMap, bo));
                auditVo.setPhotoFront(buildPhotoFront(frontIdCard, rejectMap, bo));
                if (backIdCard != null) {
                    auditVo.setPhotoBack(buildPhotoBack(backIdCard, rejectMap, bo));
                }
                if (icon != null) {
                    auditVo.setPhotoRealtime(buildPhotoRealtime(icon, rejectMap, bo));
                }
                String idNo = bo.getIdNo();
                if (StringUtils.isNotBlank(idNo)) {
                    String sexStr = String.valueOf(idNo.charAt(16));
                    int sex = Integer.parseInt(sexStr);
                    if (sex % 2 == 0)
                        auditVo.setSex(0); // 性别为女
                    else
                        auditVo.setSex(1); // 性别为男
                }
                auditVo.setVideo(buildVideo(video, rejectMap, bo));
            }
            return auditVo;
        } catch (Exception e) {
            log.error("build audit vo accoured exception:", e.getMessage());
            return null;
        }
    }

    @Override
    public AUserProductAuditStatusEntity getProductAuditStatusByUserId(@Param("userId") Long userId){
        return userProductAuditStatusMapper.getProductAuditStatusByUserId(userId);
    }

    /**
     * 构建视频信息
     * 
     * @param vo
     * @param rejectMap
     *            拒绝原因
     * @param bo
     * @return
     */
    public Video buildVideo(ReasonCategoryVO vo, Map<Integer, ARejectReasonMasterEntity> rejectMap,
            AUserProductAuditEntity bo) {
        Video result = new Video();
        JSONObject ftpConfigJsonObject = JSONObject.parseObject(bo.getFtpConfigJson());
        Object ftpMarker = null;
        String strDate = null;
        if (ftpConfigJsonObject != null) {
            ftpMarker = ftpConfigJsonObject.get("ftp_marker");
            if (ftpMarker == null || (int) ftpMarker == GET_FILE_TO_FASTDFS_FLAG) {
                log.info("ftp marker is null or 100, get file from fastdfs");
                // 到fastFtp中获取影像文件

            } else {
                log.info("ftp marker is not null and 100, get file from ftp..., ftpMarker is {}", ftpMarker);
                // 从ftp中获取影像资料
            }
            Date registerDate = null;

            if (ftpConfigJsonObject.get("register_date") != null) {
                registerDate = new Date((Long) (ftpConfigJsonObject.get("register_date")));
                strDate = sdf.format(registerDate);
            }
        }

        String videoURL = "image/ftp/show?path=" + bo.getVideo() + "&ftp_marker=" + ftpMarker + "&register_date="
                + strDate;
        String downloadURL = "video/ftp/download?path=" + bo.getVideo() + "&ftp_marker=" + ftpMarker + "&register_date="
                + strDate;
        result.setType(vo.getType());
        result.setUrl(videoURL);
        result.setDownloadUrl(downloadURL);
        if (vo.getWholeGreenFlag() != null && 1 == vo.getWholeGreenFlag()) {
            result.setOcrFlag(false);
            result.setGreenPrompt("视频已通过");
        } else {
            result.setOcrFlag(true);
            String greenIds = vo.getGreenIds();
            String redIds = vo.getRedIds();
            String markIds = vo.getMarkIds();
            Map<Integer, Reason> videoReason = loadRejectReasonByType(8);
            List<Reason> reasons = new ArrayList<Reason>();
            if (redIds != null && !redIds.isEmpty() && !"null".equals(redIds)) {
                String[] array = redIds.split(",");
                for (String string : array) {
                    Reason reason = new Reason();
                    reason.setCode(Integer.valueOf(string));
                    ARejectReasonMasterEntity entity = rejectMap.get(Integer.valueOf(string));
                    reason.setMessage(entity.getCauseMessage());
                    reason.setChange(entity.getJoinRefuse());
                    reason.setCheck(true);
                    reasons.add(reason);
                    videoReason.put(Integer.valueOf(string), reason);
                }
            }
            if (greenIds != null && !greenIds.isEmpty() && !"null".equals(greenIds)) {
                result.setGreenPrompt("所读内容已通过");
                String[] array = greenIds.split(",");
                for (String string : array) {
                    Reason reason = new Reason();
                    reason.setCode(Integer.valueOf(string));
                    ARejectReasonMasterEntity entity = rejectMap.get(Integer.valueOf(string));
                    reason.setMessage(entity.getCauseMessage());
                    reason.setChange(entity.getJoinRefuse());
                    reason.setCheck(false);
                    reasons.add(reason);
                    videoReason.remove(Integer.valueOf(string));
                }
            }
            List<Reason> reasonList = new LinkedList<Reason>();
            for (Map.Entry<Integer, Reason> entry : videoReason.entrySet()) {
                reasonList.add(entry.getValue());
            }
            if (reasonList.size() > 1) {
                Collections.sort(reasonList, new Comparator<Reason>() {
                    public int compare(Reason arg0, Reason arg1) {
                        return arg0.getCode().compareTo(arg1.getCode());
                    }
                });
            }
            result.setReason(reasonList);
        }
        return result;
    }

    public PhotoRealtime buildPhotoRealtime(ReasonCategoryVO vo, Map<Integer, ARejectReasonMasterEntity> rejectMap,
            AUserProductAuditEntity bo) {
        PhotoRealtime result = new PhotoRealtime();
        result.setType(vo.getType());
        JSONObject ftpConfigJsonObject = JSONObject.parseObject(bo.getFtpConfigJson());
        Object ftpMarker = null;
        String strDate = null;
        if (ftpConfigJsonObject != null) {
            ftpMarker = ftpConfigJsonObject.get("ftp_marker");
            if (ftpMarker == null || (int) ftpMarker == GET_FILE_TO_FASTDFS_FLAG) {
                log.info("ftp marker is null or 100, get file from fastdfs");
                // 到fastFtp中获取影像文件

            } else {
                log.info("ftp marker is not null and 100, get file from ftp..., ftpMarker is {}", ftpMarker);
                // 从ftp中获取影像资料
            }
            Date registerDate = null;

            if (ftpConfigJsonObject.get("register_date") != null) {
                registerDate = new Date((Long) (ftpConfigJsonObject.get("register_date")));
                strDate = sdf.format(registerDate);
            }
        }

        String realTimeURL = "image/ftp/show?path=" + bo.getPhotoRealTime() + "&ftp_marker=" + ftpMarker
                + "&register_date=" + strDate;

        if (bo.getPhotoRealTime() == null || bo.getPhotoRealTime().isEmpty()) {
            result.setOldBos("旧版本上行，无实时头像");
            return result;
        } else {
            result.setUrl(realTimeURL);
        }
        Map<Integer, Reason> realReason = loadRejectReasonByType(7);
        if (vo.getWholeGreenFlag() != null && 1 == vo.getWholeGreenFlag()) {
            result.setOcrFlag(false);
            result.setGreenPrompt("人头像照片已通过");
        } else {
            result.setOcrFlag(true);
            String greenIds = vo.getGreenIds();
            String redIds = vo.getRedIds();
            String markIds = vo.getMarkIds();
            List<Reason> reasons = new ArrayList<Reason>();
            if (redIds != null && !redIds.isEmpty() && !"null".equals(redIds)) {
                String[] array = redIds.split(",");
                for (String string : array) {
                    Reason reason = new Reason();
                    reason.setCode(Integer.valueOf(string));
                    ARejectReasonMasterEntity entity = rejectMap.get(Integer.valueOf(string));
                    reason.setMessage(entity.getCauseMessage());
                    reason.setChange(entity.getJoinRefuse());
                    reason.setCheck(true);
                    reasons.add(reason);
                    realReason.put(Integer.valueOf(string), reason);
                }
            }
            if (greenIds != null && !greenIds.isEmpty() && !"null".equals(greenIds)) {
                String[] array = greenIds.split(",");
                for (String string : array) {
                    Reason reason = new Reason();
                    reason.setCode(Integer.valueOf(string));
                    ARejectReasonMasterEntity entity = rejectMap.get(Integer.valueOf(string));
                    reason.setMessage(entity.getCauseMessage());
                    reason.setChange(entity.getJoinRefuse());
                    reason.setCheck(false);
                    reasons.add(reason);
                    realReason.remove(Integer.valueOf(string));
                }
            }
            List<Reason> reasonList = new LinkedList<Reason>();
            for (Map.Entry<Integer, Reason> entry : realReason.entrySet()) {
                reasonList.add(entry.getValue());
            }
            if (reasonList.size() > 1) {
                Collections.sort(reasonList, new Comparator<Reason>() {
                    public int compare(Reason arg0, Reason arg1) {
                        return arg0.getCode().compareTo(arg1.getCode());
                    }
                });
            }
            result.setReason(reasonList);
        }
        return result;
    }

    public PhotoBack buildPhotoBack(ReasonCategoryVO vo, Map<Integer, ARejectReasonMasterEntity> rejectMap,
            AUserProductAuditEntity bo) {
        JSONObject ftpConfigJsonObject = JSONObject.parseObject(bo.getFtpConfigJson());
        String strDate = null;
        Object ftpMarker = null;
        if (ftpConfigJsonObject != null) {
            ftpMarker = ftpConfigJsonObject.get("ftp_marker");
            if (ftpMarker == null || (int) ftpMarker == GET_FILE_TO_FASTDFS_FLAG) {
                log.info("ftp marker is null or 100, get file from fastdfs");
                // 到fastFtp中获取影像文件

            } else {
                log.info("ftp marker is not null and 100, get file from ftp..., ftpMarker is {}", ftpMarker);
                // 从ftp中获取影像资料
            }
            Date registerDate = null;

            if (ftpConfigJsonObject.get("register_date") != null) {
                registerDate = new Date((Long) (ftpConfigJsonObject.get("register_date")));
                strDate = sdf.format(registerDate);
            }
        }

        String idCardBackURL = "image/ftp/show?path=" + bo.getPhotoBack() + "&ftp_marker=" + ftpMarker
                + "&register_date=" + strDate;

        PhotoBack result = new PhotoBack();
        result.setType(vo.getType());
        if (bo.getPhotoBack() == null || bo.getPhotoBack().isEmpty()) {
            result.setOldBoc("旧版本中银，无身份证反面");
            return result;
        } else {
            result.setUrl(idCardBackURL);
        }
        Map<Integer, Reason> backReason = loadRejectReasonByType(11);
        if (vo.getWholeGreenFlag() != null && 1 == vo.getWholeGreenFlag()) {
            result.setOcrFlag(false);
            result.setGreenPrompt("身份证反面已通过");
        } else {
            result.setOcrFlag(true);
            String greenIds = vo.getGreenIds();
            String redIds = vo.getRedIds();
            String markIds = vo.getMarkIds();
            List<Reason> reasons = new ArrayList<Reason>();
            if (redIds != null && !redIds.isEmpty() && !"null".equals(redIds)) {
                String[] array = redIds.split(",");
                for (String string : array) {
                    Reason reason = new Reason();
                    reason.setCode(Integer.valueOf(string));
                    ARejectReasonMasterEntity entity = rejectMap.get(Integer.valueOf(string));
                    reason.setMessage(entity.getCauseMessage());
                    reason.setChange(entity.getJoinRefuse());
                    reason.setCheck(true);
                    reasons.add(reason);
                    backReason.put(Integer.valueOf(string), reason);
                }
            }
            if (greenIds != null && !greenIds.isEmpty() && !"null".equals(greenIds)) {
                String[] array = greenIds.split(",");
                for (String string : array) {
                    Reason reason = new Reason();
                    reason.setCode(Integer.valueOf(string));
                    ARejectReasonMasterEntity entity = rejectMap.get(Integer.valueOf(string));
                    reason.setMessage(entity.getCauseMessage());
                    reason.setChange(entity.getJoinRefuse());
                    reason.setCheck(false);
                    reasons.add(reason);
                    backReason.remove(Integer.valueOf(string));
                }
            }
            List<Reason> reasonList = new LinkedList<Reason>();
            for (Map.Entry<Integer, Reason> entry : backReason.entrySet()) {
                reasonList.add(entry.getValue());
            }
            if (reasonList.size() > 1) {
                Collections.sort(reasonList, new Comparator<Reason>() {
                    public int compare(Reason arg0, Reason arg1) {
                        return arg0.getCode().compareTo(arg1.getCode());
                    }
                });
            }

            result.setReason(reasonList);
        }
        return result;
    }

    public PhotoFront buildPhotoFront(ReasonCategoryVO vo, Map<Integer, ARejectReasonMasterEntity> rejectMap,
            AUserProductAuditEntity bo) {
        JSONObject ftpConfigJsonObject = JSONObject.parseObject(bo.getFtpConfigJson());
        Object ftpMarker = null;
        String strDate = null;
        if (ftpConfigJsonObject != null) {
            ftpMarker = ftpConfigJsonObject.get("ftp_marker");
            if (ftpMarker == null || (int) ftpMarker == GET_FILE_TO_FASTDFS_FLAG) {
                log.info("ftp marker is null or 100, get file from fastdfs");
                // 到fastFtp中获取影像文件

            } else {
                log.info("ftp marker is not null and 100, get file from ftp..., ftpMarker is {}", ftpMarker);
                // 从ftp中获取影像资料
            }
            Date registerDate = null;

            if (ftpConfigJsonObject.get("register_date") != null) {
                registerDate = new Date((Long) (ftpConfigJsonObject.get("register_date")));
                strDate = sdf.format(registerDate);
            }

        }

        PhotoFront result = new PhotoFront();
        result.setType(vo.getType());

        String idCardFrontURL = "image/ftp/show?path=" + bo.getPhotoFront() + "&ftp_marker=" + ftpMarker
                + "&register_date=" + strDate;
        result.setUrl(idCardFrontURL);
        if (vo.getWholeGreenFlag() != null && 1 == vo.getWholeGreenFlag()) {
            result.setOcrFlag(false);
            result.setGreenPrompt("身份证正面已通过");
        } else {
            Map<Integer, Reason> frontReason = loadRejectReasonByType(6);
            result.setOcrFlag(true);
            String greenIds = vo.getGreenIds();
            String redIds = vo.getRedIds();
            String markIds = vo.getMarkIds();
            List<Reason> reasons = new ArrayList<Reason>();

            if (redIds != null && !redIds.isEmpty() && !"null".equals(redIds)) {
                String[] array = redIds.split(",");
                for (String string : array) {
                    Reason reason = new Reason();
                    reason.setCode(Integer.valueOf(string));
                    ARejectReasonMasterEntity entity = rejectMap.get(Integer.valueOf(string));
                    reason.setMessage(entity.getCauseMessage());
                    reason.setChange(entity.getJoinRefuse());
                    reason.setCheck(true);
                    reasons.add(reason);
                    frontReason.put(Integer.valueOf(string), reason);
                }
            }
            if (greenIds != null && !greenIds.isEmpty() && !"null".equals(greenIds)) {
                String[] array = greenIds.split(",");
                for (String string : array) {
                    Reason reason = new Reason();
                    reason.setCode(Integer.valueOf(string));
                    ARejectReasonMasterEntity entity = rejectMap.get(Integer.valueOf(string));
                    reason.setMessage(entity.getCauseMessage());
                    reason.setChange(entity.getJoinRefuse());
                    reason.setCheck(false);
                    reasons.add(reason);
                    frontReason.remove(Integer.valueOf(string));
                }
            }
            List<Reason> reasonList = new LinkedList<Reason>();
            for (Map.Entry<Integer, Reason> entry : frontReason.entrySet()) {
                reasonList.add(entry.getValue());
            }
            if (reasonList.size() > 1) {
                Collections.sort(reasonList, new Comparator<Reason>() {
                    public int compare(Reason arg0, Reason arg1) {
                        return arg0.getCode().compareTo(arg1.getCode());
                    }
                });
            }
            result.setReason(reasonList);
        }
        return result;
    }

    public Rela buildRela(ReasonCategoryVO vo, Map<Integer, ARejectReasonMasterEntity> rejectMap,
            AUserProductAuditEntity bo) {
        Rela result = new Rela();
        result.setType(vo.getType());
        Reason resultReason = null;
        Map<Integer, Reason> contactReason = loadRejectReasonByType(5);
        for (Map.Entry<Integer, Reason> entry : contactReason.entrySet()) {
            resultReason = entry.getValue();
        }
        if (vo.getWholeGreenFlag() != null && 1 == vo.getWholeGreenFlag()) {
            result.setGreenPrompt("联系人已通过");
        } else {
            String redIds = vo.getRedIds();
            Reason reason = new Reason();
            if (redIds != null && !redIds.isEmpty() && !"null".equals(redIds)) {
                String[] array = redIds.split(",");
                reason.setCode(Integer.valueOf(array[0]));
                ARejectReasonMasterEntity entity = rejectMap.get(Integer.valueOf(array[0]));
                if (entity != null) {
                    reason.setCheck(true);
                    reason.setChange(entity.getJoinRefuse());
                    reason.setMessage(entity.getCauseMessage());
                    contactReason.put(Integer.valueOf(array[0]), reason);
                }
            }
            for (Map.Entry<Integer, Reason> entry : contactReason.entrySet()) {
                if (entry.getKey() == 41) {
                    resultReason = entry.getValue();
                }

            }

        }
        result.setReason(resultReason);
        List<Info> infos = new ArrayList<Info>();
        infos.add(new Info("relaFamily", bo.getRelaFamily(), "B"));
        infos.add(new Info("relaColleague", bo.getRelaColleague(), "B"));
        infos.add(new Info("relaFriend", bo.getRelaFriend(), "B"));

        result.setInfo(infos);
        return result;
    }

    public JobUnit buildJobUnit(ReasonCategoryVO vo, Map<Integer, ARejectReasonMasterEntity> rejectMap,
            AUserProductAuditEntity bo) {
        JobUnit result = new JobUnit();
        result.setType(vo.getType());
        Map<Integer, Reason> unitNameReason = loadRejectReasonByType(3);
        Reason resultReason = null;
        for (Map.Entry<Integer, Reason> entry : unitNameReason.entrySet()) {
            resultReason = entry.getValue();
        }
        if (vo.getWholeGreenFlag() != null && 1 == vo.getWholeGreenFlag()) {
            result.setGreenPrompt("公司名称已通过");
        } else {
            String redIds = vo.getRedIds();
            Reason reason = new Reason();
            if (redIds != null && !redIds.isEmpty() && !"null".equals(redIds)) {
                String[] array = redIds.split(",");
                reason.setCode(Integer.valueOf(array[0]));
                ARejectReasonMasterEntity entity = rejectMap.get(Integer.valueOf(array[0]));
                if (entity != null) {
                    reason.setCheck(true);
                    reason.setChange(entity.getJoinRefuse());
                    reason.setMessage(entity.getCauseMessage());
                    unitNameReason.put(Integer.valueOf(array[0]), reason);
                }
            }
            for (Map.Entry<Integer, Reason> entry : unitNameReason.entrySet()) {
                if (entry.getKey() == 18) {
                    resultReason = entry.getValue();
                }
            }

        }
        result.setReason(resultReason);
        List<Info> infos = new ArrayList<Info>();
        infos.add(new Info("jobUnit", bo.getJobUnit(), "B"));
        result.setInfo(infos);
        return result;
    }

    public UnitAddr buildUnitAddr(ReasonCategoryVO vo, Map<Integer, ARejectReasonMasterEntity> rejectMap,
            AUserProductAuditEntity bo) {
        UnitAddr result = new UnitAddr();
        result.setType(vo.getType());
        Map<Integer, Reason> unitAddrReason = loadRejectReasonByType(2);
        Reason resultReason = null;
        for (Map.Entry<Integer, Reason> entry : unitAddrReason.entrySet()) {
            resultReason = entry.getValue();
        }
        if (vo.getWholeGreenFlag() != null && 1 == vo.getWholeGreenFlag()) {
            result.setGreenPrompt("公司地址已通过");
        } else {
            String greenIds = vo.getGreenIds();
            String redIds = vo.getRedIds();
            String markIds = vo.getMarkIds();
            Reason reason = new Reason();
            if (redIds != null && !redIds.isEmpty() && !"null".equals(redIds)) {
                String[] array = redIds.split(",");
                reason.setCode(Integer.valueOf(array[0]));
                ARejectReasonMasterEntity entity = rejectMap.get(Integer.valueOf(array[0]));
                if (entity != null) {
                    reason.setCheck(true);
                    reason.setChange(entity.getJoinRefuse());
                    reason.setMessage(entity.getCauseMessage());
                    unitAddrReason.put(Integer.valueOf(array[0]), reason);
                }
            }
            for (Map.Entry<Integer, Reason> entry : unitAddrReason.entrySet()) {
                resultReason = entry.getValue();
            }

        }
        result.setReason(resultReason);
        List<Info> infos = new ArrayList<Info>();
        infos.add(new Info("unitProvince", bo.getUnitProvince(), "B"));
        infos.add(new Info("unitCity", bo.getUnitCity(), "B"));
        infos.add(new Info("unitsDistrict", bo.getUnitsDistrict(), "B"));
        infos.add(new Info("unitAddr", bo.getUnitAddr(), "B"));
        result.setInfo(infos);
        return result;
    }

    public HomeAddr buildHomeAddr(ReasonCategoryVO vo, Map<Integer, ARejectReasonMasterEntity> rejectMap,
            AUserProductAuditEntity bo) {
        HomeAddr result = new HomeAddr();
        result.setType(vo.getType());
        Map<Integer, Reason> homeAddrReason = loadRejectReasonByType(4);
        Reason resultReason = null;
        for (Map.Entry<Integer, Reason> entry : homeAddrReason.entrySet()) {
            resultReason = entry.getValue();
        }
        if (vo.getWholeGreenFlag() != null && 1 == vo.getWholeGreenFlag()) {
            result.setGreenPrompt("家庭地址已通过");
        } else {
            String redIds = vo.getRedIds();
            Reason reason = new Reason();
            if (redIds != null && !redIds.isEmpty() && !"null".equals(redIds)) {
                String[] array = redIds.split(",");
                reason.setCode(Integer.valueOf(array[0]));
                ARejectReasonMasterEntity entity = rejectMap.get(Integer.valueOf(array[0]));
                if (entity != null) {
                    reason.setCheck(true);
                    reason.setChange(entity.getJoinRefuse());
                    reason.setMessage(entity.getCauseMessage());
                    homeAddrReason.put(Integer.valueOf(array[0]), reason);
                }
            }
            for (Map.Entry<Integer, Reason> entry : homeAddrReason.entrySet()) {
                resultReason = entry.getValue();
            }

        }
        result.setReason(resultReason);
        List<Info> infos = new ArrayList<Info>();
        infos.add(new Info("homeProvince", bo.getHomeProvince(), "B"));
        infos.add(new Info("homeCity", bo.getHomeCity(), "B"));
        infos.add(new Info("homeDistrict", bo.getHomeDistrict(), "B"));
        infos.add(new Info("homeAddr", bo.getHomeAddr(), "B"));
        result.setInfo(infos);
        return result;
    }

}
