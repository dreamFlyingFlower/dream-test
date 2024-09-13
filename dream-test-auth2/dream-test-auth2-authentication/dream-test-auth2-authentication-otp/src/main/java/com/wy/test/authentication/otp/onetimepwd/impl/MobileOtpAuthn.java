package com.wy.test.authentication.otp.onetimepwd.impl;

import com.wy.test.authentication.otp.onetimepwd.AbstractOtpAuthn;
import com.wy.test.core.entity.UserEntity;

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
