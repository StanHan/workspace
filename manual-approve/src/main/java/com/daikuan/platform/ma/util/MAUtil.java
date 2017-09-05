package com.daikuan.platform.ma.util;

import java.util.Arrays;

import com.daikuan.platform.ma.common.response.ResponseDataVO;
import com.daikuan.platform.ma.common.vo.ResponseErrorVO;
import com.daikuan.platform.ma.common.vo.ResponseResultVO;

/**
 * 
 * @author yangqb
 * @date 2017年3月7日
 */
public class MAUtil {

    /**
     * 封装返回给客户端的数据
     * 
     * @param status
     * @param err
     * @param errCode
     * @param message
     * @return
     */
    public static ResponseDataVO getResponseDataVO(Byte status, String errCode, String message) {
        ResponseDataVO rd = new ResponseDataVO();

        ResponseResultVO rr = new ResponseResultVO();
        rr.setStatus(status);

        rd.setResult(rr);

        if (status == 0) {
            ResponseErrorVO re = new ResponseErrorVO();
            re.setErrCode(errCode);
            re.setMessage(message);
            rd.setError(re);
        }

        return rd;
    }

    /**
     * 校验产品比例
     * 
     * @param proportion
     * @return
     */
    public static boolean checkProportion(String productsProportion) {
        if (productsProportion == null || productsProportion.isEmpty()) {
            return false;
        }
        String[] tmp = productsProportion.split("=");
        if (tmp.length != 2) {
            return false;
        }
        String[] productArray = tmp[0].split(":");
        String[] proportionArray = tmp[1].split(":");
        if (productArray.length == proportionArray.length) {

            try {
                // 产品校验
                Arrays.stream(productArray).mapToInt(Integer::parseInt).sum();
                // 比例校验
                long count = Arrays.stream(proportionArray).mapToLong(Long::parseLong).filter(e -> e<0).count();
                if(count >0){
                    return false;
                }
                long sum = Arrays.stream(proportionArray).mapToLong(Long::parseLong).sum();
                if (sum <= 0) {
                    return false;
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
    
}
