package com.daikuan.platform.ma.pojo.vo;

import java.io.Serializable;

import com.daikuan.platform.ma.pojo.AUserProductAuditEntity;

/**
 * 
 * @author yangqb
 * @date 2017年3月13日
 * @desc
 */
public class UserProductAuditVO extends AUserProductAuditEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 55131044071867331L;

    /** 身份证人像面性别 ic_portrait_gender 字面描述 **/
    private String icPortraitGenderName;

    /** 身份证人像面民族 ic_portrait_ethnicity 字面描述 **/
    private String icPortraitEthnicityName;

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

    @Override
    public String toString() {
        return "UserProductAuditVO [icPortraitGenderName=" + icPortraitGenderName + ", icPortraitEthnicityName="
                + icPortraitEthnicityName + ", toString()=" + super.toString() + "]";
    }

}
