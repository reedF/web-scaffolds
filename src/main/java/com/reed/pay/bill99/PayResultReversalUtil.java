package com.reed.pay.bill99;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bill99.asap.exception.CryptoException;
import com.bill99.asap.service.ICryptoService;
import com.bill99.asap.service.impl.CryptoServiceFactory;
import com.bill99.schema.asap.commons.Mpf;
import com.bill99.schema.asap.data.SealedData;
import com.bill99.schema.asap.data.UnsealedData;
import com.bill99.schema.commons.Version;
import com.bill99.schema.fo.commons.Pay2bankTypeV2;
import com.bill99.schema.fo.commons.RequestHeader;
import com.bill99.schema.fo.commons.SealDataType;
import com.bill99.schema.fo.settlement.ApplyRequestType;
import com.bill99.schema.fo.settlement.BatchSettlementApplyRequest;
import com.bill99.schema.fo.settlement.BatchSettlementApplyResponse;
import com.bill99.schema.fo.settlement.BatchidQueryRequest;
import com.bill99.schema.fo.settlement.BatchidQueryRequestType;
import com.bill99.schema.fo.settlement.BatchidQueryResponse;
import com.bill99.schema.fo.settlement.ComplexQueryRequest;
import com.bill99.schema.fo.settlement.ComplexQueryRequestType;
import com.bill99.schema.fo.settlement.ComplexQueryResponse;
import com.bill99.schema.fo.settlement.SettlementPkiApiRequest;
import com.bill99.schema.fo.settlement.SettlementPkiApiResponse;
import com.bill99.schema.fo.settlement.SettlementPkiRequestType;
import com.bill99.schema.fo.settlement.SettlementPkiResponseType;
import com.bill99.seashell.common.util.Base64Util;
import com.bill99.seashell.common.util.DateUtil;
import com.reed.common.util.JsonUtil;
import com.reed.pay.bill99.entry.DealInfoEntity;
import com.reed.pay.bill99.entry.OrderInfoEntity;

//import org.apache.commons.codec.binary.Base64;

/**
 * 
 * 快钱支付 封装请求结果/返回 对象对应CustomerUtil
 */
public class PayResultReversalUtil {

	private static final Logger logger = LoggerFactory
			.getLogger(PayResultReversalUtil.class);

	private PayResultReversalUtil() {
	}

	/**
	 * BatchSettlementApplyRequest转换为xml格式
	 * 
	 * @param request
	 *            付款请求（原文）
	 * @return String xml 字符串
	 */
	public static String batchSettlementApplyRequestToXml(
			BatchSettlementApplyRequest request) {
		if (request == null) {
			return null;
		}
		try {
			IBindingFactory bfact = BindingDirectory
					.getFactory(BatchSettlementApplyRequest.class);
			IMarshallingContext mctx = bfact.createMarshallingContext();
			mctx.setIndent(2);
			StringWriter sw = new StringWriter();
			mctx.setOutput(sw);
			mctx.marshalDocument(request);
			return sw.toString();
		} catch (JiBXException e) {
			logger.error(
					"batchSettlementApplyRequestToXml request:"
							+ JsonUtil.toJson(request) + " error", e);
		}
		return null;
	}

	/**
	 * SettlementPkiApiRequest转换为xml格式
	 * 
	 * @param request
	 *            付款请求（密文）
	 * @return String xml 字符串
	 */
	public static String settlementPkiApiRequestToXml(
			SettlementPkiApiRequest request) {
		if (request == null) {
			return null;
		}
		try {
			IBindingFactory bfact = BindingDirectory
					.getFactory(SettlementPkiApiRequest.class);
			IMarshallingContext mctx = bfact.createMarshallingContext();
			mctx.setIndent(2);
			StringWriter sw = new StringWriter();
			mctx.setOutput(sw);
			mctx.marshalDocument(request);
			return sw.toString();
		} catch (JiBXException e) {
			logger.error(
					"settlementPkiApiRequestToXml request:"
							+ JsonUtil.toJson(request) + " error", e);
		}
		return null;
	}

