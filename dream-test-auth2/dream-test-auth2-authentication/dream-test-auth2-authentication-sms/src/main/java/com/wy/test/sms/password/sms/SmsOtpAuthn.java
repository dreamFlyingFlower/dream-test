package com.wy.test.sms.password.sms;

import java.io.IOException;

import org.springframework.core.env.StandardEnvironment;

import com.wy.test.core.entity.UserEntity;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SmsOtpAuthn extends AbstractOtpAuthn {

	protected StandardEnvironment properties;

	@Override
	public boolean produce(UserEntity userInfo) {
		String token = this.genToken(userInfo);
		// You must add send sms code here
		log.debug("send sms code" + token);
		return true;
	}

	@Override
	public boolean validate(UserEntity userInfo, String token) {
		return true;
	}

	public void setProperties(StandardEnvironment properties) {
		this.properties = properties;
	}

	protected void loadProperties() throws IOException {

	}

	@Override
	public void initPropertys() {

	}
}