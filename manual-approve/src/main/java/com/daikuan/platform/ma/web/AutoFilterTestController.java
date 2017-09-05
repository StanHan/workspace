/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.web;

import com.daikuan.platform.ma.bean.ProductApproveStatisticsVO;
import com.daikuan.platform.ma.bean.ReasonCategoryVO;
import com.daikuan.platform.ma.common.vo.AuditStatisticsVO;
import com.daikuan.platform.ma.exception.AutoFilterException;
import com.daikuan.platform.ma.pojo.AUserProductAuditEntity;
import com.daikuan.platform.ma.service.AuditStatisticsService;
import com.daikuan.platform.ma.service.AutoDealFilterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @author feizy
 * @version AutoFilterTestController, v0.1 2017/3/24 10:08
 */
@RestController
@RequestMapping("/ma/test")
public class AutoFilterTestController {
    @Autowired
    private AutoDealFilterService autoDealFilterService;
    @Autowired
    private AuditStatisticsService auditStatisticsService;
   /* @Value("${config.test}")*/
    @Value("test")
    private String configTest;

    @RequestMapping("/autofilter/start")
    @CrossOrigin(origins = "*")
    public void testAutoFilterStart(Long userId, String familyContact, String colleagueContact, String friendContact,
            String unitName, String unitAddr, String homeAddr, String videoFtpPath, int frontOcrPass, int faceScoreIcon,
            int faceScoreVideo) throws AutoFilterException, IOException {
        System.err.println(configTest);
        AUserProductAuditEntity aUserProductAuditEntity = new AUserProductAuditEntity();
        aUserProductAuditEntity.setApplyAt(new Date());
        aUserProductAuditEntity.setApplyId(20170324001L);
        aUserProductAuditEntity.setApplyTimes((byte) 1);
        aUserProductAuditEntity.setAuditType((byte) 1);
        aUserProductAuditEntity.setCreateAt(new Date());
        aUserProductAuditEntity.setCustomerName("feizy");
        aUserProductAuditEntity.setCreditType((byte) 1);
        aUserProductAuditEntity.setFaceScoreIcon(faceScoreIcon);
        aUserProductAuditEntity.setFaceScoreVideo(faceScoreVideo);
        aUserProductAuditEntity.setHomeAddr(homeAddr);
        aUserProductAuditEntity.setHomeCity("旧金山");
        aUserProductAuditEntity.setHomeDistrict("g");
        aUserProductAuditEntity.setHomeProvince("美国");
        aUserProductAuditEntity.setIcEmblemIssuer("1");
        aUserProductAuditEntity.setIcEmblemNationality("1");
        aUserProductAuditEntity.setIcEmblemType("1");
        aUserProductAuditEntity.setIcEmblemValidEnd("1");
        aUserProductAuditEntity.setIcEmblemValidStart("1");
        aUserProductAuditEntity.setIcPortraitAddress("1");
        aUserProductAuditEntity.setIcPortraitBirthday("1");
        aUserProductAuditEntity.setIcPortraitEthnicity((byte) 1);

        aUserProductAuditEntity.setIcPortraitOcrPass((byte) frontOcrPass);
        aUserProductAuditEntity.setId(null);
        aUserProductAuditEntity.setIdNo("32055566");
        aUserProductAuditEntity.setJobUnit(unitName);
        aUserProductAuditEntity.setPhotoBack("/fgg");
        aUserProductAuditEntity.setPhotoRealTime("/fasgf");
        aUserProductAuditEntity.setPhotoFront("/g");
        aUserProductAuditEntity.setRelaColleague(colleagueContact);
        aUserProductAuditEntity.setRelaFamily(familyContact);
        aUserProductAuditEntity.setRelaFriend(friendContact);
        aUserProductAuditEntity.setUnitAddr(unitAddr);
        aUserProductAuditEntity.setVideo(videoFtpPath);
        aUserProductAuditEntity.setUnitProvince("江苏");
        aUserProductAuditEntity.setUnitTelArea("盐城市");
        aUserProductAuditEntity.setUnitsDistrict("阜宁县");
        aUserProductAuditEntity.setUserId(userId);
        Map<String, ReasonCategoryVO> reasonCategoryVOMap = autoDealFilterService
                .enterAutoDealService(aUserProductAuditEntity);
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(reasonCategoryVOMap);
        System.err.println(s);
    }

    @RequestMapping("/statistics/query")
    @CrossOrigin(origins = "*")
    public void testProductApproveStatistics(int productId, long beginTime, long endTime) {
        AuditStatisticsVO productApproveStatisticsVO = auditStatisticsService.countEnterAuditPiece(productId,
                beginTime, endTime);
        System.err.println(productApproveStatisticsVO.toString());
    }
}
