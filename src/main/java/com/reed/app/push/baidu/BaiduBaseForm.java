package com.reed.app.push.baidu;

import java.util.Date;

/**
 * 基础参数form
 * 
 * @author reed
 * 
 */
public class BaiduBaseForm {
	/** */
	private String method;
	/** */
	private String apikey;
	/** */
	private Long user_id;
	/**
	 * 云推送支持多种设备，各种设备的类型编号如下：
	 * 
	 * 1：浏览器设备；
	 * 
	 * 2：PC设备；
	 * 
	 * 3：Andriod设备；
	 * 
	 * 4：iOS设备；
	 * 
	 * 5：Windows Phone设备；
	 * 
	 * 如果存在此字段，则向指定的设备类型推送消息。 默认为android设备类型
	 * 注：推送类型push_type=3，所有人时，会按本字段推送到对应设备，其他设备不会收到广播
	 */
	private Short device_type;
	/** */
	private Long timestamp = new Date().getTime();
	/** */
	private String sign;
	/** */
	private Long expires;
	/** */
	private Long v;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getApikey() {
		return apikey;
	}

	public void setApikey(String apikey) {
		this.apikey = apikey;
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}

	public Short getDevice_type() {
		return device_type;
	}

	public void setDevice_type(Short device_type) {
		this.device_type = device_type;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Long getExpires() {
		return expires;
	}

	public void setExpires(Long expires) {
		this.expires = expires;
	}

	public Long getV() {
		return v;
	}

	public void setV(Long v) {
		this.v = v;
	}

}
