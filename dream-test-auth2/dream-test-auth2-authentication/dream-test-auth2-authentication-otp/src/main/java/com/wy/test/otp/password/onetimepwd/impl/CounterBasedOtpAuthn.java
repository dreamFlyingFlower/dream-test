package com.wy.test.otp.password.onetimepwd.impl;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.test.core.entity.UserEntity;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.otp.password.onetimepwd.algorithm.TimeBasedOTP;

import dream.flying.flower.framework.core.crypto.Base32Helpers;

public class CounterBasedOtpAuthn extends AbstractOtpAuthn {

	private static final Logger _logger = LoggerFactory.getLogger(CounterBasedOtpAuthn.class);

	public CounterBasedOtpAuthn() {
		otpType = OtpTypes.HOTP_OTP;
	}

	@Override
	public boolean produce(UserEntity userInfo) {
		return true;
	}

	@Override
	public boolean validate(UserEntity userInfo, String token) {
		_logger.debug("SharedCounter : " + userInfo.getSharedCounter());
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

		_logger.debug("token : " + token);
		_logger.debug("counterBasedToken : " + counterBasedToken);
		if (token.equalsIgnoreCase(counterBasedToken)) {
			return true;
		}
		return false;
	}
}