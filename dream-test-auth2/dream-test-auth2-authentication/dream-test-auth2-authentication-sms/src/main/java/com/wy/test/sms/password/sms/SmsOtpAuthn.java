package com.wy.test.sms.password.sms;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.StandardEnvironment;

import com.wy.test.core.entity.UserInfo;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;

public class SmsOtpAuthn extends AbstractOtpAuthn {

	private static final Logger logger = LoggerFactory.getLogger(SmsOtpAuthn.class);

	protected StandardEnvironment properties;

	@Override
	public boolean produce(UserInfo userInfo) {
		String token = this.genToken(userInfo);
		// You must add send sms code here
		logger.debug("send sms code" + token);
		return true;
	}

	@Override
	public boolean validate(UserInfo userInfo, String token) {
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