package com.daikuan.platform.ma.util;

public class RedisKey {

    //////////////////// 如下是REDIS KEY 常量 /////////////////////////
    public static final String KEY_ADMIN_AUDIT = "ma_admin_audit";

    /** 审核员MAP，field为审核员ID，value为该审核员拿的审核件ID,多个审核件以分号分隔，如id1;id2...idn */
    public static final String KEY_AUDIT_AUDITOR_MAP = "ma_audit_auditor_map";

    /** 缓存的申件对象KEY值前缀 */
    public static final String KEY_USER_PRODUCT_AUDIT = "ma_product_auditor_";

    /** SET，key为adminId,value为被该adminID加载的a_product_audit_status表主键id */
    public static final String SET_LOADED_ = "ma_loaded_adminId_";

    /** SET,key:被放弃的ID,即a_user_product_audit_status的主键,value:放弃过该applyId的adminId */
    public static final String SET_GIVE_UP_ = "ma_give_up_applyId_";

    /** SET，value:里面缓存着一定数量的待审批的审批进件表的ID,或按照产品比例分配或按产品优先级分配 */
    public static final String SET_MIXED = "ma_audit_mixed_set";

    /** 当前坐席前缀，存放某审核员加载过的申件VO */
    public static final String KEY_LOADED_APP_ = "ma_loaded_object_";

    /** LIST，key:adminId,value:申领过但还未处理的a_product_audit_status表主键id */
    public static final String LIST_CLAIMED_ = "ma_claimed_adminId_";

    /**
     * SET:key为产品类型，value为审批集合，里面缓存着一定数量的 某一产品类型的 待审批的ID,即a_user_product_audit_status的主键
     */
    public static final String SET_PRODUCT_ = "ma_audit_product_";
    /**
     * 产品比例，如："1:5:6=1:1:1"，等号左边是产品id,等号右边是产品的比例
     */
    public static final String KEY_PRODUCT_PROPORTION = "ma_product_proportion";
}
