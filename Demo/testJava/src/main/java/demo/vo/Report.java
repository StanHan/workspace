package demo.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * @author hanjy
 */
public class Report implements Serializable {

    private static final long serialVersionUID = 2162333712692256055L;

    private String type;
    private String dt;
    private long showTime;
    private long pointTime;
    private long shareTime;
    private long wxzoneTime;
    private long qzoneTime;
    private long wxTime;
    private long qqTime;
    private long copyTime;
    private Date createAt;

    @Override
    public String toString() {
        return "Report [type=" + type + ", dt=" + dt + ", showTime=" + showTime + ", pointTime=" + pointTime
                + ", shareTime=" + shareTime + ", wxzoneTime=" + wxzoneTime + ", qzoneTime=" + qzoneTime + ", wxTime="
                + wxTime + ", qqTime=" + qqTime + ", copyTime=" + copyTime + ", createAt=" + createAt + "]";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDt() {
        return dt;
    }

    public void setDt(String dt) {
        this.dt = dt;
    }

    public long getShowTime() {
        return showTime;
    }

    public void setShowTime(long showTime) {
        this.showTime = showTime;
    }

    public long getPointTime() {
        return pointTime;
    }

    public void setPointTime(long pointTime) {
        this.pointTime = pointTime;
    }

    public long getShareTime() {
        return shareTime;
    }

    public void setShareTime(long shareTime) {
        this.shareTime = shareTime;
    }

    public long getWxzoneTime() {
        return wxzoneTime;
    }

    public void setWxzoneTime(long wxzoneTime) {
        this.wxzoneTime = wxzoneTime;
    }

    public long getQzoneTime() {
        return qzoneTime;
    }

    public void setQzoneTime(long qzoneTime) {
        this.qzoneTime = qzoneTime;
    }

    public long getWxTime() {
        return wxTime;
    }

    public void setWxTime(long wxTime) {
        this.wxTime = wxTime;
    }

    public long getQqTime() {
        return qqTime;
    }

    public void setQqTime(long qqTime) {
        this.qqTime = qqTime;
    }

    public long getCopyTime() {
        return copyTime;
    }

    public void setCopyTime(long copyTime) {
        this.copyTime = copyTime;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
