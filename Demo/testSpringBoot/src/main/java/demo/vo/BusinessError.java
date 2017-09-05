package demo.vo;

public enum BusinessError implements ErrorMsg {
    YES("000","OK"),
    
    NO("001"," NOT OK ")
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
