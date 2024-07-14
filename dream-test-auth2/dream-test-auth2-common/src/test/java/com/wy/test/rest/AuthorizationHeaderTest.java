package com.wy.test.rest;

import org.junit.jupiter.api.Test;

import com.wy.test.util.AuthorizationHeader;
import com.wy.test.util.AuthorizationHeaderUtils;

public class AuthorizationHeaderTest {

	@Test
	public void test() {

		String basic = AuthorizationHeaderUtils.createBasic("Aladdin", "open sesame");
		System.out.println(basic);

		String ahc_basic = "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==";
		System.out.println(AuthorizationHeaderUtils.resolve(ahc_basic));

		AuthorizationHeader ahc = new AuthorizationHeader("Aladdin");
		System.out.println(ahc.transform());

		System.out.println(AuthorizationHeaderUtils.resolve(ahc.transform()));

	}
}
