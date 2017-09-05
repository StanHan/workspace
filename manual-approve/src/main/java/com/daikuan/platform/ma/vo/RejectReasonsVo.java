package com.daikuan.platform.ma.vo;

import com.daikuan.platform.ma.pojo.ARejectReasonMasterEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by xiady on 2017/4/13.
 */
public class RejectReasonsVo implements Serializable {

    private Reason homeAddr;

    private  Reason unitAddr;

    private Reason jobUnit;

    private Reason rela;

    private List<Reason> photoFront;

    private List<Reason> photoBack;

    private List<Reason>  photoRealtime;

    private  List<Reason> video;

    public Reason getHomeAddr() {
        return homeAddr;
    }

    public void setHomeAddr(Reason homeAddr) {
        this.homeAddr = homeAddr;
    }

    public Reason getUnitAddr() {
        return unitAddr;
    }

    public void setUnitAddr(Reason unitAddr) {
        this.unitAddr = unitAddr;
    }

    public Reason getJobUnit() {
        return jobUnit;
    }

    public void setJobUnit(Reason jobUnit) {
        this.jobUnit = jobUnit;
    }

    public Reason getRela() {
        return rela;
    }

    public void setRela(Reason rela) {
        this.rela = rela;
    }

    public List<Reason> getPhotoFront() {
        return photoFront;
    }

    public void setPhotoFront(List<Reason> photoFront) {
        this.photoFront = photoFront;
    }

    public List<Reason> getPhotoBack() {
        return photoBack;
    }

    public void setPhotoBack(List<Reason> photoBack) {
        this.photoBack = photoBack;
    }

    public List<Reason> getPhotoRealtime() {
        return photoRealtime;
    }

    public void setPhotoRealtime(List<Reason> photoRealtime) {
        this.photoRealtime = photoRealtime;
    }

    public List<Reason> getVideo() {
        return video;
    }

    public void setVideo(List<Reason> video) {
        this.video = video;
    }
}





