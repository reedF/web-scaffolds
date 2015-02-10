package com.reed.pay.bill99;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * HttpClient 连接池工具类(快钱交互专用)
 * 
 */
public class HttpClientBill99 {

	private static final Logger logger = LoggerFactory.getLogger(HttpClientBill99.class);

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
	private HttpClientBill99() {
	}

	public static HttpClient getHttpClient() {
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,HttpVersion.HTTP_1_1);

		params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 120000);
		params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 120000);
		return new DefaultHttpClient(cm, params);
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
	public static String doPost(String url, String content) {
		String resp = null;
		HttpClient httpClient = getHttpClient();
		HttpPost postMethod = new HttpPost(url);
		// 设置表单提交编码为UTF-8
		try {
			ContentType type = ContentType.create("text/xml",Consts.UTF_8);
			HttpEntity entity = new StringEntity(content,type);
			postMethod.setEntity(entity);
			HttpResponse response = httpClient.execute(postMethod);
			logger.info("快钱交互,请求参数={}",content);
			int statusCode = response.getStatusLine().getStatusCode();
			resp = EntityUtils.toString(response.getEntity());
			if (statusCode != HttpStatus.SC_OK) {
				logger.error("快钱交互,返回码={}", statusCode);
				logger.error("快钱交互,返回内容={}", resp);
			}
			logger.info("快钱交互,请求返回内容= " + resp);
		} catch (IOException e) {
			logger.error(">>>>>>>>>HTTP client ex:{}", e.getMessage());
		} finally {
			closeRequest(postMethod);
		}
		return resp;
	}

}
