/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.common.vo;

import java.util.Date;

/**
 * @author feizy
 * @version AdminApproveStatisticsVO, v0.1 2017/4/20 16:05
 */
public class AdminApproveStatisticsVO extends BaseVO {
    private Long id;//自增id
    private Integer productId;//产品id
    private Long adminId;//审核员id
    private String adminName;//审核员的用户名
    private Date statisticsTime;//统计时间
    private Integer type;//1数量统计、2效率（花费时间）统计
    private Float h09;//9am-10am
    private Float h10;//10am-11am
    private Float h11;//11am-12am
    private Float h12;//12am-1pm
    private Float h13;//1pm-2pm
    private Float h14;//2pm-3pm
    private Float h15;//3pm-4pm
    private Float h16;//4pm-5pm
    private Float h17;//5pm-6pm
    private Float h18;//6pm-7pm
    private Float h19;//7pm-8pm
    private Float h20;//8pm-9pm
    private Float subtotal;//总计数量/效率
    private Float subavg;//平均数量/效率
    private Date lastApproveTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public Date getStatisticsTime() {
        return statisticsTime;
    }

    public void setStatisticsTime(Date statisticsTime) {
        this.statisticsTime = statisticsTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Float getH09() {
        return h09;
    }

    public void setH09(Float h09) {
        this.h09 = h09;
    }

    public Float getH10() {
        return h10;
    }

    public void setH10(Float h10) {
        this.h10 = h10;
    }

    public Float getH11() {
        return h11;
    }

    public void setH11(Float h11) {
        this.h11 = h11;
    }

    public Float getH12() {
        return h12;
    }

    public void setH12(Float h12) {
        this.h12 = h12;
    }

    public Float getH13() {
        return h13;
    }

    public void setH13(Float h13) {
        this.h13 = h13;
    }

    public Float getH14() {
        return h14;
    }

    public void setH14(Float h14) {
        this.h14 = h14;
    }

    public Float getH15() {
        return h15;
    }

    public void setH15(Float h15) {
        this.h15 = h15;
    }

    public Float getH16() {
        return h16;
    }

    public void setH16(Float h16) {
        this.h16 = h16;
    }

    public Float getH17() {
        return h17;
    }

    public void setH17(Float h17) {
        this.h17 = h17;
    }

    public Float getH18() {
        return h18;
    }

    public void setH18(Float h18) {
        this.h18 = h18;
    }

    public Float getH19() {
        return h19;
    }

    public void setH19(Float h19) {
        this.h19 = h19;
    }

    public Float getH20() {
        return h20;
    }

    public void setH20(Float h20) {
        this.h20 = h20;
    }

    public Float getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
    }

    public Float getSubavg() {
        return subavg;
    }

    public void setSubavg(Float subavg) {
        this.subavg = subavg;
    }

    public Date getLastApproveTime() {
        return lastApproveTime;
    }

    public void setLastApproveTime(Date lastApproveTime) {
        this.lastApproveTime = lastApproveTime;
    }

    @Override
    public String toString() {
        return "AdminApproveStatisticsVO{" +
                "id=" + id +
                ", productId=" + productId +
                ", adminId=" + adminId +
                ", adminName='" + adminName + '\'' +
                ", statisticsTime=" + statisticsTime +
                ", type=" + type +
                ", h09=" + h09 +
                ", h10=" + h10 +
                ", h11=" + h11 +
                ", h12=" + h12 +
                ", h13=" + h13 +
                ", h14=" + h14 +
                ", h15=" + h15 +
                ", h16=" + h16 +
                ", h17=" + h17 +
                ", h18=" + h18 +
                ", h19=" + h19 +
                ", h20=" + h20 +
                ", subtotal=" + subtotal +
                ", subavg=" + subavg +
                ", lastApproveTime=" + lastApproveTime +
                "} " + super.toString();
    }
}
