package com.reed.pay.bill99.entry;

import java.io.Serializable;
import java.util.List;

/**
 * 与批次交易相关的数据
 * */
public class DealInfoEntity implements Serializable {
	/** 
	 * 自动增长ID
	 */
	private static final long serialVersionUID = 316185246251023757L;
	
	/** 版本号*/
	private String version="1.0";
	/** 提交类型*/
	private String serviceType;
	/** 商户编号*/
	private String memberCode;
	/** 加密类型*/
	private String featureCode;
//	/** 上传结果*/
//	private String ftpUploadResult;
	
	/** 付款方帐号*/
	private String payerAcctCode;
	/** 批次号*/
	private String batchNo;
	/** 发起日期*/
	private String applyDate;
	/** 付款人名称*/
	private String name;
	/** 总金额*/
	private String totalAmt;
	/** 总笔数*/
	private String totalCnt;
	/** 付费方式*/
	private String feeType;
	/** 币种*/
	private String cur;
	/** 是否验证金额*/
	private String checkAmtCnt;
	/** 是否整批失败*/
	private String batchFail;
	/** 充值方式*/
	private String rechargeType;
	/** 是否自动退款*/
	private String autoRefund;
	/** 是否短信通知*/
	private String phoneNoteFlag;
	/** 预留字段1*/
	private String merchantMemo1;
	/** 预留字段2*/
	private String merchantMemo2;
	/** 预留字段3*/
	private String merchantMemo3;
	
	/** 付款信息明细列表*/
	private List<OrderInfoEntity> ordersInfo;

	/** 处理状态*/
	private String status;
	/** 订单号*/
	private String orderSeqId;
	/** 申请成功总金额*/
	private String totalApplySuccAmt;
	/** 申请成功总笔数*/
	private String totalApplySuccCnt;
	/** 总手续费*/
	private String totalFee;
	/** 结束时间*/
	private String finishPayDate;
	/** 快钱预留*/
	private String memo;

//	/** 会员号*/
//	private String memberCode;
	/** 文件名*/
	private String fileName;
	/** 扩展参数*/
	private String ext;
	/** 错误代码*/
	private String errorCode;
	/** 错误描述*/
	private String errorMsg;

	/** 请求时间*/
	private String requestTime;

	/** 是否显示明细*/
	private String listFlag;
	/** 页码*/
	private String page;
	/** 页码显示条数*/
	private String pageSize;
	/** 总页数*/
	private String totalPage;

