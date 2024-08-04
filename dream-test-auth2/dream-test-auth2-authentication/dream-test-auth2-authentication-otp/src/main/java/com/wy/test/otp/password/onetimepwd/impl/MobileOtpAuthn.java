package com.wy.test.otp.password.onetimepwd.impl;

import com.wy.test.core.entity.UserEntity;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;

public class MobileOtpAuthn extends AbstractOtpAuthn {

	public MobileOtpAuthn() {
		otpType = OtpTypes.SMS;
	}

	@Override
	public boolean produce(UserEntity userInfo) {
		return false;
	}

	@Override
	public boolean validate(UserEntity userInfo, String token) {
		return false;
	}

}
