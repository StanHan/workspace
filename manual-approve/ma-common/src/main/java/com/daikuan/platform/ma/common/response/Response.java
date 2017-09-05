/*
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.common.response;

import java.util.List;

/**
 * @author taosj
 * @version Response.java, v0.1 2017年3月15日 下午5:22:51
 */
public class Response<T> {
	public static final String SUCCESS = "00";
	public static final String SUCCESS_MSG = "成功";
	public static final String FAIL = "99";
	public static final String FAIL_MSG = "失败";

	private String rtn;
	private String message;
	// @JsonInclude(Include.NON_NULL)
	private T result;
	private List<T> resultList;

	public Response() {
		this.rtn = SUCCESS;
		this.message = SUCCESS_MSG;
	}

	public Response(String rtn, String message) {
		this.rtn = rtn;
		this.message = message;
	}

	public Response(String rtn, String message, T result) {
		this.rtn = rtn;
		this.message = message;
		this.result = result;
	}

	public String getRtn() {
		return rtn;
	}

	public void setRtn(String rtn) {
		this.rtn = rtn;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public List<T> getResultList() {
		return resultList;
	}

	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
}
