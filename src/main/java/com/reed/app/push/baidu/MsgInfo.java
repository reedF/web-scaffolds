package com.reed.app.push.baidu;

import java.util.HashMap;
import java.util.Map;

/**
 * Baidu message_type = 1 (通知类型) doc:
 * http://developer.baidu.com/wiki/index.php?title
 * =docs/cplat/push/api/list#push_msg
 * 
 * { //android必选，ios可选 "title" : "hello" , “description: "hello world"
 * 
 * //android特有字段，可选 "notification_builder_id": 0, "notification_basic_style": 7,
 * "open_type":0, "net_support" : 1, "user_confirm": 0, "url":
 * "http://developer.baidu.com", "pkg_content":"", "pkg_name" :
 * "com.baidu.bccsclient", "pkg_version":"0.1",
 * 
 * //android自定义字段 "custom_content": { "key1":"value1", "key2":"value2" },
 * 
 * //ios特有字段，可选 "aps": { "alert":"Message From Baidu Push", "sound":"",
 * "badge":0 },
 * 
 * //ios的自定义字段 "key1":"value1", "key2":"value2" }
 * 
 * @author reed
 * 
 */
public class MsgInfo {
	/** 消息标题 , ,为空时显示应用名 */
	private String title;
	/** 消息弹窗点击后显示的内容 */
	private String description;
	/** android */
	private int notification_builder_id = 0;
	private int notification_basic_style = 7;
	private int open_type = 2;
	private int user_confirm = 0;
	private String url = "http://developer.baidu.com";
	// private String pkg_content = "";
	// private int net_support = 1;
	// private String pkg_name = "com.baidu.bccsclient";
	// private String pkg_version = "0.1";
	/** 消息体，业务数据，可以是文本或json */
	private Map<String, String> custom_content = new HashMap<String, String>();
	// ios
	private Map<String, String> aps;

	public MsgInfo() {

	}

	public MsgInfo(String title, String description) {
		super();
		// this.title = title;
		this.description = title;
		this.custom_content.put("data", description);
		this.aps = new HashMap<String, String>();
		this.aps.put("alert", title);
		this.aps.put("sound", "");
		this.aps.put("badge", "0");
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getNotification_builder_id() {
		return notification_builder_id;
	}

	public void setNotification_builder_id(int notification_builder_id) {
		this.notification_builder_id = notification_builder_id;
	}

	public int getNotification_basic_style() {
		return notification_basic_style;
	}

	public void setNotification_basic_style(int notification_basic_style) {
		this.notification_basic_style = notification_basic_style;
	}

	public int getOpen_type() {
		return open_type;
	}

	public void setOpen_type(int open_type) {
		this.open_type = open_type;
	}

	public int getUser_confirm() {
		return user_confirm;
	}

	public void setUser_confirm(int user_confirm) {
		this.user_confirm = user_confirm;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	// public int getNet_support() {
	// return net_support;
	// }
	//
	// public void setNet_support(int net_support) {
	// this.net_support = net_support;
	// }
	// public String getPkg_content() {
	// return pkg_content;
	// }
	//
	// public void setPkg_content(String pkg_content) {
	// this.pkg_content = pkg_content;
	// }
	//
	// public String getPkg_name() {
	// return pkg_name;
	// }
	//
	// public void setPkg_name(String pkg_name) {
	// this.pkg_name = pkg_name;
	// }

	// public String getPkg_version() {
	// return pkg_version;
	// }
	//
	// public void setPkg_version(String pkg_version) {
	// this.pkg_version = pkg_version;
	// }

	public Map<String, String> getCustom_content() {
		return custom_content;
	}

	public void setCustom_content(Map<String, String> custom_content) {
		this.custom_content = custom_content;
	}

	public Map<String, String> getAps() {
		return aps;
	}

	public void setAps(Map<String, String> aps) {
		this.aps = aps;
	}

}
