package com.wy.test.web.api.scim.resources;

import com.wy.test.web.api.scim.ScimServiceProviderConfigController;

public class ScimParameters {

	int startIndex = 1;

	int count = ScimServiceProviderConfigController.MAX_RESULTS;

	String ﬁlter;

	String sortBy;

	String sortOrder = "ascending";

	String attributes;

	public ScimParameters() {
	}

	public void parse() {
		if (startIndex == -1) {
			count = ScimServiceProviderConfigController.MAX_RESULTS_LIMIT;
		}

		if (startIndex <= 0) {
			startIndex = 1;
		}

		if (count > ScimServiceProviderConfigController.MAX_RESULTS
				&& count != ScimServiceProviderConfigController.MAX_RESULTS_LIMIT) {
			count = ScimServiceProviderConfigController.MAX_RESULTS;
		}
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public String getﬁlter() {
		return ﬁlter;
	}

	public void setﬁlter(String ﬁlter) {
		this.ﬁlter = ﬁlter;
	}

	public String getSortBy() {
		return sortBy;
	}

	public void setSortBy(String sortBy) {
		this.sortBy = sortBy;
	}

	public String getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(String sortOrder) {
		this.sortOrder = sortOrder;
	}

	public String getAttributes() {
		return attributes;
	}

	public void setAttributes(String attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return "ScimParameters [count=" + count + ", startIndex=" + startIndex + ", ﬁlter=" + ﬁlter + ", sortBy="
				+ sortBy + ", sortOrder=" + sortOrder + ", attributes=" + attributes + "]";
	}

}
