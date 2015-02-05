package com.reed.app.push.baidu;

import com.reed.common.util.JsonUtil;

/**
 * push message form doc:{@link http
 * ://developer.baidu.com/wiki/index.php?title=docs
 * /cplat/push/faq#.E4.B8.BA.E4.BD.95.E9.80.9A.E8.BF.87Server_SDK.E6
 * .8E.A8.E9.80.81.E6.88.90.E5.8A.9F.EF.BC.8CAndroid.E7.AB.AF.E5.8D.B4.E6.94.B6.E4.B8.8D.E5.88.B0.E9.80.9A.E7.9F.A5.EF.BC.9F
 * * }
 * 
 * @author reed
 * 
 */
public class PushMsgForm extends BaiduBaseForm {
	/**
	 * 推送类型，取值范围为：1～3 1：单个人，必须指定user_id 和
	 * channel_id（指定用户的指定设备）或者user_id（指定用户的所有设备） 2：一群人，必须指定 tag
	 * 3：所有人，无需指定tag、user_id、channel_id
	 */
	private short push_type = 1;

	private Long channel_id;

	private String tag;
	/**
	 * 消息类型 0：消息（透传给应用的消息体）
	 * 
	 * 1：通知（对应设备上的消息通知） IOS仅响应1 默认值为0
	 */
	private short message_type = 0;
	/** 消息体 */
	private String messages;
	/** 消息唯一标识 */
	private String msg_keys;
	/** 过期时间 */
	private Long message_expires;

	/**
	 * 部署状态。指定应用当前的部署状态，可取值： 1：开发状态
	 * 
	 * 2：生产状态
	 * 
	 * 若不指定，则默认设置为生产状态。特别提醒：该功能只支持ios设备类型。
	 */
	private short deploy_status = 2;

	public short getDeploy_status() {
		return deploy_status;
	}

	public void setDeploy_status(short deploy_status) {
		this.deploy_status = deploy_status;
	}

	public short getPush_type() {
		return push_type;
	}

	public void setPush_type(short push_type) {
		this.push_type = push_type;
	}

	public Long getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(Long channel_id) {
		this.channel_id = channel_id;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public short getMessage_type() {
		return message_type;
	}

	public void setMessage_type(short message_type) {
		this.message_type = message_type;
	}

	public String getMessages() {
		return messages;
	}

	public void setMessages(String messages) {
		this.messages = messages;
	}

	/**
	 * 根据message_type类型，组装消息体格式
	 * 
	 * @param title
	 *            message_type = 0时可空
	 * @param msg
	 */
	public void setMsg(String title, String msg) {
		if (this.message_type == 0) {
			this.messages = msg;
		} else {
			MsgInfo m = new MsgInfo(title, msg);
			this.messages = JsonUtil.toJson(m);
		}
	}

	public String getMsg_keys() {
		return msg_keys;
	}

	public void setMsg_keys(String msg_keys) {
		this.msg_keys = msg_keys;
	}

	public Long getMessage_expires() {
		return message_expires;
	}

	public void setMessage_expires(Long message_expires) {
		this.message_expires = message_expires;
	}

}
