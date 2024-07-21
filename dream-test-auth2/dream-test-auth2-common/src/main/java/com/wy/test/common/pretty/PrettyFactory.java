package com.wy.test.common.pretty;

import com.wy.test.common.pretty.impl.JsonPretty;
import com.wy.test.common.pretty.impl.XmlPretty;

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