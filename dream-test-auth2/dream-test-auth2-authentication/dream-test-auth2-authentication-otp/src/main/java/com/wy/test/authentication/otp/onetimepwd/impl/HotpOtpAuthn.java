package com.wy.test.authentication.otp.onetimepwd.impl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.wy.test.authentication.otp.onetimepwd.AbstractOtpAuthn;
import com.wy.test.authentication.otp.onetimepwd.algorithm.HOTP;
import com.wy.test.core.entity.UserEntity;

import dream.flying.flower.framework.safe.helper.Base32Helpers;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HotpOtpAuthn extends AbstractOtpAuthn {

	boolean addChecksum;

	int truncation = -1;

	public HotpOtpAuthn() {
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
		String hotpToken;
		try {
			hotpToken = HOTP.generateOTP(byteSharedSecret, Long.parseLong(userInfo.getSharedCounter()), digits,
					addChecksum, truncation);
			log.debug("token : " + token);
			log.debug("hotpToken : " + hotpToken);
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