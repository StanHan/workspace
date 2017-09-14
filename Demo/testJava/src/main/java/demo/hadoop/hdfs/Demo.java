package demo.hadoop.hdfs;

import demo.vo.GcsLogBean;

public class Demo {

	public static void main(String[] args) {
		test();
	}
	public static void test() {
		GcsLogBean bean = new GcsLogBean();
		bean.setTradeDate("2015-10-19");// 交易日期
		bean.setTradeTime("23:47:45");// 交易时间
		bean.setTradeApplication("FUNDS.TRANSFER");// 交易APPLICATION
		bean.setTradeVersion("ST-FT-STD-AAA");// 交易VERSION
		bean.setTradeCode("ST-FT-STD-AAA");// 交易代码
		bean.setChannelId("S72");// CHANNEL.ID
		bean.setFbId("179");// FB.ID
		bean.setFtTxnType("CBIB0001");// 本地交易类型
		bean.setProcessingTime("1198");// 处理时间
		bean.setResultFlag("1");// 交易成功/失败标志,1表示成功，否则为失败
		bean.setRspCode("0000");// 报错码,0000表示成功
		bean.setRspMsg("SUCC");// 错误信息
		bean.setIsReferMoney("1");// 是否涉及账务
		bean.setAmount("123098");// 交易金额
		bean.setBfeTraceNo("5f026593-b1cb-4ab8-bbb5-8acf7d8a8d3b");// 交易流水
		bean.setAdapter("BFEII.TXN");// 核心ADAPTER
		System.out.println(bean.toBeSavedString("\u007F"));
		String[] aa = bean.toBeSavedString("\u007F").split("\u007F");
		for (String a : aa) {
			System.out.println(a);
		}
	}
}
