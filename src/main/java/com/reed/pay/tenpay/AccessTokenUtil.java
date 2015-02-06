package com.reed.pay.tenpay;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.reed.common.util.JsonUtil;
import com.reed.pay.tenpay.client.TenpayHttpClient;

@Component
/**
 * 微信支付access_token工具类
 * 获取微信支付访问TOKEN，一天最多获取200次，需要所有用户共享值
 * token的expires_in默认为7200秒
 */
public class AccessTokenUtil {
	private static Logger logger = LoggerFactory.getLogger("PAY");

	@Resource(name = "pay_redisTemplate")
	private RedisTemplate<String, String> redisTemplate;
	private static String access_token_key = "pay.tenpay.access.token";

	/**
	 * 获取微信支付访问TOKEN，一天最多获取200次，需要所有用户共享值 token的expires_in默认为7200秒
	 * 
	 * @return
	 */
	public String getAccessToken() {
		Boolean hasKey = redisTemplate.hasKey(access_token_key);
		String token = null;
		if (!hasKey) {
			String requestUrl = TenpayConfig.token_url
					+ "?grant_type=client_credential&appid="
					+ TenpayConfig.app_id + "&secret="
					+ TenpayConfig.app_secret;
			try {
				// 发送请求，返回json
				TenpayHttpClient httpClient = new TenpayHttpClient();
				httpClient.setReqContent(requestUrl);
				String resContent = "";
				if (httpClient.callHttpPost(requestUrl, "")) {
					resContent = httpClient.getResContent();

					TypeReference<Map<String, String>> ref = new TypeReference<Map<String, String>>() {
					};
					Map<String, String> map = (Map<String, String>) JsonUtil
							.json2GenericObject(resContent, ref);
					// 判断返回是否含有access_token
					String expires_in = null;
					if (map.containsKey("access_token")) {
						// 更新application值
						token = map.get("access_token");
						expires_in = map.get("expires_in");
					} else {
						logger.error(String.format("get token err ,info = %s",
								map.get("errmsg")));
					}
					logger.debug(String.format("res json= %s", resContent));
					redisTemplate.opsForValue().set(access_token_key, token);
					if (null != StringUtils.trimToNull(expires_in)
							&& StringUtils.isNumeric(expires_in)) {
						redisTemplate.expire(
								access_token_key,
								Long.valueOf(expires_in) == 7200 ? 7080 : Long
										.valueOf(expires_in), TimeUnit.SECONDS);
					} else {

						redisTemplate.expire(access_token_key, 7200,
								TimeUnit.SECONDS);
					}
				}

			} catch (Exception e) {
				logger.error("获取微信支付【access_token】失败:" + e.getMessage());
			}
		} else {
			token = redisTemplate.opsForValue().get(access_token_key);
		}
		return token;
	}

}
