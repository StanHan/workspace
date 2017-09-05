/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.bean;

/**
 * @author feizy
 * @version ResponseVO, v0.1 2017/3/16 9:16
 */
public class ResponseVO<T> {
    public static final String REQUESTION_SUCCESS_CODE = "000";// 请求成功
    public static final String REQUESTION_SUCCESS_DESC = "请求成功";// 请求成功描述
    public static final String REQUESTION_ERROR_CODE = "100";// 请求失败的错误码
    public static final String REQUESTION_ERROR_DESC = "请求失败";// 请求失败的描述
    private String result;
    private String desc;
    private T body;

    public ResponseVO(String result, String desc) {
        this.result = result;
        this.desc = desc;
    }

    public ResponseVO() {
        this.result = REQUESTION_SUCCESS_CODE;
        this.desc = REQUESTION_SUCCESS_DESC;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "ResponseVO{" + "result='" + result + '\'' + ", desc='" + desc + '\'' + ", body=" + body + '}';
    }
}
