package com.reed.pay.controller;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 支付宝服务器异步通知form
 * 
 */
public class TenpayNotifyFormBean {

	// 协议参数
	/** 签名方式 */
	private String sign_type;
	/** 字符编码 */
	private String input_charset;
	/** 签名 */
	@NotNull
	private String sign;

	// 业务参数,支付异步通知参数
	/** 交易模式:1-即时到账 */
	private Integer trade_mode;
	/** 交易状态:0-成功 */
	private Integer trade_state;
	/** 商户号 */
	private String partner;
	/** 银行类型：WX-微信 */
	private String bank_type;
	/** 总金额,以分为单位 */
	private Integer total_fee;
	/** 币种:1-人民币 */
	private Integer fee_type;
	/** 通知ID */
	private String notify_id;
	/** 财付通订单号 */
	private String transaction_id;
	/** 商户订单号 */
	private String out_trade_no;
	/** 商户数据包 */
	private String attach;
	/** 支付完成时间 */
	@DateTimeFormat(pattern = "yyyyMMddHHmmss")
	private Date time_end;

	// 业务参数,退款异步通知参数
	/** 返回状态码 */
	private Integer retcode;
	/** 返回消息 */
	private String retmsg;
	// private String partner;
	// private String transaction_id;
	// private String out_trade_no;
	/** 商户退款单号 */
	private String out_refund_no;
	/** 财付通退款单号 */
	private String refund_id;
	/** 退款渠道,0:退到财付通、1:退到银行 */
	private Integer refund_channel;
	/** 退款金额,单位为分 */
	private Integer refund_fee;
	/** 退款状态 */
	private Integer refund_status;
	/** 接收人账号 */
	private String recv_user_id;
	/** 接收人姓名 */
	private String reccv_user_name;

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getInput_charset() {
		return input_charset;
	}

	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Integer getTrade_mode() {
		return trade_mode;
	}

	public void setTrade_mode(Integer trade_mode) {
		this.trade_mode = trade_mode;
	}

	public Integer getTrade_state() {
		return trade_state;
	}

	public void setTrade_state(Integer trade_state) {
		this.trade_state = trade_state;
	}

	public String getPartner() {
		return partner;
	}

	public void setPartner(String partner) {
		this.partner = partner;
	}

	public String getBank_type() {
		return bank_type;
	}

	public void setBank_type(String bank_type) {
		this.bank_type = bank_type;
	}

	public Integer getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(Integer total_fee) {
		this.total_fee = total_fee;
	}

	public Integer getFee_type() {
		return fee_type;
	}

	public void setFee_type(Integer fee_type) {
		this.fee_type = fee_type;
	}

	public String getNotify_id() {
		return notify_id;
	}

	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}

	public String getTransaction_id() {
		return transaction_id;
	}

	public void setTransaction_id(String transaction_id) {
		this.transaction_id = transaction_id;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public Date getTime_end() {
		return time_end;
	}

	public void setTime_end(Date time_end) {
		this.time_end = time_end;
	}

	public Integer getRetcode() {
		return retcode;
	}

	public void setRetcode(Integer retcode) {
		this.retcode = retcode;
	}

	public String getRetmsg() {
		return retmsg;
	}

	public void setRetmsg(String retmsg) {
		this.retmsg = retmsg;
	}

	public String getOut_refund_no() {
		return out_refund_no;
	}

	public void setOut_refund_no(String out_refund_no) {
		this.out_refund_no = out_refund_no;
	}

	public String getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}

	public Integer getRefund_channel() {
		return refund_channel;
	}

	public void setRefund_channel(Integer refund_channel) {
		this.refund_channel = refund_channel;
	}

	public Integer getRefund_fee() {
		return refund_fee;
	}

	public void setRefund_fee(Integer refund_fee) {
		this.refund_fee = refund_fee;
	}

	public Integer getRefund_status() {
		return refund_status;
	}

	public void setRefund_status(Integer refund_status) {
		this.refund_status = refund_status;
	}

	public String getRecv_user_id() {
		return recv_user_id;
	}

	public void setRecv_user_id(String recv_user_id) {
		this.recv_user_id = recv_user_id;
	}

	public String getReccv_user_name() {
		return reccv_user_name;
	}

	public void setReccv_user_name(String reccv_user_name) {
		this.reccv_user_name = reccv_user_name;
	}

}