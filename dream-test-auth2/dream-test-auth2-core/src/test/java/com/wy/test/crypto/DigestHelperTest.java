package com.wy.test.crypto;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import dream.flying.flower.digest.DigestHelper;

public class DigestHelperTest {

	/*
	 * @Test public void test() {
	 * 
	 * System.out.println(DigestUtils.shaB64("mytest"));
	 * 
	 * System.out.println(DigestUtils.sha1B64("e707c852-29a4-bf56-f8b9-014716850d89"
	 * ));
	 * 
	 * System.out.println(DigestUtils.sha256B64("mytest"));
	 * 
	 * System.out.println(DigestUtils.sha384B64("mytest"));
	 * 
	 * System.out.println(DigestUtils.sha512B64("mytest"));
	 * 
	 * System.out.println(DigestUtils.md5B64("e707c852-29a4-bf56-f8b9-014716850d89")
	 * ); }
	 */
	@Test
	public void testHex() {
		/*
		 * System.out.println(DigestUtils.shaHex("mytest"));
		 * 
		 * System.out.println(DigestUtils.sha1Hex("mytest"));
		 * 
		 * System.out.println(DigestUtils.sha256Hex("mytest"));
		 * 
		 * System.out.println(DigestUtils.sha384Hex("mytest"));
		 * 
		 * System.out.println(DigestUtils.sha512Hex("mytest"));
		 * 
		 * System.out.println(DigestUtils.md5Hex("seamingxy99"));
		 * System.out.println((new Date()).getTime());
		 */

		// String
		// zentaoLogin="http://127.0.0.1/biz/api.php?m=user&f=apilogin&account=%s&code=%s&time=%s&token=%s";
		String zentaoLogin = "http://127.0.0.1/zentao/api.php?m=user&f=apilogin&account=%s&code=%s&time=%s&token=%s";
		String code = "dream";
		// String key = "430ba509ba95094e580b925fc4839459";
		String key = "f71792dfebf23d62bc4d65d1513087e3";
		// String time = ""+System.currentTimeMillis();
		String time = "" + Instant.now().getEpochSecond();
		// String time = "1615370929";
		// String code = "myApp";
		// String key = "427c579384224abf9570779d82969d1e";
		// String time = "1557034496";

		String token = DigestHelper.md5Hex(code + key + time);

		System.out.println("currentTimeMillis " + System.currentTimeMillis());
		System.out.println(DigestHelper.md5Hex(code + key + time));
		String account = "admin";

		String redirec_uri = String.format(zentaoLogin, account, code, time, token);
		System.out.println("redirec_uri : \n" + redirec_uri);
	}
}