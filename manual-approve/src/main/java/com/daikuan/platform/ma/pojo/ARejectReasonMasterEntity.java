package com.daikuan.platform.ma.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author yangqb
 * @date 2017年3月14日
 * @desc 只保存最新版本的数据
 */
@Table(name = "a_reject_reason_master")
public class ARejectReasonMasterEntity implements Serializable {
    /**
     * 保存到redis中需要序列化
     */
    private static final long serialVersionUID = 8106231341249062740L;

    // 确保各属性为驼峰形式
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /** reason_id **/
    private Integer reasonId;

    /** type **/
    private Byte type;

    /** version **/
    private String version;

    /** cause_message **/
    private String causeMessage;

    /** status **/
    private Byte status;

    /** create_at **/
    private Date createAt;

    private Byte joinRefuse;

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

    public String getCauseMessage() {
        return causeMessage;
    }

    public void setCauseMessage(String causeMessage) {
        this.causeMessage = causeMessage;
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

    public Byte getJoinRefuse() {
        return joinRefuse;
    }

    public void setJoinRefuse(Byte joinRefuse) {
        this.joinRefuse = joinRefuse;
    }

    @Override
    public String toString() {
        return "ARejectReasonMasterEntity [id=" + id + ", reasonId=" + reasonId + ", type=" + type + ", version="
                + version + ", causeMessage=" + causeMessage + ", status=" + status + ", createAt=" + createAt
                + ", joinRefuse=" + joinRefuse + "]";
    }

}
