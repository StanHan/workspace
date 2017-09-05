package demo.vo;

/**
 * 统一接口结果封装类
 * @author hanjy
 * @since 2017/08/30
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
