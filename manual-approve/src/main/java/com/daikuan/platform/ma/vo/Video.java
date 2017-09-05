package com.daikuan.platform.ma.vo;

import java.io.Serializable;
import java.util.List;

/** 视频信息 */
public class Video implements Serializable {
    private static final long serialVersionUID = -1233705210615218307L;
    private Integer type;// 错误原因类型
    private String greenPrompt;// 视频已通过， 如果全绿的话, 前台显示的提示信息,
                               // msg为null,则说明自动化审核未通过, 这个标识即表示全绿, 也表示一个绿的提示
    private String url;// 音像资料地址
    private Boolean ocrFlag;// 是否要标红框, 1为标红框 0为不标红框
    private List<Reason> reason;

    private String downloadUrl;

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

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
