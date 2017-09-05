package com.daikuan.platform.ma.exception.service;

/**
 * 
 * @author yangqb
 * @date 2017年3月13日
 * @desc
 */
public class ServiceApplyIdNullException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -8866953442251263291L;

    public ServiceApplyIdNullException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceApplyIdNullException(String message) {
        super(message);
    }

}
