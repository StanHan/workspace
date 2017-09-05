/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.service.impl;

import com.daikuan.platform.ma.bean.RegularData;
import com.daikuan.platform.ma.bean.ResponseVO;
import com.daikuan.platform.ma.exception.AutoFilterError;
import com.daikuan.platform.ma.exception.AutoFilterException;
import com.daikuan.platform.ma.service.TsService;
import com.daikuan.platform.ma.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.List;

/**
 * @author feizy
 * @version TsServiceImpl, v0.1 2017/3/21 14:11
 */
@Service
public class TsServiceImpl implements TsService {
    private static final String TS_URL_REG_VERIFY_CONTACT = "http://TS//ts/reg/verify/contact";
    private static final String TS_URL_REG_VERIFY_SAME_CONTACT = "http://TS//ts/reg/verify/contact/samename";
    private static final String TS_URL_REG_VERIFY_UNITNAME = "http://TS//ts/reg/verify/unitname";
    private static final String TS_URL_REG_VERIFY_ADDRESS = "http://TS//ts/reg/verify/address";
    private static final String TS_URL_REG_VERIFY_VIDEO = "http://TS//ts/reg/verify/audio";
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public ResponseVO<List<RegularData>> verifyContactReg(int type, String relativeContact, String colleagueContact,
            String friendContact) throws AutoFilterException {
        // 封装参数
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("type", type);
        params.add("fromSys", Constants.SYSTEM_NAME);
        params.add("relativeContact", relativeContact);
        params.add("colleagueContact", colleagueContact);
        params.add("friendContact", friendContact);
        ResponseVO responseVO = restTemplate.exchange(TS_URL_REG_VERIFY_CONTACT, HttpMethod.POST,
                new HttpEntity<MultiValueMap<String, Object>>(params),
                new ParameterizedTypeReference<ResponseVO<List<RegularData>>>() {
                }).getBody();
        if (responseVO == null || !ResponseVO.REQUESTION_SUCCESS_CODE.equals(responseVO.getResult())) {
            throw new AutoFilterException(AutoFilterError.REQ_TS_CONTACT_REG_RESPONSE_EXCEP);
        }
        return responseVO;
    }

    @Override
    public ResponseVO<List<RegularData>> verifySameContactReg(int type, String relativeContact, String colleagueContact,
            String friendContact) throws AutoFilterException {
        // 封装参数
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("fromSys", Constants.SYSTEM_NAME);
        params.add("type", type);
        params.add("relativeContact", relativeContact);
        params.add("colleagueContact", colleagueContact);
        params.add("friendContact", friendContact);
        ResponseVO<List<RegularData>> responseVO = restTemplate.exchange(TS_URL_REG_VERIFY_SAME_CONTACT,
                HttpMethod.POST, new HttpEntity<MultiValueMap<String, Object>>(params),
                new ParameterizedTypeReference<ResponseVO<List<RegularData>>>() {
                }).getBody();
        if (responseVO == null || !ResponseVO.REQUESTION_SUCCESS_CODE.equals(responseVO.getResult())) {
            throw new AutoFilterException(AutoFilterError.REQ_TS_SAME_CONTACT_REG_RESPONSE_EXCEP);
        }
        return responseVO;
    }

    @Override
    public ResponseVO<List<RegularData>> verifyUnitName(int type, String unitname) throws AutoFilterException {
        // 封装参数
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("fromSys", Constants.SYSTEM_NAME);
        params.add("type", type);
        params.add("unitname", unitname);
        ResponseVO<List<RegularData>> responseVO = restTemplate.exchange(TS_URL_REG_VERIFY_UNITNAME, HttpMethod.POST,
                new HttpEntity<MultiValueMap<String, Object>>(params),
                new ParameterizedTypeReference<ResponseVO<List<RegularData>>>() {
                }).getBody();
        if (responseVO == null || !ResponseVO.REQUESTION_SUCCESS_CODE.equals(responseVO.getResult())
                || responseVO.getBody() == null || responseVO.getBody().size() < 1) {
            throw new AutoFilterException(AutoFilterError.REQ_TS_UNITNAME_REG_RESPONSE_EXCEP);
        }
        return responseVO;
    }

    @Override
    public ResponseVO<List<RegularData>> verifyUnitAddr(int type, String unitAddr) throws AutoFilterException {
        // 封装参数
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("fromSys", Constants.SYSTEM_NAME);
        params.add("type", type);
        params.add("unitAddress", unitAddr);
        ResponseVO<List<RegularData>> responseVO = restTemplate.exchange(TS_URL_REG_VERIFY_ADDRESS, HttpMethod.POST,
                new HttpEntity<MultiValueMap<String, Object>>(params),
                new ParameterizedTypeReference<ResponseVO<List<RegularData>>>() {
                }).getBody();
        if (responseVO == null || !ResponseVO.REQUESTION_SUCCESS_CODE.equals(responseVO.getResult())
                || responseVO.getBody() == null || responseVO.getBody().size() < 1) {
            throw new AutoFilterException(AutoFilterError.REQ_TS_UNITNADDR_REG_RESPONSE_EXCEP);
        }
        return responseVO;
    }

    @Override
    public ResponseVO<List<RegularData>> verifyFamilyAddr(int type, String familyAddr) throws AutoFilterException {
        // 封装参数
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("fromSys", Constants.SYSTEM_NAME);
        params.add("type", type);
        params.add("familyAddress", familyAddr);
        ResponseVO<List<RegularData>> responseVO = restTemplate.exchange(TS_URL_REG_VERIFY_ADDRESS, HttpMethod.POST,
                new HttpEntity<MultiValueMap<String, Object>>(params),
                new ParameterizedTypeReference<ResponseVO<List<RegularData>>>() {
                }).getBody();
        if (responseVO == null || !ResponseVO.REQUESTION_SUCCESS_CODE.equals(responseVO.getResult())
                || responseVO.getBody() == null || responseVO.getBody().size() < 1) {
            throw new AutoFilterException(AutoFilterError.REQ_TS_FAMILY_ADDR_REG_RESPONSE_EXCEP);
        }
        return responseVO;
    }

    @Override
    public ResponseVO<List<RegularData>> verifyVideoSpeech(int type, String videoFtpPath, String ftpConfigJson)
            throws AutoFilterException {
        // 封装参数
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("fromSys", Constants.SYSTEM_NAME);
        params.add("type", type);
        params.add("videoFtpPath", videoFtpPath);
        params.add("ftpConfigJson", ftpConfigJson);
        ResponseVO<List<RegularData>> responseVO = restTemplate.exchange(TS_URL_REG_VERIFY_VIDEO, HttpMethod.POST,
                new HttpEntity<MultiValueMap<String, Object>>(params),
                new ParameterizedTypeReference<ResponseVO<List<RegularData>>>() {
                }).getBody();
        if (responseVO == null || !ResponseVO.REQUESTION_SUCCESS_CODE.equals(responseVO.getResult())
                || responseVO.getBody() == null || responseVO.getBody().size() < 1) {
            throw new AutoFilterException(AutoFilterError.REQ_TS_VIDEO_REG_RESPONSE_EXCEP);
        }
        return responseVO;
    }
}
