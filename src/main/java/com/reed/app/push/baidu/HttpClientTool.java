package com.reed.app.push.baidu;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HttpClient 连接池工具类
 * 
 * @author reed
 * 
 */
public class HttpClientTool {

	private static final Logger logger = LoggerFactory
			.getLogger(HttpClientTool.class);

	private static PoolingClientConnectionManager cm = null;

	static {
		// 连接池走默认参数
		cm = new PoolingClientConnectionManager();
		int maxTotal = 500;
		cm.setMaxTotal(maxTotal);
		// 每条通道的并发连接数设置（连接池）
		int defaultMaxConnection = 50;
		cm.setDefaultMaxPerRoute(defaultMaxConnection);
	}

	// 不允许自己实例化
	private HttpClientTool() {

	}

	public static HttpClient getHttpClient() {
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);

		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		return new DefaultHttpClient(cm, params);
	}

	/**
	 * 判断请求是成功 返回true 成功 false 失败
	 * 
	 * @param response
	 * @return
	 */
	public static boolean isHttpOk(HttpResponse response) {
		if (response == null) {
			return false;
		}
		int code = response.getStatusLine().getStatusCode();
		String scode = String.valueOf(code);
		if (!(scode.startsWith("2") || scode.startsWith("3"))) {
			return false;
		}
		return true;
	}

	/**
	 * 关闭连接
	 * 
	 * @param request
	 */
	public static void closeRequest(HttpRequestBase request) {
		if (request != null) {
			request.releaseConnection();
		}
	}

	/**
	 * 模拟post请求
	 */
	public static String doPost(String url, Map<String, String> params) {
		String resp = null;
		HttpClient httpClient = getHttpClient();
		HttpPost postMethod = new HttpPost(url);
		// 填入各个表单域的值
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (null != params) {
			for (String key : params.keySet()) {
				nvps.add(new BasicNameValuePair(key, String.valueOf(params
						.get(key))));
			}
		}
		// 设置表单提交编码为UTF-8
		try {
			postMethod.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			HttpResponse response = httpClient.execute(postMethod);
			logger.info("请求参数={}", nvps.toString());
			int statusCode = response.getStatusLine().getStatusCode();
			resp = EntityUtils.toString(response.getEntity());
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("返回码={}", statusCode);
				logger.error("返回内容={}", resp);
			}
			logger.info("请求返回内容= " + resp);
		} catch (IOException e) {
			logger.error(">>>>>>>>>HTTP client ex:{}", e.getMessage());
		} finally {
			closeRequest(postMethod);
		}
		return resp;
	}

	/**
	 * 配置post charset
	 * 
	 * @param url
	 * @param params
	 * @param charset
	 * @return
	 * @throws HttpException
	 */
	public static String doPostByCharSet(String url,
			Map<String, String> params, String charset) {
		String resp = null;
		HttpClient httpClient = getHttpClient();
		HttpPost postMethod = new HttpPost(url);
		// 填入各个表单域的值
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		if (null != params) {
			for (String key : params.keySet()) {
				nvps.add(new BasicNameValuePair(key, String.valueOf(params
						.get(key))));
			}
		}
		// 设置表单提交编码
		try {
			postMethod.setEntity(new UrlEncodedFormEntity(nvps, charset));
			HttpResponse response = httpClient.execute(postMethod);
			logger.info("请求参数={}", nvps.toString());
			int statusCode = response.getStatusLine().getStatusCode();
			resp = EntityUtils.toString(response.getEntity());
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("返回码={}", statusCode);
				logger.error("返回内容={}", resp);
			}
			logger.info("请求返回内容= " + resp);
		} catch (IOException e) {
			logger.error(">>>>>>>>>HTTP client ex:{}", e.getMessage());
		} finally {
			closeRequest(postMethod);
		}
		return resp;
	}

	/**
	 * 模拟get请求
	 */
	public static String doGet(String url, Map<String, String> params) {
		String resp = null;
		// 填入各个表单域的值
		String paramStr = "";
		if (params != null) {
			Iterator<Entry<String, String>> iter = params.entrySet().iterator();
			try {
				while (iter.hasNext()) {
					Map.Entry<String, String> entry = iter.next();
					String key = entry.getKey();
					String val = String.valueOf(entry.getValue());
					if (StringUtils.isBlank(paramStr)) {
						paramStr += key + "=" + URLEncoder.encode(val, "utf-8");
					} else {
						paramStr += paramStr = "&" + key + "="
								+ URLEncoder.encode(val, "utf-8");
					}
				}
			} catch (UnsupportedEncodingException e1) {
				logger.error("不支持的编码格式 utf-8", e1);
			}
		}
		if (StringUtils.isNotBlank(paramStr)) {
			url = url + "?" + paramStr;
		}
		HttpClient httpClient = getHttpClient();
		HttpGet getMethod = new HttpGet(url);
		// 设置表单提交编码为UTF-8
		try {
			HttpResponse response = httpClient.execute(getMethod);
			logger.info("请求参数={}", paramStr.toString());
			int statusCode = response.getStatusLine().getStatusCode();
			resp = EntityUtils.toString(response.getEntity());
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("返回码={}", statusCode);
				logger.error("返回内容={}", resp);
			}
			logger.info("请求返回内容= " + resp);
		} catch (IOException e) {
			logger.error(">>>>>>>>>HTTP client ex:{}", e.getMessage());
		} finally {
			closeRequest(getMethod);
		}
		return resp;
	}

	/**
	 * 模拟post请求发送json数据
	 */
	public static String doPost(String url, String jsonContent) {
		String resp = null;
		HttpClient httpClient = getHttpClient();
		HttpPost postMethod = new HttpPost(url);
		if (null != jsonContent) {
			HttpEntity entity = new StringEntity(jsonContent,
					ContentType.APPLICATION_JSON);
			postMethod.setEntity(entity);
		}
		// 设置表单提交编码为UTF-8
		try {
			HttpResponse response = httpClient.execute(postMethod);
			logger.info("请求参数={}", postMethod.getEntity().toString());
			int statusCode = response.getStatusLine().getStatusCode();
			resp = EntityUtils.toString(response.getEntity());
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("返回码={}", statusCode);
				logger.error("返回内容={}", resp);
			}
			logger.info("请求返回内容= " + resp);
		} catch (IOException e) {
			logger.error(">>>>>>>>>HTTP client ex:{}", e.getMessage());
		} finally {
			closeRequest(postMethod);
		}
		return resp;
	}

	/**
	 * 模拟rest put请求
	 * 
	 */
	public static String doPut(String url, String params) {
		String resp = null;
		HttpClient httpClient = getHttpClient();
		HttpPut putMethod = new HttpPut(url);
		putMethod.addHeader("Accept", "application/json");
		putMethod.addHeader("Content-Type", "application/json");
		try {
			StringEntity se = new StringEntity(params);
			putMethod.setEntity(se);
			// 设置表单提交编码为UTF-8
			HttpResponse response = httpClient.execute(putMethod);
			logger.info("请求参数={}", se.toString());
			int statusCode = response.getStatusLine().getStatusCode();
			resp = EntityUtils.toString(response.getEntity());
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("返回码={}", statusCode);
				logger.error("返回内容={}", resp);
			}
			logger.info("请求返回内容= " + resp);
		} catch (UnsupportedEncodingException e1) {
			logger.error("不支持的编码格式 utf-8", e1);
		} catch (IOException e) {
			logger.error(">>>>>>>>>HTTP client ex:{}", e.getMessage());
		} finally {
			closeRequest(putMethod);
		}
		return resp;
	}

}
