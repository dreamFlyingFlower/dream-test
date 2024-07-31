package com.wy.test.core.util;

import org.junit.jupiter.api.Test;

import dream.flying.flower.framework.core.pretty.strategy.XMLHelper;

public class XMLHelperTest {

	@Test
	public void testSqlFormat() {
		String sqlString =
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><xml><data><name>dream</name><age v=\"20\"/></data></xml>";
		System.out.println(XMLHelper.prettyPrintXML(sqlString));
		System.out.println(XMLHelper.transformer(sqlString));
	}
}