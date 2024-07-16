package com.wy.test.otp.password.onetimepwd.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.test.entity.UserInfo;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.otp.password.onetimepwd.algorithm.HOTP;

import dream.flying.flower.framework.core.crypto.Base32Helpers;

public class HotpOtpAuthn extends AbstractOtpAuthn {

	private static final Logger _logger = LoggerFactory.getLogger(HotpOtpAuthn.class);

	boolean addChecksum;

	int truncation = -1;

	public HotpOtpAuthn() {
		otpType = OtpTypes.HOTP_OTP;
	}

	@Override
	public boolean produce(UserInfo userInfo) {
		return true;
	}

	@Override
	public boolean validate(UserInfo userInfo, String token) {
		_logger.debug("SharedCounter : " + userInfo.getSharedCounter());
		byte[] byteSharedSecret = Base32Helpers.decode(userInfo.getSharedSecret());
		String hotpToken;
		try {
			hotpToken = HOTP.generateOTP(byteSharedSecret, Long.parseLong(userInfo.getSharedCounter()), digits,
					addChecksum, truncation);
			_logger.debug("token : " + token);
			_logger.debug("hotpToken : " + hotpToken);
			if (token.equalsIgnoreCase(hotpToken)) {
				return true;
			}
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * the addChecksum.
	 */
	public boolean isAddChecksum() {
		return addChecksum;
	}

	/**
	 * addChecksum the addChecksum to set.
	 */
	public void setAddChecksum(boolean addChecksum) {
		this.addChecksum = addChecksum;
	}

	/**
	 * the truncation.
	 */
	public int getTruncation() {
		return truncation;
	}

	/**
	 * truncation the truncation to set.
	 */
	public void setTruncation(int truncation) {
		this.truncation = truncation;
	}
}