package com.wy.test.authentication.sms.password.sms.impl;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import com.wy.test.authentication.sms.password.sms.SmsOtpAuthn;
import com.wy.test.core.entity.UserEntity;

import lombok.extern.slf4j.Slf4j;

/**
 * 腾讯云短信验证
 *
 * @author 飞花梦影
 * @date 2024-08-07 15:22:23
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Slf4j
public class SmsOtpAuthnTencentCloud extends SmsOtpAuthn {

	String secretId;

	String secretKey;

	// 短信SDKAPPID
	String smsSdkAppid;

	// 短信模板
	String templateId;

	// 签名
	String sign;

	public SmsOtpAuthnTencentCloud() {
		otpType = OtpTypes.SMS;
	}

	public SmsOtpAuthnTencentCloud(String secretId, String secretKey, String smsSdkAppid, String templateId,
			String sign) {
		otpType = OtpTypes.SMS;
		this.secretId = secretId;
		this.secretKey = secretKey;
		this.smsSdkAppid = smsSdkAppid;
		this.templateId = templateId;
		this.sign = sign;
	}

	@Override
	public boolean produce(UserEntity userInfo) {
		// 手机号
		String mobile = userInfo.getMobile();
		if (mobile != null && !mobile.equals("")) {
			try {
				Credential cred = new Credential(secretId, secretKey);

				HttpProfile httpProfile = new HttpProfile();
				httpProfile.setEndpoint("sms.tencentcloudapi.com");

				ClientProfile clientProfile = new ClientProfile();
				clientProfile.setHttpProfile(httpProfile);

				SmsClient client = new SmsClient(cred, "ap-beijing", clientProfile);
				String token = this.genToken(userInfo);
				String params = "{\"PhoneNumberSet\":[\"" + mobile + "\"]," + "\"TemplateID\":\"" + templateId
						+ "\",\"Sign\":\"" + sign + "\"," + "\"TemplateParamSet\":[\"" + token + "\",\"" + this.interval
						+ "\"]," + "\"SmsSdkAppid\":\"" + smsSdkAppid + "\"}";

				SendSmsRequest req = SendSmsRequest.fromJsonString(params, SendSmsRequest.class);

				SendSmsResponse resp = client.SendSms(req);

				log.debug("responseString " + SendSmsRequest.toJsonString(resp));
				if (resp.getSendStatusSet()[0].getCode().equalsIgnoreCase("Ok")) {
					this.optTokenStore.store(userInfo, token, userInfo.getMobile(), OtpTypes.SMS);
					return true;
				}

			} catch (Exception e) {
				log.error(" produce code error ", e);
			}
		}
		return false;
	}

	@Override
	public boolean validate(UserEntity userInfo, String token) {
		return this.optTokenStore.validate(userInfo, token, OtpTypes.SMS, interval);
	}

	public String getSecretId() {
		return secretId;
	}

	public void setSecretId(String secretId) {
		this.secretId = secretId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getSmsSdkAppid() {
		return smsSdkAppid;
	}

	public void setSmsSdkAppid(String smsSdkAppid) {
		this.smsSdkAppid = smsSdkAppid;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
}