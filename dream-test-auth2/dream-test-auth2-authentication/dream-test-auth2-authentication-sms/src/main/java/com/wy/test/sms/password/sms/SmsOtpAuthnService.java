package com.wy.test.sms.password.sms;

import java.sql.Types;
import java.util.concurrent.TimeUnit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.core.configuration.EmailConfig;
import com.wy.test.core.constants.ConstsBoolean;
import com.wy.test.core.crypto.password.PasswordReciprocal;
import com.wy.test.core.entity.EmailSenders;
import com.wy.test.core.entity.SmsProvider;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.otp.password.onetimepwd.impl.MailOtpAuthn;
import com.wy.test.otp.password.onetimepwd.token.RedisOtpTokenStore;
import com.wy.test.persistence.service.EmailSendersService;
import com.wy.test.persistence.service.SmsProviderService;
import com.wy.test.sms.password.sms.impl.SmsOtpAuthnAliyun;
import com.wy.test.sms.password.sms.impl.SmsOtpAuthnTencentCloud;
import com.wy.test.sms.password.sms.impl.SmsOtpAuthnYunxin;

public class SmsOtpAuthnService {

	protected static final Cache<String, AbstractOtpAuthn> smsAuthnStore =
			Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).build();

	SmsProviderService smsProviderService;

	EmailSendersService emailSendersService;

	RedisOtpTokenStore redisOptTokenStore;

	public SmsOtpAuthnService(SmsProviderService smsProviderService, EmailSendersService emailSendersService) {
		this.smsProviderService = smsProviderService;
		this.emailSendersService = emailSendersService;
	}

	public SmsOtpAuthnService(SmsProviderService smsProviderService, EmailSendersService emailSendersService,
			RedisOtpTokenStore redisOptTokenStore) {
		this.smsProviderService = smsProviderService;
		this.emailSendersService = emailSendersService;
		this.redisOptTokenStore = redisOptTokenStore;
	}

	public AbstractOtpAuthn getByInstId(String instId) {
		AbstractOtpAuthn otpAuthn = smsAuthnStore.getIfPresent(instId);
		if (otpAuthn == null) {
			SmsProvider smsProvider = smsProviderService.findOne("where instid = ? ", new Object[] { instId },
					new int[] { Types.VARCHAR });
			if (smsProvider != null) {

				if (smsProvider.getProvider().equalsIgnoreCase("aliyun")) {
					SmsOtpAuthnAliyun aliyun = new SmsOtpAuthnAliyun(smsProvider.getAppKey(),
							PasswordReciprocal.getInstance().decoder(smsProvider.getAppSecret()),
							smsProvider.getTemplateId(), smsProvider.getSignName());
					if (redisOptTokenStore != null) {
						aliyun.setOptTokenStore(redisOptTokenStore);
					}
					otpAuthn = aliyun;
				} else if (smsProvider.getProvider().equalsIgnoreCase("tencentcloud")) {
					SmsOtpAuthnTencentCloud tencentCloud = new SmsOtpAuthnTencentCloud(smsProvider.getAppKey(),
							PasswordReciprocal.getInstance().decoder(smsProvider.getAppSecret()),
							smsProvider.getSmsSdkAppId(), smsProvider.getTemplateId(), smsProvider.getSignName());
					if (redisOptTokenStore != null) {
						tencentCloud.setOptTokenStore(redisOptTokenStore);
					}
					otpAuthn = tencentCloud;
				} else if (smsProvider.getProvider().equalsIgnoreCase("neteasesms")) {
					SmsOtpAuthnYunxin yunxin = new SmsOtpAuthnYunxin(smsProvider.getAppKey(),
							PasswordReciprocal.getInstance().decoder(smsProvider.getAppSecret()),
							smsProvider.getTemplateId());
					if (redisOptTokenStore != null) {
						yunxin.setOptTokenStore(redisOptTokenStore);
					}
					otpAuthn = yunxin;
				} else if (smsProvider.getProvider().equalsIgnoreCase("email")) {
					EmailSenders emailSender = emailSendersService.findOne("where instid = ? ", new Object[] { instId },
							new int[] { Types.VARCHAR });

					String credentials = PasswordReciprocal.getInstance().decoder(emailSender.getCredentials());
					EmailConfig emailConfig = new EmailConfig(emailSender.getAccount(), credentials,
							emailSender.getSmtpHost(), emailSender.getPort(),
							ConstsBoolean.isTrue(emailSender.getSslSwitch()), emailSender.getSender());
					MailOtpAuthn mailOtpAuthn = new MailOtpAuthn(emailConfig);
					if (redisOptTokenStore != null) {
						mailOtpAuthn.setOptTokenStore(redisOptTokenStore);
					}
					otpAuthn = mailOtpAuthn;
				}

				smsAuthnStore.put(instId, otpAuthn);
			}
		}
		return otpAuthn;
	}

	public void setRedisOptTokenStore(RedisOtpTokenStore redisOptTokenStore) {
		this.redisOptTokenStore = redisOptTokenStore;
	}

}
