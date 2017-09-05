/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.bean;

/**
 * @author feizy
 * @version ReasonCategoryVO, v0.1 2017/3/15 9:33
 */
public class ReasonCategoryVO {
    public static final String KEY_CONTACT = "contact";
    public static final String KEY_UNIT_NAME = "unitname";
    public static final String KEY_FAMILY_ADDR = "familyAddr";
    public static final String KEY_UNIT_ADDR = "unitAddr";
    public static final String KEY_FRONT_IDCARD = "frontIdCard";
    public static final String KEY_BACK_IDCARD = "backIdCard";
    public static final String KEY_HEAD_ICON = "icon";
    public static final String KEY_VIDEO = "video";

    private Integer type;// 审批页面类目的类型
    private Integer wholeGreenFlag;// 全绿的标志 1全绿 非1不是全绿
    private String greenIds;// 飘绿的原因选项
    private String redIds;// 飘红的原因选项
    private String markIds;// 只打标记的原因选项

    public ReasonCategoryVO(Integer type) {
        this.type = type;
    }

    public ReasonCategoryVO() {
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getWholeGreenFlag() {
        return wholeGreenFlag;
    }

    public void setWholeGreenFlag(Integer wholeGreenFlag) {
        this.wholeGreenFlag = wholeGreenFlag;
    }

    public String getGreenIds() {
        return greenIds;
    }

    public void setGreenIds(String greenIds) {
        this.greenIds = greenIds;
    }

    public String getRedIds() {
        return redIds;
    }

    public void setRedIds(String redIds) {
        this.redIds = redIds;
    }

    public String getMarkIds() {
        return markIds;
    }

    public void setMarkIds(String markIds) {
        this.markIds = markIds;
    }

    @Override
    public String toString() {
        return "ReasonCategoryVO{" + "type=" + type + ", wholeGreenFlag=" + wholeGreenFlag + ", greenIds='" + greenIds
                + '\'' + ", redIds='" + redIds + '\'' + ", markIds='" + markIds + '\'' + '}';
    }
}
