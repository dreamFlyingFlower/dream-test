package com.wy.test.core.constants.ldap;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * GroupOfNames objectclass attribute top
 * 
 * @author shimingxy
 *
 */
public class GroupOfNames {

	public static ArrayList<String> OBJECTCLASS = new ArrayList<>(Arrays.asList("top", "groupOfNames"));

	public static String objectClass = "groupOfNames";

	public static final String DISTINGUISHEDNAME = "distinguishedname";

	public static final String CN = "cn";

	public static final String MEMBER = "member";

	public static final String BUSINESSCATEGORY = "businessCategory";

	public static final String SEEALSO = "seeAlso";

	public static final String OWNER = "owner";

	public static final String OU = "ou";

	public static final String O = "o";

	public static final String DESCRIPTION = "description";
}
