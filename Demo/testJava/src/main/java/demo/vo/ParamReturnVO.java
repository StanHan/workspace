package demo.vo;

import java.io.Serializable;
import java.util.Map;

/**
 * 用户参数查询结果VO
 * 
 * @author hanjy
 *
 */
public class ParamReturnVO implements Serializable {
    private static final long serialVersionUID = -3255891212905654545L;
    /**
     * 请求Id
     */
    private String requestId;
    /**
     * 用户标识
     */
    private Long userId;
    /**
     * app 类型
     */
    private Integer appType;
    /**
     * 短信参数码与参数值
     */
    private Map<String, String> codeValueMap;
    /**
     * 短信内容
     */
    private String content;

    /**
     * 错误消息
     */
    private String errorMessage;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getAppType() {
        return appType;
    }

    public void setAppType(Integer appType) {
        this.appType = appType;
    }

    public Map<String, String> getCodeValueMap() {
        return codeValueMap;
    }

    public void setCodeValueMap(Map<String, String> codeValueMap) {
        this.codeValueMap = codeValueMap;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
