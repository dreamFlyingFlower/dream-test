package com.wy.test.web.api.identity.scim.resources;

import com.wy.test.common.pretty.impl.JsonPretty;
import com.wy.test.common.util.JsonUtils;
import com.wy.test.web.apis.identity.scim.resources.ScimUser;

public class ScimUserJsonString2ObjectTest {

	public static void main(String[] args) {

		String userJsonString = ReadJson2String.read("ScimUserJsonString.json");
		ScimUser u = JsonUtils.stringToObject(userJsonString, ScimUser.class);
		System.out.println((new JsonPretty()).format(JsonUtils.toString(u)));
	}

}
