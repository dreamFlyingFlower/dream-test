package com.wy.test.core.crypto.password;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * PasswordGen.
 *
 * @author 飞花梦影
 * @date 2024-07-14 21:37:02
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class PasswordGen {

	public static String CHAR_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";

	public static String CHAR_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	public static String CHAR_NUMBERS = "0123456789";

	public static String CHAR_SPECIAL = "~@#^()[]*$-+?_&=!%{}/";

	public static String CHAR_DEFAULT = CHAR_LOWERCASE + CHAR_NUMBERS + CHAR_UPPERCASE;

	private Random random = new Random();

	public static int DEFAULT_LENGTH = 8;

	private int length;

	public PasswordGen() {
		length = DEFAULT_LENGTH;
	}

	public PasswordGen(int length) {
		this.length = length;
	}

	public String gen() {
		this.length = DEFAULT_LENGTH;
		return gen(length);
	}

	public String gen(int length) {
		this.length = length;
		return gen(CHAR_DEFAULT, length);
	}

	/**
	 * gen .
	 * 
	 * @param lowerCase int
	 * @param upperCase int
	 * @param numbers int
	 * @param special int
	 * @return
	 */
	public String gen(int lowerCase, int upperCase, int numbers, int special) {
		StringBuffer password = new StringBuffer("");
		password.append(gen(CHAR_LOWERCASE, lowerCase));
		password.append(gen(CHAR_NUMBERS, numbers));
		password.append(gen(CHAR_UPPERCASE, upperCase));
		password.append(gen(CHAR_SPECIAL, special));
		password.append(gen(CHAR_DEFAULT, length - lowerCase - upperCase - numbers - special));

		// random generator String by sequence password
		return shuffle(password.toString());
	}

	/**
	 * gen.
	 * 
	 * @param charString String
	 * @param length int
	 * @return
	 */
	public String gen(final String charString, int length) {
		if (length < 1) {
			return "";
		}
		int i = 0;
		StringBuffer password = new StringBuffer("");
		while (i < length) {
			int randomPosition = random.nextInt(charString.length());
			// duplicate check
			if (password.indexOf(charString.charAt(randomPosition) + "") < 0) {
				password.append(charString.charAt(randomPosition));
				i++;
			}
		}
		return password.toString();
	}

	public String shuffle(final String charString) {
		StringBuffer password = new StringBuffer("");
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < charString.length(); i++) {
			list.add(charString.charAt(i) + "");
		}

		for (int i = 0; i < length / 2; i++) {
			Collections.shuffle(list);
		}

		for (String chr : list) {
			password.append(chr);
		}
		return password.toString();
	}

}
