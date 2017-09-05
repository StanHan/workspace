package com.daikuan.platform.ma.vo;

import java.io.Serializable;
import java.util.List;

/** 联系人信息 */
public class Rela implements Serializable {
    private static final long serialVersionUID = 8952476531904896550L;
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
