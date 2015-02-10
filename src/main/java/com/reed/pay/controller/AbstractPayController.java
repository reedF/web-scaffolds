package com.reed.pay.controller;

/**
 * 支付基类controller,不同支付渠道实现特定的支付成功后续逻辑、退款成功后续逻辑
 * 
 * @author reed
 * 
 */
public abstract class AbstractPayController {
	/**
	 * 支付成功后，后续业务逻辑：更新订单状态等
	 * 
	 * @param orderNo
	 */
	public abstract boolean payDoneBusiness(String orderNo);

	/**
	 * 退款成功后，后续业务逻辑：更新订单状态等
	 * 
	 * @param batchNo
	 * @param detailDatas
	 */
	public abstract boolean refundDoneBusiness(String batchNo,
			String[] detailDatas);
}
