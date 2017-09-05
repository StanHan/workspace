package com.daikuan.platform.ma.pojo;

import com.daikuan.platform.ma.exception.ErrorInfo;

/**
 * @author hanjy
 */
public class WsResult<T> {

    private T result;

    private ErrorInfo error;

    public Object getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public ErrorInfo getError() {
        return error;
    }

    public void setError(ErrorInfo error) {
        this.error = error;
    }
}
