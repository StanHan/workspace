package demo.hadoop.pcc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class PccMain {
public static String requestString ="DT2015123013697846         10100              321123198007178015                                          YCL周玉才                                                                                                                          上海商嘉经贸发展有限公司                                                                                                                              中山北一路1250号3号楼704室                                                                                                                                                                                                                      CN                                      200437                                                                                                 ";
	public static void main(String[] args) throws IOException {
		PccMain.analysePccRqFile("C:\\Users\\Stan\\Desktop\\信用卡对账单补打\\PCCCUSTS.151215");
	}
	
	public static void analysePccRqFile(String fileName) throws IOException{
		File file = new File(fileName);
		FileInputStream fis = new FileInputStream(file);
		Charset gbk = Charset.forName("GBK");
		InputStreamReader isr = new InputStreamReader(fis,gbk);
		BufferedReader bufferedReader = new BufferedReader(isr);
		for(int i=0;i<10;i++){
			String line = bufferedReader.readLine();
			System.out.println(line);
			RpStmtBean bean = buildMsgToBean(line);
			System.out.println(bean);
		}
	}
	
	public static RpStmtBean buildMsgToBean(String msg){
		RpStmtBean bean = new RpStmtBean();
		bean.setMonth(msg.substring(3-1, 8));
		bean.setCustid(msg.substring(9-1,18));
		bean.setIdtype(msg.substring(28-1,33-1));
		bean.setIdno(msg.substring(47-1,65-1));
		bean.setComp(msg.substring(88-1,128));
		bean.setAdd(msg.substring(128-1, 328));
		bean.setPost(msg.substring(408-1, 418));
		return bean;
	}
	
}

/**
 * @author Stan
 *
 */
class RpStmtBean{
	//db2 "IMPORT FROM importpcc.txt OF ASC METHOD L 
	//(3 8,9 18,19 19,20 39,88 128,128 328,408 418)  MESSAGES Reprintbill_import.log INSERT INTO TOPCARD.RePrintBill
	//(month,custid,idtype,idno,comp,add,post)"
	private String month;//账单月--201512
	private String custid;//客户号--3013697846
	private String idtype;//证件类型--""
	private String idno;//证件号--"        10100     "
	private String comp;//"                   YCL周玉才           "
	private String add;//"                                                                                                              上海商嘉经贸发展有限公司                                                                 "
	private String post;
	
	@Override
	public String toString() {
		return "RpStmtBean [month=" + month + ", custid=" + custid + ", idtype=" + idtype + ", idno=" + idno + ", comp="
				+ comp + ", add=" + add + ", post=" + post + "]";
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getCustid() {
		return custid;
	}
	public void setCustid(String custid) {
		this.custid = custid;
	}
	public String getIdtype() {
		return idtype;
	}
	public void setIdtype(String idtype) {
		this.idtype = idtype;
	}
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	public String getComp() {
		return comp;
	}
	public void setComp(String comp) {
		this.comp = comp;
	}
	public String getAdd() {
		return add;
	}
	public void setAdd(String add) {
		this.add = add;
	}
	public String getPost() {
		return post;
	}
	public void setPost(String post) {
		this.post = post;
	}
}
