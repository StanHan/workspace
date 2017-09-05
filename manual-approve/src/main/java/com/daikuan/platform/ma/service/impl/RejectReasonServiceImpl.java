package com.daikuan.platform.ma.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.daikuan.platform.ma.common.request.RejectReasonsRequest;
import com.daikuan.platform.ma.common.vo.RejectReasonVO;
import com.daikuan.platform.ma.constant.MAConstant;
import com.daikuan.platform.ma.constant.ServiceExceptionConstant;
import com.daikuan.platform.ma.dao.ARejectReasonMasterHisMapper;
import com.daikuan.platform.ma.dao.ARejectReasonMasterMapper;
import com.daikuan.platform.ma.exception.ServiceException;
import com.daikuan.platform.ma.exception.dao.DaoException;
import com.daikuan.platform.ma.pojo.ARejectReasonMasterEntity;
import com.daikuan.platform.ma.pojo.ARejectReasonMasterHisEntity;
import com.daikuan.platform.ma.redis.RedisUtil;
import com.daikuan.platform.ma.service.IRejectReasonService;
import com.daikuan.platform.ma.util.Constants;
import com.daikuan.platform.ma.vo.Reason;
import com.daikuan.platform.ma.vo.RejectReasonsVo;
import com.github.abel533.entity.Example;
import com.github.abel533.entity.Example.Criteria;

/**
 * 
 * @author yangqb
 * @date 2017年3月13日
 * @desc
 */
@Service
public class RejectReasonServiceImpl implements IRejectReasonService {

    @Autowired
    private ARejectReasonMasterHisMapper rejectReasonHisMapper;

    @Autowired
    private ARejectReasonMasterMapper rejectReasonMapper;

    @Autowired
    private RedisUtil redisUtil;

    private static int NO_EXPIRATION = 0;

    @Override
    public void save(RejectReasonsRequest request) throws ServiceException, DaoException {
        List<RejectReasonVO> list = request.getRejectReasonList();
        if (request == null || CollectionUtils.isEmpty(list)) {
            throw new ServiceException(ServiceExceptionConstant.IN_PARAM_NULL, "客户端所传数据为空");
        }
        
        List<ARejectReasonMasterHisEntity> hisList = new ArrayList<>(list.size());
        String version = new java.sql.Date(System.currentTimeMillis()).toString();
        list.forEach(e -> hisList.add(buildRejectReasonMasterHis(e,version)));
        
        //批量插入 拒绝原因历史表
        try {
            ARejectReasonMasterHisEntity item = hisList.get(0);
            if (item == null) {
                throw new ServiceException("error_null", "推送拒绝原因-存在数据为空的行");
            }
            if (StringUtils.isBlank(item.getVersion())) {
                throw new ServiceException("error_null_version", "推送拒绝原因-存在数据版本号为空的行, reasonId="+item.getReasonId());
            }
            // 如果已存在该版本的数据要先删除, 如果该serivce方法存在异常, 则调用方收到异常码后要重传
            rejectReasonHisMapper.deleteByExample(getVersionExample(item.getVersion()));
            rejectReasonHisMapper.bathInsert(hisList);

        } catch (Exception e1) {
            throw new DaoException("批量插入拒绝原因历史表失败, 本次插入数据为insertRejectResonList=" + hisList, e1);
        }

        //批量插入 拒绝原因表
        Example example = getExample();
        // 为执行效率, 执行批量操作
        try {
            rejectReasonMapper.deleteByExample(example);
        } catch (Exception e) {
            throw new DaoException("清空a_reject_reason_master失败", e);
        }

        List<ARejectReasonMasterEntity> rejectReasonList = new ArrayList<ARejectReasonMasterEntity>(list.size());
        list.forEach(e -> rejectReasonList.add(buildRejectReasonMaster(e, version)));

        try {
            rejectReasonMapper.bathInsert(rejectReasonList);
        } catch (Exception e) {
            throw new DaoException("批量插入a_reject_reason_master失败, 插入数据为=" + rejectReasonList, e);
        }

        // 放入Redis一份最新数据
        redisUtil.saveOrUpdate(MAConstant.REJECT_REASON_MASTER_LIST_KEY, rejectReasonList, NO_EXPIRATION);
    }
    
