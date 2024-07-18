package com.wy.test.otp.password.onetimepwd.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.test.core.entity.UserInfo;
import com.wy.test.crypto.password.PasswordReciprocal;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.otp.password.onetimepwd.algorithm.TimeBasedOTP;

import dream.flying.flower.framework.core.crypto.Base32Helpers;

public class TimeBasedOtpAuthn extends AbstractOtpAuthn {

	private static final Logger _logger = LoggerFactory.getLogger(TimeBasedOtpAuthn.class);

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
	public boolean produce(UserInfo userInfo) {
		return true;
	}

	@Override
	public boolean validate(UserInfo userInfo, String token) {
		_logger.debug("utcTime : " + dateFormat.format(new Date()));
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
		_logger.debug("token : " + token);
		_logger.debug("timeBasedToken : " + timeBasedToken);
		if (token.equalsIgnoreCase(timeBasedToken)) {
			return true;
		}
		return false;
	}
}