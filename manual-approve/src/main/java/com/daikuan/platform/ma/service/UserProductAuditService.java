package com.daikuan.platform.ma.service;

import java.util.Map;

import com.daikuan.platform.ma.exception.MQException;
import com.daikuan.platform.ma.exception.ServiceException;
import com.daikuan.platform.ma.exception.dao.DaoException;
import com.daikuan.platform.ma.pojo.AUserProductAuditStatusEntity;
import com.daikuan.platform.ma.pojo.vo.AuditCallbackVO;
import com.daikuan.platform.ma.pojo.vo.UserProductAuditVO;

/**
 * 
 * @author yangqb
 * @date 2017年3月13日
 * @desc
 */
public interface UserProductAuditService {
    /**
     * 接收进件
     * 
     * @param userProductAuditVO
     * @param 如果该单据直接飘绿了,
     *            则把这个审核直接通过的状态给上游
     * @param map
     *            存放applyId回滚用 auditStatus, 返回给上游状态用
     * @throws DaoException
     * @throws InterruptedException
     * @throws ServiceException
     */
    void save(UserProductAuditVO userProductAuditVO, Map<String, Object> map)
            throws DaoException, InterruptedException, ServiceException;

    /**
     * 人工审核后, 回调上游接口
     * 
     * @param userProductAuditStatus
     * @throws InterruptedException
     * @throws ServiceException
     * @throws DaoException
     */
    void send(AUserProductAuditStatusEntity userProductAuditStatus)
            throws InterruptedException, ServiceException, DaoException;

    /**
     * 进件处理中间如果失败, 则删除已持久化的表中的数据, 否则由于判重下次这个applyId再也不能传进来 由于异常要捕获返回异常码给调用方,
     * 不再用@Transactional处理
     * 
     * @param applyId
     */
    void rollback(Long applyId, Long adminTaskId);
    
    /**
     * 人工审核后, 通过MQ返回审核结果给上游
     * @param auditCallbackVO
     * @return
     * @throws MQException
     */
    public boolean sendMQ(AuditCallbackVO auditCallbackVO) throws MQException;



}
