package com.daikuan.platform.ma.pojo;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "a_user_product_audit_send")
public class AUserProductAuditSendEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 申请id apply_id **/
    private Long applyId;

    /** 对应a_user_product_audit的id product_audit_id **/
    private Long productAuditId;

    /** 审核时间 approve_time **/
    private Date approveTime;

    /** 回调状态1: 成功 2:失败 status **/
    private Byte status;

    /** status为2时有值, 错误码 errorcode **/
    private String errorcode;

    /** 错误信息 MESSAGE **/
    private String message;

    /** 创建时间 create_at **/
    private Date createAt;

    /** 更新时间 update_at **/
    private Date updateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductAuditId() {
        return productAuditId;
    }

    public void setProductAuditId(Long productAuditId) {
        this.productAuditId = productAuditId;
    }

    public Date getApproveTime() {
        return approveTime;
    }

    public void setApproveTime(Date approveTime) {
        this.approveTime = approveTime;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    @Override
    public String toString() {
        return "AUserProductAuditSendEntity [id=" + id + ", productAuditId=" + productAuditId + ", approveTime="
                + approveTime + ", status=" + status + ", errorcode=" + errorcode + ", message=" + message
                + ", createAt=" + createAt + ", updateAt=" + updateAt + ", applyId=" + applyId + "]";
    }

}
