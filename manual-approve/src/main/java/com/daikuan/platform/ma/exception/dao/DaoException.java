package com.daikuan.platform.ma.exception.dao;

/**
 * 
 * @author yangqb
 * @date 2017年3月13日
 * @desc
 */
public class DaoException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = -3668619258348869400L;

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(String message) {
        super(message);
    }
}
