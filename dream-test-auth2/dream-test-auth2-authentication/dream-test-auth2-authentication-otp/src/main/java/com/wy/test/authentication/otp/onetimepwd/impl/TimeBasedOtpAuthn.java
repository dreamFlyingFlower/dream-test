package com.wy.test.authentication.otp.onetimepwd.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Hex;

import com.wy.test.authentication.otp.onetimepwd.AbstractOtpAuthn;
import com.wy.test.authentication.otp.onetimepwd.algorithm.TimeBasedOTP;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;

import dream.flying.flower.framework.crypto.helper.Base32Helpers;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeBasedOtpAuthn extends AbstractOtpAuthn {

	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public TimeBasedOtpAuthn() {
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	public TimeBasedOtpAuthn(int digits, int interval) {
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		super.digits = digits;
		super.interval = interval;
	}

	@Override
	public boolean produce(UserEntity userInfo) {
		return true;
	}

	@Override
	public boolean validate(UserEntity userInfo, String token) {
		log.debug("utcTime : " + dateFormat.format(new Date()));
		long currentTimeSeconds = System.currentTimeMillis() / 1000;
		String sharedSecret = PasswordReciprocal.getInstance().decoder(userInfo.getSharedSecret());
		byte[] byteSharedSecret = Base32Helpers.decode(sharedSecret);
		String hexSharedSecret = Hex.encodeHexString(byteSharedSecret);
		String timeBasedToken = "";
		if (crypto.equalsIgnoreCase("HmacSHA1")) {
			timeBasedToken = TimeBasedOTP.genOTP(hexSharedSecret,
					Long.toHexString(currentTimeSeconds / interval).toUpperCase() + "", digits + "");
		} else if (crypto.equalsIgnoreCase("HmacSHA256")) {
			timeBasedToken = TimeBasedOTP.genOTPHmacSHA256(hexSharedSecret,
					Long.toHexString(currentTimeSeconds / interval).toUpperCase() + "", digits + "");
		} else if (crypto.equalsIgnoreCase("HmacSHA512")) {
			timeBasedToken = TimeBasedOTP.genOTPHmacSHA512(hexSharedSecret,
					Long.toHexString(currentTimeSeconds / interval).toUpperCase() + "", digits + "");
		}
		log.debug("token : " + token);
		log.debug("timeBasedToken : " + timeBasedToken);
		if (token.equalsIgnoreCase(timeBasedToken)) {
			return true;
		}
		return false;
	}
}