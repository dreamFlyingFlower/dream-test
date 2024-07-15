package com.wy.test.web.api.identity.scim.resources;

import com.wy.test.pretty.impl.JsonPretty;
import com.wy.test.util.JsonUtils;
import com.wy.test.web.apis.identity.scim.resources.ScimGroup;

public class ScimGroupJsonString2ObjectTest {

	public static void main(String[] args) {
		String userJsonString = ReadJson2String.read("ScimGroupJsonString.json");
		ScimGroup g = JsonUtils.stringToObject(userJsonString, ScimGroup.class);

		System.out.println((new JsonPretty()).format(JsonUtils.toString(g)));
	}
}