package com.daikuan.platform.ma.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.daikuan.platform.ma.exception.dao.DaoException;
import com.daikuan.platform.ma.pojo.ARejectReasonMasterHisEntity;
import com.github.abel533.mapper.Mapper;

/**
 * 
 * @author yangqb
 * @date 2017年3月13日
 * @desc 接收上游推送过来的数据, 原样保存
 */
public interface ARejectReasonMasterHisMapper extends Mapper<ARejectReasonMasterHisEntity> {
    int bathInsert(@Param("rejectReasonMasterHisList") List<ARejectReasonMasterHisEntity> rejectReasonMasterHisList)
            throws DaoException;
}
