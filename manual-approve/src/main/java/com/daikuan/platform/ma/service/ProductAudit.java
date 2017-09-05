package com.daikuan.platform.ma.service;

import java.util.Date;

import org.apache.ibatis.annotations.Param;

import com.daikuan.platform.ma.exception.ServiceException;
import com.daikuan.platform.ma.pojo.AUserProductAuditStatusEntity;
import com.daikuan.platform.ma.vo.AuditVo;
import com.daikuan.platform.ma.vo.CommitVO;

/**
 * 产品申请服务接口
 * 
 * @author hanjy
 *
 */
public interface ProductAudit {

    /**
     * 指定认领申件，如果applyId为空则为随机认领
     * 
     * @param adminId
     *            申件人ID
     * @param applyId
     *            申件ID
     * @return 申件ID，null:指定认领失败
     */
    public String claim(int adminId, String applyId) throws ServiceException;

    /**
     * 随机认领申件
     * 
     * @param adminId
     *            申件人ID
     * @return 申件ID，null:暂无申件可以领取
     */
    public String claim(int adminId);

    /**
     * 从已认领的队列里拿件,如果已认领的队列里无件可那，则重新申领一件
     * 
     * @param adminId
     * @return
     */
    public String fetchMember(int adminId);

    /**
     * 当放弃操作时重置REDIS状态
     * 
     * @param adminId
     * @param applyId
     */
    public void resetRedisWhenGiveUp(Integer adminId, Long applyId);

    /**
     * 当取消所有认领时重置REDIS状态
     * 
     * 
     * @param adminId
     */
    public void resetRedisWhenCancel(Integer adminId);

    /**
     * 增加操作日志
     * 
     * @param adminId
     * @param applyId
     * @param action：1放弃,2拒绝,3通过
     * @return
     */
    public int addAdminTaskRecord(Integer adminId,String adminName, AuditVo vo, Byte action, Date claimTime, Date displayTime,
            long spendTime);

    /**
     * 从数据库拉数据到REDIS里
     */
    public void initData();

    /**
     * 提交后重置REDIS状态
     * 
     * @param adminId
     * @param applyId
     */
    public void resetRedisWhenSubmit(String adminId, String applyId);

    /**
     * 更新审核状态
     */
    public int updateAuditStatus(CommitVO commitVO);

    /**
     * 申请ID added by yangqb
     * 
     * @param applyId
     * @return
     */
    AUserProductAuditStatusEntity selectAUserProductAuditStatusByApplyId(@Param("applyId") Long applyId)
            throws ServiceException;

    public AuditVo buildAuditVo(AUserProductAuditStatusEntity vo);


    AUserProductAuditStatusEntity getProductAuditStatusByUserId(@Param("userId") Long userId);

}
