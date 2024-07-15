package com.wy.test.web.api.identity.scim.resources;

import com.wy.test.pretty.impl.JsonPretty;
import com.wy.test.util.JsonUtils;
import com.wy.test.web.apis.identity.scim.resources.ScimEnterpriseUser;

public class ScimEnterpriseUserJsonString2ObjectTest {

	public static void main(String[] args) {

		String userJsonString = ReadJson2String.read("ScimEnterpriseUserJsonString.json");
		ScimEnterpriseUser u = JsonUtils.stringToObject(userJsonString, ScimEnterpriseUser.class);

		System.out.println((new JsonPretty()).format(JsonUtils.toString(u)));
		System.out.println(u.getEnterprise().getCostCenter());
	}

}
