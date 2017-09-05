package com.daikuan.platform.ma.pojo;

import java.util.Date;

public class AUserProductAuditStatusEntity {
    /** 编号（自增id, 主键） id **/
    private Long id;

    /** 用户ID user_id **/
    private Long userId;

    /** 申请id apply_id **/
    private Long applyId;

    /** 产品id product_id **/
    private Integer productId;

    /** 操作员ID admin_id **/
    private Integer adminId;

    /** 审批的状态(0待认领，1已认领，2通过，3拒绝) audit_status **/
    private Byte auditStatus;

    /** 对应a_user_product_audit的id product_audit_id **/
    private Long productAuditId;

    /** 失败原因ID reason_ids **/
    private String reasonIds;

    /** 版本号 reason_version **/
    private String reasonVersion;

    /** 处理时间 approve_time **/
    private Date approveTime;

    /** 审批结果标志1自动，2人工 result_type **/
    private Byte resultType;

    /** 备注 audit_remarks **/
    private String auditRemarks;

    /** 创建时间 create_at **/
    private Date createAt;

    /** 最后更新时间 update_at **/
    private Date updateAt;

    /** 失败原因 reason **/
    private String reason;

    /** 自动化飘绿结果 auto_green **/
    private String autoGreen;

    /** 人工飘绿结果 manual_green **/
    private String manualGreen;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
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

    public Long getProductAuditId() {
        return productAuditId;
    }

    public void setProductAuditId(Long productAuditId) {
        this.productAuditId = productAuditId;
    }

    public String getReasonIds() {
        return reasonIds;
    }

    public void setReasonIds(String reasonIds) {
        this.reasonIds = reasonIds;
    }

    public String getReasonVersion() {
        return reasonVersion;
    }

    public void setReasonVersion(String reasonVersion) {
        this.reasonVersion = reasonVersion;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public Byte getResultType() {
        return resultType;
    }

    public void setResultType(Byte resultType) {
        this.resultType = resultType;
    }

    public String getAuditRemarks() {
        return auditRemarks;
    }

    public void setAuditRemarks(String auditRemarks) {
        this.auditRemarks = auditRemarks;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAutoGreen() {
        return autoGreen;
    }

    public void setAutoGreen(String autoGreen) {
        this.autoGreen = autoGreen;
    }

    public String getManualGreen() {
        return manualGreen;
    }

    public void setManualGreen(String manualGreen) {
        this.manualGreen = manualGreen;
    }

    @Override
    public String toString() {
        return "AUserProductAuditStatusEntity [id=" + id + ", userId=" + userId + ", applyId=" + applyId
                + ", productId=" + productId + ", adminId=" + adminId + ", auditStatus=" + auditStatus
                + ", productAuditId=" + productAuditId + ", reasonIds=" + reasonIds + ", reasonVersion=" + reasonVersion
                + ", approveTime=" + approveTime + ", resultType=" + resultType + ", auditRemarks=" + auditRemarks
                + ", createAt=" + createAt + ", updateAt=" + updateAt + ", reason=" + reason + ", autoGreen="
                + autoGreen + ", manualGreen=" + manualGreen + "]";
    }
}