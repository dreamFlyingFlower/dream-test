package com.wy.test.otp.password.onetimepwd;

import java.sql.Types;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.mail.MailProperties;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.entity.EmailSenders;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.otp.password.onetimepwd.impl.MailOtpAuthn;
import com.wy.test.otp.password.onetimepwd.token.RedisOtpTokenStore;
import com.wy.test.persistence.service.EmailSendersService;

import dream.flying.flower.framework.core.enums.BooleanEnum;

public class MailOtpAuthnService {

	protected static final Cache<String, AbstractOtpAuthn> otpAuthnStore =
			Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).build();

	EmailSendersService emailSendersService;

	RedisOtpTokenStore redisOptTokenStore;

	public MailOtpAuthnService(EmailSendersService emailSendersService) {
		this.emailSendersService = emailSendersService;
	}

	public MailOtpAuthnService(RedisOtpTokenStore redisOptTokenStore) {
		this.redisOptTokenStore = redisOptTokenStore;
	}

	public AbstractOtpAuthn getMailOtpAuthn(String instId) {
		AbstractOtpAuthn otpAuthn = otpAuthnStore.getIfPresent(instId);
		if (otpAuthn == null) {
			EmailSenders emailSender = emailSendersService.findOne("where instid = ? ", new Object[] { instId },
					new int[] { Types.VARCHAR });

			String credentials = PasswordReciprocal.getInstance().decoder(emailSender.getCredentials());

			MailProperties mailProperties = new MailProperties();
			mailProperties.setUsername(emailSender.getAccount());
			mailProperties.setPassword(credentials);
			mailProperties.setHost(emailSender.getSmtpHost());
			mailProperties.setPort(emailSender.getPort());
			mailProperties.getProperties().put("ssl", String.valueOf(BooleanEnum.isTrue(emailSender.getSslSwitch())));
			mailProperties.getProperties().put("sender", emailSender.getSender());
			MailOtpAuthn mailOtpAuthn = new MailOtpAuthn(mailProperties);
			mailOtpAuthn.setInterval(60 * 5);// 5 minute
			if (redisOptTokenStore != null) {
				mailOtpAuthn.setOptTokenStore(redisOptTokenStore);
			}
			otpAuthn = mailOtpAuthn;
		}
		otpAuthnStore.put(instId, otpAuthn);
		return otpAuthn;
	}

	public void setRedisOptTokenStore(RedisOtpTokenStore redisOptTokenStore) {
		this.redisOptTokenStore = redisOptTokenStore;
	}
}