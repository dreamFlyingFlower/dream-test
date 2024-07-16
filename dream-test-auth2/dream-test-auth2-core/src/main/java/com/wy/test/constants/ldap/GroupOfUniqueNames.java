package com.wy.test.constants.ldap;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * GroupOfUniqueNames objectclass attribute
 * top
 */
public class GroupOfUniqueNames {
	public static ArrayList<String> OBJECTCLASS = new ArrayList<>(Arrays.asList("top", "groupOfUniqueNames"));
	
	public static String	   objectClass				 	 = "groupOfUniqueNames";
	public static final String DISTINGUISHEDNAME 			 = "distinguishedname";
	public static final String CN                            = "cn";
	public static final String UNIQUEMEMBER                  = "uniqueMember";
	public static final String BUSINESSCATEGORY              = "businessCategory";
	public static final String SEEALSO                       = "seeAlso";
	public static final String OWNER                         = "owner";
	public static final String OU                            = "ou";
	public static final String O                           	 = "o";
	public static final String DESCRIPTION                   = "description";
}
