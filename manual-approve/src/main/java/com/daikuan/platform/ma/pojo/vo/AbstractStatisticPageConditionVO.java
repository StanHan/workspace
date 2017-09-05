package com.daikuan.platform.ma.pojo.vo;

import java.util.Date;

/**
 * 
 * @author yangqb
 * @date 2017年3月20日
 * @desc
 */
public abstract class AbstractStatisticPageConditionVO {
    private Integer productId;
    private Date approveTimeStart;
    private Date approveTimeEnd;
    private int pageSize;
    private int pageNum;

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

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    @Override
    public String toString() {
        return "AbstractStatisticPageCondition [productId=" + productId + ", approveTimeStart=" + approveTimeStart
                + ", approveTimeEnd=" + approveTimeEnd + ", pageSize=" + pageSize + ", pageNum=" + pageNum + "]";
    }

}
