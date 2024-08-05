package com.wy.test.web.api.identity.scim.resources;

import com.wy.test.web.api.scim.resources.ScimEnterpriseUser;

import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.framework.core.pretty.strategy.JsonPretty;

public class ScimEnterpriseUserJsonString2ObjectTest {

	public static void main(String[] args) {

		String userJsonString = ReadJson2String.read("ScimEnterpriseUserJsonString.json");
		ScimEnterpriseUser u = JsonHelpers.read(userJsonString, ScimEnterpriseUser.class);

		System.out.println((new JsonPretty()).format(JsonHelpers.toString(u)));
		System.out.println(u.getEnterprise().getCostCenter());
	}

}
