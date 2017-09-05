package com.daikuan.platform.ma.service;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.daikuan.platform.ma.common.response.ResponseDataVO;
import com.daikuan.platform.ma.pojo.vo.AuditCallbackVO;

/**
 * 
 * @author yangqb
 * @date 2017年3月16日
 * @desc 上游审核回调接口
 */

@FeignClient("IC111")
public interface IAuditCallback {
    /**
     * 审核后回调上游接口-审核通过或拒绝
     * 
     * @param auditCallback
     * @return
     */
    @RequestMapping(value = "/audit/callback/audit", method = RequestMethod.POST)
    ResponseDataVO callbackAudit(@RequestBody AuditCallbackVO auditCallback);

    /**
     * 进件后回调业务接口, 告不要再重传
     * 
     * @param auditCallback
     * @return
     */
    @RequestMapping(value = "/audit/callback/receive/{applyId}", method = RequestMethod.GET)
    ResponseDataVO callbackReceive(@PathVariable(value = "applyId", required = true) Long applyId);

    @RequestMapping(value = "/ic/user/testcallback", method = RequestMethod.GET)
    String test();
}
