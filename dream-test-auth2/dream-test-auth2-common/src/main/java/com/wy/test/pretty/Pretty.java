package com.wy.test.pretty;

public interface Pretty {

	String LINE_BREAK = "\n";

	String format(String source);

	String formatln(String source);
}