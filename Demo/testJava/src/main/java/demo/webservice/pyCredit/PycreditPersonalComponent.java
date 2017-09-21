package demo.webservice.pyCredit;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

/**
 * 鹏元接口处理类
 * 
 * @author Cytus
 *
 */
public class PycreditPersonalComponent {

    private static Map<String, String> linkMap = new HashMap<String, String>();

    /**
     * 发送鹏元报文
     * 
     * @param url
     *            地址 http://10.240.97.198:20011/services/WebServiceSingleQuery
     * @param condition
     *            查询条件
     * @param userId
     *            用户 shhyhwsquery
     * @param password
     *            密码 6rCYRIfcfc4Hetpl39C9ug==
     * @return
     * @throws Exception
     */
    /*
     * public String sendPycredit(String url, String condition, String userId, String password, String appId) throws
     * Exception { condition = (condition + "").replaceAll("<", "&lt;").replaceAll(">", "&gt;") ;
     * 
     * StringBuffer sb = new StringBuffer(); sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
     * sb.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" "); sb.
     * append("xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
     * ); sb.append("<soapenv:Body>");
     * sb.append("<ns1:queryReport soapenv:encodingStyle=\"http://schemas.xmlsoap.org/soap/encoding/\" ");
     * sb.append("xmlns:ns1=\"http://batoffline.report.szpcs.scrc.com\">");
     * sb.append("<userID xsi:type=\"soapenc:string\" xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">");
     * sb.append(userId).append("</userID>"); sb.append("<password xsi:type=\"soapenc:string\" ");
     * sb.append("xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">").append(password).append("</password>");
     * sb.append("<queryCondition xsi:type=\"soapenc:string\" ");
     * sb.append("xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">").append(condition).append(
     * "</queryCondition>"); sb.append("<outputStyle xsi:type=\"soapenc:string\" ");
     * sb.append("xmlns:soapenc=\"http://schemas.xmlsoap.org/soap/encoding/\">xml</outputStyle>");
     * sb.append("</ns1:queryReport></soapenv:Body></soapenv:Envelope>"); System.out.println("send pycredit message:"+
     * sb.toString());
     * 
     * DataObject messageLog = this.insertRcaMessageLog4Py(sb.toString(), appId); String response = ""; try { response =
     * send(url, sb.toString() ,"UTF-8"); } catch (Exception e) { this.updateRcaMessageLog4Py(messageLog, "");
     * e.printStackTrace(); throw e; } response = (response + "").replaceAll("&lt;", "<").replaceAll("&gt;", ">"); int
     * start = response.indexOf("<status>"); int end = response.lastIndexOf("</status>"); String status =
     * response.substring(start+8, end); if(!"1".equals(status)){ return status; } start =
     * response.indexOf("<returnValue>"); end = response.lastIndexOf("</returnValue>"); String returnValue =
     * response.substring(start+13, end); //解码 byte[] bytes = Base64.decode(returnValue); //解压 returnValue =
     * unzipBytes(bytes); this.updateRcaMessageLog4Py(messageLog, returnValue);
     * System.out.println("pycredit return xml message"+ returnValue); return returnValue; }
     */

    /**
     * 解压字节流
     * 
     * @param bytes
     * @return
     * @throws IOException
     */
    public String unzipBytes(byte[] bytes) throws IOException {
        ByteArrayInputStream tempIStream = null;
        BufferedInputStream tempBIStream = null;
        ZipInputStream tempZIStream = null;
        byte[] tempUncompressedBuf = null;
        tempIStream = new ByteArrayInputStream(bytes, 0, bytes.length);
        tempBIStream = new BufferedInputStream(tempIStream);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        tempZIStream = new ZipInputStream(tempBIStream);
        tempZIStream.getNextEntry();
        tempUncompressedBuf = new byte[1024];
        int count = 0;
        while ((count = tempZIStream.read(tempUncompressedBuf)) != -1) {
            outStream.write(tempUncompressedBuf, 0, count);
        }
        outStream.flush();
        outStream.close();
        tempZIStream.close();
        byte[] bs = outStream.toByteArray();
        return new String(bs, "GBK");
    }

