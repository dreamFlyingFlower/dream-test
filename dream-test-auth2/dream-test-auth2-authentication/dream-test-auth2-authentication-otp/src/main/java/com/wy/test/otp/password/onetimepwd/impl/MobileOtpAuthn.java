package com.wy.test.otp.password.onetimepwd.impl;

import com.wy.test.core.entity.UserInfo;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;

public class MobileOtpAuthn extends AbstractOtpAuthn {

	public MobileOtpAuthn() {
		otpType = OtpTypes.SMS;
	}

	@Override
	public boolean produce(UserInfo userInfo) {
		return false;
	}

	@Override
	public boolean validate(UserInfo userInfo, String token) {
		return false;
	}

}
