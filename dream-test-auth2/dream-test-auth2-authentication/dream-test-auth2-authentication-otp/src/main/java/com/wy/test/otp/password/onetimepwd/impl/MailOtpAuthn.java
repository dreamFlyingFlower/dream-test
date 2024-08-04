package com.wy.test.otp.password.onetimepwd.impl;

import java.text.MessageFormat;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.boot.autoconfigure.mail.MailProperties;

import com.wy.test.core.entity.UserEntity;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MailOtpAuthn extends AbstractOtpAuthn {

	MailProperties mailProperties;

	String subject = "One Time PassWord";

	String messageTemplate = "{0} You Token is {1} , it validity in {2}  minutes.";

	public MailOtpAuthn() {
		otpType = OtpTypes.EMAIL;
	}

	public MailOtpAuthn(MailProperties mailProperties) {
		otpType = OtpTypes.EMAIL;
		this.mailProperties = mailProperties;
	}

	public MailOtpAuthn(MailProperties mailProperties, String subject, String messageTemplate) {
		otpType = OtpTypes.EMAIL;
		this.mailProperties = mailProperties;
		this.subject = subject;
		this.messageTemplate = messageTemplate;
	}

	@Override
	public boolean produce(UserEntity userInfo) {
		try {
			String token = this.genToken(userInfo);
			Email email = new HtmlEmail();
			email.setCharset(this.defaultEncoding);
			email.setHostName(mailProperties.getHost());
			email.setSmtpPort(mailProperties.getPort());
			email.setSSLOnConnect(Boolean.parseBoolean(mailProperties.getProperties().get("ssl")));
			email.setAuthenticator(
					new DefaultAuthenticator(mailProperties.getUsername(), mailProperties.getPassword()));

			email.setFrom(mailProperties.getProperties().get("sender"));
			email.setSubject(subject);
			email.setMsg(MessageFormat.format(messageTemplate, userInfo.getUsername(), token, (interval / 60)));

			email.addTo(userInfo.getEmail());
			try {
				email.send();
			} catch (Exception e) {
				e.printStackTrace();
			}
			log.debug("token " + token + " send to user " + userInfo.getUsername() + ", email " + userInfo.getEmail());
			// 成功返回
			this.optTokenStore.store(userInfo, token, userInfo.getMobile(), OtpTypes.EMAIL);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean validate(UserEntity userInfo, String token) {
		return this.optTokenStore.validate(userInfo, token, OtpTypes.EMAIL, interval);
	}

	public void setMailProperties(MailProperties mailProperties) {
		this.mailProperties = mailProperties;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessageTemplate() {
		return messageTemplate;
	}

	public void setMessageTemplate(String messageTemplate) {
		this.messageTemplate = messageTemplate;
	}
}