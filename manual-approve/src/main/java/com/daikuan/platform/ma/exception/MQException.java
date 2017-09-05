/*
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.exception;

/**
 * 
 * @author taosj
 * @version MQException.java, v0.1 2017年3月7日 上午9:29:06
 */
public class MQException extends Exception {

    /**
     * MQ 异常
     */
    private static final long serialVersionUID = 1L;

    public MQException() {
        super();
    }

    public MQException(String message, Throwable cause) {
        super(message, cause);
    }
}
