package com.daikuan.platform.ma.util;

public class Constants {
    public static final String SYSTEM_NAME = "MA";// 微服务的名称
    public static final String VIDEO_FASTDFS_GROUP = "group1";// 微服务的名称

    
    //////////////////// 如下是审核结果状态 常量 /////////////////////////
    /** 0未处理,1放弃,2通过,3拒绝 */
    public static final Byte AUDIT_UNDECIDED = 0;
    /** 0未处理,1放弃,2通过,3拒绝 */
    public static final Byte AUDIT_GIVE_UP = 1;
    /** 0未处理,1放弃,2通过,3拒绝 */
    public static final Byte AUDIT_REFUSE = 3;
    /** 0未处理,1放弃,2通过,3拒绝 */
    public static final Byte AUDIT_PASS = 2;

    //////////////////// 如下是自动审批结果放入MAP的key常量 /////////////////////////
    public static final String KEY_CONTACT = "contact";
    public static final String KEY_UNIT_NAME = "unitname";
    public static final String KEY_FAMILY_ADDR = "familyAddr";
    public static final String KEY_UNIT_ADDR = "unitAddr";
    public static final String KEY_FRONT_IDCARD = "frontIdCard";
    public static final String KEY_BACK_IDCARD = "backIdCard";
    public static final String KEY_HEAD_ICON = "icon";
    public static final String KEY_VIDEO = "video";

    /** 审批的类目 **/
    public static final int AUDIT_CATEGORY_TYPE_CONTACT = 5;// 联系人
    public static final int AUDIT_CATEGORY_TYPE_COMPANY_ADDRESS = 2;// 单位地址
    public static final int AUDIT_CATEGORY_TYPE_COMPANY_NAME = 3;// 公司名称
    public static final int AUDIT_CATEGORY_TYPE_FAMILAY_ADDRESS = 4;// 家庭住址
    public static final int AUDIT_CATEGORY_TYPE_IDCARDFRONT = 6;// 身份证头像面
    public static final int AUDIT_CATEGORY_TYPE_CURRECT_ICON = 7;// 实时头像
    public static final int AUDIT_CATEGORY_TYPE_VIDEO = 8;// 视频
    public static final int AUDIT_CATEGORY_TYPE_IDCARDBACK = 11;// 身份证国徽面

    /** 语音解析类型 **/
    public static final int AUDIO_REG_JUDGE_TYPE_BOC_8 = 8;// 中银视频语音解析
    public static final int AUDIO_REG_JUDGE_TYPE_BOSH_801 = 801;// 上银及小贷等其他产品（非中银）语音解析

    /** 审批拒绝原因id **/
    public static final int AUDIT_REJECT_REASON_CONTACT_ILLEGAL = 41;// 联系人不合规

    public static final int AUDIT_REJECT_REASON_UNIT_NAME = 18;// 公司名称异常

    public static final int AUDIT_REJECT_REASON_UNIT_ADDR = 19;// 单位地址不详

    public static final int AUDIT_REJECT_REASON_FAMILY_ADDR = 20;// 家庭地址不详

    public static final int AUDIT_REJECT_REASON_IC_FRONT_FUZZY = 44;// 身份证不清晰
    public static final int AUDIT_REJECT_REASON_IC_FRONT_NOTFACE = 45;// 身份证非人像面
    public static final int AUDIT_REJECT_REASON_IC_FRONT_NOTORIGINAL = 46;// 身份证非原件或不完整
    public static final int AUDIT_REJECT_REASON_NOTSELF = 57;// 非本人身份证
    public static final int AUDIT_REJECT_REASON_INTENTIONAL_FORGERY = 112;// 蓄意假证

    public static final int AUDIT_REJECT_REASON_HEAD_ICON_UNTRUTHFUL = 47;// 实时头像不真实
    public static final int AUDIT_REJECT_REASON_HEAD_ICON_FUZZY = 48;// 实时头像不清晰
    public static final int AUDIT_REJECT_REASON_HEAD_ICON_NOTSELF = 58;// 非本人头像

    public static final int AUDIT_REJECT_REASON_VIDEO_FUZZY = 49;// 视频人脸不清晰
    public static final int AUDIT_REJECT_REASON_VIDEO_SPEECH_ERROR = 50;// 视频所读内容有误
    public static final int AUDIT_REJECT_REASON_VIDEO_NOT_SPEECH = 51;// 视频未读
    public static final int AUDIT_REJECT_REASON_VIDEO_NOT_MATCH_IC = 52;// 视频与身份证人物不匹配

    public static final int AUDIT_OCR_FACE_MATCH_CURRENT_ICON_SCORE_PASS = 55;// 实时头像和身份证依图人脸比对通过的分数阈值
    public static final int AUDIT_OCR_FACE_MATCH_VIDEO_SCORE_PASS = 45;// 视频截图和身份证依图人脸比对通过的分数阈值

    //////////////////// 如下是审批途径：人工审批或自动审批 /////////////////////////
    public static final byte AUDIT_AUTO = 1;
    public static final byte AUDIT_MANUAL = 2;

    /**
     * 默认的产品分流规则
     */
    public static final String DEFAULT_PRODUCTS_PROPORTION = "1:5:6:8=1:1:1:1";

    public static final String CLAIM_HAS_EXIST = "exist";

    public static final String CLAIM_APPROVED ="approved";


}
