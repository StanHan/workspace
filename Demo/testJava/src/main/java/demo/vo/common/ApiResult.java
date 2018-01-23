package demo.vo.common;

/**
 * 统一接口结果封装类
 * @author hanjy
 * @since 2017/08/30
 */
public class ApiResult<T> {
    
    private T data;

    private ErrorCode errorMsg;

    public ApiResult() {
    }

    public ApiResult(T data, ErrorMsg errorMsg) {
        this.data = data;
        if (errorMsg != null) {
            this.errorMsg = new ErrorCode(errorMsg.getErrCode(), errorMsg.getMessage());
        }
    }

    public ErrorMsg getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(ErrorCode errorMsg) {
        this.errorMsg = errorMsg;
    }

    public T getResult() {
        return data;
    }

    public void setResult(T result) {
        this.data = result;
    }
}
