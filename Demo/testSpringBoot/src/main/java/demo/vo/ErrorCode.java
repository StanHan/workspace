package demo.vo;

/**
 * 错误代码及其描述
 * @author hanjy
 *
 */
public class ErrorCode implements ErrorMsg {
    
    private String errCode;
    private String message;

    public ErrorCode() {
        super();
    }

    public ErrorCode(String errCode, String message) {
        this.errCode = errCode;
        this.message = message;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getErrCode() {
        return errCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
