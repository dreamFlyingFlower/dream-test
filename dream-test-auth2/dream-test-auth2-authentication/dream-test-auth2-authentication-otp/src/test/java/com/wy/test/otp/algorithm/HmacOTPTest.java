package com.wy.test.otp.algorithm;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.wy.test.authentication.otp.onetimepwd.algorithm.HOTP;
import com.wy.test.authentication.otp.onetimepwd.algorithm.HmacOTP;

import dream.flying.flower.framework.safe.helper.Base32Helpers;

public class HmacOTPTest {

	public static void main(String[] args) {

		byte[] byteseed = Base32Helpers.decode("DCGAGPE2BCDBD6D3FG4NX2QGACVIHXP4");

		System.out.println(HmacOTP.gen(Base32Helpers.decode("DCGAGPE2BCDBD6D3FG4NX2QGACVIHXP4"), 3, 6));

		try {
			System.out.println(HOTP.generateOTP(byteseed, 3, 6, false, -1));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}