    /**
     * 获得鹏远征信发送字符串
     * 
     * @param req
     * @return
     */
    /*
     * public String getSendPycreditStr(PYCreditPersonalReq req) { StringBuilder sb = new StringBuilder();
     * sb.append("<?xml version=\"1.0\" encoding=\"GBK\"?><conditions>"); sb.append("<condition queryType=\""+
     * req.getQueryType() +"\">"); Map<Object, Object> map = req.getItem(); if (map.size() > 0) { for (Object key :
     * map.keySet()) { Object value = map.get(key) ; if(value != null){ sb.append("<item><name>"+ key +"</name>");
     * sb.append("<value>"+value +"</value></item>"); } } } sb.append("</condition></conditions>");
     * System.out.println("pycredit sendMessage:"+ sb); return sb.toString(); }
     */

    /**
     * 鹏元征信插入数据库
     * 
     * @param cisReports
     * @throws Exception
     */
    /*
     * public String insertPycreditInfo(CisReports cisReports, String appId) throws Exception {
     * 
     * Timestamp timestamp = new Timestamp(DateDemo.getJVMDate().getTime()); boolean result = false; try {
     * System.out.println("pycreadit return information insert into the database starts......"); List<CisReport> list =
     * cisReports.getCisReport(); if (list == null || list.size() <= 0) return "3"; for (CisReport cisReport : list) {
     * if (cisReport.getTreatResult() != "2" && cisReport.getTreatResult() !="3") result = true; DataObject
     * scrPycreditInfo; if (cisReport.getEducationInfo2() != null &&
     * "1".equals(cisReport.getEducationInfo2().getTreatResult())) { EducationInfo2 edu2 =
     * cisReport.getEducationInfo2(); List<EducationInfo> edus = edu2.getEducationInfo(); EducationInfo edu =
     * edus.get(0); //只取最高学历 //for (EducationInfo edu : edus) { scrPycreditInfo =
     * DataObjectUtil.createDataObject(ConstAPP.DATA_SRC_PYCREDIT_INFO); scrPycreditInfo.set("pkId",
     * CommonUtil.getUUID()); scrPycreditInfo.set("appId", appId); scrPycreditInfo.set("dIdType", "10100");
     * scrPycreditInfo.set("pyciQueryMsg", cisReport.getTreatResult()); scrPycreditInfo.set("dCustName",
     * edu2.getPersonBaseInfo().getName()); scrPycreditInfo.set("dIdNo", edu2.getPersonBaseInfo().getDocumentNo());
     * scrPycreditInfo.set("pyciRptNo", cisReport.getReportID()); scrPycreditInfo.set("pyciSex",
     * edu2.getPersonBaseInfo().getGender()); if (edu2.getPersonBaseInfo().getAge() != null) {
     * scrPycreditInfo.set("pyciAge", Short.valueOf(edu2.getPersonBaseInfo().getAge())); } if
     * (edu2.getPersonBaseInfo().getBirthday() != null) { scrPycreditInfo.set("pyciBrd",
     * getDate(edu2.getPersonBaseInfo().getBirthday())); } scrPycreditInfo.set("pyciStrAddr",
     * edu2.getPersonBaseInfo().getOriginalAddress()); scrPycreditInfo.set("pyciSchool",
     * edu.getDegreeInfo().getCollege()); scrPycreditInfo.set("pyciMajor", edu.getDegreeInfo().getSpecialty());
     * scrPycreditInfo.set("pyciLastEdu", getLastEduCode(edu.getDegreeInfo().getLevelNo()));
     * scrPycreditInfo.set("pyciEndRst", edu.getDegreeInfo().getStudyResult()); scrPycreditInfo.set("pyciEduTyp",
     * edu.getDegreeInfo().getStudyType()); scrPycreditInfo.set("pyciStudyTyp", edu.getDegreeInfo().getStudyStyle()); if
     * (edu.getDegreeInfo().getStartTime() != null) { scrPycreditInfo.set("pyciStrDt",
     * getDate(edu.getDegreeInfo().getStartTime())); } if (edu.getDegreeInfo().getGraduateTime() != null) {
     * scrPycreditInfo.set("pyciEndDt", getDate(edu.getDegreeInfo().getGraduateTime())); }
     * scrPycreditInfo.set("pyciSchoolAddr", edu.getCollegeInfo().getAddress()); scrPycreditInfo.set("pyciSchoolAttr",
     * edu.getCollegeInfo().getColgCharacter()); scrPycreditInfo.set("pyciIs211", edu.getCollegeInfo().getIs211()); if
     * (cisReports.getReceiveTime() != null) { scrPycreditInfo.set("pyciQueryTm", new
     * Timestamp(getDate(cisReports.getReceiveTime()).getTime())); } IUserObject user = CommonUtil.getIUserObject();
     * scrPycreditInfo.set("inputUsr", user.getUserId()); scrPycreditInfo.set("inputOrg",
     * user.getAttributes().get("orgcode"));
     * 
     * scrPycreditInfo.set("inputDt", timestamp); scrPycreditInfo.set("lastChgUsr", user.getUserId());
     * scrPycreditInfo.set("lastChgDt", timestamp); DatabaseUtil.insertEntity("", scrPycreditInfo); //} }
     * 
     * if (cisReport.getLastEducationInfo() != null && "1".equals(cisReport.getLastEducationInfo().getTreatResult())) {
     * LastEducationInfo lastEdu = cisReport.getLastEducationInfo(); scrPycreditInfo =
     * DataObjectUtil.createDataObject(ConstAPP.DATA_SRC_PYCREDIT_INFO); scrPycreditInfo.set("pkId",
     * CommonUtil.getUUID()); scrPycreditInfo.set("appId", appId); scrPycreditInfo.set("pyciQueryMsg",
     * cisReport.getTreatResult()); scrPycreditInfo.set("dCustName", lastEdu.getPersonBaseInfo().getName());
     * scrPycreditInfo.set("dIdNo", lastEdu.getPersonBaseInfo().getDocumentNo()); scrPycreditInfo.set("dIdType",
     * "10100"); scrPycreditInfo.set("pyciRptNo", cisReport.getReportID()); scrPycreditInfo.set("pyciSex",
     * lastEdu.getPersonBaseInfo().getGender()); if (lastEdu.getPersonBaseInfo().getAge() != null) {
     * scrPycreditInfo.set("pyciAge", Short.valueOf(lastEdu.getPersonBaseInfo().getAge())); } if
     * (lastEdu.getPersonBaseInfo().getBirthday() != null) { scrPycreditInfo.set("pyciBrd",
     * getDate(lastEdu.getPersonBaseInfo().getBirthday())); } scrPycreditInfo.set("pyciStrAddr",
     * lastEdu.getPersonBaseInfo().getOriginalAddress()); scrPycreditInfo.set("pyciSchool",
     * lastEdu.getEducationInfo().getDegreeInfo().getCollege()); scrPycreditInfo.set("pyciMajor",
     * lastEdu.getEducationInfo().getDegreeInfo().getSpecialty()); scrPycreditInfo.set("pyciLastEdu",
     * getLastEduCode(lastEdu.getEducationInfo().getDegreeInfo().getLevelNo())); scrPycreditInfo.set("pyciEndRst",
     * lastEdu.getEducationInfo().getDegreeInfo().getStudyResult()); scrPycreditInfo.set("pyciEduTyp",
     * lastEdu.getEducationInfo().getDegreeInfo().getStudyType()); scrPycreditInfo.set("pyciStudyTyp",
     * lastEdu.getEducationInfo().getDegreeInfo().getStudyStyle()); if
     * (lastEdu.getEducationInfo().getDegreeInfo().getStartTime() != null) { scrPycreditInfo.set("pyciStrDt",
     * getDate(lastEdu.getEducationInfo().getDegreeInfo().getStartTime())); } if
     * (lastEdu.getEducationInfo().getDegreeInfo().getGraduateTime() != null) { scrPycreditInfo.set("pyciEndDt",
     * getDate(lastEdu.getEducationInfo().getDegreeInfo().getGraduateTime())); } scrPycreditInfo.set("pyciSchoolAddr",
     * lastEdu.getEducationInfo().getCollegeInfo().getAddress()); scrPycreditInfo.set("pyciSchoolAttr",
     * lastEdu.getEducationInfo().getCollegeInfo().getColgCharacter()); scrPycreditInfo.set("pyciIs211",
     * lastEdu.getEducationInfo().getCollegeInfo().getIs211()); if (cisReports.getReceiveTime() != null) {
     * scrPycreditInfo.set("pyciQueryTm", new Timestamp(getDate(cisReports.getReceiveTime()).getTime())); } IUserObject
     * user = CommonUtil.getIUserObject(); scrPycreditInfo.set("inputUsr", user.getUserId());
     * scrPycreditInfo.set("inputOrg", user.getAttributes().get("orgcode")); scrPycreditInfo.set("inputDt", timestamp);
     * scrPycreditInfo.set("lastChgUsr", user.getUserId()); scrPycreditInfo.set("lastChgDt", timestamp);
     * DatabaseUtil.insertEntity("", scrPycreditInfo); } }
     * 
     * System.out.println("Insert success!"); } catch (Exception e) { System.out.println("Insert exception!", e);
     * e.printStackTrace(); throw new Exception(e); } if (result) return "0"; else return "3"; }
     */

