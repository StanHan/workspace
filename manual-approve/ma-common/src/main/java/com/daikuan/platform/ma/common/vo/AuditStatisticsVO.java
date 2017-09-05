/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.common.vo;

/**
 * @author feizy
 * @version AuditStatisticsVO, v0.1 2017/4/20 15:48
 */
public class AuditStatisticsVO extends BaseVO {
    private Integer productId;
    private long claimAmt;
    private long approveAmt;
    private long agreeTaskAmt;
    private long agreeCustomerAmt;
    private long rejectTaskAmt;
    private long rejectCustomerAmt;
    private long autoPassAmt;
    private long autoPassCustomerAmt;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public long getClaimAmt() {
        return claimAmt;
    }

    public void setClaimAmt(long claimAmt) {
        this.claimAmt = claimAmt;
    }

    public long getApproveAmt() {
        return approveAmt;
    }

    public void setApproveAmt(long approveAmt) {
        this.approveAmt = approveAmt;
    }

    public long getAgreeTaskAmt() {
        return agreeTaskAmt;
    }

    public void setAgreeTaskAmt(long agreeTaskAmt) {
        this.agreeTaskAmt = agreeTaskAmt;
    }

    public long getAgreeCustomerAmt() {
        return agreeCustomerAmt;
    }

    public void setAgreeCustomerAmt(long agreeCustomerAmt) {
        this.agreeCustomerAmt = agreeCustomerAmt;
    }

    public long getRejectTaskAmt() {
        return rejectTaskAmt;
    }

    public void setRejectTaskAmt(long rejectTaskAmt) {
        this.rejectTaskAmt = rejectTaskAmt;
    }

    public long getRejectCustomerAmt() {
        return rejectCustomerAmt;
    }

    public void setRejectCustomerAmt(long rejectCustomerAmt) {
        this.rejectCustomerAmt = rejectCustomerAmt;
    }

    public long getAutoPassAmt() {
        return autoPassAmt;
    }

    public void setAutoPassAmt(long autoPassAmt) {
        this.autoPassAmt = autoPassAmt;
    }

    public long getAutoPassCustomerAmt() {
        return autoPassCustomerAmt;
    }

    public void setAutoPassCustomerAmt(long autoPassCustomerAmt) {
        this.autoPassCustomerAmt = autoPassCustomerAmt;
    }

    @Override
    public String toString() {
        return "AuditStatisticsVO{" +
                "productId=" + productId +
                ", claimAmt=" + claimAmt +
                ", approveAmt=" + approveAmt +
                ", agreeTaskAmt=" + agreeTaskAmt +
                ", agreeCustomerAmt=" + agreeCustomerAmt +
                ", rejectTaskAmt=" + rejectTaskAmt +
                ", rejectCustomerAmt=" + rejectCustomerAmt +
                ", autoPassAmt=" + autoPassAmt +
                ", autoPassCustomerAmt=" + autoPassCustomerAmt +
                "} " + super.toString();
    }
}
