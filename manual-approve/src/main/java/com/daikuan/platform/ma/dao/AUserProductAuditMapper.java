package com.daikuan.platform.ma.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.daikuan.platform.ma.pojo.AUserProductAuditEntity;

public interface AUserProductAuditMapper extends BaseMapper<AUserProductAuditEntity> {

    List<Long> selectIdByVo(@Param("status") int status, @Param("productId") int productId, @Param("offset") int offset,
            @Param("limit") int limit);

    // yangqb
    AUserProductAuditEntity queryByApplyId(Long applyId);

    // yangqb
    int deleteByApplyId(@Param("applyId") Long applyId);

    public AUserProductAuditEntity queryLastRecord(Long userId);
}