    /**
     * 将字符串格式的日期格式化成date类型
     * 
     * @param date
     *            yyyyMMdd HH:mm:ss格式的字符串日期
     * @return
     * @throws ParseException
     */
    public Date getDate(String date) throws ParseException {

        if (date == null || "".equals(date))
            return null;
        SimpleDateFormat format = null;
        if (date.length() == 4) {
            return new Date(Integer.valueOf(date) - 1900, 6, 1);
        } else if (date.length() == 6) {
            format = new SimpleDateFormat("yyyyMM");
        } else if (date.length() == 8)
            format = new SimpleDateFormat("yyyyMMdd");
        else
            format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        return format.parse(date);
    }

    /**
     * 获得鹏元征信发送信息
     * 
     * @param appId
     * @return
     */
    /*
     * public PYCreditPersonalReq getPyCreditPersonalReq(String appId) {
     * 
     * PYCreditPersonalReq req = null;
     * 
     * try { System.out.println("pycredit began to send information to obtain....."); req = new PYCreditPersonalReq();
     * Object[] objects = DatabaseExt.queryByNamedSql("", ConstAPP.QUERY_SEND_PY_INFO, appId); if (objects.length > 0) {
     * Object obj = objects[0]; if (obj instanceof Map) { Map map = (Map) obj; //String type = (String)
     * map.get("FLW_PY_TYP"); //2014-12-10 防止转换时不存在或该字段值为null报错 //if ("2".equals(type)) { //新版学历
     * req.setQueryType("25120"); Map<Object, Object> item = new HashMap<Object, Object>(); item.put("name",
     * map.get("PNL_NAME")); item.put("documentNo", map.get("PNL_ID_NO")); item.put("subreportIDs", "11103");
     * item.put("queryReasonID", "201"); item.put("distibuteFlag", null); item.put("refID", map.get("APP_ID"));
     * req.setItem(item); //} else { //最高学历 2014-12-10 modify by Cytus 在重启行外征信时，流程配置没有配置鹏元信息则默认查询最高学历
     * req.setQueryType("25120"); Map<Object, Object> item = new HashMap<Object, Object>(); item.put("name",
     * map.get("PNL_NAME")); item.put("documentNo", map.get("PNL_ID_NO")); item.put("subreportIDs", "11103");
     * item.put("queryReasonID", "201"); item.put("distibuteFlag", null); item.put("refID", map.get("APP_ID"));
     * item.put("levelNo", null); item.put("graduateYear", "2003"); item.put("college", null); req.setItem(item); //} }
     * } } catch (Exception e) { LogUtil.logError("get fail!", e); e.printStackTrace(); }
     * 
     * return req; }
     */

