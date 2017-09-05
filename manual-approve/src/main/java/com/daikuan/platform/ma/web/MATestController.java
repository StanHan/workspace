package com.daikuan.platform.ma.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daikuan.platform.ma.common.request.RejectReasonsRequest;
import com.daikuan.platform.ma.dao.ARejectReasonMasterHisMapper;
import com.daikuan.platform.ma.dao.AUserProductAuditMapper;
import com.daikuan.platform.ma.dao.AUserProductAuditStatusMapper;
import com.daikuan.platform.ma.exception.ServiceException;
import com.daikuan.platform.ma.exception.dao.DaoException;
import com.daikuan.platform.ma.pojo.ARejectReasonMasterHisEntity;
import com.daikuan.platform.ma.pojo.AUserProductAuditEntity;
import com.daikuan.platform.ma.pojo.AUserProductAuditStatusEntity;
import com.daikuan.platform.ma.pojo.vo.UserProductAuditVO;
import com.daikuan.platform.ma.service.IAuditCallback;
import com.daikuan.platform.ma.service.IRejectReasonService;
import com.daikuan.platform.ma.service.UserProductAuditService;
import com.github.abel533.entity.Example;
import com.github.abel533.entity.Example.Criteria;

@RestController
@RequestMapping("/platform")
public class MATestController {

    private static Logger logger = LoggerFactory.getLogger(MATestController.class);
    @Autowired
    private IAuditCallback auditCallback;

    @Autowired
    private AUserProductAuditMapper userProductAuditMapper;

    @Autowired
    private UserProductAuditService userProductAuditService;

    @Autowired
    private AUserProductAuditStatusMapper userProductAuditStatusMapper;

    @Autowired
    private ARejectReasonMasterHisMapper rejectReasonHisMapper;

    @Autowired
    private IRejectReasonService rejectReasonService;

    @GetMapping("/test")
    @CrossOrigin(origins = "*")
    public void test() {
        logger.info("test entry ==================");
        String r = auditCallback.test();
        System.out.println(r + "==================");
    }

    @GetMapping("entry")
    @CrossOrigin(origins = "*")
    public void test2() {
        System.out.println("entry .... ");
        AUserProductAuditEntity productAudit = userProductAuditMapper.queryByApplyId(1L);
        productAudit.setId(null);
        productAudit.setApplyId(5L);
        productAudit.setVideo("M00/00/00/rBAXHVjWrKqAc-6SABukbRy5atw233.mp4");
        Map<String, Object> map = new HashMap<String, Object>();

        UserProductAuditVO userProductAuditVO = new UserProductAuditVO();
        BeanUtils.copyProperties(productAudit, userProductAuditVO);
        // 模拟进件
        try {
            userProductAuditService.save(userProductAuditVO, map);
        } catch (Exception e) {
            // 给一次回滚机会
            if (map.get("applyId") != null) {
                userProductAuditService.rollback((long) map.get("applyId"), (long) map.get("adminTaskId"));
            }
        }
    }

    @GetMapping("send")
    @CrossOrigin(origins = "*")
    public void testsend() throws InterruptedException, ServiceException, DaoException {
        System.out.println("send ............. ");
        AUserProductAuditStatusEntity userProductAuditStatus = userProductAuditStatusMapper.queryByProductAuditId(51L);
        userProductAuditService.send(userProductAuditStatus);
    }

    @GetMapping("reject")
    @CrossOrigin(origins = "*")
    public void testreject() throws InterruptedException, ServiceException, DaoException {
        Example example = new Example(ARejectReasonMasterHisEntity.class);
        Criteria createCriteria = example.createCriteria();
        createCriteria.andGreaterThan("id", -1);
        List<ARejectReasonMasterHisEntity> list = rejectReasonHisMapper.selectByExample(example);
        RejectReasonsRequest vo = new RejectReasonsRequest();
        vo.setRejectReasonList(null);
        rejectReasonService.save(vo);
    }

//    public static void main(String[] args) {
//        // AUserProductAuditEntity productAudit = new AUserProductAuditEntity();
//        // productAudit.setApplyId(22L);
//        // UserProductAuditVO userProductAuditVO = new UserProductAuditVO();
//        // BeanUtils.copyProperties(productAudit, userProductAuditVO);
//        // System.out.println(userProductAuditVO.getApplyId());
//        // Map<String, Object> rMap = new HashMap<String, Object>();
//        // System.out.println(rMap.get("jj"));
//        String redIds = "11,22,33";
//        String[] split = redIds.split(",");
//        System.out.println(Arrays.asList(split));
//    }
}
