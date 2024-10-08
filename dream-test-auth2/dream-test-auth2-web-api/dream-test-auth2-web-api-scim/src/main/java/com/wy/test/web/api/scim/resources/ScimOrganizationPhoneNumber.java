package com.wy.test.web.api.scim.resources;

import java.io.Serializable;

public class ScimOrganizationPhoneNumber extends ScimMultiValuedAttribute implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3201987266085144715L;

	public static class UserPhoneNumberType {

		public static final String WORK = "work";

		public static final String HOME = "home";

		public static final String MOBILE = "mobile";

		public static final String FAX = "fax";

		public static final String PAGER = "pager";

		public static final String OTHER = "other";

	}
}
