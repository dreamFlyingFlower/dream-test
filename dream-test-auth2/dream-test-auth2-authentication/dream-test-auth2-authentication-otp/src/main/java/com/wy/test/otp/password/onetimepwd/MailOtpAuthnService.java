package com.wy.test.otp.password.onetimepwd;

import java.sql.Types;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.configuration.EmailConfig;
import com.wy.test.constants.ConstsBoolean;
import com.wy.test.crypto.password.PasswordReciprocal;
import com.wy.test.entity.EmailSenders;
import com.wy.test.otp.password.onetimepwd.impl.MailOtpAuthn;
import com.wy.test.otp.password.onetimepwd.token.RedisOtpTokenStore;
import com.wy.test.persistence.service.EmailSendersService;

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
			EmailConfig emailConfig = new EmailConfig(emailSender.getAccount(), credentials, emailSender.getSmtpHost(),
					emailSender.getPort(), ConstsBoolean.isTrue(emailSender.getSslSwitch()), emailSender.getSender());
			MailOtpAuthn mailOtpAuthn = new MailOtpAuthn(emailConfig);
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
