package com.daikuan.platform.ma.exception;

import org.springframework.http.HttpStatus;

/**
 * 异常信息
 * @author hanjy
 *
 */
public class ErrorInfo implements ErrorCode {

    private String errCode;
    
    private String message;
    
    public ErrorInfo(String msg, HttpStatus httpStatus) {
        this.errCode = "" + httpStatus.value();
        this.message = msg + " ," +httpStatus.getReasonPhrase();
    }
    
    public ErrorInfo(String errCode, String message) {
        this.errCode = errCode;
        this.message = message;
    }
    
    public ErrorInfo() {
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getErrCode() {
        return this.errCode;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
