package demo.spring.cloud.vo;

/**
 * 错误码枚举类
 * 
 * @author hanjy
 *
 */
public enum ErrorCode {
    /** 正常 */
    OK(0, "OK"),
    /** 没有数据 */
    NO_DATA(1, "NO DATA"),
    /** 系统异常 */
    SYSTEM_ERROR(2, "SYSTEM ERROR: "),
    /** 无效的客户端 */
    CLIENT_ERROR(3, "INVALID CLIENT"),
    /** 签名认证失败 */
    SIGN_ERROR(4, "INVALID SIGN"),
    /** 参数错误 */
    PARAM_ERROR(5, "参数错误"),
    /** 远程调用异常 */
    RPC_ERROR(6, "远程调用异常：");

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    private ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
