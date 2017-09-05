package com.daikuan.platform.ma.exception.service;

/**
 * 
 * @author yangqb
 * @date 2017年3月13日
 * @desc
 */
public class ServiceRepeatedDataException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = -4904822017243066526L;

    public ServiceRepeatedDataException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceRepeatedDataException(String message) {
        super(message);
    }

}
