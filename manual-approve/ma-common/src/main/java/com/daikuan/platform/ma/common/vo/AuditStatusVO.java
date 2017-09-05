/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.common.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author feizy
 * @version AuditStatusVO, v0.1 2017/4/20 14:31
 */
public class AuditStatusVO extends BaseVO implements Serializable {
    private Long userId;
    private Long productAuditStatusId;
    private Long productAuditId;
    private String customerName;
    private String phone;//手机号
    private Integer status;//审核状态
    private String idcd;//身份证号
    private Date applyTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductAuditStatusId() {
        return productAuditStatusId;
    }

    public void setProductAuditStatusId(Long productAuditStatusId) {
        this.productAuditStatusId = productAuditStatusId;
    }

    public Long getProductAuditId() {
        return productAuditId;
    }

    public void setProductAuditId(Long productAuditId) {
        this.productAuditId = productAuditId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getIdcd() {
        return idcd;
    }

    public void setIdcd(String idcd) {
        this.idcd = idcd;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    @Override
    public String toString() {
        return "AuditStatusVO{" +
                "userId=" + userId +
                ", productAuditStatusId=" + productAuditStatusId +
                ", productAuditId=" + productAuditId +
                ", customerName='" + customerName + '\'' +
                ", phone='" + phone + '\'' +
                ", status=" + status +
                ", idcd='" + idcd + '\'' +
                ", applyTime=" + applyTime +
                "} " + super.toString();
    }
}