    /**
     * 发送信息
     * 
     * @param url
     * @param xml
     * @return
     * @throws Exception
     */
    public String send(String url, String xml) throws Exception {
        HttpResponse response = HttpRequest.post(url).contentType("application/soap+xml; charset=utf-8")
                .header("SOAPAction", "").body(xml).send();
        int statusCode = response.statusCode();
        String soapResponseData = response.bodyText();
        System.out.println("receive pycredit return message:" + soapResponseData);
        if (statusCode == 200) {
            return soapResponseData;
        } else {
            throw new Exception("返回信息：" + statusCode);
        }
    }

    /**
     * 发送信息
     * 
     * @param url
     * @param xml
     * @param encoding
     * @return
     * @throws Exception
     */
    public String send(String url, String xml, String encoding) throws Exception {
        HttpResponse response = HttpRequest.post(url).charset(encoding).header("SOAPAction", "")
                .body(xml.getBytes(encoding), "application/soap+xml; charset=" + encoding).send();
        int statusCode = response.statusCode();
        String soapResponseData = response.bodyText();
        System.out.println("receive pycredit return message:" + soapResponseData);
        if (statusCode == 200) {
            return soapResponseData;
        } else {
            throw new Exception("返回信息：" + statusCode);
        }
    }

    /**
     * 毕业证书编号规则： 毕（结）业证书编号即为注册号，使用阿拉伯数字，统一规范为17位。 （一）普通、成人高等教育毕（结）业证书注册号由学校或其他教育机构按以下顺序编排： 前5位为学校或其他教育机构的国标代码；
     * 第6位为办学类型代码； 第7至10位为年份； 第11至12位为培养层次代码； 第13位至17位为学校对毕（结）业证书编排的序号。
     * （二）高等教育自学考试及高等教育学历文凭考试毕业证书注册号，由各省（自治区、直辖市）高等教育自学考试委员会按以下顺序编排： 第1位为办学类型代码； 第2位为培养层次代码； 第3、4位为省（自治区、直辖市）国标代码；
     * 第5、6位为地（市）国标代码； 第7、8位为县（区）国标代码； 第9、10位为年度代码； 第11位为上、下半年考试考次代码； 第12至16位为准考证序号代码； 第17位为校验代码。 （三）办学类型代码： 普通高等教育1；
     * 成人高等教育5； 高等教育自学考试和高等教育学历文凭考试6。 （四）培养层次代码： 博士研究生01； 硕士研究生02； 第二学士学位04； 本科05； 专科（含高职）06。
     * 2002年学历证书编号扩充至18位(但当年有部分高校仍使用17位)
     */ // modify by Cytus 2014-11-27 鹏元本科以上学历要求高亮显示，故而将中文名称转化为字典项
    static {
        linkMap.put("01", "01"); // 博士研究生
        linkMap.put("02", "02"); // 硕士研究生
        linkMap.put("05", "03"); // 本科
        linkMap.put("04", "11"); // 第二学士学位
        linkMap.put("06", "12"); // 专科（含高职）
    }

