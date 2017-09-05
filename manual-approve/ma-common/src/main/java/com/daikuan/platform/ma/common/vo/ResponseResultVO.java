package com.daikuan.platform.ma.common.vo;

/**
 * 
 * @author yangqb
 * @date 2017年3月13日
 * @desc
 */
public class ResponseResultVO {
    /**
     * 响应码 1:成功 2:失败
     */
    private Byte status;

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "ResponseResultVO [status=" + status + "]";
    }

}
