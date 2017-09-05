package com.daikuan.platform.ma.common.vo;

/**
 * 
 * @author yangqb
 * @date 2017年3月13日
 * @desc
 */
public class ResponseErrorVO {
    /**
     * 错误码
     */
    private String errCode;
    /**
     * 错误描述
     */
    private String message;

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ResponseErrorVO [errCode=" + errCode + ", message=" + message + "]";
    }

}
