package com.wy.test.word;

public class SubStr {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String browser = "Chrome/44.0.2369.0";
		System.out.println(browser.indexOf('.'));
		String passwordAlgorithm = "MD5-HEX";
		System.out.println(passwordAlgorithm.substring(0, passwordAlgorithm.indexOf("-HEX")));
	}

}
