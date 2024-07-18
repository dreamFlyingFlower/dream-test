package com.wy.test.otp.password.onetimepwd.impl;

import com.wy.test.core.entity.UserInfo;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;

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
	public boolean produce(UserInfo userInfo) {
		return false;
	}

	@Override
	public boolean validate(UserInfo userInfo, String token) {
		return false;
	}

}
