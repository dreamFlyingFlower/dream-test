package com.wy.test.web.api.identity.scim.resources;

import com.wy.test.web.apis.identity.scim.resources.ScimUser;

import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.framework.core.pretty.strategy.JsonPretty;

public class ScimUserJsonString2ObjectTest {

	public static void main(String[] args) {

		String userJsonString = ReadJson2String.read("ScimUserJsonString.json");
		ScimUser u = JsonHelpers.read(userJsonString, ScimUser.class);
		System.out.println((new JsonPretty()).format(JsonHelpers.toString(u)));
	}

}
