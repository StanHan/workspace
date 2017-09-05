package com.daikuan.platform.ma.common.request;

import java.util.List;

import com.daikuan.platform.ma.common.vo.RejectReasonVO;

/**
 * 拒绝原因请求类
 * @author hanjy
 *
 */
public class RejectReasonsRequest {

    private List<RejectReasonVO> rejectReasonList;

    public List<RejectReasonVO> getRejectReasonList() {
        return rejectReasonList;
    }

    public void setRejectReasonList(List<RejectReasonVO> rejectReasonList) {
        this.rejectReasonList = rejectReasonList;
    }

}
