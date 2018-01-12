package demo.vo;

/**
 * 推送服务消息
 * 
 * @author hanjy
 *
 */
public class PnsMsg {

    /**
     * 消息ID
     */
    private Long msgId;
    /**
     * userid
     */
    private Long userId;
    /**
     * 产品
     */
    private Byte appType;
    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 1 调起APP 2 打开webview 3 前往个人中心 4 前往免息券分享页面 5 前往免息券邀请页面
     */
    private Byte action;

    /**
     * 点击后动作链接
     */
    private String actionUrl;

    /**
     * 回调接口
     */
    private String callbackUrl;
    public static final String URL_CALLBACK = "/restfulservice/pnsTaskService/result?msgId=%s&rtnCode=";

    public PnsMsg(Long msgId, Long userId, Byte appType, String title, String content, Byte action, String actionUrl,
            String localHostUrl) {
        super();
        this.msgId = msgId;
        this.userId = userId;
        this.appType = appType;
        this.title = title;
        this.content = content;
        this.action = action;
        this.actionUrl = actionUrl;
        this.callbackUrl = localHostUrl + String.format(URL_CALLBACK, msgId);
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Long getMsgId() {
        return msgId;
    }

    public void setMsgId(Long msgId) {
        this.msgId = msgId;
    }

    public PnsMsg() {
        super();
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getAppType() {
        return appType;
    }

    public void setAppType(Byte appType) {
        this.appType = appType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Byte getAction() {
        return action;
    }

    public void setAction(Byte action) {
        this.action = action;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

}
