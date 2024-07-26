package com.wy.test.web.authentication.kerberos;

import java.util.regex.Pattern;

public class KerberosPrincipal {

	/**
	 * 
	 */
	public KerberosPrincipal() {
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String kerberosPrincipal = "Administrator@CONNSEC.COM";
		kerberosPrincipal = kerberosPrincipal.substring(0, kerberosPrincipal.indexOf("@"));
		System.out.println(kerberosPrincipal);

		if (Pattern.matches("[0-9]+", "TWO_WEEK")) {
			System.out.println("true");
		} else {
			System.out.println("false");
		}
	}
}