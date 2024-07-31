package com.wy.test.core.util;

import org.junit.jupiter.api.Test;

import dream.flying.flower.framework.core.pretty.PrettyFactory;

public class SqlPrettyTest {

	public SqlPrettyTest() {

	}

	@Test
	public void testSqlFormat() {
		String sqlString = "select * from userinfo where t='111' order by  t,s,t";
		System.out.println(PrettyFactory.getSqlPretty().format(sqlString));
	}
}