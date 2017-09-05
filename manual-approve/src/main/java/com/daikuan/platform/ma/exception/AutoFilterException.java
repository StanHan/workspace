/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.exception;

/**
 * @author feizy
 * @version AutoFilterException, v0.1 2017/3/21 20:05
 */
public class AutoFilterException extends Exception {
    private ErrorCode errorCode;

    public AutoFilterException(ErrorCode errorCode) {
        super(errorCode.toString());
        this.errorCode = errorCode;
    }

    public AutoFilterException() {
        super();
    }

    public AutoFilterException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.toString(), cause);
        this.errorCode = errorCode;
    }

    public AutoFilterException(String message) {
        super(message);
    }

    public AutoFilterException(String message, Throwable cause) {
        super(message, cause);
    }

    public AutoFilterException(Throwable cause) {
        super(cause);
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
