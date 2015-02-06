package com.reed.pay.controller;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * 支付宝服务器异步通知form
 * 
 * 
 */
public class AlipayNotifyFormBean {

	/** 通知时间 */
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date notify_time;
	/** 通知类型:trade_status_sync */
	@NotNull
	private String notify_type;
	/** 通知校验ID */
	@NotNull
	private String notify_id;
	/** 签名方式:RSA */
	@NotNull
	private String sign_type;
	/** 签名 */
	@NotNull
	private String sign;

	/** 商户网站唯一订单号，是请求时对应的参数，原样返回 */
	private String out_trade_no;

	/** 商品名称，是请求时对应的参数，原样通知回来 */
	private String subject;

	/** 支付类型，默认值为：1（商品购买） */
	private String payment_type;

	/** 支付宝交易号 */
	private String trade_no;

	/** 交易状态，example：TRADE_SUCCESS */
	private String trade_status;

	/** 卖家支付宝用户号 */
	private String seller_id;

	/** 卖家支付宝账号 */
	private String seller_email;

	/** 买家支付宝用户号 */
	private String buyer_id;

	/** 买家支付宝账号 */
	private String buyer_email;

	/** 交易金额，请求时对应的参数，原样通知回来 */
	private BigDecimal total_fee;

	/** 购买数量 */
	private String quantity;

	/** 商品单价 */
	private String price;

	/** 商品描述，对应请求时的body参数，原样通知回来 */
	private String body;

	/** 交易创建时 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date gmt_create;

	/** 交易付款时间 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date gmt_payment;

	/** 是否调整总价，example:N */
	private String is_total_fee_adjust;

	/** 是否使用红包买家，example:N */
	private String use_coupon;

	/** 折扣 */
	private String discount;

	/** 退款状态，example:REFUND_SUCCESS */
	private String refund_status;

	/** 退款时间 */
	// @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss.sss")
	// private Date gmt_refund;

	// 以下是退款回调参数
	/** 退款批次号 */
	private String batch_no;
	/** 退款成功总数 */
	private String success_num;
	/** 退款结果明细,格式：交易号^退款金额^处理结果$退费账号^退费账户ID^退费金额^处理结果； */
	private String result_details;

	public Date getNotify_time() {
		return notify_time;
	}

	public void setNotify_time(Date notify_time) {
		this.notify_time = notify_time;
	}

	public String getNotify_type() {
		return notify_type;
	}

	public void setNotify_type(String notify_type) {
		this.notify_type = notify_type;
	}

	public String getNotify_id() {
		return notify_id;
	}

	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPayment_type() {
		return payment_type;
	}

	public void setPayment_type(String payment_type) {
		this.payment_type = payment_type;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public String getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(String trade_status) {
		this.trade_status = trade_status;
	}

	public String getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(String seller_id) {
		this.seller_id = seller_id;
	}

	public String getSeller_email() {
		return seller_email;
	}

	public void setSeller_email(String seller_email) {
		this.seller_email = seller_email;
	}

	public String getBuyer_id() {
		return buyer_id;
	}

	public void setBuyer_id(String buyer_id) {
		this.buyer_id = buyer_id;
	}

	public String getBuyer_email() {
		return buyer_email;
	}

	public void setBuyer_email(String buyer_email) {
		this.buyer_email = buyer_email;
	}

	public BigDecimal getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(BigDecimal total_fee) {
		this.total_fee = total_fee;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Date getGmt_create() {
		return gmt_create;
	}

	public void setGmt_create(Date gmt_create) {
		this.gmt_create = gmt_create;
	}

	public Date getGmt_payment() {
		return gmt_payment;
	}

	public void setGmt_payment(Date gmt_payment) {
		this.gmt_payment = gmt_payment;
	}

	public String getIs_total_fee_adjust() {
		return is_total_fee_adjust;
	}

	public void setIs_total_fee_adjust(String is_total_fee_adjust) {
		this.is_total_fee_adjust = is_total_fee_adjust;
	}

	public String getUse_coupon() {
		return use_coupon;
	}

	public void setUse_coupon(String use_coupon) {
		this.use_coupon = use_coupon;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getRefund_status() {
		return refund_status;
	}

	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}

	// public Date getGmt_refund() {
	// return gmt_refund;
	// }
	//
	// public void setGmt_refund(Date gmt_refund) {
	// this.gmt_refund = gmt_refund;
	// }

	public String getBatch_no() {
		return batch_no;
	}

	public void setBatch_no(String batch_no) {
		this.batch_no = batch_no;
	}

	public String getSuccess_num() {
		return success_num;
	}

	public void setSuccess_num(String success_num) {
		this.success_num = success_num;
	}

	public String getResult_details() {
		return result_details;
	}

	public void setResult_details(String result_details) {
		this.result_details = result_details;
	}

}