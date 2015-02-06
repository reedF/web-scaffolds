package com.reed.pay.tenpay;

import java.io.File;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/* *
 *类名：TenpayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 */
@Component
public class TenpayConfig {
	
	/** 收款方 */
//	public static String spname;
	/** 商户号 */
	public static String partner;
	
	public static String partner_password;
	/** 密钥 */
	public static String partner_key;
	/** appid */
	public static String app_id;
	public static String app_secret;
	/** appkey */
	public static String app_key;
	/** 支付完成后的回调处理页面 */
	public static String notify_url;
	
	/** 银行通道类型  */
	public static String bank_type="WX";
	/** 币种，1人民币  */
	public static String fee_type="1";
	/** */
	public static String sign_method="sha1";
	
	public static String _package="Sign=WXPay";
	public static String input_charset="GBK";
	
	/** Token获取网关地址地址 */
	public static String token_url = "https://api.weixin.qq.com/cgi-bin/token";
	/** 订单查询url */
	public static String order_query_url = "https://api.weixin.qq.com/pay/orderquery";
	/** 提交预支付单网关 */
	public static String prepay_url = "https://api.weixin.qq.com/pay/genprepay";
	/** 退款网关 */
	public static String refund_url = "https://mch.tenpay.com/refundapi/gateway/refund.xml";
	
	public static String ca_path;
	
	public static String cert_path;
	

	@Value("${pay.tenpay.partner}")
	private void setPartner(String partner) {
		TenpayConfig.partner = partner;
	}
	
	@Value("${pay.tenpay.partner.password}")
	private void setPartnerPassword(String partnerPassword) {
		TenpayConfig.partner_password = partnerPassword;
	}
	
	@Value("${pay.tenpay.partner.key}")
	private void setPartnerKey(String partnerKey) {
		TenpayConfig.partner_key = partnerKey;
	}

	@Value("${pay.tenpay.app.id}")
	private void setAppId(String appId) {
		TenpayConfig.app_id = appId;
	}

	@Value("${pay.tenpay.app.secret}")
	private void setAppSecret(String appSecret) {
		TenpayConfig.app_secret = appSecret;
	}

	@Value("${pay.tenpay.app.key}")
	private void setAppKey(String appKey) {
		TenpayConfig.app_key = appKey;
	}

	@Value("${pay.tenpay.notify.url}")
	private void setNotifyUrl(String NotifyUrl){
		TenpayConfig.notify_url = NotifyUrl;
	}

	@Value("${app.instance.config}")
	private void setAppInstanceConfig(String config) {
		TenpayConfig.ca_path = config+File.separator+"tenpay"+File.separator+"cacert.pem";
		TenpayConfig.cert_path = config+File.separator+"tenpay"+File.separator+"XXXXXXXX.pfx";
	}
	
}
