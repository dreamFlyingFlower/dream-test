package com.wy.test.core.entity.userinfo;

public class NameEntity {

	private String givenName;

	private String middleName;

	private String familyName;

	private String honorificPrefix;

	private String honorificSuffix;

	private String formattedName;

	/**
	 * 
	 */
	public NameEntity() {

	}

	public String getGivenName() {
		return givenName;
	}

	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	public String getHonorificPrefix() {
		return honorificPrefix;
	}

	public void setHonorificPrefix(String honorificPrefix) {
		this.honorificPrefix = honorificPrefix;
	}

	public String getHonorificSuffix() {
		return honorificSuffix;
	}

	public void setHonorificSuffix(String honorificSuffix) {
		this.honorificSuffix = honorificSuffix;
	}

	public String getFormattedName() {
		return formattedName;
	}

	public void setFormattedName(String formattedName) {
		this.formattedName = formattedName;
	}

	@Override
	public String toString() {
		return "NameEntity [givenName=" + givenName + ", middleName=" + middleName + ", familyName=" + familyName
				+ ", honorificPrefix=" + honorificPrefix + ", honorificSuffix=" + honorificSuffix + ", formattedName="
				+ formattedName + "]";
	}
}
