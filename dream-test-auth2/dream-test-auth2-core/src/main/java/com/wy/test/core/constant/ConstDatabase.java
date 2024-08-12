package com.wy.test.core.constant;

public class ConstDatabase {

	public static String databaseProduct = "MySQL";

	public static String MYSQL = "MySQL";

	public static String POSTGRESQL = "PostgreSQL";

	public static String ORACLE = "Oracle";

	public static String MSSQLSERVER = "SQL Server";

	public static String DB2 = "db2";

	public static boolean compare(String databaseProduct) {
		if (ConstDatabase.databaseProduct.equalsIgnoreCase(databaseProduct)) {
			return true;
		}
		return false;
	}
}