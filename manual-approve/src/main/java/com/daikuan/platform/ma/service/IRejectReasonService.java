package com.daikuan.platform.ma.service;

import java.util.List;

import com.daikuan.platform.ma.common.request.RejectReasonsRequest;
import com.daikuan.platform.ma.exception.ServiceException;
import com.daikuan.platform.ma.exception.dao.DaoException;
import com.daikuan.platform.ma.pojo.ARejectReasonMasterEntity;
import com.daikuan.platform.ma.vo.RejectReasonsVo;

/**
 * 
 * @author yangqb
 * @date 2017年3月13日
 * @desc 拒绝原因
 */
public interface IRejectReasonService {
    /**
     * 接收上游系统推送过来的最新数据, 更新本地数据库
     * 
     * @param refuseReasonVO
     * @throws ServiceException
     * @throws DaoException
     */
    void save(RejectReasonsRequest refuseReasonVO) throws ServiceException, DaoException;

    /**
     * 查询全部拒绝原因
     */
    List<ARejectReasonMasterEntity> queryAll() throws DaoException;

    RejectReasonsVo getRejectReasonByType()  throws DaoException;;
}
