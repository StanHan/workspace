package com.daikuan.platform.ma.pojo;

import java.util.Date;

public class AAdminTaskEntity {
    /** 编号（自增id, 主键） id **/
    private Long id;

    /** 用户ID user_id **/
    private Long userId;

    /** 申请ID apply_id **/
    private Long applyId;

    /** 产品id product_id **/
    private Integer productId;

    /** 操作员ID admin_id **/
    private Integer adminId;

    /** 审核员的用户名 **/
    private String adminName;

    /** 对应a_user_product_audit的id product_audit_id **/
    private Long productAuditId;

    /** 认领时间（前台拿数据时间） claim_time **/
    private Date claimTime;

    /** （前台显示时时间） display_time **/
    private Date displayTime;

    /** 花费时间，处理时间-开始处理时间 spend_time **/
    private Long spendTime;

    /** 审核员操作类型（1认领，2通过，3拒绝，4放弃） claim_status **/
    private Byte claimStatus;

    /** 处理时间 approve_time **/
    private Date approveTime;

    /**
     * 自动过的标志 1为自动过 0为人工过
     */
    private Byte autoPass;

    /** 创建时间 create_at **/
    private Date createAt;

    /** 最后更新时间 update_at **/
    private Date updateAt;

    /** 编号（自增id, 主键） id **/
    public Long getId() {
        return id;
    }

    /** 编号（自增id, 主键） id **/
    public void setId(Long id) {
        this.id = id;
    }

    /** 申请ID apply_id **/
    public Long getApplyId() {
        return applyId;
    }

    /** 用户ID user_id **/
    public Long getUserId() {
        return userId;
    }

    /** 用户ID user_id **/
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /** 申请ID apply_id **/
    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    /** 产品id product_id **/
    public Integer getProductId() {
        return productId;
    }

    /** 产品id product_id **/
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /** 操作员ID admin_id **/
    public Integer getAdminId() {
        return adminId;
    }

    /** 操作员ID admin_id **/
    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    /** 对应a_user_product_audit的id product_audit_id **/
    public Long getProductAuditId() {
        return productAuditId;
    }

    /** 对应a_user_product_audit的id product_audit_id **/
    public void setProductAuditId(Long productAuditId) {
        this.productAuditId = productAuditId;
    }

    /** 认领时间（前台拿数据时间） claim_time **/
    public Date getClaimTime() {
        return claimTime;
    }

    /** 认领时间（前台拿数据时间） claim_time **/
    public void setClaimTime(Date claimTime) {
        this.claimTime = claimTime;
    }

    /** （前台显示时时间） display_time **/
    public Date getDisplayTime() {
        return displayTime;
    }

    /** （前台显示时时间） display_time **/
    public void setDisplayTime(Date displayTime) {
        this.displayTime = displayTime;
    }

    /** 审核员操作类型（1认领，2通过，3拒绝，4放弃） claim_status **/
    public Byte getClaimStatus() {
        return claimStatus;
    }

    /** 花费时间，处理时间-开始处理时间 spend_time **/
    public Long getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(Long spendTime) {
        this.spendTime = spendTime;
    }

    /** 审核员操作类型（1认领，2通过，3拒绝，4放弃） claim_status **/
    public void setClaimStatus(Byte claimStatus) {
        this.claimStatus = claimStatus;
    }

    /** 处理时间 approve_time **/
    public Date getApproveTime() {
        return approveTime;
    }

    /** 处理时间 approve_time **/
    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public Byte getAutoPass() {
        return autoPass;
    }

    public void setAutoPass(Byte autoPass) {
        this.autoPass = autoPass;
    }

    /** 创建时间 create_at **/
    public Date getCreateAt() {
        return createAt;
    }

    /** 创建时间 create_at **/
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /** 最后更新时间 update_at **/
    public Date getUpdateAt() {
        return updateAt;
    }

    /** 最后更新时间 update_at **/
    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    @Override
    public String toString() {
        return "AAdminTaskEntity{" +
                "id=" + id +
                ", userId=" + userId +
                ", applyId=" + applyId +
                ", productId=" + productId +
                ", adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                ", productAuditId=" + productAuditId +
                ", claimTime=" + claimTime +
                ", displayTime=" + displayTime +
                ", spendTime=" + spendTime +
                ", claimStatus=" + claimStatus +
                ", approveTime=" + approveTime +
                ", autoPass=" + autoPass +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }
}