package com.wy.test.core.rest;

import org.junit.jupiter.api.Test;

import dream.flying.flower.framework.core.helper.TokenHeader;
import dream.flying.flower.framework.core.helper.TokenHelpers;

public class AuthorizationHeaderTest {

	@Test
	public void test() {

		String basic = TokenHelpers.createBasic("Aladdin", "open sesame");
		System.out.println(basic);

		String ahc_basic = "Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==";
		System.out.println(TokenHelpers.resolve(ahc_basic));

		TokenHeader ahc = new TokenHeader("Aladdin");
		System.out.println(ahc.transform());

		System.out.println(TokenHelpers.resolve(ahc.transform()));

	}
}
