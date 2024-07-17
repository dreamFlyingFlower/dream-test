package com.wy.test.constants.ldap;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Organization objectclass attribute top
 * 
 * @author shimingxy
 *
 */
public class Organization {

	public static ArrayList<String> OBJECTCLASS = new ArrayList<>(Arrays.asList("top", "organization"));

	public static String objectClass = "organization";

	public static final String DISTINGUISHEDNAME = "distinguishedname";

	/** Organization o */
	public static final String O = "o";

	/** Organization userPassword */
	public static final String USERPASSWORD = "userPassword";

	/** Organization searchGuide */
	public static final String SEARCHGUIDE = "searchGuide";

	/** Organization seeAlso */
	public static final String SEEALSO = "seeAlso";

	/** Organization description */
	public static final String DESCRIPTION = "description";

	/** Organization businessCategory */
	public static final String BUSINESSCATEGORY = "businessCategory";

	/** Organization x121Address */
	public static final String X121ADDRESS = "x121Address";

	/** Organization registeredAddress */
	public static final String REGISTEREDADDRESS = "registeredAddress";

	/** Organization destinationIndicator */
	public static final String DESTINATIONINDICATOR = "destinationIndicator";

	/** Organization preferredDeliveryMethod */
	public static final String PREFERREDDELIVERYMETHOD = "preferredDeliveryMethod";

	/** Organization telexNumber */
	public static final String TELEXNUMBER = "telexNumber";

	/** Organization teletexTerminalIdentifier */
	public static final String TELETEXTERMINALIDENTIFIER = "teletexTerminalIdentifier";

	/** Organization telephoneNumber */
	public static final String TELEPHONENUMBER = "telephoneNumber";

	/** Organization internationaliSDNNumber */
	public static final String INTERNATIONALISDNNUMBER = "internationaliSDNNumber";

	/** Organization facsimileTelephoneNumber */
	public static final String FACSIMILETELEPHONENUMBER = "facsimileTelephoneNumber";

	/** Organization street */
	public static final String STREET = "street";

	/** Organization postOfficeBox */
	public static final String POSTOFFICEBOX = "postOfficeBox";

	/** Organization postalCode */
	public static final String POSTALCODE = "postalCode";

	/** Organization postalAddress */
	public static final String POSTALADDRESS = "postalAddress";

	/** Organization physicalDeliveryOfficeName */
	public static final String PHYSICALDELIVERYOFFICENAME = "physicalDeliveryOfficeName";

	/** Organization st */
	public static final String ST = "st";

	/** Organization l */
	public static final String L = "l";

	// for id
	public static final String CN = "cn";

}
