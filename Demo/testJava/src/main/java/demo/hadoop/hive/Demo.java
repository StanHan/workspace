package demo.hadoop.hive;

import java.io.IOException;
import java.util.Vector;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.Driver;
import org.apache.hadoop.hive.ql.session.SessionState;

import test.hadoop.hdfs.GcsLogBean;

public class Demo {

	public static final String FieldDelimit = "\u007F";//字段分隔符，007F是unicode的127，表示删除符
	
    public static void main(String args[]) {
    	System.out.println("-----------------------Command example-----------------------");
		System.out.println("hadoop jar Test.jar test.hadoop.hive.Demo "
				+ "hdfsFile [i.e /data/han/tmp/aa.txt]");
    	
    	if(args.length ==0){
    		System.err.println("no arguments");
    		System.exit(-1);
    	}
    	GcsLogBean bean = new GcsLogBean();
		bean.setTradeDate("2015-10-19");//交易日期	
		bean.setTradeTime("23:47:45");//交易时间	
		bean.setTradeApplication("FUNDS.TRANSFER");//交易APPLICATION	
		bean.setTradeVersion("ST-FT-STD-AAA");//交易VERSION	
		bean.setTradeCode("ST-FT-STD-AAA");//交易代码	
		bean.setChannelId("S72");//CHANNEL.ID	
		bean.setFbId("179");//FB.ID	
		bean.setFtTxnType("CBIB0001");//本地交易类型	
		bean.setProcessingTime("1198");//处理时间	
		bean.setResultFlag("1");//交易成功/失败标志,1表示成功，否则为失败	
		bean.setRspCode("0000");//报错码,0000表示成功	
		bean.setRspMsg("SUCC");//错误信息	
		bean.setIsReferMoney("1");//是否涉及账务	
		bean.setAmount("123098");//交易金额
		bean.setBfeTraceNo("5f026593-b1cb-4ab8-bbb5-8acf7d8a8d3b");//交易流水
		bean.setAdapter("BFEII.TXN");//核心ADAPTER
		String aa = bean.toBeSavedString(FieldDelimit)+"\n";
		System.out.println(aa);
		
		for (int i = 0; i < args.length; i++) {
			System.out.println("args["+i+"] = "+args[i]);
		}
		
		String hdfsFile = args[0];
		
		try {
			HdfsCommon hdfsCommon = new HdfsCommon(hdfsFile);
			hdfsCommon.writeFile(bean);
			hdfsCommon.writeFile(bean);
			hdfsCommon.writeFile(bean);
			hdfsCommon.writeFile(bean);
			hdfsCommon.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    /*public void test(){
    	Vector<String> res = new Vector<String>();
        String sql = "SELECT * from test";
        Driver driver = new Driver(new HiveConf(SessionState.class));
        int ret = driver.run(sql);
        try {
            driver.getResults(res);
        } catch(IOException e) {
            e.printStackTrace();
        }
        driver.close();
        System.out.println(driver.getMaxRows());
        try {
            System.out.println(driver.getSchema());
            //System.out.println(driver.getThriftSchema());
        } catch(Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
//        System.out.println(res);
//        System.out.println(ret);
    }*/
}
