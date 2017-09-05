/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.exception;

/**
 * @author feizy
 * @version AutoFilterError, v0.1 2017/3/21 19:59
 */
public enum AutoFilterError implements ErrorCode {
    FAIL_PARSE_AUTORESULT("auto_0001", "反序列化audit_status表中的auto_result异常"),
    REQ_TS_CONTACT_REG_RESPONSE_EXCEP("auto_0002", "请求ts校验联系人正则服务异常"),

    REQ_TS_SAME_CONTACT_REG_RESPONSE_EXCEP("auto_0003", "请求ts校验联系人是否相同的服务异常"),

    REQ_TS_UNITNAME_REG_RESPONSE_EXCEP("auto_0004", "请求ts校验公司名称正则服务异常"),

    REQ_TS_UNITNADDR_REG_RESPONSE_EXCEP("auto_0005", "请求ts校验单位地址正则服务异常"),

    REQ_TS_FAMILY_ADDR_REG_RESPONSE_EXCEP("auto_0006", "请求ts校验家庭地址正则服务异常"),

    REQ_TS_VIDEO_REG_RESPONSE_EXCEP("auto_0007", "请求ts校验视频语音正则服务异常"),

    AUDIT_CATAGORY_TYPE_ILLEGAL("auto_0008", "根据审批项的类型进行自动处理时,类型非法"),

    LAST_AUDIT_FRONT_IC_NULL("auto_0009", "存在之前审批的自动处理结果，但获取身份证人相面的上次结果为空"),

    LAST_AUDIT_FRONT_IC_AND_VIDEO_NULL("auto_0010", "存在之前审批的自动处理结果，但获取身份证人相面或视频的上次结果为空"),

    JUDGE_FRONT_IC_CHANGE_DATA_NULL("auto_0011", "判断身份证人相面信息是否修改时,上次或本次的身份证人相面为空"),

    JUDGE_BACK_IC_CHANGE_DATA_NULL("auto_0012", "判断身份证国徽面信息是否修改时,上次或本次的身份证人相面为空"),

    LAST_AUDIT_BACK_IC_NULL("auto_0013", "存在之前审批的自动处理结果，但获取身份证背面的上次审批结果为空");


    @Override
    public String toString() {
        return "AutoFilterError{" +
                "errCode='" + errCode + '\'' +
                ", message='" + message + '\'' +
                "} " + super.toString();
    }

    private String errCode;
    private String message;

    @Override
    public String getErrCode() {
        return errCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    private AutoFilterError(String errCode, String message) {
        this.errCode = errCode;
        this.message = message;
    }
}
