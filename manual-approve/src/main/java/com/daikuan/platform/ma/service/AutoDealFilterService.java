/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.service;

import com.daikuan.platform.ma.bean.ReasonCategoryVO;
import com.daikuan.platform.ma.exception.AutoFilterException;
import com.daikuan.platform.ma.pojo.AUserProductAuditEntity;
import com.daikuan.platform.ma.pojo.AUserProductAuditStatusEntity;
import java.util.Map;

/**
 * 审批自动化处理
 * 
 * @author feizy
 * @version AutoDealFilterService, v0.1 2017/3/14 17:34
 */
public interface AutoDealFilterService {
    /**
     * 审批进件自动处理的入口
     * 
     * @param userProductAudit
     */
    public Map<String, ReasonCategoryVO> enterAutoDealService(AUserProductAuditEntity userProductAudit)
            throws AutoFilterException;

    /**
     * 根据不同类型进行不同的自动化处理
     * 
     * @param newUserProductAudit
     * @param oldUserProductAudit
     * @param oldProductAuditStatus
     * @param lastAutoResult
     * @param type
     * @return
     */
    public ReasonCategoryVO enterAutoProcessByType(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            Map<String, ReasonCategoryVO> lastAutoResult, int type) throws AutoFilterException;

    /**
     * 执行联系人自动处理
     * 
     * @param newUserProductAudit
     * @param oldUserProductAudit
     * @param oldProductAuditStatus
     * @return
     */
    public ReasonCategoryVO invokeContactAutoProcess(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            ReasonCategoryVO oldReasonCategoryVO) throws AutoFilterException;

    /**
     * 执行单位名称自动处理
     * 
     * @param newUserProductAudit
     * @param oldUserProductAudit
     * @param oldProductAuditStatus
     * @param oldReasonCategoryVO
     * @return
     */
    public ReasonCategoryVO invokeUnitNameAutoProcess(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            ReasonCategoryVO oldReasonCategoryVO) throws AutoFilterException;

    /**
     * 执行单位地址正则自动处理
     * 
     * @param newUserProductAudit
     * @param oldUserProductAudit
     * @param oldProductAuditStatus
     * @param oldReasonCategoryVO
     * @return
     * @throws AutoFilterException
     */
    ReasonCategoryVO invokeCompanyAddressAutoProcess(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            ReasonCategoryVO oldReasonCategoryVO) throws AutoFilterException;

    /**
     * 执行家庭住址自动处理
     * 
     * @param newUserProductAudit
     * @param oldUserProductAudit
     * @param oldProductAuditStatus
     * @param oldReasonCategoryVO
     * @return
     */
    public ReasonCategoryVO invokeFamilyAddressAutoProcess(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            ReasonCategoryVO oldReasonCategoryVO) throws AutoFilterException;

    /**
     * 执行身份证头像面自动处理过程
     * 
     * @param newUserProductAudit
     * @param oldUserProductAudit
     * @param oldProductAuditStatus
     * @param oldReasonCategoryVO
     * @return
     */
    public ReasonCategoryVO invokeIdcardFrontAutoProcess(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            ReasonCategoryVO oldReasonCategoryVO);

    /**
     * 执行实时头像自动处理
     * 
     * @param newUserProductAudit
     * @param oldUserProductAudit
     * @param oldProductAuditStatus
     * @param lastAutoResult
     * @return
     */
    public ReasonCategoryVO invokeCurrentIconAutoProcess(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            Map<String, ReasonCategoryVO> lastAutoResult) throws AutoFilterException;

    /**
     * 执行视频自动处理
     * 
     * @param newUserProductAudit
     * @param oldUserProductAudit
     * @param oldProductAuditStatus
     * @param lastAutoResult
     * @return
     * @throws AutoFilterException
     */
    public ReasonCategoryVO invokeVideoAutoProcess(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            Map<String, ReasonCategoryVO> lastAutoResult) throws AutoFilterException;

    /**
     * 执行身份证背面自动处理
     * 
     * @param newUserProductAudit
     * @param oldUserProductAudit
     * @param oldProductAuditStatus
     * @param lastAutoResult
     * @return
     */
    public ReasonCategoryVO invokeIdcardBackAutoProcess(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            Map<String, ReasonCategoryVO> lastAutoResult) throws AutoFilterException;
}
