package com.wy.test.constants;

/**
 * Define int for boolean 0 false 1 true.
 * 
 */
public class ConstsBoolean {

	public static final int FALSE = 0;

	public static final int TRUE = 1;

	private int value = FALSE;

	public ConstsBoolean() {

	}

	public int getValue() {
		return value;
	}

	public boolean isValue() {
		return TRUE == value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public static boolean isTrue(int value) {
		return TRUE == value;
	}

	public static boolean isYes(String value) {
		return "YES" == value.toUpperCase();
	}

	public static boolean isFalse(int value) {
		return FALSE == value;
	}

}