    /**
     * 构造历史表
     * @param vo
     * @param version
     * @return
     */
    private ARejectReasonMasterHisEntity buildRejectReasonMasterHis(RejectReasonVO vo,String version){
        if(vo == null){
            return null;
        }
        ARejectReasonMasterHisEntity pojo = new ARejectReasonMasterHisEntity();
        pojo.setCreateAt(new Date());
        pojo.setCauseMessage(vo.getCauseMessage());
        pojo.setId(null);
        pojo.setReasonId(vo.getId());
        pojo.setStatus(vo.getStatus().byteValue());
        pojo.setType(vo.getType().byteValue());
        pojo.setVersion(version);
        pojo.setMessage(vo.getMessage());
        pojo.setSimpleReason(vo.getSimpleReason());
        pojo.setUpdateAt(null);
        return pojo;
    }

    private ARejectReasonMasterEntity buildRejectReasonMaster(RejectReasonVO vo,String version) {
        if(vo == null){
            return null;
        }
        ARejectReasonMasterEntity pojo = new ARejectReasonMasterEntity();
        pojo.setCreateAt(new Date());
        pojo.setCauseMessage(vo.getReason());//哎！数据定义不一样，所以将就吧
        pojo.setId(null);
        pojo.setReasonId(vo.getId());
        pojo.setStatus(vo.getStatus().byteValue());
        pojo.setType(vo.getType().byteValue());
        pojo.setVersion(version);
        Byte joinRefuse = null;
        Integer effectReject = vo.getEffectReject();
        if(effectReject != null){
            joinRefuse = (byte) (effectReject == 1 ? 0:1);
        }
        pojo.setJoinRefuse(joinRefuse);
        return pojo;
    }

    private Example getExample() {
        Example example = new Example(ARejectReasonMasterEntity.class);
        Criteria criteria = example.createCriteria();
        // 全表操作, 这里设置恒等条件
        criteria.andGreaterThan("id", -1);
        return example;
    }

    private Example getVersionExample(String version) {
        Example example = new Example(ARejectReasonMasterEntity.class);
        Criteria criteria = example.createCriteria();
        // 全表操作, 这里设置恒等条件
        criteria.andEqualTo("version", version);
        return example;
    }

    @Override
    public List<ARejectReasonMasterEntity> queryAll() throws DaoException {
        Example example = getExample();
        List<ARejectReasonMasterEntity> rejectReasonList = null;
        try {
            rejectReasonList = rejectReasonMapper.selectByExample(example);
        } catch (Exception e) {
            throw new DaoException("查询a_reject_reason_master表当前最新版本数据失败", e);
        }
        return rejectReasonList;
    }

