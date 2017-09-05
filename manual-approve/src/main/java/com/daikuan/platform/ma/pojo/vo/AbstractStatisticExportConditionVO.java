package com.daikuan.platform.ma.pojo.vo;

import java.util.Date;

/**
 * 
 * @author yangqb
 * @date 2017年3月20日
 * @desc
 */
public abstract class AbstractStatisticExportConditionVO {
    private Integer productId;
    private Date approveTimeStart;
    private Date approveTimeEnd;

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Date getApproveTimeStart() {
        return approveTimeStart;
    }

    public void setApproveTimeStart(Date approveTimeStart) {
        this.approveTimeStart = approveTimeStart;
    }

    public Date getApproveTimeEnd() {
        return approveTimeEnd;
    }

    public void setApproveTimeEnd(Date approveTimeEnd) {
        this.approveTimeEnd = approveTimeEnd;
    }

    @Override
    public String toString() {
        return "AbstractStatisticCondition [productId=" + productId + ", approveTimeStart=" + approveTimeStart
                + ", approveTimeEnd=" + approveTimeEnd + "]";
    }

}
