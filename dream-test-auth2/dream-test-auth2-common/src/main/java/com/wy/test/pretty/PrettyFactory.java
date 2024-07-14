package com.wy.test.pretty;

import com.wy.test.pretty.impl.JsonPretty;
import com.wy.test.pretty.impl.XmlPretty;

public class PrettyFactory {

	public static Pretty getJsonPretty() {
		return JsonPretty.getInstance();
	}

	public static Pretty getXmlPretty() {
		return XmlPretty.getInstance();
	}

	public static Pretty getSqlPretty() {
		return XmlPretty.getInstance();
	}
}