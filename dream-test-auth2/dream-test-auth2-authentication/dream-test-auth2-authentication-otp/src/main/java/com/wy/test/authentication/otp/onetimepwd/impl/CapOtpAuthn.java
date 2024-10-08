package com.wy.test.authentication.otp.onetimepwd.impl;

import com.wy.test.authentication.otp.onetimepwd.AbstractOtpAuthn;
import com.wy.test.core.entity.UserEntity;

/**
 * Chip Authentication Program EMV stands for Europay, MasterCard and Visa, a global standard for inter-operation of
 * integrated circuit cards (IC cards or "chip cards") and IC card capable point of sale (POS) terminals and automated
 * teller machines (ATMs), for authenticating credit and debit card transactions.
 * 
 */
public class CapOtpAuthn extends AbstractOtpAuthn {

	public CapOtpAuthn() {
		otpType = OtpTypes.CAP_OTP;
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
