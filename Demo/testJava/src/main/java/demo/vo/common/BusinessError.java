package demo.vo.common;

public enum BusinessError implements ErrorMsg {
    NO_DATA("no_data","案件不存在"),
    
    CLAIMED("claimed","案件已审批"),

    NOT_LOGIN("not_login","用户未登录")
    ;

    private String errCode;
    private String message;

    private BusinessError(String errCode, String message) {
        this.errCode = errCode;
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
