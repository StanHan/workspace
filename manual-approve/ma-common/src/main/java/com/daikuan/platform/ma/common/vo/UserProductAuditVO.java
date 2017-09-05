/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.common.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author feizy
 * @version UserProductAuditVO, v0.1 2017/6/27 17:05
 */
public class UserProductAuditVO implements Serializable {
    private static final long serialVersionUID = 6192378989163335853L;
    /** 编号（自增id, 主键） id **/
    private Long id;

    /** 用户ID user_id **/
    private Long userId;

    /** 申请id apply_id **/
    private Long applyId;

    /** 身份证号 id_no **/
    private String idNo;

    /** 客户姓名 customer_name **/
    private String customerName;

    /** 手机号 mobile_no **/
    private String mobileNo;

    /** 住宅地址（省） home_province **/
    private String homeProvince;

    /** 住宅地址（市） home_city **/
    private String homeCity;

    /** 住宅地址（详细） home_addr **/
    private String homeAddr;

    /** 住宅地址（区） home_district **/
    private String homeDistrict;

    /** 单位名称 job_unit **/
    private String jobUnit;

    /** 单位地址（省） unit_province **/
    private String unitProvince;

    /** 单位地址（市） unit_city **/
    private String unitCity;

    /** 单位地址（详细） unit_addr **/
    private String unitAddr;

    /** 单位地址（区） units_district **/
    private String unitsDistrict;

    /** 单位电话（区号） unit_tel_area **/
    private String unitTelArea;

    /** 单位电话 unit_tel_no **/
    private String unitTelNo;

    /** 单位电话（分机号） unit_tel_ext **/
    private String unitTelExt;

    /** 联系人姓名（家人） rela_family **/
    private String relaFamily;

    /** 联系人手机号（家人） rela_family_tel **/
    private String relaFamilyTel;

    /** 联系人姓名（同事） rela_colleague **/
    private String relaColleague;

    /** 联系人手机号（同事） rela_colleague_tel **/
    private String relaColleagueTel;

    /** 联系人姓名（朋友） rela_friend **/
    private String relaFriend;

    /** 联系人手机号（朋友） rela_friend_tel **/
    private String relaFriendTel;

    /**
     * fastDFS 文件分组
     */
    private String groupName;

    /** 身份证正面保存路径 photo_front **/
    private String photoFront;

    /** 身份证反面保存路径 photo_back **/
    private String photoBack;

    /** 实时头像 photo_real_time **/
    private String photoRealTime;

    /** 10秒短视频 video **/
    private String video;

    /** 申请时间 Apply_at **/
    private Date applyAt;

    /** 进件类型 audit_type **/
    private Byte auditType;

    /** 有无人行 credit_type **/
    private Byte creditType;

    /** 实时头像人脸比对分数 face_score_icon **/
    private Double faceScoreIcon;

    /** 视频人脸比对分数 face_score_video **/
    private Double faceScoreVideo;

    /** 产品id product_id **/
    private Integer productId;

    /** 身份证国徽面国籍 ic_emblem_nationality **/
    private String icEmblemNationality;

    /** 身份证国徽面证件类型 ic_emblem_type **/
    private String icEmblemType;

    /** 身份证国徽面签发机关 ic_emblem_issuer **/
    private String icEmblemIssuer;

    /** 身份证国徽面有效期起始日 ic_emblem_valid_start **/
    private String icEmblemValidStart;

    /** 身份证国徽面有效期截止 ic_emblem_valid_end **/
    private String icEmblemValidEnd;

    /** 身份证人像面姓名 ic_portrait_name **/
    private String icPortraitName;

    /** 身份证人像面性别 ic_portrait_gender **/
    private Byte icPortraitGender;

    /** 身份证人像面民族 ic_portrait_ethnicity **/
    private Byte icPortraitEthnicity;

    /** 身份证人像面出生年月 ic_portrait_birthday **/
    private String icPortraitBirthday;

    /** 身份证人像面住址 ic_portrait_address **/
    private String icPortraitAddress;

    /** 身份证人像面公民身份号码 ic_portrait_no **/
    private String icPortraitNo;

