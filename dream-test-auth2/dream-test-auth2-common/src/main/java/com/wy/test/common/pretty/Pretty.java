package com.wy.test.common.pretty;

public interface Pretty {

	String LINE_BREAK = "\n";

	String format(String source);

	String formatln(String source);
}