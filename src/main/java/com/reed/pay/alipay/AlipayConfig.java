package com.reed.pay.alipay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 */
@Component
public class AlipayConfig {
	
	/** 合作身份者ID，以2088开头由16位纯数字组成的字符串*/
	public static String partner;
	
	/** 卖方支付宝账号 */
	public static String seller_id;
	
	/** 拉手私钥 */
	public static String private_key;
	
	/** 支付宝的公钥，无需修改该值 */
	public static String ali_public_key;

	/** 字符编码格式utf-8 */
	public static String input_charset;
	
	/** 签名方式,不需修改 */
	public static String sign_type;
	
	/**支付类型，1（商品购买）*/
	public static String payment_type;
	
	/**未付款交易的超时时间，m-分钟，h-小时，d-天，1c-当天*/
	public static String it_b_pay;
	
	/** 服务器异步通知页面路径 */
	public static String notify_url;
	
	/** 移动支付：接口名称。固定值。不可空mobile.securitypay.pay  */
	public static String service;
	
	/** 退款无密码 */
	public static String refund_service="refund_fastpay_by_platform_nopwd";
	
	@Value("${pay.alipay.partner}")
	private void setPartner(String partner) {
		AlipayConfig.partner = partner;
	}

	@Value("${pay.alipay.seller.id}")
	private void setSellerId(String sellerId){
		AlipayConfig.seller_id = sellerId;
	}
	
	@Value("${pay.alipay.private.key}")
	private void setPrivateKey(String privateKey) {
		AlipayConfig.private_key = privateKey;
	}

	@Value("${pay.alipay.ali.public.key}")
	private void setAliPublicKey(String aliPublicKey) {
		AlipayConfig.ali_public_key = aliPublicKey;
	}

	@Value("${pay.alipay.input.charset}")
	private void setInputCharset(String inputCharset) {
		AlipayConfig.input_charset = inputCharset;
	}

	@Value("${pay.alipay.sign.type}")
	private void setSignType(String signType) {
		AlipayConfig.sign_type = signType;
	}

	@Value("${pay.alipay.payment.type}")
	private void setPaymentType(String paymentType) {
		AlipayConfig.payment_type = paymentType;
	}

	@Value("${pay.alipay.it.b.pay}")
	private void setItBPay(String itBPay) {
		AlipayConfig.it_b_pay = itBPay;
	}
	
	@Value("${pay.alipay.notify.url}")
	private void setNotifyUrl(String NotifyUrl){
		AlipayConfig.notify_url = NotifyUrl;
	}

	@Value("${pay.alipay.service}")
	private void setService(String service){
		AlipayConfig.service = service;
	}
}
