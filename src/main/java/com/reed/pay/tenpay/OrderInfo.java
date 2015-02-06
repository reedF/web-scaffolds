package com.reed.pay.tenpay;

/**
 * tenpay 订单查询接口返回的订单信息
 * 
 */
public class OrderInfo {

	private Integer ret_code;
	private String ret_msg;
	private String input_charset;
	private String trade_state;
	private String trade_mode;
	private String partner;
	private String bank_type;
	private String bank_billno;
	private String total_fee;
	private String fee_type;
	private String transaction_id;
	private String out_trade_no;
	private String is_split;
	private String is_refund;
	private String attach;
	private String time_end;
	private String transport_fee;
	private String product_fee;
	private String discount;
	private String rmb_total_fee;

	public Integer getRet_code() {
		return ret_code;
	}

	public void setRet_code(Integer ret_code) {
		this.ret_code = ret_code;
	}

	public String getRet_msg() {
		return ret_msg;
	}

	public void setRet_msg(String ret_msg) {
		this.ret_msg = ret_msg;
	}

	public String getInput_charset() {
		return input_charset;
	}

	public void setInput_charset(String input_charset) {
		this.input_charset = input_charset;
	}

	public String getTrade_state() {
		return trade_state;
	}

	public void setTrade_state(String trade_state) {
		this.trade_state = trade_state;
	}

	public String getTrade_mode() {
		return trade_mode;
	}

	public void setTrade_mode(String trade_mode) {
		this.trade_mode = trade_mode;
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

	public String getBank_billno() {
		return bank_billno;
	}

	public void setBank_billno(String bank_billno) {
		this.bank_billno = bank_billno;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getFee_type() {
		return fee_type;
	}

	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
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

	public String getIs_split() {
		return is_split;
	}

	public void setIs_split(String is_split) {
		this.is_split = is_split;
	}

	public String getIs_refund() {
		return is_refund;
	}

	public void setIs_refund(String is_refund) {
		this.is_refund = is_refund;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getTime_end() {
		return time_end;
	}

	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}

	public String getTransport_fee() {
		return transport_fee;
	}

	public void setTransport_fee(String transport_fee) {
		this.transport_fee = transport_fee;
	}

	public String getProduct_fee() {
		return product_fee;
	}

	public void setProduct_fee(String product_fee) {
		this.product_fee = product_fee;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public String getRmb_total_fee() {
		return rmb_total_fee;
	}

	public void setRmb_total_fee(String rmb_total_fee) {
		this.rmb_total_fee = rmb_total_fee;
	}

}