    /**
     * t通过学位证编号获得学历层次
     * 
     * @param eduCode
     * @return
     */
    private String getLastEduCode(String eduCode) {
        if (eduCode == null || "".equals(eduCode))
            return "";
        else {
            try {
                String temp = eduCode.substring(10, 12);
                if (linkMap.containsKey(temp))
                    return linkMap.get(temp).toString();
                else
                    return "";
            } catch (StringIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    /**
     * 鹏元接口插入数据库日志表
     * 
     * @param sendXml
     *            发送报文
     * @param appId
     *            流水号
     * @return
     */
    /*
     * public DataObject insertRcaMessageLog4Py(String sendXml, String transq) { try { IUserObject user =
     * CommonUtil.getIUserObject(); DataObject messageLog = DataObjectUtil.createDataObject(ConstAPP.DATA_RCA_MSG_LOG);
     * messageLog.set("logNo", CommonUtil.getUUID()); messageLog.set("msgNo", transq); messageLog.set("serviceCode",
     * "PYCDT"); messageLog.set("transType", "Pycredit"); messageLog.set("loginId", user.getUserId());
     * messageLog.set("distId", "PY"); messageLog.set("accessType", "HTTP"); messageLog.set("srcId", "RCA");
     * messageLog.set("tradeBrId", user.getAttributes().get("orgcode")); messageLog.set("tradeDate",
     * TimeUtil.getCurDate()); messageLog.set("tradeUser", user.getUserName()); messageLog.set("status", "1");
     * messageLog.set("reqTime", TimeUtil.getDateTime()); messageLog.set("reqMsg", sendXml);
     * DatabaseUtil.insertEntity("", messageLog); return messageLog; } catch (Exception e) { e.printStackTrace(); }
     * return null; }
     */

    /**
     * 鹏元更新报文信息
     * 
     * @param messageLog
     * @param receiveXml
     *            接受报文
     */
    /*
     * public void updateRcaMessageLog4Py(DataObject messageLog, String receiveXml) { try { messageLog.set("status",
     * "2"); messageLog.set("repTime", TimeUtil.getDateTime()); messageLog.set("repMsg", receiveXml);
     * DatabaseUtil.updateEntity("", messageLog); } catch (Exception e) { e.printStackTrace(); } }
     */

    /**
     * 鹏元查询控制
     * 
     * @param appId
     * @return
     * @throws Exception
     */
    /*
     * public DataObject checkIfSendPycredit(String appId) throws Exception { DataObject appBasePersonal =
     * DataObjectUtil.createDataObject(ConstREC.DATA_APP_BASE_PERSONAL); appBasePersonal.set("appId", appId);
     * DataObject[] appBasePersonals = DatabaseUtil.queryEntitiesByTemplate("", appBasePersonal); if
     * (appBasePersonals.length > 0) { appBasePersonal = appBasePersonals[0]; Map<Object, Object> paramMap = new
     * HashMap<Object, Object>(); paramMap.put("dIdType", appBasePersonal.get("pnlIdType")); paramMap.put("dIdNo",
     * appBasePersonal.get("pnlIdNo")); paramMap.put("minDate", TimeUtil.getDateStr(TimeUtil.dateToStr(), -1));
     * paramMap.put("maxDate", TimeUtil.dateToStr()); DataObject[] scrPycredits =
     * DataObjectUtil.convertDataObjects(DatabaseExt.queryByNamedSql( "", ConstAPP.QUERY_SEARCH_HISTROY_INFO, paramMap),
     * ConstAPP.DATA_SRC_PYCREDIT_INFO, true); return scrPycredits.length > 0 ? scrPycredits[0] : null; } return null; }
     */

    /**
     * 将历史信息复制一条新的数据
     * 
     * @param pycreditInfo
     * @throws Exception
     */
    /*
     * public void copyPycreditHistoryInfo(DataObject scrPycreditInfo, String appId) throws Exception { IUserObject user
     * = CommonUtil.getIUserObject(); Timestamp timestamp = new Timestamp(DateDemo.getJVMTimeMillis());
     * scrPycreditInfo.set("pkId", CommonUtil.getUUID()); scrPycreditInfo.set("appId", appId);
     * scrPycreditInfo.set("inputUsr", user.getUserId()); scrPycreditInfo.set("inputOrg",
     * user.getAttributes().get("orgcode")); scrPycreditInfo.set("inputDt", timestamp);
     * scrPycreditInfo.set("lastChgUsr", user.getUserId()); scrPycreditInfo.set("lastChgDt", timestamp);
     * DatabaseUtil.insertEntity("", scrPycreditInfo); }
     */

}
