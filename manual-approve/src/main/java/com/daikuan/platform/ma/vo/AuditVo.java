package com.daikuan.platform.ma.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 待审批对象
 * 
 * @author hanjy
 *
 */
public class AuditVo implements Serializable {

    private static final long serialVersionUID = -7735531075498553356L;
    /** 申请ID，对应status表主键ID */
    private Long applyId;
    /** 用户ID */
    private String  userId;
    /** 用户姓名 */
    private String userName;
    /** 手机号 */
    private String mobileNo;
    /** 申请时间 */
    private Date applyAt;
    /** 申请次数 */
    private Integer applyTimes;
    /** 送件次数 */
    private Integer sendTimes;
    /** 申请产品ID */
    private Integer productId;
    /** 申请产品名字 */
    private String productName;
    /** 产品申请ID */
    private Long productAuditId;
    /** 家庭地址 */
    private HomeAddr homeAddr;
    /** 工作地址 */
    private UnitAddr unitAddr;
    /** 工作单位 */
    private JobUnit jobUnit;
    /** 联系人信息 */
    private Rela rela;
    /** 身份证正面 */
    private PhotoFront photoFront;
    /** 身份证反面 */
    private PhotoBack photoBack;
    /** 实时图像 */
    private PhotoRealtime photoRealtime;
    /** 视频信息 */
    private Video video;

    //推送过来的applyId
    private String appId;

    //性别
    private Integer sex;

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    //审核员已审的案件数量
    private Long approvedNum;

    public Long getApprovedNum() {
        return approvedNum;
    }

    public void setApprovedNum(Long approvedNum) {
        this.approvedNum = approvedNum;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public Date getApplyAt() {
        return applyAt;
    }

    public void setApplyAt(Date applyAt) {
        this.applyAt = applyAt;
    }

    public Integer getApplyTimes() {
        return applyTimes;
    }

    public void setApplyTimes(Integer applyTimes) {
        this.applyTimes = applyTimes;
    }

    public Integer getSendTimes() {
        return sendTimes;
    }

    public void setSendTimes(Integer sendTimes) {
        this.sendTimes = sendTimes;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getProductAuditId() {
        return productAuditId;
    }

    public void setProductAuditId(Long productAuditId) {
        this.productAuditId = productAuditId;
    }

    public HomeAddr getHomeAddr() {
        return homeAddr;
    }

    public void setHomeAddr(HomeAddr homeAddr) {
        this.homeAddr = homeAddr;
    }

    public UnitAddr getUnitAddr() {
        return unitAddr;
    }

    public void setUnitAddr(UnitAddr unitAddr) {
        this.unitAddr = unitAddr;
    }

    public JobUnit getJobUnit() {
        return jobUnit;
    }

    public void setJobUnit(JobUnit jobUnit) {
        this.jobUnit = jobUnit;
    }

    public Rela getRela() {
        return rela;
    }

    public void setRela(Rela rela) {
        this.rela = rela;
    }

    public PhotoFront getPhotoFront() {
        return photoFront;
    }

    public void setPhotoFront(PhotoFront photoFront) {
        this.photoFront = photoFront;
    }

    public PhotoBack getPhotoBack() {
        return photoBack;
    }

    public void setPhotoBack(PhotoBack photoBack) {
        this.photoBack = photoBack;
    }

    public PhotoRealtime getPhotoRealtime() {
        return photoRealtime;
    }

    public void setPhotoRealtime(PhotoRealtime photoRealtime) {
        this.photoRealtime = photoRealtime;
    }

    public Video getVideo() {
        return video;
    }

    public void setVideo(Video video) {
        this.video = video;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