    /** 申请次数 Apply_Times **/
    private Byte applyTimes;

    /** 送件次数 Send_times **/
    private Byte sendTimes;

    /** 旧版上行规范1: 是 0: 否 old_boc **/
    private Byte oldBoc;

    /** 旧版中行规范1: 是 0: 否 old_bos **/
    private Byte oldBos;

    /** 创建时间 create_at MA系统入库时间**/
    private Date createAt;

    /** 最后更新时间 update_at **/
    private Date updateAt;

    /** 默认0，0表示身份证人相面ocr不过，1表示身份证人相面ocr过 */
    private Byte icPortraitOcrPass;

    /**
     * ftp 配置信息
     */
    private String ftpConfigJson;

    /** 身份证人像面性别 ic_portrait_gender 字面描述 **/
    private String icPortraitGenderName;

    /** 身份证人像面民族 ic_portrait_ethnicity 字面描述 **/
    private String icPortraitEthnicityName;

    @Override
    public String toString() {
        return "UserProductAuditVO{" +
                "id=" + id +
                ", userId=" + userId +
                ", applyId=" + applyId +
                ", idNo='" + idNo + '\'' +
                ", customerName='" + customerName + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", homeProvince='" + homeProvince + '\'' +
                ", homeCity='" + homeCity + '\'' +
                ", homeAddr='" + homeAddr + '\'' +
                ", homeDistrict='" + homeDistrict + '\'' +
                ", jobUnit='" + jobUnit + '\'' +
                ", unitProvince='" + unitProvince + '\'' +
                ", unitCity='" + unitCity + '\'' +
                ", unitAddr='" + unitAddr + '\'' +
                ", unitsDistrict='" + unitsDistrict + '\'' +
                ", unitTelArea='" + unitTelArea + '\'' +
                ", unitTelNo='" + unitTelNo + '\'' +
                ", unitTelExt='" + unitTelExt + '\'' +
                ", relaFamily='" + relaFamily + '\'' +
                ", relaFamilyTel='" + relaFamilyTel + '\'' +
                ", relaColleague='" + relaColleague + '\'' +
                ", relaColleagueTel='" + relaColleagueTel + '\'' +
                ", relaFriend='" + relaFriend + '\'' +
                ", relaFriendTel='" + relaFriendTel + '\'' +
                ", groupName='" + groupName + '\'' +
                ", photoFront='" + photoFront + '\'' +
                ", photoBack='" + photoBack + '\'' +
                ", photoRealTime='" + photoRealTime + '\'' +
                ", video='" + video + '\'' +
                ", applyAt=" + applyAt +
                ", auditType=" + auditType +
                ", creditType=" + creditType +
                ", faceScoreIcon=" + faceScoreIcon +
                ", faceScoreVideo=" + faceScoreVideo +
                ", productId=" + productId +
                ", icEmblemNationality='" + icEmblemNationality + '\'' +
                ", icEmblemType='" + icEmblemType + '\'' +
                ", icEmblemIssuer='" + icEmblemIssuer + '\'' +
                ", icEmblemValidStart='" + icEmblemValidStart + '\'' +
                ", icEmblemValidEnd='" + icEmblemValidEnd + '\'' +
                ", icPortraitName='" + icPortraitName + '\'' +
                ", icPortraitGender=" + icPortraitGender +
                ", icPortraitEthnicity=" + icPortraitEthnicity +
                ", icPortraitBirthday='" + icPortraitBirthday + '\'' +
                ", icPortraitAddress='" + icPortraitAddress + '\'' +
                ", icPortraitNo='" + icPortraitNo + '\'' +
                ", applyTimes=" + applyTimes +
                ", sendTimes=" + sendTimes +
                ", oldBoc=" + oldBoc +
                ", oldBos=" + oldBos +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                ", icPortraitOcrPass=" + icPortraitOcrPass +
                ", ftpConfigJson='" + ftpConfigJson + '\'' +
                ", icPortraitGenderName='" + icPortraitGenderName + '\'' +
                ", icPortraitEthnicityName='" + icPortraitEthnicityName + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getHomeProvince() {
        return homeProvince;
    }

    public void setHomeProvince(String homeProvince) {
        this.homeProvince = homeProvince;
    }

    public String getHomeCity() {
        return homeCity;
    }

    public void setHomeCity(String homeCity) {
        this.homeCity = homeCity;
    }

    public String getHomeAddr() {
        return homeAddr;
    }

    public void setHomeAddr(String homeAddr) {
        this.homeAddr = homeAddr;
    }

    public String getHomeDistrict() {
        return homeDistrict;
    }

    public void setHomeDistrict(String homeDistrict) {
        this.homeDistrict = homeDistrict;
    }

    public String getJobUnit() {
        return jobUnit;
    }

    public void setJobUnit(String jobUnit) {
        this.jobUnit = jobUnit;
    }

    public String getUnitProvince() {
        return unitProvince;
    }

    public void setUnitProvince(String unitProvince) {
        this.unitProvince = unitProvince;
    }

    public String getUnitCity() {
        return unitCity;
    }

    public void setUnitCity(String unitCity) {
        this.unitCity = unitCity;
    }

    public String getUnitAddr() {
        return unitAddr;
    }

    public void setUnitAddr(String unitAddr) {
        this.unitAddr = unitAddr;
    }

    public String getUnitsDistrict() {
        return unitsDistrict;
    }

    public void setUnitsDistrict(String unitsDistrict) {
        this.unitsDistrict = unitsDistrict;
    }

    public String getUnitTelArea() {
        return unitTelArea;
    }

    public void setUnitTelArea(String unitTelArea) {
        this.unitTelArea = unitTelArea;
    }

    public String getUnitTelNo() {
        return unitTelNo;
    }

    public void setUnitTelNo(String unitTelNo) {
        this.unitTelNo = unitTelNo;
    }

    public String getUnitTelExt() {
        return unitTelExt;
    }

    public void setUnitTelExt(String unitTelExt) {
        this.unitTelExt = unitTelExt;
    }

    public String getRelaFamily() {
        return relaFamily;
    }

    public void setRelaFamily(String relaFamily) {
        this.relaFamily = relaFamily;
    }

    public String getRelaFamilyTel() {
        return relaFamilyTel;
    }

    public void setRelaFamilyTel(String relaFamilyTel) {
        this.relaFamilyTel = relaFamilyTel;
    }

    public String getRelaColleague() {
        return relaColleague;
    }

    public void setRelaColleague(String relaColleague) {
        this.relaColleague = relaColleague;
    }

    public String getRelaColleagueTel() {
        return relaColleagueTel;
    }

    public void setRelaColleagueTel(String relaColleagueTel) {
        this.relaColleagueTel = relaColleagueTel;
    }

    public String getRelaFriend() {
        return relaFriend;
    }

    public void setRelaFriend(String relaFriend) {
        this.relaFriend = relaFriend;
    }

    public String getRelaFriendTel() {
        return relaFriendTel;
    }

    public void setRelaFriendTel(String relaFriendTel) {
        this.relaFriendTel = relaFriendTel;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getPhotoFront() {
        return photoFront;
    }

    public void setPhotoFront(String photoFront) {
        this.photoFront = photoFront;
    }

    public String getPhotoBack() {
        return photoBack;
    }

    public void setPhotoBack(String photoBack) {
        this.photoBack = photoBack;
    }

    public String getPhotoRealTime() {
        return photoRealTime;
    }

    public void setPhotoRealTime(String photoRealTime) {
        this.photoRealTime = photoRealTime;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public Date getApplyAt() {
        return applyAt;
    }

    public void setApplyAt(Date applyAt) {
        this.applyAt = applyAt;
    }

    public Byte getAuditType() {
        return auditType;
    }

    public void setAuditType(Byte auditType) {
        this.auditType = auditType;
    }

    public Byte getCreditType() {
        return creditType;
    }

    public void setCreditType(Byte creditType) {
        this.creditType = creditType;
    }

    public Double getFaceScoreIcon() {
        return faceScoreIcon;
    }

    public void setFaceScoreIcon(Double faceScoreIcon) {
        this.faceScoreIcon = faceScoreIcon;
    }

    public Double getFaceScoreVideo() {
        return faceScoreVideo;
    }

    public void setFaceScoreVideo(Double faceScoreVideo) {
        this.faceScoreVideo = faceScoreVideo;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getIcEmblemNationality() {
        return icEmblemNationality;
    }

    public void setIcEmblemNationality(String icEmblemNationality) {
        this.icEmblemNationality = icEmblemNationality;
    }

    public String getIcEmblemType() {
        return icEmblemType;
    }

    public void setIcEmblemType(String icEmblemType) {
        this.icEmblemType = icEmblemType;
    }

    public String getIcEmblemIssuer() {
        return icEmblemIssuer;
    }

    public void setIcEmblemIssuer(String icEmblemIssuer) {
        this.icEmblemIssuer = icEmblemIssuer;
    }

    public String getIcEmblemValidStart() {
        return icEmblemValidStart;
    }

    public void setIcEmblemValidStart(String icEmblemValidStart) {
        this.icEmblemValidStart = icEmblemValidStart;
    }

    public String getIcEmblemValidEnd() {
        return icEmblemValidEnd;
    }

    public void setIcEmblemValidEnd(String icEmblemValidEnd) {
        this.icEmblemValidEnd = icEmblemValidEnd;
    }

    public String getIcPortraitName() {
        return icPortraitName;
    }

    public void setIcPortraitName(String icPortraitName) {
        this.icPortraitName = icPortraitName;
    }

    public Byte getIcPortraitGender() {
        return icPortraitGender;
    }

    public void setIcPortraitGender(Byte icPortraitGender) {
        this.icPortraitGender = icPortraitGender;
    }

    public Byte getIcPortraitEthnicity() {
        return icPortraitEthnicity;
    }

    public void setIcPortraitEthnicity(Byte icPortraitEthnicity) {
        this.icPortraitEthnicity = icPortraitEthnicity;
    }

    public String getIcPortraitBirthday() {
        return icPortraitBirthday;
    }

    public void setIcPortraitBirthday(String icPortraitBirthday) {
        this.icPortraitBirthday = icPortraitBirthday;
    }

    public String getIcPortraitAddress() {
        return icPortraitAddress;
    }

    public void setIcPortraitAddress(String icPortraitAddress) {
        this.icPortraitAddress = icPortraitAddress;
    }

    public String getIcPortraitNo() {
        return icPortraitNo;
    }

    public void setIcPortraitNo(String icPortraitNo) {
        this.icPortraitNo = icPortraitNo;
    }

    public Byte getApplyTimes() {
        return applyTimes;
    }

    public void setApplyTimes(Byte applyTimes) {
        this.applyTimes = applyTimes;
    }

    public Byte getSendTimes() {
        return sendTimes;
    }

    public void setSendTimes(Byte sendTimes) {
        this.sendTimes = sendTimes;
    }

    public Byte getOldBoc() {
        return oldBoc;
    }

    public void setOldBoc(Byte oldBoc) {
        this.oldBoc = oldBoc;
    }

    public Byte getOldBos() {
        return oldBos;
    }

    public void setOldBos(Byte oldBos) {
        this.oldBos = oldBos;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    public Byte getIcPortraitOcrPass() {
        return icPortraitOcrPass;
    }

    public void setIcPortraitOcrPass(Byte icPortraitOcrPass) {
        this.icPortraitOcrPass = icPortraitOcrPass;
    }

    public String getFtpConfigJson() {
        return ftpConfigJson;
    }

    public void setFtpConfigJson(String ftpConfigJson) {
        this.ftpConfigJson = ftpConfigJson;
    }

    public String getIcPortraitGenderName() {
        return icPortraitGenderName;
    }

    public void setIcPortraitGenderName(String icPortraitGenderName) {
        this.icPortraitGenderName = icPortraitGenderName;
    }

    public String getIcPortraitEthnicityName() {
        return icPortraitEthnicityName;
    }

    public void setIcPortraitEthnicityName(String icPortraitEthnicityName) {
        this.icPortraitEthnicityName = icPortraitEthnicityName;
    }
}
