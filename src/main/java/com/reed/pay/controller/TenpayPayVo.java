package com.reed.pay.controller;

public class TenpayPayVo {

	private String retCode;
	private String retMsg;
	private TenpayPayInfoVo payInfo;
	
	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public TenpayPayInfoVo getPayInfo() {
		return payInfo;
	}

	public void setPayInfo(TenpayPayInfoVo payInfo) {
		this.payInfo = payInfo;
	}

}
