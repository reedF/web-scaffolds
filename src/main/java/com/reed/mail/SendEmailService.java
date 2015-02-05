/**
 * SendMailService.java
 * Copyright (c) 2013 by lashou.com
 */
package com.reed.mail;

import java.io.File;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

/**
 * send email
 * 
 * @author reed
 */
@Service
public class SendEmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger("EMAIL");
	/** 测试地址 */
	private static final String testEmail = "test@test.com";

	@Autowired
	private VelocityEngine commonVelocityEngine;
	@Autowired
	private JavaMailSenderImpl commonJavaMailSender;

	/**
	 * 发送邮件
	 * 
	 * @param template
	 *            模板
	 * @param from
	 *            发件人
	 * @param address
	 *            收件人
	 * @param title
	 *            标题
	 * @param map
	 *            生成模板用数据
	 * @param cc
	 *            抄送人地址：规则英文,号分隔
	 * @return
	 */
	public boolean sendEmail(final String template, final String from,
			final String address, final String title,
			final Map<String, Object> map, final String cc, final boolean isTest) {
		if (StringUtils.isBlank(address)) {
			return false;
		}

		final String content = VelocityEngineUtils.mergeTemplateIntoString(
				commonVelocityEngine, template, "UTF-8", map);

		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
						"UTF-8");
				if (isTest) {
					message.setTo(testEmail);
				} else {
					message.setTo(address);
				}
				if (StringUtils.isNotBlank(cc)) {
					for (final String c : cc.split(",")) {
						message.addCc(c);
					}
				}
				message.setSubject(title);
				message.setText(content, true);
			}
		};

		try {
			commonJavaMailSender.send(preparator);
		} catch (Exception e) {
			LOGGER.error(String.format("邮件发送失败,地址:%s", address), e);
			return false;
		}
		return true;
	}

	/**
	 * 发送邮件smtp
	 * 
	 * @param template
	 *            邮件正文模板
	 * @param from
	 *            发件人
	 * @param address
	 *            收件人地址 数组
	 * @param title
	 *            标题
	 * @param map
	 *            生成模板用数据
	 * @param cc
	 *            抄送人地址 数组
	 * @param bcc
	 *            暗送人地址 数组
	 * @return
	 */
	public boolean sendEmail(final String template, final String from,
			final String[] address, final String title,
			final Map<String, Object> map, final String[] cc,
			final String[] bcc, final boolean isTest) {

		final String content = VelocityEngineUtils.mergeTemplateIntoString(
				commonVelocityEngine, template, "UTF-8", map);
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
						"UTF-8");
				if (isTest) {
					message.setTo(testEmail);
				} else {
					message.setTo(address);
				}
				if (cc != null) {
					message.setCc(cc);
				}
				if (bcc != null) {
					message.setBcc(bcc);
				}
				message.setSubject(title);
				message.setText(content, true);
			}
		};
		try {
			commonJavaMailSender.send(preparator);
		} catch (Exception e) {
			LOGGER.error(
					String.format("邮件发送失败,地址:%s",
							StringUtils.join(address, ",")), e);
			return false;
		}
		return true;
	}

	/**
	 * 发送邮件smtp
	 * 
	 * @param template
	 *            邮件正文模板
	 * @param from
	 *            发件人
	 * @param address
	 *            收件人地址 数组
	 * @param title
	 *            标题
	 * @param map
	 *            生成模板用数据
	 * @param cc
	 *            抄送人地址 数组
	 * @param bcc
	 *            暗送人地址 数组
	 * @return
	 */
	public boolean sendEmail(final String template, final String from,
			final String[] address, final String title,
			final Map<String, Object> map, final String[] cc,
			final String[] bcc, final Map<String, String> attachement,
			final boolean isTest) {
		final String content = VelocityEngineUtils.mergeTemplateIntoString(
				commonVelocityEngine, template, "UTF-8", map);
		MimeMessagePreparator preparator = new MimeMessagePreparator() {
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage,
						true, "UTF-8");
				if (isTest) {
					message.setTo(testEmail);
				} else {
					message.setTo(address);
				}
				if (cc != null) {
					message.setCc(cc);
				}
				if (bcc != null) {
					message.setBcc(bcc);
				}
				message.setSubject(title);
				message.setText(content, true);
				for (String key : attachement.keySet()) {
					message.addAttachment(key, new File(attachement.get(key)));
				}
			}

		};
		try {
			commonJavaMailSender.send(preparator);
		} catch (Exception e) {
			LOGGER.error(
					String.format("邮件发送失败,地址:%s",
							StringUtils.join(address, ",")), e);
			return false;
		}
		return true;
	}

}
