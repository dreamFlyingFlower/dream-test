package com.wy.test.authentication.otp.password.onetimepwd.impl;

import com.wy.test.authentication.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.core.entity.UserEntity;

/**
 * Chip Authentication Program EMV stands for Europay, MasterCard and Visa, a global standard for inter-operation of
 * integrated circuit cards (IC cards or "chip cards") and IC card capable point of sale (POS) terminals and automated
 * teller machines (ATMs), for authenticating credit and debit card transactions.
 */
public class RsaOtpAuthn extends AbstractOtpAuthn {

	public RsaOtpAuthn() {
		otpType = OtpTypes.RSA_OTP;
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
