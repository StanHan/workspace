/*
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.daikuan.platform.ma.common.response.Response;
import com.daikuan.platform.ma.exception.ReturnMessageException;



/**
 * @author taosj
 * @version GlobalExceptionHandler.java, v0.1 2017年3月7日 上午9:18:18
 */
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private static final String SYSTEM_ERROR = "100";
    private static final String SYSTEM_ERROR_MSG = "系统异常";

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Response<?> defaultErrorHandler(HttpServletRequest req, Exception e) {
        logger.error("system error", e);
        Response<?> response = new Response();
        response.setRtn(SYSTEM_ERROR);
        response.setMessage(SYSTEM_ERROR_MSG);
        return response;
    }

    @ExceptionHandler(value = ReturnMessageException.class)
    @ResponseBody
    public Response<?> defaultReturnErrorHandler(HttpServletRequest req, ReturnMessageException e) {
        Response<?> response = new Response();
        response.setRtn(e.getError().getErrCode());
        response.setMessage(e.getError().getMessage());
        return response;
    }
}

