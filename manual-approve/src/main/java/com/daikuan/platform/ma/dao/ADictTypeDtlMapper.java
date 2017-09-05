package com.daikuan.platform.ma.dao;

import com.daikuan.platform.ma.pojo.ADictTypeDtlEntity;

public interface ADictTypeDtlMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ADictTypeDtlEntity record);

    int insertSelective(ADictTypeDtlEntity record);

    ADictTypeDtlEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ADictTypeDtlEntity record);

    int updateByPrimaryKey(ADictTypeDtlEntity record);
}