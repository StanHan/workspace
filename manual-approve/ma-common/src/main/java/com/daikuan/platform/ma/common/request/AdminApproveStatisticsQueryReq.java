/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.common.request;

/**
 * @author feizy
 * @version AdminApproveStatisticsQueryReq, v0.1 2017/4/20 15:44
 */
public class AdminApproveStatisticsQueryReq {
    private Integer productId;
    private Integer type;
    private Long auditDate;
    private Long adminId;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getAuditDate() {
        return auditDate;
    }

    public void setAuditDate(Long auditDate) {
        this.auditDate = auditDate;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    @Override
    public String toString() {
        return "AdminApproveStatisticsQueryReq{" +
                "productId=" + productId +
                ", type=" + type +
                ", auditDate=" + auditDate +
                ", adminId=" + adminId +
                '}';
    }
}
