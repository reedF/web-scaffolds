package com.reed.app.push.baidu;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.reed.common.util.JsonUtil;

/**
 * Baidu云推送公用方法类,针对Baidu rest API 2.0封装
 * doc:http://developer.baidu.com/wiki/index.php?title=docs/cplat/push/api
 * 
 * @author reed
 * 
 */
public class BaiduPushTools {
	private static final Logger logger = LoggerFactory
			.getLogger(BaiduPushTools.class);

	/** push msg url */
	public static final String push_msg = "http://channel.api.duapp.com/rest/2.0/channel/channel";

	/**
	 * 查询设备、应用、用户与百度Channel的绑定关系
	 * 
	 * @param userId
	 *            baidu userId
	 * @param key
	 *            要查询的字段，参照http://developer.baidu.com/wiki/index.php?title=docs/
	 *            cplat/push/api/list#query_bindlist
	 * @param apiKey
	 * @param secretKey
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String queryBind(Long userId, String key, String apiKey,
			String secretKey) {
		String result = null;
		BaiduPushResponse<Map<String, Object>> r = null;
		BaiduBaseForm f = new BaiduBaseForm();
		f.setApikey(apiKey);
		f.setUser_id(userId);
		if (f != null) {
			f.setMethod("query_bindlist");
			String sign = getSign(push_msg, secretKey, obj2Map(f));
			f.setSign(sign);
			String res = HttpClientTool.doPost(push_msg, obj2Map(f));
			if (StringUtils.isNotBlank(res)) {
				r = (BaiduPushResponse<Map<String, Object>>) JsonUtil
						.json2Object(res, BaiduPushResponse.class);
				if (r != null) {
					Map<String, Object> m = (Map<String, Object>) r
							.getResponse_params();
					if (m != null && m.get("binds") != null) {
						List<Map<String, Object>> binds = (List<Map<String, Object>>) m
								.get("binds");
						if (binds != null && binds.size() > 0) {
							for (Map<String, Object> b : binds) {
								if (b != null
										&& ((Integer) b.get("bind_status") == 0)) {
									result = String.valueOf(b.get(key));
									break;
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	/**
	 * 推送消息,自定义具体推送参数. 注： (1)推送类型push_type=3所有人时，会按device_type推送到对应设备，其他设备不会收到广播
	 * (2)PushMsgForm.deploy_status=1时，仅推送给测试环境，线上用户无法获取
	 * 
	 * @param f
	 *            参数列表
	 * @param secretKey
	 *            PushMsgForm.apikey对应app的私钥
	 */
	@SuppressWarnings("unchecked")
	public static void pushMsg(PushMsgForm f, String secretKey) {
		BaiduPushResponse<String> r = null;
		if (f != null) {
			f.setMethod("push_msg");
			String sign = getSign(push_msg, secretKey, obj2Map(f));
			f.setSign(sign);
			String res = HttpClientTool.doPost(push_msg, obj2Map(f));
			if (StringUtils.isNotBlank(res)) {
				r = (BaiduPushResponse<String>) JsonUtil.json2Object(res,
						BaiduPushResponse.class);
				if (r != null) {
					r.getResponse_params();
				}
			}
		}
	}

	/**
	 * 广播，推送给secretKey对应app的ios与android
	 * 
	 * @param f
	 * @param isTest
	 *            是否是测试环境
	 * @param secretKey
	 */
	public static void boradcastMsg(PushMsgForm f, boolean isTest,
			String secretKey) {
		if (isTest) {
			f.setDeploy_status((short) 1);
		}
		// ios
		f.setDevice_type((short) 4);
		BaiduPushTools.pushMsg(f, secretKey);
		// android
		f.setDevice_type((short) 3);
		BaiduPushTools.pushMsg(f, secretKey);
	}

	/**
	 * 广播，推送给secretKey对应app的ios与android
	 * 
	 * @param title
	 *            标题
	 * @param msg
	 *            消息体
	 * @param isTest
	 *            是否是测试环境
	 * @param apiKey
	 *            app key
	 * @param secretKey
	 *            app secretKey
	 */
	public void boradcastMsg(String title, String msg, boolean isTest,
			String apiKey, String secretKey) {
		PushMsgForm f = new PushMsgForm();
		f.setApikey(apiKey);
		f.setPush_type((short) 3);
		f.setMessage_type((short) 1);
		f.setMsg(title, msg);
		f.setMsg_keys("" + new Date().getTime());
		f.setTag(null);
		if (isTest) {
			// test
			f.setDeploy_status((short) 1);
		}
		BaiduPushTools.boradcastMsg(f, isTest, secretKey);
	}

