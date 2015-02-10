package com.reed.pay.bill99.entry;

import java.io.Serializable;

/**
 * 与单笔订单相关的数据
 * */
public class OrderInfoEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/** 商家订单态 */
	private String merchantId;
	/** 金额 */
	private String amt;
	/** 银行名称 */
	private String bank;
	/** 户名 */
	private String name;
	/** 卡号 */
	private String bankCardNo;
	/** 开户行 */
	private String branchBank;
	/** 对公对私 */
	private String payeeType;
	/** 省份 */
	private String province;
	/** 城市 */
	private String city;
	/** 快钱交易备注 */
	private String memo;
	/** 银行交易用途 */
	private String bankPurpose;
	/** 银行交易备注 */
	private String bankMemo;
	/** 收款方通知内容 */
	private String payeeNote;
	/** 收款方手机号 */
	private String payeeMobile;
	/** 收款方邮件 */
	private String payeeEmail;
	/** 到账时效 */
	private String period;
	/** 商户预留字段1 */
	private String merchantMemo1;
	/** 商户预留字段2 */
	private String merchantMemo2;
	/** 商户预留字段3 */
	private String merchantMemo3;
	
	/** 交易申请时间 */
	private String applyDate;
	/** 交易完成时间 */
	private String endDate;
	/** 订单号 */
	private String orderSeqId;
	/** 费用 */
	private String fee;
	/** 明细处理状态 */
	private String status;
	/** 错误代码 */
	private String errorCode;
	/** 错误原因 */
	private String errorMsg;
	/** 银行错误代码 */
	private String bankErrorCode;
	/** 银行错误原因 */
	private String bankErrorMsg;
	
	
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getBankPurpose() {
		return bankPurpose;
	}
	public void setBankPurpose(String bankPurpose) {
		this.bankPurpose = bankPurpose;
	}
	public String getBankMemo() {
		return bankMemo;
	}
	public void setBankMemo(String bankMemo) {
		this.bankMemo = bankMemo;
	}
	public String getPayeeNote() {
		return payeeNote;
	}
	public void setPayeeNote(String payeeNote) {
		this.payeeNote = payeeNote;
	}
	public String getPayeeMobile() {
		return payeeMobile;
	}
	public void setPayeeMobile(String payeeMobile) {
		this.payeeMobile = payeeMobile;
	}
	public String getPayeeEmail() {
		return payeeEmail;
	}
	public void setPayeeEmail(String payeeEmail) {
		this.payeeEmail = payeeEmail;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
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
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getOrderSeqId() {
		return orderSeqId;
	}
	public void setOrderSeqId(String orderSeqId) {
		this.orderSeqId = orderSeqId;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getBankErrorCode() {
		return bankErrorCode;
	}
	public void setBankErrorCode(String bankErrorCode) {
		this.bankErrorCode = bankErrorCode;
	}
	public String getBankErrorMsg() {
		return bankErrorMsg;
	}
	public void setBankErrorMsg(String bankErrorMsg) {
		this.bankErrorMsg = bankErrorMsg;
	}
	
	
	
}