	/**
	 * BatchidQueryRequest转换为xml格式
	 * 
	 * @param request
	 *            查询请求(根据批次号batchNo查询)
	 * @return String xml 字符串
	 */
	public static String batchidQueryRequestToXml(BatchidQueryRequest request) {
		if (request == null) {
			return null;
		}
		try {
			IBindingFactory bfact = BindingDirectory
					.getFactory(BatchidQueryRequest.class);
			IMarshallingContext mctx = bfact.createMarshallingContext();
			mctx.setIndent(2);
			StringWriter sw = new StringWriter();
			mctx.setOutput(sw);
			mctx.marshalDocument(request);
			return sw.toString();
		} catch (JiBXException e) {
			logger.error(
					"batchidQueryRequestToXml request:"
							+ JsonUtil.toJson(request) + " error", e);
		}
		return null;
	}

	/**
	 * ComplexQueryRequest转换为xml格式
	 * 
	 * @param request
	 *            查询请求(多条件查询类)
	 * @return String xml 字符串
	 */
	public static String complexQueryRequestToXml(ComplexQueryRequest request) {
		if (request == null) {
			return null;
		}
		try {
			IBindingFactory bfact = BindingDirectory
					.getFactory(ComplexQueryRequest.class);
			IMarshallingContext mctx = bfact.createMarshallingContext();
			mctx.setIndent(2);
			StringWriter sw = new StringWriter();
			mctx.setOutput(sw);
			mctx.marshalDocument(request);
			return sw.toString();
		} catch (JiBXException e) {
			logger.error(
					"complexQueryRequestToXml request:"
							+ JsonUtil.toJson(request) + " error", e);
		}
		return null;
	}

	/**
	 * 把输入流转换为SettlementPkiApiRequest
	 * 
	 * @param responseXml
	 *            请求相应xml
	 * @return SettlementPkiApiRequest 请求
	 */
	public static SettlementPkiApiRequest xmlToSettlementPkiApiRequest(
			String responseXml) {
		if (StringUtils.isBlank(responseXml)) {
			return null;
		}
		try {
			InputStream input = new ByteArrayInputStream(
					responseXml.getBytes(BillConstant.ENCODING));
			IBindingFactory bfact = BindingDirectory
					.getFactory(SettlementPkiApiRequest.class);
			IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
			SettlementPkiApiRequest response = (SettlementPkiApiRequest) uctx
					.unmarshalDocument(input, null);
			return response;
		} catch (Exception e) {
			logger.error("xmlToSettlementPkiApiRequest responseXml:"
					+ responseXml + " error", e);
		}
		return null;
	}

