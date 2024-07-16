package com.wy.test.web.apis.identity.scim.resources;

import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScimEnterpriseUser extends ScimUser {

	private static final long serialVersionUID = 3212312511630459427L;

	public static final String SCHEMA = "urn:ietf:params:scim:schemas:extension:enterprise:2.0:User";

	@JsonProperty(SCHEMA)
	ScimEnterprise enterprise;

	public ScimEnterpriseUser() {
		schemas = new HashSet<String>();
		schemas.add(ScimUser.SCHEMA);
		schemas.add(SCHEMA);
	}

	@Override
	public ScimEnterprise getEnterprise() {
		return enterprise;
	}

	@Override
	public void setEnterprise(ScimEnterprise enterprise) {
		this.enterprise = enterprise;
	}
}