	/**
	 * 推送给单个人
	 * 
	 * @param userId
	 * @param channelId
	 * @param title
	 * @param msg
	 * @param isTest
	 *            是否是测试环境
	 * @param apiKey
	 * @param secretKey
	 */
	public void pushMsgToTarget(Long userId, Long channelId, String title,
			String msg, boolean isTest, String apiKey, String secretKey) {
		PushMsgForm f = new PushMsgForm();
		f.setUser_id(userId);
		f.setChannel_id(channelId);
		f.setApikey(apiKey);
		f.setPush_type((short) 1);
		f.setMessage_type((short) 1);
		//f.setDevice_type((short) 4);
		String deviceType = queryBind(userId, "device_type", apiKey, secretKey);
		if (StringUtils.isNotBlank(deviceType)) {
			f.setDevice_type(Short.valueOf(deviceType));
		}
		f.setMsg(title, msg);
		f.setMsg_keys("" + new Date().getTime());
		f.setTag(null);
		if (isTest) {
			// test
			f.setDeploy_status((short) 1);
		}
		BaiduPushTools.pushMsg(f, secretKey);
	}

	/**
	 * obj 2 map
	 * 
	 * @param bean
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map obj2Map(Object bean) {
		Class type = bean.getClass();
		Map returnMap = new HashMap();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(type);
			PropertyDescriptor[] propertyDescriptors = beanInfo
					.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (!propertyName.equals("class")) {
					Method readMethod = descriptor.getReadMethod();
					Object result = readMethod.invoke(bean, new Object[0]);
					if (result != null) {
						returnMap.put(propertyName, result);
					} else {
						// returnMap.put(propertyName, "");
					}
				}
			}
		} catch (IntrospectionException ex) {
			logger.error(">>>>>>>>>>>>>can not converObj to map",
					ex.getMessage());
		} catch (InvocationTargetException ex) {
			logger.error(">>>>>>>>>>>>>can not converObj to map",
					ex.getMessage());
		} catch (IllegalAccessException ex) {
			logger.error(">>>>>>>>>>>>>can not converObj to map",
					ex.getMessage());
		}
		return returnMap;
	}

	/**
	 * 生成签名
	 * 
	 * @param url
	 * @param secretKey
	 * @param map
	 * @return
	 */
	public static String getSign(String url, String secretKey,
			Map<String, Object> map) {
		String sign = null;
		if (map != null && !map.isEmpty()) {
			StringBuffer sf = new StringBuffer("POST").append(url);
			Collection<String> keyset = map.keySet();
			List<String> keys = new ArrayList<String>(keyset);
			// 对key键值按字典升序排序
			Collections.sort(keys);
			for (String s : keys) {
				if (StringUtils.isNotBlank(s) && !s.equals("sign")) {
					sf.append(s).append("=").append(map.get(s));
				}
			}
			sf.append(secretKey);
			try {
				String end = URLEncoder.encode(sf.toString(), "utf-8");
				sign = DigestUtils.md5Hex(end);
			} catch (UnsupportedEncodingException e) {
				logger.error(">>>>>>>>>>>>>encoding failed:{}", e.getMessage());
			}
		}
		return sign;
	}

	public static void main(String[] args) {
		String apiKey = "lSW1ekBWCRw7rqpQZrNVfnLs";
		String secKey = "XKKkIHFAdXqRBIWd2QLGuGYOAeibQKoT";
		// PushMsgForm f = new PushMsgForm();
		// f.setApikey(apiKey);
		// f.setUser_id(603789426887031103l);
		// f.setChannel_id(4050060892618280481l);
		// f.setMessage_type((short) 1);
		// f.setMsg("title", JsonUtil.toJson(f));
		// f.setDevice_type((short) 4);
		// f.setMsg_keys("" + new Date().getTime());
		// f.setPush_type((short) 1);
		// f.setTag(null);
		// f.setDeploy_status((short) 1);
		// BaiduPushTools.pushMsg(f, secKey);

		BaiduPushTools t = new BaiduPushTools();
		// t.boradcastMsg("test", "" + new Date().getTime(),true, apiKey,
		// secKey);
		t.pushMsgToTarget(603789426887031103l, 4050060892618280481l, "test", ""
				+ new Date().getTime(), true, apiKey, secKey);
		t.queryBind(603789426887031103l, "device_type", apiKey, secKey);
	}
}
