package com.daikuan.platform.ma.vo;

import java.io.Serializable;
import java.util.List;

/** 家庭地址 */
public class HomeAddr implements Serializable {
    private static final long serialVersionUID = -8897658203873234229L;
    private Integer type;
    private String greenPrompt;
    private List<Info> info;
    private Reason reason;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getGreenPrompt() {
        return greenPrompt;
    }

    public void setGreenPrompt(String greenPrompt) {
        this.greenPrompt = greenPrompt;
    }

    public List<Info> getInfo() {
        return info;
    }

    public void setInfo(List<Info> info) {
        this.info = info;
    }

    public Reason getReason() {
        return reason;
    }

    public void setReason(Reason reason) {
        this.reason = reason;
    }
}
