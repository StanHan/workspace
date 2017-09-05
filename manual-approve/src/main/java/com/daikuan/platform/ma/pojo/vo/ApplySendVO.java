package com.daikuan.platform.ma.pojo.vo;

import java.util.Date;

/**
 * 
 * @author yangqb
 * @date 2017年3月20日
 * @desc 申请送件
 */
public class ApplySendVO {

    /**
     * 申请ID
     */
    private Long applyId;

    /**
     * 申请人ID
     */
    private Long userId;

    /**
     * 申请人姓名
     */
    private String customerName;

    /**
     * 申请时间
     */
    private Date applyAt;

    /**
     * 审批时间
     */
    private Date approveTime;

    /**
     * 申请人手机号
     */
    private String mobileNo;

    /**
     * 被拒绝原因
     */
    private String reason;

    /**
     * 被拒原因数量
     */
    private Short reasonCount;

    /**
     * 能否再次申请
     */
    private Short applyAgain;

    /**
     * 备注信息
     */
    private String auditRemarks;

    /**
     * 有无人行
     */
    private Short creditType;

    /**
     * 审核员姓名
     */
    private String username;

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getApplyAt() {
        return applyAt;
    }

    public void setApplyAt(Date applyAt) {
        this.applyAt = applyAt;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Short getReasonCount() {
        return reasonCount;
    }

    public void setReasonCount(Short reasonCount) {
        this.reasonCount = reasonCount;
    }

    public Short getApplyAgain() {
        return applyAgain;
    }

    public void setApplyAgain(Short applyAgain) {
        this.applyAgain = applyAgain;
    }

    public String getAuditRemarks() {
        return auditRemarks;
    }

    public void setAuditRemarks(String auditRemarks) {
        this.auditRemarks = auditRemarks;
    }

    public Short getCreditType() {
        return creditType;
    }

    public void setCreditType(Short creditType) {
        this.creditType = creditType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "ApplySendVO [applyId=" + applyId + ", userId=" + userId + ", customerName=" + customerName
                + ", applyAt=" + applyAt + ", approveTime=" + approveTime + ", mobileNo=" + mobileNo + ", reason="
                + reason + ", reasonCount=" + reasonCount + ", applyAgain=" + applyAgain + ", auditRemarks="
                + auditRemarks + ", creditType=" + creditType + ", username=" + username + "]";
    }

}
