package com.daikuan.platform.ma.pojo.vo;

import java.io.Serializable;

/**
 * 审批系统审批成功后回调业务系统
 * 
 * @author yangqb
 * @date 2017年3月16日
 * @desc
 */
public class AuditCallbackVO implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 3608738716411978672L;

    private Long userId;

    private Long applyId;

    private Integer productId;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * 1: 成功 2: 失败
     */
    private Byte status;


    private String reasonIds;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getReasonIds() {
        return reasonIds;
    }

    public void setReasonIds(String reasonIds) {
        this.reasonIds = reasonIds;
    }

    @Override
    public String toString() {
        return "AuditCallbackVO [userId=" + userId + ", applyId=" + applyId + ", status=" + status + ", resonIds="
                + reasonIds + "]";
    }

}
