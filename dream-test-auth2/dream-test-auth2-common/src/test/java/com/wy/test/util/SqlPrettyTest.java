package com.wy.test.util;

import org.junit.jupiter.api.Test;

import com.wy.test.common.pretty.PrettyFactory;

public class SqlPrettyTest {

	public SqlPrettyTest() {

	}

	@Test
	public void testSqlFormat() {
		String sqlString = "select * from userinfo where t='111' order by  t,s,t";
		System.out.println(PrettyFactory.getSqlPretty().format(sqlString));
	}

}
