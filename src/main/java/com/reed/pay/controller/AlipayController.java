package com.reed.pay.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.reed.pay.alipay.AlipayConfig;
import com.reed.pay.alipay.AlipayCore;
import com.reed.pay.alipay.AlipayNotify;
import com.reed.pay.alipay.RSA;

/**
 * 支付宝服务器异步通知
 * 
 * @author zhangzhiyong
 * 
 */
@RestController
@RequestMapping("/alipay")
public abstract class AlipayController extends AbstractPayController {

	private static final Logger LOGGER = LoggerFactory.getLogger("PAY");

	/**
	 * 支付宝回调接口(支付宝调用此接口，通知订单的最终支付、退款状态)
	 * 
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/notify", method = RequestMethod.POST)
	public String notify(@Valid AlipayNotifyFormBean formBean,
			HttpServletRequest request, HttpServletResponse response) {
		// 获取支付宝POST过来反馈信息
		Map<String, String> params = new HashMap<String, String>();
		Map<String, String[]> requestParams = request.getParameterMap();
		for (Iterator<String> iter = requestParams.keySet().iterator(); iter
				.hasNext();) {
			String name = iter.next();
			String[] values = requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		if (AlipayNotify.verify(params)) {// 验证成功
			if ("trade_status_sync".equals(formBean.getNotify_type())) {
				// 支付异步通知
				return payNotify(formBean);

			} else if ("batch_refund_notify".equals(formBean.getNotify_type())) {
				// 退款异步通知
				return refundNotify(formBean);
			}
			return "success";
		} else {// 验证失败
			return "fail";
		}
	}

	/**
	 * 生成alipay的订单支付信息
	 * 
	 * @param orderNo
	 *            订单号
	 * @param price
	 *            订单金额
	 * @param procductNames
	 *            商品名集合
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String generateOrderInfoOfAlipay(String orderNo, String price,
			List<String> procductNames) throws UnsupportedEncodingException {
		StringBuffer stringBuffer = new StringBuffer("");
		for (String d : procductNames) {
			if (StringUtils.isNotEmpty(d)) {
				stringBuffer.append(d);
			}
		}
		String subject = StringUtils.abbreviate(stringBuffer.toString(), 128);
		String body = StringUtils.abbreviate(stringBuffer.toString(), 512);

		Map<String, String> sArray = new HashMap<String, String>();
		sArray.put("partner", AlipayConfig.partner);
		sArray.put("seller_id", AlipayConfig.seller_id);
		sArray.put("out_trade_no", orderNo);

		sArray.put("subject", subject);
		sArray.put("body", body);
		sArray.put("total_fee", price);
		sArray.put("notify_url", AlipayConfig.notify_url);
		sArray.put("service", AlipayConfig.service);
		sArray.put("payment_type", AlipayConfig.payment_type);
		sArray.put("_input_charset", AlipayConfig.input_charset);
		sArray.put("it_b_pay", AlipayConfig.it_b_pay);

		Map<String, String> params = AlipayCore.paraFilter(sArray);
		String orderInfo = AlipayCore.createLinkString(params);
		String strsign = RSA.sign(orderInfo, AlipayConfig.private_key, "UTF-8");
		if (StringUtils.isBlank(strsign)) {
			LOGGER.error(String.format("请检查AlipayConfig和properties配置"));
		}
		strsign = URLEncoder.encode(strsign, "UTF-8");

		String payOrderInfo = orderInfo + "&sign=" + strsign + "&sign_type="
				+ AlipayConfig.sign_type;
		return payOrderInfo;
	}

	/**
	 * 支付异步回调
	 * 
	 * @param formBean
	 * @return
	 */
	private String payNotify(AlipayNotifyFormBean formBean) {
		if ("TRADE_FINISHED".equals(formBean.getTrade_status())) {
			// 判断该笔订单是否在商户网站中已经做过处理
			// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
			// 如果有做过处理，不执行商户的业务程序
			// 注意：
			// 该种交易状态只在两种情况下出现
			// 1、开通了普通即时到账，买家付款成功后。
			// 2、开通了高级即时到账，从该笔交易成功时间算起，过了签约时的可退款时限（如：三个月以内可退款、一年以内可退款等）后。
		} else if ("TRADE_SUCCESS".equals(formBean.getTrade_status())) {
			// 判断该笔订单是否在商户网站中已经做过处理
			// 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
			// 如果有做过处理，不执行商户的业务程序
			// 注意：
			// 该种交易状态只在一种情况下出现——开通了高级即时到账，买家付款成功后。
			String orderNo = formBean.getOut_trade_no();
			// TODO pay done then to do business
			if (!payDoneBusiness(orderNo)) {
				return "fail";
			}
		}
		return "success"; // 请不要修改或删除
	}

	/**
	 * 退款异步回调
	 * 
	 * @param formBean
	 * @return
	 */
	private String refundNotify(AlipayNotifyFormBean formBean) {
		String[] detailDatas = formBean.getResult_details().split("\\^");
		if (detailDatas.length != 3) {
			LOGGER.error("退款|退款结果明细【result_details】格式不正确"
					+ formBean.getResult_details());
			return "fail";
		}
		String batch_no = formBean.getBatch_no();
		String tradeNo = detailDatas[0];
		String refundPrice = detailDatas[1];
		String tradeStatus = detailDatas[2];
		if ("SUCCESS".equals(tradeStatus)) {
			// TODO refund done then to do business
			if (!refundDoneBusiness(batch_no, detailDatas)) {
				return "fail";
			}
		}
		// batch_no.substring(beginIndex, endIndex)
		// 判断是否在商户网站中已经做过了这次通知返回的处理
		// 如果没有做过处理，那么执行商户的业务程序
		// 如果有做过处理，那么不执行商户的业务程序
		// LOGGER.info("notify_type:"+notify_type+",batch_no:"+batch_no+",result_details:"+result_details+",success_num:"+success_num);
		return "success";
	}

}