	/** 商家订单号*/
	private String merchantId;
	/** 起始时间*/
	private String beginApplyDate;
	/** 结束时间*/
	private String endApplyDate;
	/** 银行名称*/
	private String bank;
	/** 卡号*/
	private String bankCardNo;
	/** 开户行*/
	private String branchBank;
	/** 对公对私*/
	private String payeeType;
	/** 省份*/
	private String province;
	/** 城市*/
	private String city;
	/** 查询交易的状态*/
	private String orderStatus;
	/** 查询交易的错误代码 */
	private String orderErrorCode;
	/** 查询交易的银行错误代码 */
	private String orderBankErrorCode;

	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getServiceType() {
		return serviceType;
	}
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	public String getMemberCode() {
		return memberCode;
	}
	public void setMemberCode(String memberCode) {
		this.memberCode = memberCode;
	}
	public String getFeatureCode() {
		return featureCode;
	}
	public void setFeatureCode(String featureCode) {
		this.featureCode = featureCode;
	}
	public String getPayerAcctCode() {
		return payerAcctCode;
	}
	public void setPayerAcctCode(String payerAcctCode) {
		this.payerAcctCode = payerAcctCode;
	}
	public String getBatchNo() {
		return batchNo;
	}
	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(String totalAmt) {
		this.totalAmt = totalAmt;
	}
	public String getTotalCnt() {
		return totalCnt;
	}
	public void setTotalCnt(String totalCnt) {
		this.totalCnt = totalCnt;
	}
	public String getFeeType() {
		return feeType;
	}
	public void setFeeType(String feeType) {
		this.feeType = feeType;
	}
	public String getCur() {
		return cur;
	}
	public void setCur(String cur) {
		this.cur = cur;
	}
	public String getCheckAmtCnt() {
		return checkAmtCnt;
	}
	public void setCheckAmtCnt(String checkAmtCnt) {
		this.checkAmtCnt = checkAmtCnt;
	}
	public String getBatchFail() {
		return batchFail;
	}
	public void setBatchFail(String batchFail) {
		this.batchFail = batchFail;
	}
	public String getRechargeType() {
		return rechargeType;
	}
	public void setRechargeType(String rechargeType) {
		this.rechargeType = rechargeType;
	}
	public String getAutoRefund() {
		return autoRefund;
	}
	public void setAutoRefund(String autoRefund) {
		this.autoRefund = autoRefund;
	}
	public String getPhoneNoteFlag() {
		return phoneNoteFlag;
	}
	public void setPhoneNoteFlag(String phoneNoteFlag) {
		this.phoneNoteFlag = phoneNoteFlag;
	}
	public String getMerchantMemo1() {
		return merchantMemo1;
	}
	public void setMerchantMemo1(String merchantMemo1) {
		this.merchantMemo1 = merchantMemo1;
	}
	public String getMerchantMemo2() {
		return merchantMemo2;
	}
	public void setMerchantMemo2(String merchantMemo2) {
		this.merchantMemo2 = merchantMemo2;
	}
	public String getMerchantMemo3() {
		return merchantMemo3;
	}
	public void setMerchantMemo3(String merchantMemo3) {
		this.merchantMemo3 = merchantMemo3;
	}
	public List<OrderInfoEntity> getOrdersInfo() {
		return ordersInfo;
	}
	public void setOrdersInfo(List<OrderInfoEntity> ordersInfo) {
		this.ordersInfo = ordersInfo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getOrderSeqId() {
		return orderSeqId;
	}
	public void setOrderSeqId(String orderSeqId) {
		this.orderSeqId = orderSeqId;
	}
	public String getTotalApplySuccAmt() {
		return totalApplySuccAmt;
	}
	public void setTotalApplySuccAmt(String totalApplySuccAmt) {
		this.totalApplySuccAmt = totalApplySuccAmt;
	}
	public String getTotalApplySuccCnt() {
		return totalApplySuccCnt;
	}
	public void setTotalApplySuccCnt(String totalApplySuccCnt) {
		this.totalApplySuccCnt = totalApplySuccCnt;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	public String getFinishPayDate() {
		return finishPayDate;
	}
	public void setFinishPayDate(String finishPayDate) {
		this.finishPayDate = finishPayDate;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public String getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(String requestTime) {
		this.requestTime = requestTime;
	}
	public String getListFlag() {
		return listFlag;
	}
	public void setListFlag(String listFlag) {
		this.listFlag = listFlag;
	}
	public String getPage() {
		return page;
	}
	public void setPage(String page) {
		this.page = page;
	}
	public String getPageSize() {
		return pageSize;
	}
	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}
	public String getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}
	
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getBeginApplyDate() {
		return beginApplyDate;
	}
	public void setBeginApplyDate(String beginApplyDate) {
		this.beginApplyDate = beginApplyDate;
	}
	public String getEndApplyDate() {
		return endApplyDate;
	}
	public void setEndApplyDate(String endApplyDate) {
		this.endApplyDate = endApplyDate;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getBankCardNo() {
		return bankCardNo;
	}
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
	public String getBranchBank() {
		return branchBank;
	}
	public void setBranchBank(String branchBank) {
		this.branchBank = branchBank;
	}
	public String getPayeeType() {
		return payeeType;
	}
	public void setPayeeType(String payeeType) {
		this.payeeType = payeeType;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderErrorCode() {
		return orderErrorCode;
	}
	public void setOrderErrorCode(String orderErrorCode) {
		this.orderErrorCode = orderErrorCode;
	}
	public String getOrderBankErrorCode() {
		return orderBankErrorCode;
	}
	public void setOrderBankErrorCode(String orderBankErrorCode) {
		this.orderBankErrorCode = orderBankErrorCode;
	}
}
