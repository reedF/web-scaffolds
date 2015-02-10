package com.reed.pay.bill99;

import com.bill99.schema.fo.settlement.SettlementPkiApiRequest;
import com.bill99.schema.fo.settlement.SettlementPkiApiResponse;

/**
 * 对应FoApiPkiWSClient 快钱支付应答
 */
public class SendRequestUtil {

	private SendRequestUtil() {
	}

	/**
	 * 用于把请求信息发送给快钱的webservices服务，同时拿到对应的应答信息
	 * */
	public static SettlementPkiApiResponse doURLSend(
			SettlementPkiApiRequest request) {
		SettlementPkiApiResponse response = null;
		String postContent = StringUtils.ReqFormat(PayResultReversalUtil
				.settlementPkiApiRequestToXml(request));
		String sbr = HttpClientBill99.doPost(BillConstant.URL, postContent);
		if (sbr.length() > 0) {
			String responseXML = StringUtils.ResFormat(sbr);
			response = PayResultReversalUtil
					.xmlToSettlementPkiApiResponse(responseXML);
		}
		return response;
	}

}
