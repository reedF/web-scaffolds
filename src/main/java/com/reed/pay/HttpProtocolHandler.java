package com.reed.pay;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

/* *
 *类名：HttpProtocolHandler
 *功能：HttpClient方式访问
 *详细：获取远程HTTP数据
 *版本：3.3
 *日期：2012-08-17
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class HttpProtocolHandler {

    private static String              DEFAULT_CHARSET                     = "GBK";

    /** 连接超时时间，由bean factory设置，缺省为8秒钟 */
    private int                        defaultConnectionTimeout            = 8000;

    /** 回应超时时间, 由bean factory设置，缺省为30秒钟 */
    private int                        defaultSoTimeout                    = 30000;

    /** 闲置连接超时时间, 由bean factory设置，缺省为60秒钟 */
    private int                        defaultIdleConnTimeout              = 60000;

    private int                        defaultMaxConnPerHost               = 50;

    private int                        defaultMaxTotalConn                 = 80;

    /** 默认等待HttpConnectionManager返回连接超时（只有在达到最大连接数时起作用）：1秒*/
    private static final long          defaultHttpConnectionManagerTimeout = 3 * 1000;

    /**
     * HTTP连接管理器，该连接管理器必须是线程安全的.
     */
    private PoolingClientConnectionManager connectionManager;

//    private static HttpProtocolHandler httpProtocolHandler                 = new HttpProtocolHandler();

    /**
     * 工厂方法
     * 
     * @return
     */
//    public static HttpProtocolHandler getInstance() {
//        return httpProtocolHandler;
//    }

    /**
     * 私有的构造方法
     */
    public HttpProtocolHandler() {
        // 创建一个线程安全的HTTP连接池
//    	connectionManager = new PoolingClientConnectionManager();
//    	connectionManager.setDefaultMaxPerRoute(defaultMaxConnPerHost);
//    	connectionManager.setMaxTotal(defaultMaxTotalConn);
    }

    /**
     * 执行Http请求
     * 
     * @param request 请求数据
     * @param strParaFileName 文件类型的参数名
     * @param strFilePath 文件路径
     * @return 
     * @throws HttpException, IOException 
     * @throws URISyntaxException 
     */
    public HttpResponse execute(HttpRequest request) throws IOException {
    	
    	HttpParams params = new BasicHttpParams();  
        //设置连接超时时间   
    	int connectionTimeout = defaultConnectionTimeout;
        if (request.getConnectionTimeout() > 0) {
            connectionTimeout = request.getConnectionTimeout();
        }
        // 设置回应超时
    	HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
    	int soTimeout = defaultSoTimeout;
        if (request.getTimeout() > 0) {
            soTimeout = request.getTimeout();
        }
    	HttpConnectionParams.setSoTimeout(params, soTimeout);
    
        HttpClient httpclient = new DefaultHttpClient();
        // 设置等待ConnectionManager释放connection的时间
//        httpclient.getParams().setConnectionManagerTimeout(defaultHttpConnectionManagerTimeout);

        String charset = request.getCharset();
        charset = charset == null ? DEFAULT_CHARSET : charset;
        HttpUriRequest method = null;
//
        //get模式且不带上传文件
        if (request.getMethod().equals(HttpRequest.METHOD_GET)) {
        	method = new HttpGet(request.getUrl()+request.getQueryString());
            // parseNotifyConfig会保证使用GET方法时，request一定使用QueryString
//            method.setQueryString(request.getQueryString());
        } else{
        	//post模式且不带上传文件
        	method = new HttpPost(request.getUrl());
        	List<NameValuePair> qparams = new ArrayList<NameValuePair>();
        	NameValuePair[] parameters = request.getParameters();
        	for (NameValuePair nameValuePair : parameters) {
        		qparams.add(nameValuePair);
			}
        	UrlEncodedFormEntity entity = new UrlEncodedFormEntity(qparams, charset);
        	((HttpPost) method).setEntity(entity);
        }
        HttpResponse response = new HttpResponse();
        try {
        	org.apache.http.HttpResponse execute = httpclient.execute(method);
        	HttpEntity entity = execute.getEntity();
            if (request.getResultType().equals(HttpResultType.STRING)) {
                response.setStringResult(EntityUtils.toString(entity));
            } else if (request.getResultType().equals(HttpResultType.BYTES)) {
                response.setByteResult(EntityUtils.toByteArray(entity));
            }
            response.setResponseHeaders(method.getAllHeaders());
            EntityUtils.consume(entity);
        } catch (IOException ex) {

            return null;
        } catch (Exception ex) {

            return null;
        } finally {
        	httpclient.getConnectionManager().shutdown(); 
        }
        return response;
    }

    /**
     * 将NameValuePairs数组转变为字符串
     * 
     * @param nameValues
     * @return
     */
    protected String toString(NameValuePair[] nameValues) {
        if (nameValues == null || nameValues.length == 0) {
            return "null";
        }

        StringBuffer buffer = new StringBuffer();

        for (int i = 0; i < nameValues.length; i++) {
            NameValuePair nameValue = nameValues[i];

            if (i == 0) {
                buffer.append(nameValue.getName() + "=" + nameValue.getValue());
            } else {
                buffer.append("&" + nameValue.getName() + "=" + nameValue.getValue());
            }
        }

        return buffer.toString();
    }
}
