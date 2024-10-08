package com.wy.test.core.constant;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * OrganizationalUnit objectclass attribute top
 */
public class OrganizationalUnit {

	public static ArrayList<String> OBJECTCLASS = new ArrayList<>(Arrays.asList("top", "OrganizationalUnit"));

	public static String objectClass = "OrganizationalUnit";

	public static final String DISTINGUISHEDNAME = "distinguishedname";

	/** OrganizationalUnit ou */
	public static final String OU = "ou";

	/** OrganizationalUnit userPassword */
	public static final String USERPASSWORD = "userPassword";

	/** OrganizationalUnit searchGuide */
	public static final String SEARCHGUIDE = "searchGuide";

	/** OrganizationalUnit seeAlso */
	public static final String SEEALSO = "seeAlso";

	/** OrganizationalUnit description */
	public static final String DESCRIPTION = "description";

	/** OrganizationalUnit businessCategory */
	public static final String BUSINESSCATEGORY = "businessCategory";

	/** OrganizationalUnit x121Address */
	public static final String X121ADDRESS = "x121Address";

	/** OrganizationalUnit registeredAddress */
	public static final String REGISTEREDADDRESS = "registeredAddress";

	/** OrganizationalUnit destinationIndicator */
	public static final String DESTINATIONINDICATOR = "destinationIndicator";

	/** OrganizationalUnit preferredDeliveryMethod */
	public static final String PREFERREDDELIVERYMETHOD = "preferredDeliveryMethod";

	/** OrganizationalUnit telexNumber */
	public static final String TELEXNUMBER = "telexNumber";

	/** OrganizationalUnit teletexTerminalIdentifier */
	public static final String TELETEXTERMINALIDENTIFIER = "teletexTerminalIdentifier";

	/** OrganizationalUnit telephoneNumber */
	public static final String TELEPHONENUMBER = "telephoneNumber";

	/** OrganizationalUnit internationaliSDNNumber */
	public static final String INTERNATIONALISDNNUMBER = "internationaliSDNNumber";

	/** OrganizationalUnit facsimileTelephoneNumber */
	public static final String FACSIMILETELEPHONENUMBER = "facsimileTelephoneNumber";

	/** OrganizationalUnit street */
	public static final String STREET = "street";

	/** OrganizationalUnit postOfficeBox */
	public static final String POSTOFFICEBOX = "postOfficeBox";

	/** OrganizationalUnit postalCode */
	public static final String POSTALCODE = "postalCode";

	/** OrganizationalUnit postalAddress */
	public static final String POSTALADDRESS = "postalAddress";

	/** OrganizationalUnit physicalDeliveryOfficeName */
	public static final String PHYSICALDELIVERYOFFICENAME = "physicalDeliveryOfficeName";

	/** OrganizationalUnit st */
	public static final String ST = "st";// 省/州

	/** OrganizationalUnit l */
	public static final String L = "l";// 县市

	public static final String CO = "co"; // 中国

	public static final String C = "c"; // CN

	public static final String COUNTRYCODE = "countryCode";// 156

	public static final String NAME = "name";

	// for id
	public static final String CN = "cn";

}
