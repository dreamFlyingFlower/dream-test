package com.wy.test.constants;

import com.wy.test.configuration.ApplicationConfig;

public class ConstsDatabase {

	public static String MYSQL = "MySQL";

	public static String POSTGRESQL = "PostgreSQL";

	public static String ORACLE = "Oracle";

	public static String MSSQLSERVER = "SQL Server";

	public static String DB2 = "db2";

	public static boolean compare(String databaseProduct) {
		if (databaseProduct.equalsIgnoreCase(ApplicationConfig.databaseProduct)) {
			return true;
		}
		return false;
	}

}
