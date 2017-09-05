/**
 * @Copyright: 2005-2017 www.2345.com. All rights reserved.
 */
package com.daikuan.platform.ma.service;

import com.daikuan.platform.ma.bean.RegularData;
import com.daikuan.platform.ma.bean.ResponseVO;
import com.daikuan.platform.ma.exception.AutoFilterException;

import java.util.List;

/**
 * @author feizy
 * @version TsService, v0.1 2017/3/21 14:11
 */
public interface TsService {
    /**
     * 调用ts微服务的联系人正则校验
     * 
     * @param type
     * @param relativeContact
     * @param colleagueContact
     * @param friendContact
     * @return
     */
    public ResponseVO<List<RegularData>> verifyContactReg(int type, String relativeContact, String colleagueContact,
            String friendContact) throws AutoFilterException;

    /**
     * 调用ts微服务的联系人是否存在相同校验服务
     * 
     * @param type
     * @param relativeContact
     * @param colleagueContact
     * @param friendContact
     * @return
     */
    public ResponseVO<List<RegularData>> verifySameContactReg(int type, String relativeContact, String colleagueContact,
            String friendContact) throws AutoFilterException;

    /**
     * 调用ts微服务的公司名称正则服务
     * 
     * @param type
     * @param unitname
     * @return
     */
    public ResponseVO<List<RegularData>> verifyUnitName(int type, String unitname) throws AutoFilterException;

    /**
     * 调用ts微服务的单位地址正则校验服务
     * 
     * @param type
     * @param unitAddr
     * @return
     */
    public ResponseVO<List<RegularData>> verifyUnitAddr(int type, String unitAddr) throws AutoFilterException;

    /**
     * 调用ts微服务的家庭地址正则校验服务
     * 
     * @param type
     * @param familyAddr
     * @return
     */
    public ResponseVO<List<RegularData>> verifyFamilyAddr(int type, String familyAddr) throws AutoFilterException;

    /**
     * 调用ts微服务的视频语音校验处理
     * 
     * @param type
     * @param videoFtpPath
     * @return
     */
    public ResponseVO<List<RegularData>> verifyVideoSpeech(int type, String videoFtpPath, String ftpConfigJson)
            throws AutoFilterException;

}
