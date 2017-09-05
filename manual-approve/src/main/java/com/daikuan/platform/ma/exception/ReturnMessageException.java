/*
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.exception;

/**
 * @author taosj
 * @version ReturnMessgeException.java, v0.1 2017年3月17日 下午6:27:15
 */
public class ReturnMessageException extends RuntimeException {

    /**
     * 业务处理异常
     */
    private static final long serialVersionUID = 1L;

    private ErrorCode error;

    public ReturnMessageException(ErrorCode error) {
        this.error = error;
    }
    
    public ReturnMessageException(ErrorCode error, Throwable e) {
        super(e);
        this.error = error;
    }

    public ErrorCode getError() {
        return error;
    }

    public void setError(ErrorCode error) {
        this.error = error;
    }

}
