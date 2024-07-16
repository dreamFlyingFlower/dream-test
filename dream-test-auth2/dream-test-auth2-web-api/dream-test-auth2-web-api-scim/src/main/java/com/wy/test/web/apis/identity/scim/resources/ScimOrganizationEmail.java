package com.wy.test.web.apis.identity.scim.resources;

import java.io.Serializable;

public class ScimOrganizationEmail extends ScimMultiValuedAttribute implements Serializable {

	private static final long serialVersionUID = -41327146592552688L;

	public static class UserEmailType {

		public static final String WORK = "work";

		public static final String HOME = "home";

		public static final String OTHER = "other";

	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public void setType(String type) {
		this.type = type;
	}

	@Override
	public boolean isPrimary() {
		return primary;
	}

	@Override
	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public ScimOrganizationEmail() {
	}

	public ScimOrganizationEmail(String value, String type, boolean primary) {
		super();
		this.value = value;
		this.type = type;
		this.primary = primary;
	}

}
