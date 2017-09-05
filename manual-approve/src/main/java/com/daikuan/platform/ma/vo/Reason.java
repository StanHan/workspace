package com.daikuan.platform.ma.vo;

import java.io.Serializable;

/** 页面上显示的拒绝原因 */
public class Reason implements Serializable {
    private static final long serialVersionUID = 7432981021311210443L;
    private Integer code;// 具体错误码
    private String message;// 错误描述, 每一个拒绝原因对应校验通过时, 这个原因要用绿色文字显示通过
    private Boolean check;// 如果自动化审核未通过, 前台显示时, 默认不勾选
    private Byte change;// 审核前台勾选时, 审批按钮是否变为:审核拒绝

    private Integer type;//拒绝类型

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getCheck() {
        return check;
    }

    /**
     * 是否勾选
     */
    public void setCheck(Boolean check) {
        this.check = check;
    }

    public Byte getChange() {
        return change;
    }

    public void setChange(Byte change) {
        this.change = change;
    }

    public Reason() {
    }

    public Reason(Integer code, String message, Boolean check, Byte change) {
        super();
        this.code = code;
        this.message = message;
        this.check = check;
        this.change = change;
    }

}