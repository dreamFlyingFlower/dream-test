package com.wy.test.web.api.identity.scim.resources;

import com.wy.test.common.pretty.impl.JsonPretty;
import com.wy.test.web.apis.identity.scim.resources.ScimEnterpriseUser;

import dream.flying.flower.framework.core.json.JsonHelpers;

public class ScimEnterpriseUserJsonString2ObjectTest {

	public static void main(String[] args) {

		String userJsonString = ReadJson2String.read("ScimEnterpriseUserJsonString.json");
		ScimEnterpriseUser u = JsonHelpers.read(userJsonString, ScimEnterpriseUser.class);

		System.out.println((new JsonPretty()).format(JsonHelpers.toString(u)));
		System.out.println(u.getEnterprise().getCostCenter());
	}

}
