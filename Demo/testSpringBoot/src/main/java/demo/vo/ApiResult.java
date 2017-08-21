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

    private ErrorMsg errorMsg;

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(ErrorMsg errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }


}
