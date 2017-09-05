package com.daikuan.platform.ma.vo;

import java.io.Serializable;
import java.util.List;

/** 身份证正面 */
public class PhotoFront implements Serializable {
    private static final long serialVersionUID = -6385136312311900826L;
    private String greenPrompt;// 身份证正面已通过
    private String url;
    private Boolean ocrFlag;// 是否要标红框, 1为标红框 0为不标红框
    private List<Reason> reason;
    private Integer type;

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getOcrFlag() {
        return ocrFlag;
    }

    public void setOcrFlag(Boolean ocrFlag) {
        this.ocrFlag = ocrFlag;
    }

    public List<Reason> getReason() {
        return reason;
    }

    public void setReason(List<Reason> reason) {
        this.reason = reason;
    }
}
