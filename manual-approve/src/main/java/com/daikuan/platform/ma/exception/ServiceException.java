/*
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.exception;

/**
 * 业务处理异常
 * 
 * @author taosj
 * @version ServiceException.java, v0.1 2017年3月7日 上午9:29:17
 */
public class ServiceException extends Exception {
    /**
     * 业务处理异常
     */
    private static final long serialVersionUID = 1L;
    /**
     * modified by yangqb
     */
    private String errCode;
    private String message;

    public ServiceException(ErrorInfo errorInfo) {
        this.errCode = errorInfo.getErrCode();
        this.message = errorInfo.getMessage();
    }
    
    public ServiceException(String errCode, String message) {
        super(message);
        this.errCode = errCode;
        this.message = message;
    }

    public ServiceException(String errCode, String message, Throwable cause) {
        super(message, cause);
        this.errCode = errCode;
        this.message = message;
    }

    public String getErrCode() {
        return errCode;
    }

    public String getMessage() {
        return message;
    }
}
