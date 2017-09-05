package com.daikuan.platform.ma.constant;

/**
 * 
 * @author yangqb
 * @date 2017年3月7日
 */
public interface MAConstant {
    // 用户信息MONGO表
    String MONGO_TAB_REPORT_CACHE = "report_cache";

    // 进件 - 接收失败
    byte RES_RESULT_STATUS_RECEIVE_ERR = 0;
    // 进件 - 接收成功
    byte RES_RESULT_STATUS_RECEIVE_SUC = 1;
    // 审批通过
    byte RES_RESULT_STATUS_AUDIT_SUC = 2;

    // 通用
    byte RES_RESULT_STATUS_SUC = 1;
    byte RES_RESULT_STATUS_ERR = 2;

    // 用户基本信息
    String USER_BASIC_INFO = "USER_BASIC_";

    // 缓存天数
    long REDIS_USER_CACHE_TIME = 30;

    // 开户
    int USER_INFO_ORIGIN_ID_0 = 0;
    // 借款
    int USER_INFO_ORIGIN_ID_1 = 1;

    String DEVICE_TOKEN_IOS = "00000000-0000-0000-0000-000000000000";

    // ios
    int DEVICE_TYPE_1 = 1;

    Byte VALID_STATUS = 1;

    // s_promotions_ios_check 可否推广：不可推广
    int PROMOTIONS_IOS_CHECK_CAN_PROMOTE_0 = 0;
    // s_promotions_ios_check 可否推广：可推广
    int PROMOTIONS_IOS_CHECK_CAN_PROMOTE_1 = 1;

    // s_promotions_ios_check 已注册
    int PROMOTIONS_IOS_CHECK_STATUS_REGISTER = 100;

    // s_promotions_ios_check 打开app
    int PROMOTIONS_IOS_CHECK_STATUS_OPEN = 1;

    // ios 渠道刷量 回调时机 注册后：0
    int PROMOTION_IOS_CHANNEL_CALL_TIME_REGISTER = 0;
    // ios 渠道刷量 回调时机 app启动后：1
    int PROMOTION_IOS_CHANNEL_CALL_TIME_OPEN = 1;

    String MQ_PRO_IOS_CHANNEL_GROUP = "ios_group";
    String MQ_PRO_IOS_CHANNEL_INS_NAME = "ios_ins";
    String MQ_PRO_IOS_CHANNEL_TOPIC = "ios_topic";
    String MQ_PRO_IOS_CHANNEL_TAGS = "ios_tags";

    String IOS_CHANNEL_CALLBACK_SUCCESS = "success";
    // 回调结果 - 失败
    String IOS_CHANNEL_CALLBACK_FAILURE = "failure";
    // 回调结果 - 忽略
    String IOS_CHANNEL_CALLBACK_SKIP = "skip";
    // 回调结果 - 过期
    String IOS_CHANNEL_CALLBACK_EXPIRED = "expired";

    // 第三方API超时
    int THIRD_API_TIME_OUT = 5000;

    // ios渠道推广回调比率
    int IOS_CHANNEL_CALLBACK_SKIP_RATE = 10; // 这年加上了RATE后缀, 以与上面的变量区别
    int IOS_CHANNEL_CALLBACK_MAX = 10;

    // 审批后回调业务接口, 失败后再次重传的间隔时间, 单位秒
    int CALLBACK_RETRY_INTERVAL = 3;

    // 审批后回调业务接口, 失败后最大重传次数
    int CALLBACK_RETRY_FREQUENCY = 3;

    int REJECT_RESON_CURRETN_VERSION = 1;

    // 飘绿 - 全绿
    int WHOLE_GREEN_FLAG_T = 1;
    // 飘绿 - 非全绿
    int WHOLE_GREEN_FLAG_F = 0;

    // 审批结果标志 - 自动
    byte RESULT_TYPE_AUTO = 1;
    // 审批结果标志 - 人工
    byte RESULT_TYPE_MAN = 0;

    // 审核员日志表审核通过标志 1:自动过 0:人工过
    byte AUTO_PASS_A = 1;
    // 审批结果标志 - 人工
    byte AUTO_PASS_M = 0;

    String SEPARATOR_COMMA = ",";

    // 自动化审核通过的adminId
    byte AUTO_ADMIN_ID = -1;

    String REJECT_REASON_MASTER_LIST_KEY = "rejectReasonMasterList";
}
