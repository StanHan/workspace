package com.daikuan.platform.ma.vo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.daikuan.platform.ma.bean.ReasonCategoryVO;

/**
 * 
 * @author yangqb
 * @date 2017年3月23日
 * @desc 人工审核提交后向后台传送的数据
 */
public class CommitVO implements Serializable {

    private static final long serialVersionUID = -7925919635150163563L;

    private String applyId;
    private String userId;
    private Integer adminId;
    private String adminName;

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    private Byte auditStatus;
    private List<Integer> reasonIds;
    private Date approveTime;
    private Map<String, ReasonCategoryVO> snapshot;
    private Date claimTime;
    private Date displayTime;
    private Long spendTime;
    private String remark;

    public String getApplyId() {
        return applyId;
    }

    public void setApplyId(String applyId) {
        this.applyId = applyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private RejectReasonsVo rejectReasonsVo;

    public RejectReasonsVo getRejectReasonsVo() {
        return rejectReasonsVo;
    }

    public void setRejectReasonsVo(RejectReasonsVo rejectReasonsVo) {
        this.rejectReasonsVo = rejectReasonsVo;
    }

    private Integer productId;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    private String appId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Map<String, ReasonCategoryVO> getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Map<String, ReasonCategoryVO> snapshot) {
        this.snapshot = snapshot;
    }

    public Date getClaimTime() {
        return claimTime;
    }

    public void setClaimTime(Date claimTime) {
        this.claimTime = claimTime;
    }

    public Date getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(Date displayTime) {
        this.displayTime = displayTime;
    }

    public Long getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(Long spendTime) {
        this.spendTime = spendTime;
    }



    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public Byte getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
    }

    public List<Integer> getReasonIds() {
        return reasonIds;
    }

    public void setReasonIds(List<Integer> reasonIds) {
        this.reasonIds = reasonIds;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    @Override
    public String toString() {
        return "CommitVO [applyId=" + applyId + ", userId=" + userId + ", adminId=" + adminId +",adminName=" + adminName+ ", auditStatus="
                + auditStatus + ", reasonIds=" + reasonIds + ", approveTime=" + approveTime + "]";
    }

}
