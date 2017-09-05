package com.daikuan.platform.ma.vo;

import java.io.Serializable;
import java.util.List;

/** 身份证反面 */
public class PhotoBack implements Serializable {
    private static final long serialVersionUID = -3745936230561901876L;
    private Integer type;
    private String greenPrompt;// 身份证反面已通过
    private String url;
    private Boolean ocrFlag;// 是否要标红框, 1为标红框 0为不标红框
    private String oldBoc;// 旧版本提交的中银资料规范，则审批页面中身份证反面默认为空，替换为文案提示“旧版本中银，无身份证反面”，文案居中显示,
                          // 如果为null, 则按一般处理
    private List<Reason> reason;

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

    public String getOldBoc() {
        return oldBoc;
    }

    public void setOldBoc(String oldBoc) {
        this.oldBoc = oldBoc;
    }

    public List<Reason> getReason() {
        return reason;
    }

    public void setReason(List<Reason> reason) {
        this.reason = reason;
    }
}
