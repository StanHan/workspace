package com.daikuan.platform.ma.common.vo;

/**
 * 拒绝原因
 * @author hanjy
 *
 */
public class RejectReasonVO {
    
    private Integer id;

    private Integer type;

    private String reason;

    private String message;

    private String causeMessage;

    private String simpleReason;

    private Integer status;

    /**
     * 1，不影响审批，不驳回
     */
    private Integer effectReject;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getEffectReject() {
        return effectReject;
    }

    public void setEffectReject(Integer effectReject) {
        this.effectReject = effectReject;
    }

}
