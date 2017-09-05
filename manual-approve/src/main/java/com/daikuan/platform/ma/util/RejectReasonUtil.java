package com.daikuan.platform.ma.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.daikuan.platform.ma.common.request.RejectReasonsRequest;
import com.daikuan.platform.ma.constant.ServiceExceptionConstant;
import com.daikuan.platform.ma.exception.ServiceException;

public class RejectReasonUtil {
    private static Logger logger = LoggerFactory.getLogger(RejectReasonUtil.class);

    /**
     * 考滤到调用方所传数据包格式会有变化, 这里提供一个转换接口, 使这边具体业务处理代码与数据包格式解耦 如果调用方会出的数据包有多种格式,
     * 只须更改这个工具类即可
     * 
     * @param object
     * @return
     * @throws Exception
     */
    public static RejectReasonsRequest object2RejectReasonVO(Object object) throws ServiceException {
        if (null == object) {
            logger.error("客户端传过来的数据为空");
            throw new ServiceException(ServiceExceptionConstant.IN_PARAM_NULL, "客户端传过来的数据为空");
        }
        if (object instanceof RejectReasonsRequest) {
            return (RejectReasonsRequest) object;
        }

        if (!(object instanceof RejectReasonsRequest)) {
            // 如果是其它情况, 转换成RejectReasonVO, 再返回 ...
            return null;
        } else {
            logger.error("无法识别的请求包,object=" + object);
            throw new ServiceException(ServiceExceptionConstant.OTHER_ERR_CODE, "无法识别的请求包,object=" + object);
        }

    }
}
