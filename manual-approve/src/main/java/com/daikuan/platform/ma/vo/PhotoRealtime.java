package com.daikuan.platform.ma.vo;

import java.io.Serializable;
import java.util.List;

/** 实时图像 */
public class PhotoRealtime implements Serializable {
    private static final long serialVersionUID = -1837913089584246614L;
    private String greenPrompt; // 人头像照片已通过
    private String url;
    private Boolean ocrFlag;// 是否要标红框, 1为标红框 0为不标红框
    private String oldBos;// 如果用户使用旧版本提交的上行资料规范，则审批页面中头像默认为空，替换为文案提示“旧版本上行，无实时头像”，文案居中显示,
                          // 如果为null, 则按一般处理
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

    public String getOldBos() {
        return oldBos;
    }

    public void setOldBos(String oldBos) {
        this.oldBos = oldBos;
    }

    public List<Reason> getReason() {
        return reason;
    }

    public void setReason(List<Reason> reason) {
        this.reason = reason;
    }
}
