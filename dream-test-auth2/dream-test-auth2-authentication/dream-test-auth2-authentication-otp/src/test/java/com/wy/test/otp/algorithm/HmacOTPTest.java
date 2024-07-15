package com.wy.test.otp.algorithm;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import com.wy.test.crypto.Base32Utils;
import com.wy.test.otp.password.onetimepwd.algorithm.HOTP;
import com.wy.test.otp.password.onetimepwd.algorithm.HmacOTP;

public class HmacOTPTest {

	public static void main(String[] args) {

		byte[] byteseed = Base32Utils.decode("DCGAGPE2BCDBD6D3FG4NX2QGACVIHXP4");

		System.out.println(HmacOTP.gen(Base32Utils.decode("DCGAGPE2BCDBD6D3FG4NX2QGACVIHXP4"), 3, 6));

		try {
			System.out.println(HOTP.generateOTP(byteseed, 3, 6, false, -1));
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
}
