package com.daikuan.platform.ma.common.response;

import com.daikuan.platform.ma.common.vo.ResponseErrorVO;
import com.daikuan.platform.ma.common.vo.ResponseResultVO;

/**
 * 
 * @author yangqb
 * @date 2017年3月13日
 * @desc
 */
public class ResponseDataVO {
    /**
     * 响应结果: MAConstant.RES_RESULT_STATUS
     */
    private ResponseResultVO result;
    /**
     * 错误信息
     */
    private ResponseErrorVO error;

    public ResponseResultVO getResult() {
        return result;
    }

    public void setResult(ResponseResultVO result) {
        this.result = result;
    }

    public ResponseErrorVO getError() {
        return error;
    }

    public void setError(ResponseErrorVO error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ResponseDataVO [result=" + result + ", error=" + error + "]";
    }
}
