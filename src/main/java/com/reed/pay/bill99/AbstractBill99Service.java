package com.reed.pay.bill99;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bill99.schema.fo.settlement.ApplyResponseType;
import com.bill99.schema.fo.settlement.BatchSettlementApplyResponse;
import com.bill99.schema.fo.settlement.SettlementPkiApiResponse;
import com.bill99.schema.fo.settlement.SettlementPkiResponseType;
import com.reed.common.util.JsonUtil;
import com.reed.pay.bill99.entry.DealInfoEntity;
import com.reed.pay.bill99.entry.OrderInfoEntity;

/**
 * 快钱service,封装基本服务，特定业务逻辑可继承本类实现特定方法
 */
@Service
public abstract class AbstractBill99Service {

	private static final Logger logger = LoggerFactory
			.getLogger(AbstractBill99Service.class);

	private static final SimpleDateFormat format = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	@Autowired
	private BillConfig billConfig;

	/**
	 * 发起快钱支付
	 * 
	 * @param batchNo
	 * @param totalPrice
	 * @param totalNums
	 */
	public void doPay(String batchNo, BigDecimal totalPrice, String totalNums) {
		DealInfoEntity dealInfoEntity = getDealEntity(batchNo, totalPrice,
				totalNums);
		if (dealInfoEntity == null) {
			logger.error(">>>>>>99Bill getDealEntity is null,batchNO:"
					+ batchNo);
		}
		SettlementPkiApiResponse response = send(dealInfoEntity);
		if (response == null) {
			logger.error(">>>>>>>>请求快钱失败,batchNO:" + batchNo);
		}
		logger.info(">>>>>>99Bill send batchNO:" + batchNo + ",返回 response:"
				+ JsonUtil.toJson(response));
		// pay done and do business
		receive(batchNo, response, dealInfoEntity);
	}

	/**
	 * TODO 查询业务订单,子类实现
	 * 
	 * @param batchNo
	 * @return
	 */
	public abstract List<OrderInfoEntity> getOrderInfoEntityList(String batchNo);

	/**
	 * TODO 支付后续业务逻辑,子类实现
	 * 
	 * @param batchNo
	 */
	public abstract void doBusiness(String batchNo);

	/**
	 * 解析响应
	 * 
	 * @param batchNo
	 * @param response
	 * @param dealInfoEntity
	 */
	private void receive(String batchNo, SettlementPkiApiResponse response,
			DealInfoEntity dealInfoEntity) {
		SettlementPkiResponseType responseType = response.getResponseBody();
		String status = response.getResponseBody().getStatus();
		if ("1".equals(status)) {
			String errorMsg = responseType.getErrorMsg();
			if (StringUtils.isNotBlank(errorMsg)) {
				logger.error(
						">>>>>>>99Bill response error:batchNo:{},errorCode:{},msg:{}",
						batchNo, responseType.getErrorCode(),
						responseType.getErrorMsg());
			}
		}
		BatchSettlementApplyResponse applyResponse = (BatchSettlementApplyResponse) PayResultReversalUtil
				.unseal(response, dealInfoEntity);// 得到解密数据
		if (applyResponse != null) {
			ApplyResponseType applyResponseType = applyResponse
					.getResponseBody();
			if (applyResponseType == null) {
				logger.error(">>>>>>>>99Bill ApplyResponseType is null, batchNo:"
						+ batchNo);
				return;
			}
		}
		// dobusiness
		doBusiness(batchNo);
	}

	/**
	 * 封装请求参数
	 * 
	 * @param batchNo
	 * @param totalPrice
	 * @param totalNums
	 * @return
	 */
	private DealInfoEntity getDealEntity(String batchNo, BigDecimal totalPrice,
			String totalNums) {
		DealInfoEntity dealInfo = null;
		if (StringUtils.isNotBlank(batchNo)) {
			dealInfo = new DealInfoEntity();
			dealInfo.setBatchNo(batchNo);
			dealInfo.setTotalAmt(getAmt(totalPrice));
			dealInfo.setTotalCnt(totalNums);
			initDefautDealInfoEntity(dealInfo);
			dealInfo.setOrdersInfo(getOrderInfoEntityList(batchNo));
		}
		return dealInfo;
	}

	/**
	 * send to 99bill
	 * 
	 * @param dealInfoEntity
	 * @return
	 */
	private SettlementPkiApiResponse send(DealInfoEntity dealInfoEntity) {
		String batchNo = dealInfoEntity.getBatchNo();
		SettlementPkiApiResponse response = SendRequestUtil
				.doURLSend(PayResultReversalUtil
						.getSettlementPkiApiRequest(dealInfoEntity));
		if (response == null) {
			logger.error(">>>>>>99Bill send 返回response is null,batchNo:"
					+ batchNo);
			return null;
		}
		return response;

	}

	/**
	 * 初始化默认参数
	 * 
	 * @param dealInfo
	 */
	private void initDefautDealInfoEntity(DealInfoEntity dealInfo) {
		dealInfo.setPayerAcctCode(billConfig.getPayerAcctCode());
		dealInfo.setMemberCode(billConfig.getMemberCode());
		String date = format.format(new Date());
		dealInfo.setApplyDate(date);
		dealInfo.setName(billConfig.getMerchantName());
		dealInfo.setFeeType(BillConstant.FEE_TYPE);
		dealInfo.setCur(BillConstant.CUR);
		dealInfo.setCheckAmtCnt(BillConstant.CHECK_AMT_CNT);
		dealInfo.setBatchFail(BillConstant.BATCH_FAIL);
		dealInfo.setRechargeType(BillConstant.RECHARGE_TYPE);
		dealInfo.setAutoRefund(BillConstant.AUTO_REFUND);
		dealInfo.setPhoneNoteFlag(BillConstant.PHONE_NOTE_FLAG);
		dealInfo.setServiceType(BillConstant.ACTION_APPLY);
		dealInfo.setVersion(BillConstant.VERSION);
		dealInfo.setFeatureCode(BillConstant.FEATURE_CODE);
	}

	/**
	 * 转换金额为分
	 * 
	 * @param amt
	 * @return
	 */
	private String getAmt(BigDecimal amt) {
		return new BigDecimal(String.valueOf(amt)).multiply(
				new BigDecimal("100")).setScale(0)
				+ "";
	}

}
