package com.daikuan.platform.ma.pojo;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author yangqb
 * @date 2017年3月14日
 * @desc 接收上游推送过来的数据, 原样保存
 */
@Table(name = "a_reject_reason_master_his")
public class ARejectReasonMasterHisEntity {
    // 确保各属性为驼峰形式
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** 拒绝原因类型 reason_id **/
    private Integer reasonId;

    /** 具体拒绝原因编码 type **/
    private Byte type;

    /** 版本号 version **/
    private String version;

    /** 提示信息 MESSAGE **/
    private String message;

    /** 拒绝原因 cause_message **/
    private String causeMessage;

    /** 拒绝原因简述 simple_reason **/
    private String simpleReason;

    /** 默认1, 0表示已删除 status **/
    private Byte status;

    /** 创建时间 create_at **/
    private Date createAt;

    /** 最后更新时间 update_at **/
    private Date updateAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReasonId() {
        return reasonId;
    }

    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCauseMessage() {
        return causeMessage;
    }

    public void setCauseMessage(String causeMessage) {
        this.causeMessage = causeMessage;
    }

    public String getSimpleReason() {
        return simpleReason;
    }

    public void setSimpleReason(String simpleReason) {
        this.simpleReason = simpleReason;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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

    @Override
    public String toString() {
        return "ARejectReasonMasterHisEntity [id=" + id + ", reasonId=" + reasonId + ", type=" + type + ", version="
                + version + ", message=" + message + ", causeMessage=" + causeMessage + ", simpleReason=" + simpleReason
                + ", status=" + status + ", createAt=" + createAt + ", updateAt=" + updateAt + "]";
    }

}
