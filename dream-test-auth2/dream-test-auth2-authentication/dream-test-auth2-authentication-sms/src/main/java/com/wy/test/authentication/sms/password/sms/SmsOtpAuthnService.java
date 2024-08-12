package com.wy.test.authentication.sms.password.sms;

import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.mail.MailProperties;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.wy.test.authentication.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.authentication.otp.password.onetimepwd.impl.MailOtpAuthn;
import com.wy.test.authentication.otp.password.onetimepwd.token.RedisOtpTokenStore;
import com.wy.test.authentication.sms.password.sms.impl.SmsOtpAuthnAliyun;
import com.wy.test.authentication.sms.password.sms.impl.SmsOtpAuthnTencentCloud;
import com.wy.test.authentication.sms.password.sms.impl.SmsOtpAuthnYunxin;
import com.wy.test.core.entity.EmailSenderEntity;
import com.wy.test.core.entity.SmsProviderEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.persistence.service.EmailSenderService;
import com.wy.test.persistence.service.SmsProviderService;

import dream.flying.flower.framework.core.enums.BooleanEnum;

public class SmsOtpAuthnService {

	protected static final Cache<String, AbstractOtpAuthn> smsAuthnStore =
			Caffeine.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).build();

	SmsProviderService smsProviderService;

	EmailSenderService emailSenderService;

	RedisOtpTokenStore redisOptTokenStore;

	public SmsOtpAuthnService(SmsProviderService smsProviderService, EmailSenderService emailSenderService) {
		this.smsProviderService = smsProviderService;
		this.emailSenderService = emailSenderService;
	}

	public SmsOtpAuthnService(SmsProviderService smsProviderService, EmailSenderService emailSenderService,
			RedisOtpTokenStore redisOptTokenStore) {
		this.smsProviderService = smsProviderService;
		this.emailSenderService = emailSenderService;
		this.redisOptTokenStore = redisOptTokenStore;
	}

	public AbstractOtpAuthn getByInstId(String instId) {
		AbstractOtpAuthn otpAuthn = smsAuthnStore.getIfPresent(instId);
		if (otpAuthn == null) {
			SmsProviderEntity smsProvider = smsProviderService
					.getOne(new LambdaQueryWrapper<SmsProviderEntity>().eq(SmsProviderEntity::getInstId, instId));
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
					EmailSenderEntity emailSender = emailSenderService.getOne(
							new LambdaQueryWrapper<EmailSenderEntity>().eq(EmailSenderEntity::getInstId, instId));

					String credentials = PasswordReciprocal.getInstance().decoder(emailSender.getCredentials());
					MailProperties mailProperties = new MailProperties();
					mailProperties.setUsername(emailSender.getAccount());
					mailProperties.setPassword(credentials);
					mailProperties.setHost(emailSender.getSmtpHost());
					mailProperties.setPort(emailSender.getPort());
					mailProperties.getProperties().put("ssl",
							String.valueOf(BooleanEnum.isTrue(emailSender.getSslSwitch())));
					mailProperties.getProperties().put("sender", emailSender.getSender());
					MailOtpAuthn mailOtpAuthn = new MailOtpAuthn(mailProperties);
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