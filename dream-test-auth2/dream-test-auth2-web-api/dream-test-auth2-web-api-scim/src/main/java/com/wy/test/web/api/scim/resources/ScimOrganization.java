package com.wy.test.web.api.scim.resources;

import java.util.HashSet;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
public class ScimOrganization extends ScimResource {

	private static final long serialVersionUID = -8087404240254880740L;

	public static String SCHEMA = "urn:ietf:params:scim:schemas:core:2.0:Organization";

	private String code;

	private String name;

	private String fullName;

	private String parentId;

	private String parentName;

	private String type;

	private String codePath;

	private String namePath;

	private int level;

	private String division;

	private List<ScimOrganizationAddress> addresses;

	private List<ScimOrganizationEmail> emails;

	private List<ScimOrganizationPhoneNumber> phoneNumbers;

	private String sortOrder;

	private String description;

	// T/IDAC 002—2021
	private String displayName; // name

	private Integer order; // sortOrder

	private String parent; // parentId

	private String parentCode; // parent code

	public ScimOrganization() {
		schemas = new HashSet<String>();
		schemas.add(SCHEMA);
	}
}