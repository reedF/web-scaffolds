package com.reed.pay.tenpay;

/**
 * tenpay订单查询信息
 * https://api.weixin.qq.com/pay/orderquery
 * 支付成功后，tenpay提供了查询订单状态接口
 * 查询接口返回的订单信息
 *
 */
public class OrderQueryInfo {

	private Integer errcode;
	private String errmsg;
	private OrderInfo order_info;
	
	public Integer getErrcode() {
		return errcode;
	}
	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}
	public String getErrmsg() {
		return errmsg;
	}
	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	public OrderInfo getOrder_info() {
		return order_info;
	}
	public void setOrder_info(OrderInfo order_info) {
		this.order_info = order_info;
	}
	
}
