package com.wy.test.web.apis.identity.scim.resources;

import java.io.Serializable;

public class ScimUserOrganization extends ScimMultiValuedAttribute implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3201987266085144715L;

	public ScimUserOrganization() {
		super();
	}

	public ScimUserOrganization(String value, String display, boolean primary) {
		super();
		this.value = value;
		this.display = display;
		this.primary = primary;
	}
}