	/**
	 * 把xml转换为SettlementPkiApiResponse
	 * 
	 * @param input
	 *            返回应答
	 * @return SettlementPkiApiResponse 返回应答
	 * @throws java.io.IOException
	 */
	public static SettlementPkiApiResponse xmlToSettlementPkiApiResponse(
			InputStream input) {
		if (input == null) {
			return null;
		}
		try {
			IBindingFactory bfact = BindingDirectory
					.getFactory(SettlementPkiApiResponse.class);
			IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
			SettlementPkiApiResponse response = (SettlementPkiApiResponse) uctx
					.unmarshalDocument(input, null);
			return response;
		} catch (JiBXException e) {
			logger.error("xmlToSettlementPkiApiResponse input error", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 把xml转换为SettlementPkiApiResponse
	 * 
	 * @param responseXml
	 *            返回应答信息xml字符串
	 * @return SettlementPkiApiResponse 返回应答
	 */
	public static SettlementPkiApiResponse xmlToSettlementPkiApiResponse(
			String responseXml) {
		if (StringUtils.isBlank(responseXml)) {
			return null;
		}
		InputStream input = null;
		try {
			input = new ByteArrayInputStream(
					responseXml.getBytes(BillConstant.ENCODING));
			IBindingFactory bfact = BindingDirectory
					.getFactory(SettlementPkiApiResponse.class);
			IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
			SettlementPkiApiResponse response = (SettlementPkiApiResponse) uctx
					.unmarshalDocument(input, null);
			return response;
		} catch (JiBXException e) {
			logger.error("xmlToSettlementPkiApiResponse xml:[" + responseXml
					+ "] error JiBXException", e);
		} catch (UnsupportedEncodingException e) {
			logger.error("xmlToSettlementPkiApiResponse xml:[" + responseXml
					+ "] error UnsupportedEncodingException", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 把xml转换为BatchidQueryResponse，根据批次号查询
	 * 
	 * @param responseXml
	 *            返回应答信息xml字符串
	 * @return BatchidQueryResponse 返回应答
	 */
	public static BatchidQueryResponse xmlToBatchidQueryResponse(
			String responseXml) {
		InputStream input = null;
		try {
			IBindingFactory bfact = BindingDirectory
					.getFactory(BatchidQueryResponse.class);
			input = new ByteArrayInputStream(
					responseXml.getBytes(BillConstant.ENCODING));
			IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
			BatchidQueryResponse response = (BatchidQueryResponse) uctx
					.unmarshalDocument(input, null);
			return response;
		} catch (Exception e) {
			logger.error("xmlToBatchidQueryResponse xml:[" + responseXml
					+ "] error", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 把xml转换为BatchSettlementApplyResponse，付款返回应答
	 * 
	 * @param responseXml
	 *            返回应答信息xml字符串
	 * @return BatchSettlementApplyResponse 返回应答
	 */
	public static BatchSettlementApplyResponse xmlToBatchSettlementApplyResponse(
			String responseXml) {
		InputStream input = null;
		try {
			IBindingFactory bfact = BindingDirectory
					.getFactory(BatchSettlementApplyResponse.class);
			input = new ByteArrayInputStream(
					responseXml.getBytes(BillConstant.ENCODING));
			IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
			BatchSettlementApplyResponse response = (BatchSettlementApplyResponse) uctx
					.unmarshalDocument(input, null);
			return response;
		} catch (Exception e) {
			logger.error("xmlToBatchSettlementApplyResponse xml:["
					+ responseXml + "] error", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 把xml转换为ComplexQueryResponse，根据多条件查询
	 * 
	 * @param responseXml
	 *            返回应答信息xml字符串
	 * @return ComplexQueryResponse 返回应答信息类
	 */
	public static ComplexQueryResponse xmlToComplexQueryResponse(
			String responseXml) {
		InputStream input = null;
		try {
			IBindingFactory bfact = BindingDirectory
					.getFactory(ComplexQueryResponse.class);

			input = new ByteArrayInputStream(
					responseXml.getBytes(BillConstant.ENCODING));
			IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
			ComplexQueryResponse response = (ComplexQueryResponse) uctx
					.unmarshalDocument(input, null);
			return response;
		} catch (Exception e) {
			logger.error("xmlToComplexQueryResponse xml:[" + responseXml
					+ "] error", e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	/**
	 * 把请求信息数据设置到BatchSettlementApplyRequest类中
	 * 
	 * @param dealInfo
	 *            请求信息数据
	 * @return BatchSettlementApplyRequest 请求付款信息类(原文)
	 */
	public static BatchSettlementApplyRequest getBatchSettlementApplyRequest(
			DealInfoEntity dealInfo) {
		if (dealInfo == null) {
			return null;
		}
		BatchSettlementApplyRequest request = new BatchSettlementApplyRequest();
		RequestHeader head = new RequestHeader();
		Version ve = new Version();
		head.setTime(DateUtil.formatDateTime("yyyyMMddHHmmss", new Date()));
		ve.setService(dealInfo.getServiceType());
		ve.setVersion(dealInfo.getVersion());
		head.setVersion(ve);
		request.setRequestHeader(head);
		ApplyRequestType body = new ApplyRequestType();
		body.setApplyDate(dealInfo.getApplyDate() == null ? "" : dealInfo
				.getApplyDate());
		body.setAutoRefund(dealInfo.getAutoRefund() == null ? "" : dealInfo
				.getAutoRefund());
		body.setBatchFail(dealInfo.getBatchFail() == null ? "" : dealInfo
				.getBatchFail());
		body.setBatchNo(dealInfo.getBatchNo() == null ? "" : dealInfo
				.getBatchNo());
		body.setCheckAmtCnt(dealInfo.getCheckAmtCnt() == null ? "" : dealInfo
				.getCheckAmtCnt());
		body.setCur(dealInfo.getCur() == null ? "" : dealInfo.getCur());
		body.setFeeType(dealInfo.getFeeType() == null ? "" : dealInfo
				.getFeeType());
		body.setMerchantMemo1(dealInfo.getMerchantMemo1() == null ? ""
				: dealInfo.getMerchantMemo1());
		body.setMerchantMemo2(dealInfo.getMerchantMemo2() == null ? ""
				: dealInfo.getMerchantMemo2());
		body.setMerchantMemo3(dealInfo.getMerchantMemo3() == null ? ""
				: dealInfo.getMerchantMemo3());
		body.setName(dealInfo.getName() == null ? "" : dealInfo.getName());
		body.setPayerAcctCode(dealInfo.getPayerAcctCode() == null ? ""
				: dealInfo.getPayerAcctCode());
		body.setPhoneNoteFlag(dealInfo.getPhoneNoteFlag() == null ? ""
				: dealInfo.getPhoneNoteFlag());
		body.setRechargeType(dealInfo.getRechargeType() == null ? "" : dealInfo
				.getRechargeType());
		body.setTotalAmt(dealInfo.getTotalAmt() == null ? "" : dealInfo
				.getTotalAmt());
		body.setTotalCnt(dealInfo.getTotalCnt() == null ? "" : dealInfo
				.getTotalCnt());
		body.setPay2bankLists(getPay2BankList(dealInfo));
		request.setRequestBody(body);
		return request;
	}

	/**
	 * 把请求查询信息数据设置到BatchidQueryRequest类中
	 * 
	 * @param dealInfo
	 *            请求信息数据
	 * @return BatchidQueryRequest 请求查询信息类(原文）
	 */
	public static BatchidQueryRequest getBatchidQueryRequest(
			DealInfoEntity dealInfo) {
		if (dealInfo == null) {
			return null;
		}
		BatchidQueryRequest request = new BatchidQueryRequest();
		RequestHeader head = new RequestHeader();
		Version ve = new Version();
		head.setTime(DateUtil.formatDateTime("yyyyMMddHHmmss", new Date()));
		ve.setService(dealInfo.getServiceType());
		ve.setVersion(dealInfo.getVersion());
		head.setVersion(ve);
		request.setRequestHeader(head);
		BatchidQueryRequestType body = new BatchidQueryRequestType();
		body.setBatchNo(dealInfo.getBatchNo() == null ? "" : dealInfo
				.getBatchNo());
		body.setListFlag(dealInfo.getListFlag() == null ? "" : dealInfo
				.getListFlag());
		body.setPage(dealInfo.getPage() == null ? "" : dealInfo.getPage());
		body.setPageSize(dealInfo.getPageSize() == null ? "" : dealInfo
				.getPageSize());
		request.setRequestBody(body);
		return request;
	}

	/**
	 * 把请求信息数据设置到ComplexQueryRequest类中
	 * 
	 * @param dealInfo
	 *            请求信息数据
	 * @return ComplexQueryRequest 请求付款信息类(原文)
	 */
	public static ComplexQueryRequest getComplexQueryRequest(
			DealInfoEntity dealInfo) {
		if (dealInfo == null) {
			return null;
		}
		ComplexQueryRequest request = new ComplexQueryRequest();
		RequestHeader head = new RequestHeader();
		Version ve = new Version();
		head.setTime(DateUtil.formatDateTime("yyyyMMddHHmmss", new Date()));
		ve.setService(dealInfo.getServiceType());
		ve.setVersion(dealInfo.getVersion());
		head.setVersion(ve);
		request.setRequestHeader(head);
		ComplexQueryRequestType body = new ComplexQueryRequestType();
		body.setBankCardNo(dealInfo.getBankCardNo() == null ? "" : dealInfo
				.getBankCardNo());
		body.setBeginApplyTime(dealInfo.getBeginApplyDate() == null ? ""
				: dealInfo.getBeginApplyDate());
		body.setBranchBank(dealInfo.getBranchBank() == null ? "" : dealInfo
				.getBranchBank());
		body.setCity(dealInfo.getCity() == null ? "" : dealInfo.getCity());
		body.setBank(dealInfo.getBank() == null ? "" : dealInfo.getBank());
		body.setEndApplyTime(dealInfo.getEndApplyDate() == null ? "" : dealInfo
				.getEndApplyDate());
		body.setMerchantId(dealInfo.getMerchantId() == null ? "" : dealInfo
				.getMerchantId());
		body.setProvince(dealInfo.getProvince() == null ? "" : dealInfo
				.getProvince());
		body.setName(dealInfo.getName() == null ? "" : dealInfo.getName());
		body.setOrderBankErrorCode(dealInfo.getOrderBankErrorCode() == null ? ""
				: dealInfo.getOrderBankErrorCode());
		body.setOrderErrorCode(dealInfo.getOrderErrorCode() == null ? ""
				: dealInfo.getOrderErrorCode());
		body.setOrderStatus(dealInfo.getOrderStatus() == null ? "" : dealInfo
				.getOrderStatus());
		body.setPayeeType(dealInfo.getPayeeType() == null ? "" : dealInfo
				.getPayeeType());
		body.setPage(dealInfo.getPage() == null ? "" : dealInfo.getPage());
		body.setPageSize(dealInfo.getPageSize() == null ? "" : dealInfo
				.getPageSize());
		request.setRequestBody(body);
		return request;
	}

	/**
	 * 把付款详细信息设置到Pay2bankTypeV2类中，并且保存在LIST列表表
	 * 
	 * @param dealInfo
	 *            付款请求信息数据
	 * @return List<Pay2bankTypeV2> 保存有付款信息的列表类
	 */
	public static List<Pay2bankTypeV2> getPay2BankList(DealInfoEntity dealInfo) {
		if (dealInfo == null) {
			return null;
		}
		List<Pay2bankTypeV2> list = new ArrayList<Pay2bankTypeV2>();
		List<OrderInfoEntity> batchUpLoadBeanList = dealInfo.getOrdersInfo();
		for (OrderInfoEntity orderDto : batchUpLoadBeanList) {
			Pay2bankTypeV2 pay2bankType = new Pay2bankTypeV2();
			pay2bankType.setAmt(orderDto.getAmt() == null ? "" : orderDto
					.getAmt());
			pay2bankType.setBank(orderDto.getBank() == null ? "" : orderDto
					.getBank());
			pay2bankType.setBankCardNo(orderDto.getBankCardNo() == null ? ""
					: orderDto.getBankCardNo());
			pay2bankType.setBankMemo(orderDto.getBankMemo() == null ? ""
					: orderDto.getBankMemo());
			pay2bankType.setBankPurpose(orderDto.getBankPurpose() == null ? ""
					: orderDto.getBankPurpose());
			pay2bankType.setBranchBank(orderDto.getBranchBank() == null ? ""
					: orderDto.getBranchBank());
			pay2bankType.setCity(orderDto.getCity() == null ? "" : orderDto
					.getCity());
			pay2bankType.setMemo(orderDto.getMemo() == null ? "" : orderDto
					.getMemo());
			pay2bankType.setMerchantId(orderDto.getMerchantId() == null ? ""
					: orderDto.getMerchantId());
			pay2bankType
					.setMerchantMemo1(orderDto.getMerchantMemo1() == null ? ""
							: orderDto.getMerchantMemo1());
			pay2bankType
					.setMerchantMemo2(orderDto.getMerchantMemo2() == null ? ""
							: orderDto.getMerchantMemo2());
			pay2bankType
					.setMerchantMemo3(orderDto.getMerchantMemo3() == null ? ""
							: orderDto.getMerchantMemo3());
			pay2bankType.setName(orderDto.getName() == null ? "" : orderDto
					.getName());
			pay2bankType.setPayeeEmail(orderDto.getPayeeEmail() == null ? ""
					: orderDto.getPayeeEmail());
			pay2bankType.setPayeeMobile(orderDto.getPayeeMobile() == null ? ""
					: orderDto.getPayeeMobile());
			pay2bankType.setPayeeNote(orderDto.getPayeeNote() == null ? ""
					: orderDto.getPayeeNote());
			pay2bankType.setPayeeType(orderDto.getPayeeType() == null ? ""
					: orderDto.getPayeeType());
			pay2bankType.setPeriod(orderDto.getPeriod() == null ? "" : orderDto
					.getPeriod());
			pay2bankType.setProvince(orderDto.getProvince() == null ? ""
					: orderDto.getProvince());
			list.add(pay2bankType);
		}
		return list;
	}

	public static SettlementPkiApiRequest getSettlementPkiApiRequest(
			DealInfoEntity dealInfo) {
		if (dealInfo == null) {
			return null;
		}
		SettlementPkiApiRequest request = new SettlementPkiApiRequest();
		RequestHeader head = new RequestHeader();
		Version ve = new Version();
		head.setTime(DateUtil.formatDateTime("yyyyMMddHHmmss", new Date()));
		ve.setService(dealInfo.getServiceType());
		ve.setVersion(dealInfo.getVersion());
		head.setVersion(ve);
		request.setRequestHeader(head);
		SettlementPkiRequestType body = new SettlementPkiRequestType();
		SealedData sealedData = null;
		try {
			sealedData = seal(dealInfo);
			body.setMemberCode(dealInfo.getMemberCode());
			SealDataType sealdata = new SealDataType();
			byte[] byteOri = sealedData.getOriginalData();
			byte[] byteEnc = sealedData.getEncryptedData();
			byte[] byteEnv = sealedData.getDigitalEnvelope();
			byte[] byteSig = sealedData.getSignedData();
			sealdata.setOriginalData(new String(byteOri, BillConstant.ENCODING));
			sealdata.setEncryptedData(new String(byteEnc, BillConstant.ENCODING));
			sealdata.setDigitalEnvelope(new String(byteEnv,
					BillConstant.ENCODING));
			sealdata.setSignedData(new String(byteSig, BillConstant.ENCODING));
			body.setData(sealdata);
			request.setRequestBody(body);
			return request;
		} catch (UnsupportedEncodingException e) {
			logger.error(
					"getSettlementPkiApiRequest UnsupportedEncodingException dealInfo:["
							+ JsonUtil.toJson(dealInfo) + "]", e);
		}
		return null;

	}

	/**
	 * 将提交数据进行加密处理，并且返回一个加密后的数据类sealedData
	 * 
	 * @param dealInfo
	 *            请求的数据内容
	 * @return SealedData 加密后的数据
	 */
	public static SealedData seal(DealInfoEntity dealInfo) {
		if (dealInfo == null) {
			return null;
		}
		String originalData = "";
		if (BillConstant.ACTION_APPLY.equalsIgnoreCase(dealInfo
				.getServiceType())) {
			originalData = batchSettlementApplyRequestToXml(getBatchSettlementApplyRequest(dealInfo));
		} else if (BillConstant.ACTION_BATCHIDQUERY.equalsIgnoreCase(dealInfo
				.getServiceType())) {
			originalData = batchidQueryRequestToXml(getBatchidQueryRequest(dealInfo));
		} else if (BillConstant.ACTION_COMPLEXQUERY.equalsIgnoreCase(dealInfo
				.getServiceType())) {
			originalData = complexQueryRequestToXml(getComplexQueryRequest(dealInfo));
		}
		Validate.notNull(originalData);
		// logger.info("提交的原始报文为:\n" + originalData);
		SealedData sealedData = null;
		try {
			// 先压缩
			byte[] orc = GzipUtil.gzip(originalData
					.getBytes(BillConstant.ENCODING));
			Mpf mpf = new Mpf();
			mpf.setMemberCode(dealInfo.getMemberCode());
			mpf.setFeatureCode(dealInfo.getFeatureCode());
			ICryptoService service = null;
			try {
				service = CryptoServiceFactory.createCryptoService();
				// 再加密
				sealedData = service.seal(mpf, orc);

			} catch (CryptoException e) {
				logger.error("seal 加密失败", e);
			}
			byte[] nullbyte = {};
			byte[] byteOri = sealedData.getOriginalData() == null ? nullbyte
					: sealedData.getOriginalData();
			byte[] byteEnc = sealedData.getEncryptedData() == null ? nullbyte
					: sealedData.getEncryptedData();
			byte[] byteEnv = sealedData.getDigitalEnvelope() == null ? nullbyte
					: sealedData.getDigitalEnvelope();
			byte[] byteSig = sealedData.getSignedData() == null ? nullbyte
					: sealedData.getSignedData();
			byte[] byteOri2 = Base64.encodeBase64(byteOri);
			byte[] byteEnc2 = Base64.encodeBase64(byteEnc);
			byte[] byteEnv2 = Base64.encodeBase64(byteEnv);
			byte[] byteSig2 = Base64.encodeBase64(byteSig);

			sealedData.setOriginalData(byteOri2);
			sealedData.setSignedData(byteSig2);
			sealedData.setEncryptedData(byteEnc2);
			sealedData.setDigitalEnvelope(byteEnv2);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sealedData;
	}

	/**
	 * 对返回的应答信息进行解密，得到请求响应结果：成功or失败
	 * 
	 * @param dealInfo
	 *            请求的数据内容
	 * @return String 响应结果
	 * */
	public static Object unseal(SettlementPkiApiResponse response,
			DealInfoEntity dealInfo) {
		if (response == null) {
			logger.error("====应答信息为空=====");
			return null;
		} else {
			try {
				byte[] unsealedResultbyte = unsealData(response, dealInfo);
				if (unsealedResultbyte != null) {
					if (BillConstant.ACTION_BATCHIDQUERY
							.equalsIgnoreCase(dealInfo.getServiceType())) {
						BatchidQueryResponse responseObject = PayResultReversalUtil
								.xmlToBatchidQueryResponse(new String(
										unsealedResultbyte,
										BillConstant.ENCODING));
						return responseObject;
					} else if (BillConstant.ACTION_APPLY
							.equalsIgnoreCase(dealInfo.getServiceType())) {
						BatchSettlementApplyResponse responseObject = PayResultReversalUtil
								.xmlToBatchSettlementApplyResponse(new String(
										unsealedResultbyte,
										BillConstant.ENCODING));
						return responseObject;
					} else if (BillConstant.ACTION_COMPLEXQUERY
							.equalsIgnoreCase(dealInfo.getServiceType())) {
						ComplexQueryResponse responseObject = PayResultReversalUtil
								.xmlToComplexQueryResponse(new String(
										unsealedResultbyte,
										BillConstant.ENCODING));
						return responseObject;
					}
				}

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}
		return null;
	}

	/**
	 * 对应返回的应答信息做解密处理
	 * 
	 * @param response
	 *            返回的应答信息类
	 * @param dealInfo
	 *            交易数据
	 * @return byte[] 解密数据
	 * */
	public static byte[] unsealData(SettlementPkiApiResponse response,
			DealInfoEntity dealInfo) {
		if (response == null) {
			logger.error("得到的应答报文为空");
			return null;
		} else {
			try {
				String errorCode = "";
				SealDataType responseSealedData = null;
				SettlementPkiResponseType responsebody = response
						.getResponseBody();
				errorCode = responsebody.getErrorCode();
				responseSealedData = responsebody.getData();
				// System.out.println("付款商户号"+responsebody.getMemberCode());
				// System.out.println("应答状态"+responsebody.getStatus());
				if (!"0000".equals(errorCode) && !"1313".equals(errorCode)) {
					System.out.println("申请失败,失败代码" + errorCode);
					return null;
				}
				if ("0000".equals(errorCode)) {
					logger.info("申请成功");
				} else {
					logger.error("申请失败,失败代码:" + errorCode);
				}
				byte[] resOriData = responseSealedData.getOriginalData()
						.getBytes(BillConstant.ENCODING);
				byte[] resSigData = responseSealedData.getSignedData()
						.getBytes(BillConstant.ENCODING);
				byte[] resEnvData = responseSealedData.getDigitalEnvelope()
						.getBytes(BillConstant.ENCODING);
				byte[] resEncData = responseSealedData.getEncryptedData()
						.getBytes(BillConstant.ENCODING);

				// decode
				// decode? base64decode
				byte[] resDecodeOriData = Base64Util.decode(resOriData);
				byte[] resDecodeSigData = Base64Util.decode(resSigData);
				byte[] resDecodeEnvData = Base64Util.decode(resEnvData);
				byte[] resDecodeEncData = Base64Util.decode(resEncData);

				SealedData sealedData = new SealedData();
				sealedData.setSignedData(resDecodeSigData);
				sealedData.setOriginalData(resDecodeOriData);
				sealedData.setEncryptedData(resDecodeEncData);
				sealedData.setDigitalEnvelope(resDecodeEnvData);

				// 解密
				Mpf mpf = new Mpf();
				mpf.setMemberCode(dealInfo.getMemberCode());
				mpf.setFeatureCode(dealInfo.getFeatureCode());
				ICryptoService service = null;
				service = CryptoServiceFactory.createCryptoService();
				UnsealedData unsealedData = null;
				unsealedData = service.unseal(mpf, sealedData);
				if (unsealedData.getVerifySignResult()) {
					byte[] DecryptedData = unsealedData.getDecryptedData();
					if (DecryptedData == null) {
						// 解压缩
						byte[] unsealedResultbyte = GzipUtil
								.unBGzip(resDecodeOriData);
						// System.out.println("解密后的应答报文：\n"+new
						// String(unsealedResultbyte, BillConstant.ENCODING));
						return unsealedResultbyte;
					} else {
						byte[] unsealedResultbyte = GzipUtil
								.unBGzip(DecryptedData);
						// System.out.println("解密后的应答报文：\n"+new
						// String(unsealedResultbyte,BillConstant.ENCODING));
						return unsealedResultbyte;
					}
				} else {
					// System.out.println("验签失败");
					return null;
				}
			} catch (CryptoException e) {
				// e.printStackTrace();
				return null;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

}
