package demo.vo;

public class GcsLogBean {

	private String tradeDate;// 交易日期
	private String tradeTime;// 交易时间
	private String tradeApplication;// 交易APPLICATION
	private String tradeVersion;// 交易VERSION
	private String tradeCode;// 交易代码
	private String channelId;// CHANNEL.ID
	private String fbId;// FB.ID
	private String ftTxnType;// 本地交易类型
	private String processingTime;// 处理时间
	private String resultFlag;// 交易成功/失败标志,1表示成功，否则为失败
	private String rspCode;// 报错码,0000表示成功
	private String rspMsg;// 错误信息
	private String isReferMoney;// 是否涉及账务
	private String amount;// 交易金额
	private String bfeTraceNo;// 交易流水
	private String adapter;// 核心ADAPTER

	@Override
	public String toString() {
		return "GcsLogBean [tradeDate=" + tradeDate + ", tradeTime=" + tradeTime + ", tradeApplication="
				+ tradeApplication + ", tradeVersion=" + tradeVersion + ", tradeCode=" + tradeCode + ", channelId="
				+ channelId + ", fbId=" + fbId + ", ftTxnType=" + ftTxnType + ", processingTime=" + processingTime
				+ ", resultFlag=" + resultFlag + ", rspCode=" + rspCode + ", rspMsg=" + rspMsg + ", isReferMoney="
				+ isReferMoney + ", amount=" + amount + ", bfeTraceNo=" + bfeTraceNo + ", adapter=" + adapter + "]";
	}

	public String toBeSavedString(String fieldDelimite) {
		StringBuffer sb = new StringBuffer();
		sb.append("tradeDate=").append(tradeDate).append(fieldDelimite)
				.append("tradeTime=").append(tradeTime).append(fieldDelimite)
				.append("tradeApplication=").append(tradeApplication).append(fieldDelimite)
				.append("tradeVersion=").append(tradeVersion).append(fieldDelimite)
				.append("tradeCode=").append(tradeCode).append(fieldDelimite)
				.append("channelId=").append(channelId).append(fieldDelimite)
				.append("fbId=").append(fbId).append(fieldDelimite)
				.append("ftTxnType=").append(ftTxnType).append(fieldDelimite)
				.append("processingTime=").append(processingTime + fieldDelimite)
				.append("resultFlag=").append(resultFlag).append(fieldDelimite)
				.append("rspCode=").append(rspCode).append(fieldDelimite)
				.append("rspMsg=").append(rspMsg).append(fieldDelimite)
				.append("isReferMoney=").append(isReferMoney).append(fieldDelimite)
				.append("amount=").append(amount).append(fieldDelimite)
				.append("bfeTraceNo=").append(bfeTraceNo).append(fieldDelimite)
				.append("adapter=").append(adapter);
		return sb.toString();
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getTradeApplication() {
		return tradeApplication;
	}

	public void setTradeApplication(String tradeApplication) {
		this.tradeApplication = tradeApplication;
	}

	public String getTradeVersion() {
		return tradeVersion;
	}

	public void setTradeVersion(String tradeVersion) {
		this.tradeVersion = tradeVersion;
	}

	public String getTradeCode() {
		return tradeCode;
	}

	public void setTradeCode(String tradeCode) {
		this.tradeCode = tradeCode;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public String getFtTxnType() {
		return ftTxnType;
	}

	public void setFtTxnType(String ftTxnType) {
		this.ftTxnType = ftTxnType;
	}

	public String getProcessingTime() {
		return processingTime;
	}

	public void setProcessingTime(String processingTime) {
		this.processingTime = processingTime;
	}

	public String getResultFlag() {
		return resultFlag;
	}

	public void setResultFlag(String resultFlag) {
		this.resultFlag = resultFlag;
	}

	public String getRspCode() {
		return rspCode;
	}

	public void setRspCode(String rspCode) {
		this.rspCode = rspCode;
	}

	public String getRspMsg() {
		return rspMsg;
	}

	public void setRspMsg(String rspMsg) {
		this.rspMsg = rspMsg;
	}

	public String getIsReferMoney() {
		return isReferMoney;
	}

	public void setIsReferMoney(String isReferMoney) {
		this.isReferMoney = isReferMoney;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBfeTraceNo() {
		return bfeTraceNo;
	}

	public void setBfeTraceNo(String bfeTraceNo) {
		this.bfeTraceNo = bfeTraceNo;
	}

	public String getAdapter() {
		return adapter;
	}

	public void setAdapter(String adapter) {
		this.adapter = adapter;
	}
}
