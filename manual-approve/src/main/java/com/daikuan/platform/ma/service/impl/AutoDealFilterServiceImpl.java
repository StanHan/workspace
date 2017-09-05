/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.service.impl;

import com.daikuan.platform.ma.bean.ReasonCategoryVO;
import com.daikuan.platform.ma.bean.RegularData;
import com.daikuan.platform.ma.bean.ResponseVO;
import com.daikuan.platform.ma.dao.AUserProductAuditMapper;
import com.daikuan.platform.ma.dao.AUserProductAuditStatusMapper;
import com.daikuan.platform.ma.exception.AutoFilterError;
import com.daikuan.platform.ma.exception.AutoFilterException;
import com.daikuan.platform.ma.pojo.AUserProductAuditEntity;
import com.daikuan.platform.ma.pojo.AUserProductAuditStatusEntity;
import com.daikuan.platform.ma.service.AutoDealFilterService;
import com.daikuan.platform.ma.service.TsService;
import com.daikuan.platform.ma.util.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author feizy
 * @version AutoDealFilterServiceImpl, v0.1 2017/3/14 17:36
 */
@Service
public class AutoDealFilterServiceImpl implements AutoDealFilterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AutoDealFilterServiceImpl.class);
    private static final String SEPARATOR_COMMA = ",";
    private static final ObjectMapper JSONUTILS = new ObjectMapper();
    @Autowired
    private TsService tsService;
    @Autowired
    private AUserProductAuditMapper userProductAuditMapper;
    @Autowired
    private AUserProductAuditStatusMapper userProductAuditStatusMapper;

    @Override
    public Map<String, ReasonCategoryVO> enterAutoDealService(AUserProductAuditEntity newUserProductAudit)
            throws AutoFilterException {
        Map<String, ReasonCategoryVO> reasonCategoryVOMap = new HashMap<>();
        Map<String, ReasonCategoryVO> lastAutoResult = null;
        // 查询之前是否进入过审批
        AUserProductAuditEntity oldUserProductAudit = userProductAuditMapper
                .queryLastRecord(newUserProductAudit.getUserId());
        AUserProductAuditStatusEntity oldProductAuditStatus = null;
        // 如果之前进入过审批,则查询上一次的审批结果
        if (oldUserProductAudit != null) {
            oldProductAuditStatus = userProductAuditStatusMapper.queryByProductAuditId(oldUserProductAudit.getId());
        }
        if (!(oldUserProductAudit == null || oldProductAuditStatus == null
                || StringUtils.isBlank(oldProductAuditStatus.getManualGreen()))) {
            // 满足条件的算有审批记录的
            String lastAutoResultJson = oldProductAuditStatus.getManualGreen();
            try {
                lastAutoResult = JSONUTILS.readValue(lastAutoResultJson,
                        JSONUTILS.getTypeFactory().constructMapType(Map.class, String.class, ReasonCategoryVO.class));
            } catch (IOException e) {
                throw new AutoFilterException(AutoFilterError.FAIL_PARSE_AUTORESULT, e);
            }
        }
        // 1.联系人(type=5)
        ReasonCategoryVO contactCategory = null;
        try {
            contactCategory = enterAutoProcessByType(newUserProductAudit, oldUserProductAudit,
                    oldProductAuditStatus, lastAutoResult, Constants.AUDIT_CATEGORY_TYPE_CONTACT);
        } catch (Exception e) {
            LOGGER.error("contact auto deal exception...newUserProductAudit is {}", newUserProductAudit.toString(), e);
            contactCategory = new ReasonCategoryVO(Constants.AUDIT_CATEGORY_TYPE_CONTACT);
        }
        reasonCategoryVOMap.put(ReasonCategoryVO.KEY_CONTACT, contactCategory);
        // 2.公司名称(type=3)
        ReasonCategoryVO companyNameCategory = null;
        try {
            companyNameCategory = enterAutoProcessByType(newUserProductAudit, oldUserProductAudit,
                    oldProductAuditStatus, lastAutoResult, Constants.AUDIT_CATEGORY_TYPE_COMPANY_NAME);
        } catch (Exception e) {
            LOGGER.error("company name auto deal exception...newUserProductAudit is {}", newUserProductAudit.toString(), e);
            companyNameCategory = new ReasonCategoryVO(Constants.AUDIT_CATEGORY_TYPE_COMPANY_NAME);
        }
        reasonCategoryVOMap.put(ReasonCategoryVO.KEY_UNIT_NAME, companyNameCategory);
        // 3.家庭地址(type=4)
        ReasonCategoryVO familyAddressCategory = null;
        try {
            familyAddressCategory = enterAutoProcessByType(newUserProductAudit, oldUserProductAudit,
                    oldProductAuditStatus, lastAutoResult, Constants.AUDIT_CATEGORY_TYPE_FAMILAY_ADDRESS);
        } catch (Exception e) {
            LOGGER.error("family address auto deal exception...newUserProductAudit is {}", newUserProductAudit.toString(), e);
            familyAddressCategory = new ReasonCategoryVO(Constants.AUDIT_CATEGORY_TYPE_FAMILAY_ADDRESS);
        }
        reasonCategoryVOMap.put(ReasonCategoryVO.KEY_FAMILY_ADDR, familyAddressCategory);
        // 4.单位地址(type=2)
        ReasonCategoryVO companyAddressCategory = null;
        try {
            companyAddressCategory = enterAutoProcessByType(newUserProductAudit, oldUserProductAudit,
                    oldProductAuditStatus, lastAutoResult, Constants.AUDIT_CATEGORY_TYPE_COMPANY_ADDRESS);
        } catch (Exception e) {
            LOGGER.error("company address auto deal exception...newUserProductAudit is {}", newUserProductAudit.toString(), e);
            companyAddressCategory = new ReasonCategoryVO(Constants.AUDIT_CATEGORY_TYPE_COMPANY_ADDRESS);
        }
        reasonCategoryVOMap.put(ReasonCategoryVO.KEY_UNIT_ADDR, companyAddressCategory);
        // 5.身份证头像面(type=6)
        ReasonCategoryVO idcardFrontCategory = null;
        try {
            idcardFrontCategory = enterAutoProcessByType(newUserProductAudit, oldUserProductAudit,
                    oldProductAuditStatus, lastAutoResult, Constants.AUDIT_CATEGORY_TYPE_IDCARDFRONT);
        } catch (Exception e) {
            LOGGER.error("front idcard auto deal exception...newUserProductAudit is {}", newUserProductAudit.toString(), e);
            idcardFrontCategory = new ReasonCategoryVO(Constants.AUDIT_CATEGORY_TYPE_IDCARDFRONT);
        }
        reasonCategoryVOMap.put(ReasonCategoryVO.KEY_FRONT_IDCARD, idcardFrontCategory);
        // 6.身份证国徽面(type=11)
        ReasonCategoryVO idcardBackCategory = null;
        try {
            idcardBackCategory = enterAutoProcessByType(newUserProductAudit, oldUserProductAudit,
                    oldProductAuditStatus, lastAutoResult, Constants.AUDIT_CATEGORY_TYPE_IDCARDBACK);
        } catch (Exception e) {
            LOGGER.error("back idcard auto deal exception...newUserProductAudit is {}", newUserProductAudit.toString(), e);
            idcardBackCategory = new ReasonCategoryVO(Constants.AUDIT_CATEGORY_TYPE_IDCARDBACK);
        }
        reasonCategoryVOMap.put(ReasonCategoryVO.KEY_BACK_IDCARD, idcardBackCategory);
        // 7.实时头像(type=7)
        ReasonCategoryVO currentIconCategory = null;
        try {
            currentIconCategory = enterAutoProcessByType(newUserProductAudit, oldUserProductAudit,
                    oldProductAuditStatus, lastAutoResult, Constants.AUDIT_CATEGORY_TYPE_CURRECT_ICON);
        } catch (Exception e) {
            LOGGER.error("current icon auto deal exception...newUserProductAudit is {}", newUserProductAudit.toString(), e);
            currentIconCategory = new ReasonCategoryVO(Constants.AUDIT_CATEGORY_TYPE_CURRECT_ICON);
        }
        reasonCategoryVOMap.put(ReasonCategoryVO.KEY_HEAD_ICON, currentIconCategory);
        // 8.视频(type=8)
        ReasonCategoryVO videoCategory = null;
        try {
            videoCategory = enterAutoProcessByType(newUserProductAudit, oldUserProductAudit,
                    oldProductAuditStatus, lastAutoResult, Constants.AUDIT_CATEGORY_TYPE_VIDEO);
        } catch (Exception e) {
            LOGGER.error("video auto deal exception...newUserProductAudit is {}", newUserProductAudit.toString(), e);
            videoCategory = new ReasonCategoryVO(Constants.AUDIT_CATEGORY_TYPE_VIDEO);
        }
        reasonCategoryVOMap.put(ReasonCategoryVO.KEY_VIDEO, videoCategory);
        return reasonCategoryVOMap;
    }

    @Override
    public ReasonCategoryVO enterAutoProcessByType(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            Map<String, ReasonCategoryVO> lastAutoResult, int type) throws AutoFilterException {
        ReasonCategoryVO reasonCategoryVO = null;
        switch (type) {
        case Constants.AUDIT_CATEGORY_TYPE_CONTACT:
            reasonCategoryVO = invokeContactAutoProcess(newUserProductAudit, oldUserProductAudit, oldProductAuditStatus,
                    lastAutoResult == null ? null : lastAutoResult.get(ReasonCategoryVO.KEY_CONTACT));
            break;
        case Constants.AUDIT_CATEGORY_TYPE_COMPANY_NAME:
            reasonCategoryVO = invokeUnitNameAutoProcess(newUserProductAudit, oldUserProductAudit,
                    oldProductAuditStatus,
                    lastAutoResult == null ? null : lastAutoResult.get(ReasonCategoryVO.KEY_UNIT_NAME));
            break;
        case Constants.AUDIT_CATEGORY_TYPE_FAMILAY_ADDRESS:
            reasonCategoryVO = invokeFamilyAddressAutoProcess(newUserProductAudit, oldUserProductAudit,
                    oldProductAuditStatus,
                    lastAutoResult == null ? null : lastAutoResult.get(ReasonCategoryVO.KEY_FAMILY_ADDR));
            break;
        case Constants.AUDIT_CATEGORY_TYPE_COMPANY_ADDRESS:
            reasonCategoryVO = invokeCompanyAddressAutoProcess(newUserProductAudit, oldUserProductAudit,
                    oldProductAuditStatus,
                    lastAutoResult == null ? null : lastAutoResult.get(ReasonCategoryVO.KEY_UNIT_ADDR));
            break;
        case Constants.AUDIT_CATEGORY_TYPE_IDCARDFRONT:
            reasonCategoryVO = invokeIdcardFrontAutoProcess(newUserProductAudit, oldUserProductAudit,
                    oldProductAuditStatus,
                    lastAutoResult == null ? null : lastAutoResult.get(ReasonCategoryVO.KEY_FRONT_IDCARD));
            break;
        case Constants.AUDIT_CATEGORY_TYPE_IDCARDBACK:
            reasonCategoryVO = invokeIdcardBackAutoProcess(newUserProductAudit, oldUserProductAudit,
                    oldProductAuditStatus, lastAutoResult);
            break;
        case Constants.AUDIT_CATEGORY_TYPE_CURRECT_ICON:
            reasonCategoryVO = invokeCurrentIconAutoProcess(newUserProductAudit, oldUserProductAudit,
                    oldProductAuditStatus, lastAutoResult);
            break;
        case Constants.AUDIT_CATEGORY_TYPE_VIDEO:
            reasonCategoryVO = invokeVideoAutoProcess(newUserProductAudit, oldUserProductAudit, oldProductAuditStatus,
                    lastAutoResult);
            break;
        default:
            // 容错处理
            throw new AutoFilterException(AutoFilterError.AUDIT_CATAGORY_TYPE_ILLEGAL);
        }
        return reasonCategoryVO;
    }

    @Override
    public ReasonCategoryVO invokeContactAutoProcess(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            ReasonCategoryVO oldReasonCategoryVO) throws AutoFilterException {
        ReasonCategoryVO reasonCategoryVO = new ReasonCategoryVO(Constants.AUDIT_CATEGORY_TYPE_CONTACT);
        List<Integer> greenIds = new ArrayList<>();
        List<Integer> redIds = new ArrayList<>();
        int greenCount = 0;// 全绿的计数
        // 判断之前是否进入过审批
        if (oldReasonCategoryVO != null) {
            // 非首次进入审批
            // 对比历史数据
            if (oldUserProductAudit.getRelaFamily().equals(newUserProductAudit.getRelaFamily())
                    && oldUserProductAudit.getRelaColleague().equals(newUserProductAudit.getRelaColleague())
                    && oldUserProductAudit.getRelaFriend().equals(newUserProductAudit.getRelaFriend())) {
                // 联系人与上次没有变更,如果上次通过，则以上次结果为本次的结果；
                // 如果上次没通过（理论上不会出现，因为上次没通过，必须修改相应资料才可以再提交），容错处理：飘黑
                if (oldReasonCategoryVO.getWholeGreenFlag() != null && oldReasonCategoryVO.getWholeGreenFlag() == 1) {
                    // 上次全绿
                    reasonCategoryVO.setGreenIds(oldReasonCategoryVO.getGreenIds());
                    reasonCategoryVO.setWholeGreenFlag(1);
                    return reasonCategoryVO;
                } else {
                    // 容错：直接让人工审
                    return reasonCategoryVO;
                }
            }
        }
        // 第一次进入审批及非首次但是信息已经修改
        // 调用工具微服务中正则功能
        // 先判断是否存在相同的联系人
        ResponseVO<List<RegularData>> responseVO = tsService.verifySameContactReg(Constants.AUDIT_CATEGORY_TYPE_CONTACT,
                newUserProductAudit.getRelaFamily(), newUserProductAudit.getRelaColleague(),
                newUserProductAudit.getRelaFriend());
        List<RegularData> regularDatas = responseVO.getBody();
        for (RegularData temp : regularDatas) {
            if (RegularData.REJECT_0 == temp.getPass()) {
                // 飘红
                redIds.add(temp.getType());
            }
        }
        if (!(redIds == null || redIds.size() < 1)) {
            // 命中相同联系人
            // 此次飘红不做，所以出现飘红直接放开所有选项返回
            // reasonCategoryVO.setRedIds(StringUtils.join(greenIds.toArray(),
            // SEPARATOR_COMMA));
            return reasonCategoryVO;
        }
        // 进行联系人正则校验
        responseVO = tsService.verifyContactReg(Constants.AUDIT_CATEGORY_TYPE_CONTACT,
                newUserProductAudit.getRelaFamily(), newUserProductAudit.getRelaColleague(),
                newUserProductAudit.getRelaFriend());
        regularDatas = responseVO.getBody();
        for (RegularData temp : regularDatas) {
            if (RegularData.PASS_1 == temp.getPass()) {
                // 飘绿
                // greenIds.add(temp.getType());
                greenCount++;
            } else if (RegularData.REJECT_0 == temp.getPass()) {
                // 飘红
                // redIds.add(temp.getType());
            } else {
                // 飘黑不做处理
            }
        }
        reasonCategoryVO
                .setGreenIds(greenIds.size() < 1 ? null : StringUtils.join(greenIds.toArray(), SEPARATOR_COMMA));
        reasonCategoryVO.setRedIds(redIds.size() < 1 ? null : StringUtils.join(redIds.toArray(), SEPARATOR_COMMA));
        if (greenCount == 3) {
            // 全绿
            reasonCategoryVO.setWholeGreenFlag(1);
            greenIds.add(Constants.AUDIT_REJECT_REASON_CONTACT_ILLEGAL);
        }
        return reasonCategoryVO;
    }

    @Override
    public ReasonCategoryVO invokeUnitNameAutoProcess(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            ReasonCategoryVO oldReasonCategoryVO) throws AutoFilterException {
        ReasonCategoryVO reasonCategoryVO = new ReasonCategoryVO(Constants.AUDIT_CATEGORY_TYPE_COMPANY_NAME);
        // 判断之前是否进入过审批
        if (oldReasonCategoryVO != null) {
            // 非首次进入审批
            // 对比历史数据
            if (oldUserProductAudit.getJobUnit().equals(newUserProductAudit.getJobUnit())) {
                // 公司名称与上次没有变更,如果上次通过，则以上次结果为本次的结果；
                // 如果上次没通过（理论上不会出现，因为上次没通过，必须修改相应资料才可以再提交），容错处理：飘黑
                if (oldReasonCategoryVO.getWholeGreenFlag() != null && oldReasonCategoryVO.getWholeGreenFlag() == 1) {
                    // 上次全绿
                    reasonCategoryVO.setGreenIds(oldReasonCategoryVO.getGreenIds());
                    reasonCategoryVO.setWholeGreenFlag(1);
                    return reasonCategoryVO;
                } else {
                    // 容错：直接让人工审
                    return reasonCategoryVO;
                }
            }
        }
        // 第一次进入审批及非首次但是信息已经修改
        // 调用工具微服务中正则功能
        ResponseVO<List<RegularData>> responseVO = tsService.verifyUnitName(Constants.AUDIT_CATEGORY_TYPE_COMPANY_NAME,
                newUserProductAudit.getJobUnit());
        List<RegularData> regularDatas = responseVO.getBody();
        if (1 == regularDatas.get(0).getPass()) {
            // 飘绿
            reasonCategoryVO.setWholeGreenFlag(1);
            reasonCategoryVO.setGreenIds(String.valueOf(Constants.AUDIT_REJECT_REASON_UNIT_NAME));
        } else if (0 == regularDatas.get(0).getPass()) {
            // 飘红
            reasonCategoryVO.setRedIds(String.valueOf(Constants.AUDIT_REJECT_REASON_UNIT_NAME));
        } else {
            // 飘黑 不做处理
        }
        return reasonCategoryVO;
    }

    @Override
    public ReasonCategoryVO invokeCompanyAddressAutoProcess(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            ReasonCategoryVO oldReasonCategoryVO) throws AutoFilterException {
        ReasonCategoryVO reasonCategoryVO = new ReasonCategoryVO(Constants.AUDIT_CATEGORY_TYPE_COMPANY_ADDRESS);
        // 判断之前是否进入过审批
        if (oldReasonCategoryVO != null) {
            // 非首次进入审批
            // 对比历史数据
            if (oldUserProductAudit.getUnitAddr().equals(newUserProductAudit.getUnitAddr())) {
                // 单位地址与上次没有变更,如果上次通过，则以上次结果为本次的结果；
                // 如果上次没通过（理论上不会出现，因为上次没通过，必须修改相应资料才可以再提交），容错处理：飘黑
                if (oldReasonCategoryVO.getWholeGreenFlag() != null && oldReasonCategoryVO.getWholeGreenFlag() == 1) {
                    // 上次全绿
                    reasonCategoryVO.setGreenIds(oldReasonCategoryVO.getGreenIds());
                    reasonCategoryVO.setWholeGreenFlag(1);
                    return reasonCategoryVO;
                } else {
                    // 容错：直接让人工审
                    return reasonCategoryVO;
                }
            }
        }
        // 第一次进入审批及非首次但是信息已经修改
        // 调用工具微服务中正则功能
        ResponseVO<List<RegularData>> responseVO = tsService
                .verifyUnitAddr(Constants.AUDIT_CATEGORY_TYPE_COMPANY_ADDRESS, newUserProductAudit.getUnitAddr());
        List<RegularData> regularDatas = responseVO.getBody();
        if (1 == regularDatas.get(0).getPass()) {
            // 飘绿
            reasonCategoryVO.setWholeGreenFlag(1);
            reasonCategoryVO.setGreenIds(String.valueOf(Constants.AUDIT_REJECT_REASON_UNIT_ADDR));
        } else if (0 == regularDatas.get(0).getPass()) {
            // 飘红
            reasonCategoryVO.setRedIds(String.valueOf(Constants.AUDIT_REJECT_REASON_UNIT_ADDR));
        } else {
            // 飘黑 不做处理
        }
        return reasonCategoryVO;
    }

    @Override
    public ReasonCategoryVO invokeFamilyAddressAutoProcess(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            ReasonCategoryVO oldReasonCategoryVO) throws AutoFilterException {
        ReasonCategoryVO reasonCategoryVO = new ReasonCategoryVO(Constants.AUDIT_CATEGORY_TYPE_FAMILAY_ADDRESS);
        // 判断之前是否进入过审批
        if (oldReasonCategoryVO != null) {
            // 非首次进入审批
            // 对比历史数据
            if (oldUserProductAudit.getHomeAddr().equals(newUserProductAudit.getHomeAddr())) {
                // 家庭地址与上次没有变更,如果上次通过，则以上次结果为本次的结果；
                // 如果上次没通过（理论上不会出现，因为上次没通过，必须修改相应资料才可以再提交），容错处理：飘黑
                if (oldReasonCategoryVO.getWholeGreenFlag() != null && oldReasonCategoryVO.getWholeGreenFlag() == 1) {
                    // 上次全绿
                    reasonCategoryVO.setGreenIds(oldReasonCategoryVO.getGreenIds());
                    reasonCategoryVO.setWholeGreenFlag(1);
                    return reasonCategoryVO;
                } else {
                    // 容错：直接让人工审
                    return reasonCategoryVO;
                }
            }
        }
        // 第一次进入审批及非首次但是信息已经修改
        // 调用工具微服务中正则功能
        ResponseVO<List<RegularData>> responseVO = tsService
                .verifyFamilyAddr(Constants.AUDIT_CATEGORY_TYPE_FAMILAY_ADDRESS, newUserProductAudit.getHomeAddr());
        List<RegularData> regularDatas = responseVO.getBody();
        if (1 == regularDatas.get(0).getPass()) {
            // 飘绿
            reasonCategoryVO.setWholeGreenFlag(1);
            reasonCategoryVO.setGreenIds(String.valueOf(Constants.AUDIT_REJECT_REASON_FAMILY_ADDR));
        } else if (0 == regularDatas.get(0).getPass()) {
            // 飘红
            reasonCategoryVO.setRedIds(String.valueOf(Constants.AUDIT_REJECT_REASON_FAMILY_ADDR));
        } else {
            // 飘黑 不做处理
        }
        return reasonCategoryVO;
    }

    @Override
    public ReasonCategoryVO invokeIdcardFrontAutoProcess(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            ReasonCategoryVO oldReasonCategoryVO) {
        ReasonCategoryVO reasonCategoryVO = new ReasonCategoryVO(Constants.AUDIT_CATEGORY_TYPE_IDCARDFRONT);
        // 判断之前是否进入过审批
        if (oldReasonCategoryVO != null) {
            // 非首次进入审批
            // 对比历史数据
            if (oldUserProductAudit.getPhotoFront().equals(newUserProductAudit.getPhotoFront())) {
                // 身份证头像面与上次没有变更,如果上次通过，则以上次结果为本次的结果；
                // 如果上次没通过（理论上不会出现，因为上次没通过，必须修改相应资料才可以再提交），容错处理：飘黑
                if (oldReasonCategoryVO.getWholeGreenFlag() != null && oldReasonCategoryVO.getWholeGreenFlag() == 1) {
                    // 上次全绿
                    reasonCategoryVO.setGreenIds(oldReasonCategoryVO.getGreenIds());
                    // reasonCategoryVO.setRedIds(oldReasonCategoryVO.getRedIds());
                    reasonCategoryVO.setMarkIds(oldReasonCategoryVO.getMarkIds());
                    reasonCategoryVO.setWholeGreenFlag(1);
                    return reasonCategoryVO;
                } else {
                    // 容错：直接让人工审
                    return reasonCategoryVO;
                }
            }
        }
        // 第一次进入审批及非首次进入但是信息已经修改
        int icPortraitOcrPass = newUserProductAudit.getIcPortraitOcrPass();
        if (1 == icPortraitOcrPass) {
            // 飘绿原因id(身份证不清晰\身份证非人像面\非本人身份证)
            String greenIds = Constants.AUDIT_REJECT_REASON_IC_FRONT_FUZZY + SEPARATOR_COMMA
                    + Constants.AUDIT_REJECT_REASON_IC_FRONT_NOTFACE + SEPARATOR_COMMA
                    + Constants.AUDIT_REJECT_REASON_NOTSELF + SEPARATOR_COMMA
                    + Constants.AUDIT_REJECT_REASON_INTENTIONAL_FORGERY;
            reasonCategoryVO.setGreenIds(greenIds);
        } else {
            // 选项放开所有，只标红身份证人像图框的边框（约定使用0表示）---2017/03/22（在小黑屋）: 所有飘红不做（徐寅）
            // reasonCategoryVO.setRedIds("0");
        }
        return reasonCategoryVO;
    }

    @Override
    public ReasonCategoryVO invokeCurrentIconAutoProcess(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            Map<String, ReasonCategoryVO> lastAutoResult) throws AutoFilterException {
        // 如果实时头像字段为空，说明是5.1以及之前版本的上行用户
        if (StringUtils.isBlank(newUserProductAudit.getPhotoRealTime())) {
            return null;
        }
        ReasonCategoryVO reasonCategoryVO = new ReasonCategoryVO(Constants.AUDIT_CATEGORY_TYPE_CURRECT_ICON);
        // 判断之前是否进入过审批
        if (lastAutoResult != null) {
            // 非首次进入审批
            ReasonCategoryVO oldHeadIconReaCategoryVO = lastAutoResult.get(ReasonCategoryVO.KEY_HEAD_ICON);
            ReasonCategoryVO oldFrontICReaCategoryVO = lastAutoResult.get(ReasonCategoryVO.KEY_FRONT_IDCARD);
            if (oldFrontICReaCategoryVO == null) {
                throw new AutoFilterException(AutoFilterError.LAST_AUDIT_FRONT_IC_NULL);
            }
            // 对比历史数据
            if (newUserProductAudit.getPhotoRealTime().equals(oldUserProductAudit.getPhotoRealTime())) {
                // 实时头像与上次没有变更
                if (oldHeadIconReaCategoryVO.getWholeGreenFlag() != null && oldHeadIconReaCategoryVO.getWholeGreenFlag() == 1) {
                    // 上次头像通过
                    // 判断本次身份证有没有修改
                    if (!this.judgeCurrICHasChange(newUserProductAudit, oldUserProductAudit)) {
                        // 身份证人面像没有变更
                        if (oldFrontICReaCategoryVO.getWholeGreenFlag() != null && oldFrontICReaCategoryVO.getWholeGreenFlag() == 1) {
                            // 实时头像自动飘绿
                            reasonCategoryVO.setWholeGreenFlag(1);
                            reasonCategoryVO.setGreenIds(oldHeadIconReaCategoryVO.getGreenIds());
                            reasonCategoryVO.setMarkIds(oldHeadIconReaCategoryVO.getMarkIds());
                        } else {
                            // 实时头像显示所有
                        }
                        return reasonCategoryVO;
                    } else {
                        // 本次身份证信息已经更改，直接进行和首次进入情况一样的处理
                    }
                } else {// 上次预审头像拒绝
                    if (!this.judgeCurrICHasChange(newUserProductAudit, oldUserProductAudit)) {
                        // 本次身份证没有更改（理论上不会出现，因为上次没通过，必须修改相应资料才可以再提交，容错处理：放开所有选项）
                        return reasonCategoryVO;
                    } else {
                        // 本次身份证已经修改，直接进行和首次进入情况一样的处理
                    }
                }
            } else {
                // 头像已经修改，或则上次没有实时头像本次有了，直接进行和首次进入情况一样的处理
            }
        }
        // 首次进入审批及非首次进入但需要和首次进入进行一样的处理的情况
        if (newUserProductAudit.getIcPortraitOcrPass() == 1) {
            // 本次身份证ocr识别通过
            if (newUserProductAudit.getFaceScoreIcon() != null && newUserProductAudit
                    .getFaceScoreIcon() >= Constants.AUDIT_OCR_FACE_MATCH_CURRENT_ICON_SCORE_PASS) {
                // 依图人脸比对通过,则飘绿 实时头像不清晰、非本人头像
                reasonCategoryVO.setGreenIds(Constants.AUDIT_REJECT_REASON_HEAD_ICON_FUZZY + SEPARATOR_COMMA
                        + Constants.AUDIT_REJECT_REASON_HEAD_ICON_NOTSELF);
            } else {
                // 依图人脸比对未通过，放开所有选项
            }
        }
        return reasonCategoryVO;
    }

    @Override
    public ReasonCategoryVO invokeVideoAutoProcess(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            Map<String, ReasonCategoryVO> lastAutoResult) throws AutoFilterException {
        ReasonCategoryVO reasonCategoryVO = new ReasonCategoryVO(Constants.AUDIT_CATEGORY_TYPE_VIDEO);
        // 判断之前是否进入过审批
        if (lastAutoResult != null) {
            // 非首次进入审批
            ReasonCategoryVO oldVideoReaCategoryVO = lastAutoResult.get(ReasonCategoryVO.KEY_VIDEO);
            ReasonCategoryVO oldFrontICReaCategoryVO = lastAutoResult.get(ReasonCategoryVO.KEY_FRONT_IDCARD);
            if (oldFrontICReaCategoryVO == null || oldVideoReaCategoryVO == null) {
                throw new AutoFilterException(AutoFilterError.LAST_AUDIT_FRONT_IC_AND_VIDEO_NULL);
            }
            // 对比历史数据
            if (newUserProductAudit.getVideo().equals(oldUserProductAudit.getVideo())) {
                // 视频与上次没有变更
                if (oldVideoReaCategoryVO.getWholeGreenFlag() != null && oldVideoReaCategoryVO.getWholeGreenFlag() == 1) {
                    // 上次视频通过
                    // 判断本次身份证有没有修改
                    if (!this.judgeCurrICHasChange(newUserProductAudit, oldUserProductAudit)) {
                        // 身份证人面像没有变更
                        if (oldFrontICReaCategoryVO.getWholeGreenFlag() != null && oldFrontICReaCategoryVO.getWholeGreenFlag() == 1) {
                            // 视频自动飘绿
                            reasonCategoryVO.setWholeGreenFlag(1);
                            reasonCategoryVO.setGreenIds(oldVideoReaCategoryVO.getGreenIds());
                            reasonCategoryVO.setMarkIds(oldVideoReaCategoryVO.getMarkIds());
                        } else {
                            // 视频显示所有
                        }
                        return reasonCategoryVO;
                    } else {
                        // 本次身份证信息已经更改
                        if (newUserProductAudit.getIcPortraitOcrPass() == 1) {// 身份证人相面ocr通过
                            if (newUserProductAudit.getFaceScoreVideo() != null && newUserProductAudit
                                    .getFaceScoreVideo() >= Constants.AUDIT_OCR_FACE_MATCH_VIDEO_SCORE_PASS) {
                                // 依图截图对比过,飘绿 视频人脸不清晰、视频所读内容有误、视频未读
                                reasonCategoryVO.setGreenIds(Constants.AUDIT_REJECT_REASON_VIDEO_FUZZY + SEPARATOR_COMMA
                                        + Constants.AUDIT_REJECT_REASON_VIDEO_SPEECH_ERROR + SEPARATOR_COMMA
                                        + Constants.AUDIT_REJECT_REASON_VIDEO_NOT_SPEECH);
                                return reasonCategoryVO;
                            } else {
                                // 依图截图对比不通过(执行和身份证人相面ocr不通过一样的效果)
                            }
                        }
                        // 身份证人相面ocr不通过（飘绿 视频所读内容有误、视频未读）
                        reasonCategoryVO.setGreenIds(Constants.AUDIT_REJECT_REASON_VIDEO_SPEECH_ERROR + SEPARATOR_COMMA
                                + Constants.AUDIT_REJECT_REASON_VIDEO_NOT_SPEECH);
                        return reasonCategoryVO;
                    }
                } else {// 上次预审视频拒绝
                    if (!this.judgeCurrICHasChange(newUserProductAudit, oldUserProductAudit)) {
                        // 本次身份证没有更改
                        return reasonCategoryVO;
                    } else {
                        // 本次身份证已经修改，直接进行和首次进入情况一样的处理
                    }
                }
            } else {
                // 视频已经修改，直接进行和首次进入情况一样的处理
            }
        }
        // 首次进入的处理以及非首次但和首次进入做一样的处理的情况
        if (newUserProductAudit.getIcPortraitOcrPass() == 1) {// 身份证人面相ocr通过
            if (newUserProductAudit.getFaceScoreVideo() != null
                    && newUserProductAudit.getFaceScoreVideo() >= Constants.AUDIT_OCR_FACE_MATCH_VIDEO_SCORE_PASS) {
                // 依图截图对比过
                // 语音正则处理
                if (this.judgeAudioRegPass(newUserProductAudit)) {
                    // 语音通过
                    // 飘绿 视频人脸不清晰、视频所读内容有误、视频未读
                    reasonCategoryVO.setGreenIds(Constants.AUDIT_REJECT_REASON_VIDEO_FUZZY + SEPARATOR_COMMA
                            + Constants.AUDIT_REJECT_REASON_VIDEO_SPEECH_ERROR + SEPARATOR_COMMA
                            + Constants.AUDIT_REJECT_REASON_VIDEO_NOT_SPEECH);
                    return reasonCategoryVO;
                }
                // 未通过现实所有选项
                return reasonCategoryVO;
            } else {
                // 依图截图对比不通过，执行和身份证人面相ocr没有通过一样的操作
            }
        }
        // 身份证人面相ocr没有通过
        // 语音正则判断
        if (this.judgeAudioRegPass(newUserProductAudit)) {
            // 语音正则通过，飘绿 视频所读内容有误、视频未读
            reasonCategoryVO.setGreenIds(Constants.AUDIT_REJECT_REASON_VIDEO_SPEECH_ERROR + SEPARATOR_COMMA
                    + Constants.AUDIT_REJECT_REASON_VIDEO_NOT_SPEECH);
            return reasonCategoryVO;
        } else {
            // 语音正则不通过或则无法判断，放开所有选项
            return reasonCategoryVO;
        }
    }

    /**
     * 判断视频的语音是否通过
     * 
     * @param newUserProductAudit
     * @return
     * @throws AutoFilterException
     */
    private boolean judgeAudioRegPass(AUserProductAuditEntity newUserProductAudit) throws AutoFilterException {
        String videoPath = newUserProductAudit.getVideo();
        String ftpConfigJson = newUserProductAudit.getFtpConfigJson();
        if (StringUtils.isBlank(videoPath) || StringUtils.isBlank(ftpConfigJson)) {
            LOGGER.error("judge audio reg pass, the groupNum or videoPath or ftpConfigJson is blank, the userProductAudit is {}", newUserProductAudit.toString());
            return false;
        }
        Integer type;
        if (newUserProductAudit.getProductId() == null) {
            //没有产品id信息
            return false;
        }
        if (newUserProductAudit.getProductId() == 1) {
            type = Constants.AUDIO_REG_JUDGE_TYPE_BOC_8;
        } else {
            type = Constants.AUDIO_REG_JUDGE_TYPE_BOSH_801;
        }
        ResponseVO<List<RegularData>> responseVO = tsService.verifyVideoSpeech(type,
                videoPath, ftpConfigJson);
        List<RegularData> regularDatas = responseVO.getBody();
        if (regularDatas.get(0).getPass() == 1) {
            // 语音通过
            return true;
        }
        return false;
    }

    @Override
    public ReasonCategoryVO invokeIdcardBackAutoProcess(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit, AUserProductAuditStatusEntity oldProductAuditStatus,
            Map<String, ReasonCategoryVO> lastAutoResult) throws AutoFilterException {
        // 判断身份证背面是否存在（5.1以及之前版本的中银用户没有身份证背面）
        if (StringUtils.isBlank(newUserProductAudit.getPhotoBack())) {
            return null;
        }
        ReasonCategoryVO reasonCategoryVO = new ReasonCategoryVO(Constants.AUDIT_CATEGORY_TYPE_IDCARDBACK);
        //对存在身份证背面的用户进行飘绿处理
        if (lastAutoResult != null) {
            //非首次进入审批
            ReasonCategoryVO oldBackIdCardReaCategoryVO = lastAutoResult.get(ReasonCategoryVO.KEY_BACK_IDCARD);
            if (oldBackIdCardReaCategoryVO == null) {
                throw new AutoFilterException(AutoFilterError.LAST_AUDIT_BACK_IC_NULL);
            }
            //对比历史数据，判断本次是否变更
            if (!this.judgeCurrBackICHasChange(newUserProductAudit, oldUserProductAudit)) {
                //和上次相比，没有变更
                if (oldBackIdCardReaCategoryVO.getWholeGreenFlag() != null && oldBackIdCardReaCategoryVO.getWholeGreenFlag() == 1) {
                    // 身份证国徽面自动飘绿
                    reasonCategoryVO.setWholeGreenFlag(1);
                    reasonCategoryVO.setGreenIds(oldBackIdCardReaCategoryVO.getGreenIds());
                    reasonCategoryVO.setMarkIds(oldBackIdCardReaCategoryVO.getMarkIds());
                } else {
                    // 显示所有
                }
            }
        }
        return reasonCategoryVO;
    }

    /**
     * 判断本次身份证有没有变更（只针对人相面）
     * 
     * @param newUserProductAudit
     * @param oldUserProductAudit
     * @return
     * @throws AutoFilterException
     */
    private boolean judgeCurrICHasChange(AUserProductAuditEntity newUserProductAudit,
            AUserProductAuditEntity oldUserProductAudit) throws AutoFilterException {
        if (StringUtils.isBlank(newUserProductAudit.getPhotoFront())
                || StringUtils.isBlank(oldUserProductAudit.getPhotoFront())) {
            throw new AutoFilterException(AutoFilterError.JUDGE_FRONT_IC_CHANGE_DATA_NULL);
        }
        return !oldUserProductAudit.getPhotoFront().equals(newUserProductAudit.getPhotoFront());
    }

    /**
     * 判断本次身份证有没有变更（只针对国徽面）
     *
     * @param newUserProductAudit
     * @param oldUserProductAudit
     * @return
     * @throws AutoFilterException
     */
    private boolean judgeCurrBackICHasChange(AUserProductAuditEntity newUserProductAudit,
                                         AUserProductAuditEntity oldUserProductAudit) throws AutoFilterException {
        if (StringUtils.isBlank(newUserProductAudit.getPhotoBack())
                || StringUtils.isBlank(oldUserProductAudit.getPhotoBack())) {
            throw new AutoFilterException(AutoFilterError.JUDGE_BACK_IC_CHANGE_DATA_NULL);
        }
        return !oldUserProductAudit.getPhotoBack().equals(newUserProductAudit.getPhotoBack());
    }
}