    @Override
    public RejectReasonsVo getRejectReasonByType() throws DaoException {
        RejectReasonsVo vo  = new RejectReasonsVo();
        try {

            //单位地址
            List<ARejectReasonMasterEntity>  companyAdd = rejectReasonMapper.getRejectReasonByType(Constants.AUDIT_CATEGORY_TYPE_COMPANY_ADDRESS);
           if(companyAdd != null && companyAdd.size() > 0){
               ARejectReasonMasterEntity  entity = companyAdd.get(0);
               Reason reason = new Reason();
               reason.setCode(entity.getReasonId());
               reason.setCheck(false);
               reason.setChange(entity.getJoinRefuse());
               reason.setMessage(entity.getCauseMessage());
               reason.setType(Constants.AUDIT_CATEGORY_TYPE_COMPANY_ADDRESS);
               vo.setUnitAddr(reason);
           }

           //家庭地址
            List<ARejectReasonMasterEntity>  familiAddr =  rejectReasonMapper.getRejectReasonByType(Constants.AUDIT_CATEGORY_TYPE_FAMILAY_ADDRESS);
            if(familiAddr != null && familiAddr.size() > 0){
                ARejectReasonMasterEntity  entity = familiAddr.get(0);
                Reason reason = new Reason();
                reason.setCode(entity.getReasonId());
                reason.setCheck(false);
                reason.setChange(entity.getJoinRefuse());
                reason.setMessage(entity.getCauseMessage());
                reason.setType(Constants.AUDIT_CATEGORY_TYPE_FAMILAY_ADDRESS);
                vo.setHomeAddr(reason);
            }
            //联系人
            List<ARejectReasonMasterEntity>  contact =  rejectReasonMapper.getRejectReasonByType(Constants.AUDIT_CATEGORY_TYPE_CONTACT);
            if(contact != null && contact.size() > 0){
                ARejectReasonMasterEntity  entity = null;
                if(contact.size() > 1){
                    entity = contact.get(1);
                }else{
                    entity = contact.get(0);
                }
                Reason reason = new Reason();
                reason.setCode(entity.getReasonId());
                reason.setCheck(false);
                reason.setChange(entity.getJoinRefuse());
                reason.setMessage(entity.getCauseMessage());
                reason.setType(Constants.AUDIT_CATEGORY_TYPE_CONTACT);
                vo.setRela(reason);
            }

            //公司名称
            List<ARejectReasonMasterEntity>  conpanyName =  rejectReasonMapper.getRejectReasonByType(Constants.AUDIT_CATEGORY_TYPE_COMPANY_NAME);
            if(conpanyName != null && conpanyName.size() > 0){
                ARejectReasonMasterEntity  entity = conpanyName.get(0);
                Reason reason = new Reason();
                reason.setCode(entity.getReasonId());
                reason.setCheck(false);
                reason.setChange(entity.getJoinRefuse());
                reason.setMessage(entity.getCauseMessage());
                reason.setType(Constants.AUDIT_CATEGORY_TYPE_COMPANY_NAME);
                vo.setJobUnit(reason);
            }

            //身份证正面
            List<ARejectReasonMasterEntity>  idcardFront =  rejectReasonMapper.getRejectReasonByType(Constants.AUDIT_CATEGORY_TYPE_IDCARDFRONT);
            if(idcardFront != null && idcardFront.size() > 0) {
                List<Reason> list = new LinkedList<Reason>();
                for (ARejectReasonMasterEntity entity : idcardFront) {
                    Reason reason = new Reason();
                    reason.setCode(entity.getReasonId());
                    reason.setCheck(false);
                    reason.setChange(entity.getJoinRefuse());
                    reason.setMessage(entity.getCauseMessage());
                    reason.setType(Constants.AUDIT_CATEGORY_TYPE_IDCARDFRONT);
                    list.add(reason);
                }
                vo.setPhotoFront(list);
            }
                List<ARejectReasonMasterEntity>  idcardback =  rejectReasonMapper.getRejectReasonByType(Constants.AUDIT_CATEGORY_TYPE_IDCARDBACK);
                if(idcardback != null && idcardback.size() > 0) {
                    List<Reason> list = new LinkedList<Reason>();
                    for (ARejectReasonMasterEntity entity : idcardback) {
                        Reason reason = new Reason();
                        reason.setCode(entity.getReasonId());
                        reason.setCheck(false);
                        reason.setChange(entity.getJoinRefuse());
                        reason.setMessage(entity.getCauseMessage());
                        reason.setType(Constants.AUDIT_CATEGORY_TYPE_IDCARDBACK);
                        list.add(reason);
                    }
                    vo.setPhotoBack(list);

                }
                    List<ARejectReasonMasterEntity>  realTime =  rejectReasonMapper.getRejectReasonByType(Constants.AUDIT_CATEGORY_TYPE_CURRECT_ICON);
                    if(realTime != null && realTime.size() > 0) {
                        List<Reason> list = new LinkedList<Reason>();
                        for (ARejectReasonMasterEntity entity : realTime) {
                            Reason reason = new Reason();
                            reason.setCode(entity.getReasonId());
                            reason.setCheck(false);
                            reason.setChange(entity.getJoinRefuse());
                            reason.setMessage(entity.getCauseMessage());
                            reason.setType(Constants.AUDIT_CATEGORY_TYPE_CURRECT_ICON);
                            list.add(reason);
                        }
                        vo.setPhotoRealtime(list);
                    }
                        List<ARejectReasonMasterEntity>  video =  rejectReasonMapper.getRejectReasonByType(Constants.AUDIT_CATEGORY_TYPE_VIDEO);
                        if(video != null && video.size() > 0) {
                            List<Reason> list = new LinkedList<Reason>();
                            for (ARejectReasonMasterEntity entity : video) {
                                Reason reason = new Reason();
                                reason.setCode(entity.getReasonId());
                                reason.setCheck(false);
                                reason.setChange(entity.getJoinRefuse());
                                reason.setMessage(entity.getCauseMessage());
                                reason.setType(Constants.AUDIT_CATEGORY_TYPE_VIDEO);
                                list.add(reason);
                            }
                            vo.setVideo(list);
                        }
            return vo;
        } catch (Exception e) {
            throw new DaoException("查询a_reject_reason_master表当前最新版本数据失败", e);
        }
    }
}
