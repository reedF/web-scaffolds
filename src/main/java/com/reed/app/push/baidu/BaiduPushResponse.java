package com.reed.app.push.baidu;

/**
 * Baidu push response obj
 * 
 * @author reed
 * 
 */
public class BaiduPushResponse<T> {
	private Long request_id;

	private Long error_code;

	private String error_msg;
	/** 响应结果 */
	private T response_params;

	public Long getRequest_id() {
		return request_id;
	}

	public void setRequest_id(Long request_id) {
		this.request_id = request_id;
	}

	public Long getError_code() {
		return error_code;
	}

	public void setError_code(Long error_code) {
		this.error_code = error_code;
	}

	public String getError_msg() {
		return error_msg;
	}

	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	public T getResponse_params() {
		return response_params;
	}

	public void setResponse_params(T response_params) {
		this.response_params = response_params;
	}

}
