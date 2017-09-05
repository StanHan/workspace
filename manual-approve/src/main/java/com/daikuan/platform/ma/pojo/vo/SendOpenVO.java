package com.daikuan.platform.ma.pojo.vo;

import java.util.Date;

/**
 * 
 * @author yangqb
 * @date 2017年3月20日
 * @desc 送件-开户
 */
public class SendOpenVO {
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
     * 申请人手机号
     */
    private String mobileNo;
    /**
     * 审批时间
     */
    private Date approveTime;

    /**
     * 失败原因
     */
    private String reason;

    /**
     * 申请时间
     */
    private Date applyAt;

    /**
     * 送件时间
     */
    private Date sendAt;

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

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Date getApplyAt() {
        return applyAt;
    }

    public void setApplyAt(Date applyAt) {
        this.applyAt = applyAt;
    }

    public Date getSendAt() {
        return sendAt;
    }

    public void setSendAt(Date sendAt) {
        this.sendAt = sendAt;
    }

    @Override
    public String toString() {
        return "SendOpenVO [applyId=" + applyId + ", userId=" + userId + ", customerName=" + customerName
                + ", mobileNo=" + mobileNo + ", approveTime=" + approveTime + ", reason=" + reason + ", applyAt="
                + applyAt + ", sendAt=" + sendAt + "]";
    }

}
