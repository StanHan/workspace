package demo.spring.cloud.vo;

/**
 * 对外接口统一返回对象包装类
 *
 * @param <T>
 * @author hanjy
 */
public class ApiResult<T> {

    private int code;
    private String message;
    private T data;

    public ApiResult() {
    }

    public ApiResult(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiResult(T result) {
        if (result == null) {
            this.code = ErrorCode.NO_DATA.getCode();
            this.message = ErrorCode.NO_DATA.getMsg();
        } else {
            this.code = ErrorCode.OK.getCode();
            this.message = ErrorCode.OK.getMsg();
            this.data = result;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
