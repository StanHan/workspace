package com.daikuan.platform.ma.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.daikuan.platform.ma.exception.dao.DaoException;
import com.daikuan.platform.ma.pojo.ARejectReasonMasterEntity;
import com.github.abel533.mapper.Mapper;

/**
 * 
 * @author yangqb
 * @date 2017年3月13日
 * @desc MA用到的从a_reject_reason_master_his抽取的必须数据
 */
public interface ARejectReasonMasterMapper extends Mapper<ARejectReasonMasterEntity> {

    int bathInsert(@Param("rejectReasonMasterList") List<ARejectReasonMasterEntity> rejectReasonMasterList)
            throws DaoException;

    public List<ARejectReasonMasterEntity> selectAll();

    int deleteByPrimaryKey(Integer id);

    int insert(ARejectReasonMasterEntity record);

    int insertSelective(ARejectReasonMasterEntity record);

    ARejectReasonMasterEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ARejectReasonMasterEntity record);

    int updateByPrimaryKey(ARejectReasonMasterEntity record);

    String queryVersion();

    public List<ARejectReasonMasterEntity> getRejectReasonByType(Integer type);
}
