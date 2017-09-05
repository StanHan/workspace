package com.daikuan.platform.ma.dao;

import com.daikuan.platform.ma.pojo.ADictTypeEntity;

public interface ADictTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ADictTypeEntity record);

    int insertSelective(ADictTypeEntity record);

    ADictTypeEntity selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ADictTypeEntity record);

    int updateByPrimaryKey(ADictTypeEntity record);
}