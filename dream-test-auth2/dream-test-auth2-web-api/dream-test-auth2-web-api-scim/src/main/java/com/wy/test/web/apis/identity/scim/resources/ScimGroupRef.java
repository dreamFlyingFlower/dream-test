package com.wy.test.web.apis.identity.scim.resources;

public class ScimGroupRef extends ScimMultiValuedAttribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7069453283024141999L;

	public ScimGroupRef() {
		super();
	}

	public ScimGroupRef(String value, String display) {
		super();
		this.value = value;
		this.display = display;
	}
}
