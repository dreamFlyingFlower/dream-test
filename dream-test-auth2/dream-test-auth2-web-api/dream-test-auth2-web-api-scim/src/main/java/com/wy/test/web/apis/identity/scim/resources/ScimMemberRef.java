package com.wy.test.web.apis.identity.scim.resources;

public class ScimMemberRef extends ScimMultiValuedAttribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6018893424394625889L;

	public ScimMemberRef() {
	}

	public ScimMemberRef(String display, String value) {
		this.display = display;
		this.value = value;
	}
}
