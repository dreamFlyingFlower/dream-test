package com.wy.test.web.api.identity.scim.resources;

import com.wy.test.web.apis.identity.scim.resources.ScimGroup;

import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.framework.core.pretty.strategy.JsonPretty;

public class ScimGroupJsonString2ObjectTest {

	public static void main(String[] args) {
		String userJsonString = ReadJson2String.read("ScimGroupJsonString.json");
		ScimGroup g = JsonHelpers.read(userJsonString, ScimGroup.class);

		System.out.println((new JsonPretty()).format(JsonHelpers.toString(g)));
	}
}
