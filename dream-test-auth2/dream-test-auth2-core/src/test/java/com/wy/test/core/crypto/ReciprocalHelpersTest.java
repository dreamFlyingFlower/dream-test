package com.wy.test.core.crypto;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;

import com.wy.test.core.password.PasswordReciprocal;

public class ReciprocalHelpersTest {

	@Test
	public void test() throws UnsupportedEncodingException {
		/*
		 * //System.out.println(ReciprocalUtils.generateKey(ReciprocalUtils.Algorithm. AES));
		 * 
		 * System.out.println( ReciprocalUtils.aesDecoder(
		 * "7f8cbcd348ea99914f077250b9d14421e32eb7335be127f4838db9ea24f59ea0be2e17e0ce63da63ff29c50150b3343703ed778f2505ea50486236d2c682fa7f49d1efd7dc37fd62b5c518c2a7285d6063dd1d5d1a5c8cd53a622fff407c6537540f0bba5957180835d928082f3901d5aedf4e6ae873f5ab17dc46b7b385a1e306abab90696aed1fbfb147308d6114f5",
		 * "846KZSzYq56M6d5o"));
		 * 
		 * //System.out.println(ReciprocalUtils.blowfishEncode("sadf","1111"));
		 * 
		 * // System.out.println(ReciprocalUtils.blowfishDecoder("3547433be1e3a817","1111") ); System.out.println(
		 * ReciprocalUtils.encode("0eFm6iHvTgNs"));
		 * 
		 * System.out.println( ReciprocalUtils.decoder("76efad66eb7d10140dc2d9ef41c51df0"));
		 * 
		 * System.out.println( ReciprocalUtils.generatorDefaultKey(ReciprocalUtils.Algorithm.DESede));
		 * 
		 * 
		 * 
		 * 
		 * String urlencodeString="中国"; String urlencode = java.net.URLEncoder.encode(urlencodeString, "utf-8");
		 * System.out.println(urlencode); String urldecodeString=
		 * "http://exchange.dream.top/owa/?ae=Item&a=Open&t=IPM.Note&id=RgAAAABPKgpqnlfYQ7BVC%2fBfH2XIBwCS0xhUjzMYSLVky9bw7LddAAAAjov5AACS0xhUjzMYSLVky9bw7LddAAADzoy%2fAAAA&pspid=_1428036768398_867461813";
		 * String urldcode = java.net.URLDecoder.decode(urldecodeString, "utf-8");
		 * 
		 * 
		 * System.out.println(urldcode);
		 */

		String encoderString = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		encoderString = PasswordReciprocal.getInstance().encode(encoderString);
		System.out.println(encoderString);
		System.out.println("length " + encoderString.length());

		encoderString = PasswordReciprocal.getInstance().decoder(encoderString);
		System.out.println(encoderString);

	}
}
