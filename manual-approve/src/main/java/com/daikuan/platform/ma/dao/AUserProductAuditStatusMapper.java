package com.daikuan.platform.ma.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.daikuan.platform.ma.pojo.AUserProductAuditStatusEntity;

public interface AUserProductAuditStatusMapper extends BaseMapper<AUserProductAuditStatusEntity> {

    int updateByPrimaryKeyWithBLOBs(AUserProductAuditStatusEntity record);

    /**
     * 分页查询 待审批件ID
     * 
     * @param status
     * @param productId
     * @param offset
     * @param limit
     * @return a_user_product_audit_status 表主键
     */
    List<Long> selectCertainId(@Param("auditStatus") Byte auditStatus, @Param("productId") int productId,
            @Param("offset") int offset, @Param("limit") int limit);

    AUserProductAuditStatusEntity selectByApplyId(@Param("applyId") Long applyId);

    // yangqb
    int deleteByApplyId(@Param("applyId") Long applyId);

    AUserProductAuditStatusEntity queryByProductAuditId(Long productAuditId);

    AUserProductAuditStatusEntity getProductAuditStatusByUserId(@Param("userId") Long userId);


    void updateManualGreenByApplyId(AUserProductAuditStatusEntity entity);

}