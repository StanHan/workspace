/*
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package demo.vo;

/**
 * @author hanjy
 * @version ApiResult.java, v0.1 2017年3月13日 下午1:05:53
 */
public class ApiResult<T> {
    private T result;
    private Error error;

    private Boolean success;

    private String errCode;

    private String message;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
