package com.wy.test.authentication.otp.password.onetimepwd.impl;

import org.apache.commons.codec.binary.Hex;

import com.wy.test.authentication.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.authentication.otp.password.onetimepwd.algorithm.TimeBasedOTP;
import com.wy.test.core.entity.UserEntity;

import dream.flying.flower.framework.core.crypto.Base32Helpers;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CounterBasedOtpAuthn extends AbstractOtpAuthn {

	public CounterBasedOtpAuthn() {
		otpType = OtpTypes.HOTP_OTP;
	}

	@Override
	public boolean produce(UserEntity userInfo) {
		return true;
	}

	@Override
	public boolean validate(UserEntity userInfo, String token) {
		log.debug("SharedCounter : " + userInfo.getSharedCounter());
		byte[] byteSharedSecret = Base32Helpers.decode(userInfo.getSharedSecret());
		String hexSharedSecret = Hex.encodeHexString(byteSharedSecret);
		String counterBasedToken = "";
		if (crypto.equalsIgnoreCase("HmacSHA1")) {
			counterBasedToken = TimeBasedOTP.genOTP(hexSharedSecret, userInfo.getSharedCounter(), "" + digits);
		} else if (crypto.equalsIgnoreCase("HmacSHA256")) {
			counterBasedToken =
					TimeBasedOTP.genOTPHmacSHA256(hexSharedSecret, userInfo.getSharedCounter(), "" + digits);
		} else if (crypto.equalsIgnoreCase("HmacSHA512")) {
			counterBasedToken =
					TimeBasedOTP.genOTPHmacSHA512(hexSharedSecret, userInfo.getSharedCounter(), "" + digits);
		}

		log.debug("token : " + token);
		log.debug("counterBasedToken : " + counterBasedToken);
		if (token.equalsIgnoreCase(counterBasedToken)) {
			return true;
		}
		return false;
	}
}