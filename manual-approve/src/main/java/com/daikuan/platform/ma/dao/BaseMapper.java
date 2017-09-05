/*
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.dao;

public interface BaseMapper<T> {
    int deleteByPrimaryKey(Long id);

    int insert(T record);

    int insertSelective(T record);

    T selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(T record);

    int updateByPrimaryKey(T record);
}
