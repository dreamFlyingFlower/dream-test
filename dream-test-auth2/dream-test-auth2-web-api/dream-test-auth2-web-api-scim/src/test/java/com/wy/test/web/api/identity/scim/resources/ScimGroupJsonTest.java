package com.wy.test.web.api.identity.scim.resources;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.wy.test.web.api.scim.resources.ScimGroup;
import com.wy.test.web.api.scim.resources.ScimMemberRef;
import com.wy.test.web.api.scim.resources.ScimMeta;

import dream.flying.flower.framework.core.json.JsonHelpers;
import dream.flying.flower.framework.core.pretty.strategy.JsonPretty;

public class ScimGroupJsonTest {

	public static void main(String[] args) {
		ScimGroup g = new ScimGroup();

		ScimMeta meta = new ScimMeta();
		meta.setVersion("W\\/\"f250dd84f0671c3\"");
		meta.setCreated(new Date());
		meta.setLocation("https://example.com/v2/Users/2819c223...");
		meta.setResourceType("User");
		meta.setLastModified(new Date());
		g.setMeta(meta);

		g.setDisplayName("Tour Guides");

		Set<ScimMemberRef> mrSet = new HashSet<ScimMemberRef>();
		ScimMemberRef mr1 = new ScimMemberRef();
		mr1.setReference("https://example.com/v2/Users/2819c223-7f76-453a-919d-413861904646");
		mr1.setValue("2819c223-7f76-453a-919d-413861904646");
		mr1.setDisplay("Babs Jensen");
		ScimMemberRef mr2 = new ScimMemberRef();
		mr2.setReference("https://example.com/v2/Users/2819c223-7f76-453a-919d-413861904646");
		mr2.setValue("2819c223-7f76-453a-919d-413861904646");
		mr2.setDisplay("Babs Jensen");
		ScimMemberRef mr3 = new ScimMemberRef();
		mr3.setReference("https://example.com/v2/Users/2819c223-7f76-453a-919d-413861904646");
		mr3.setValue("2819c223-7f76-453a-919d-413861904646");
		mr3.setDisplay("Babs Jensen");
		mrSet.add(mr1);
		mrSet.add(mr2);
		mrSet.add(mr3);

		g.setMembers(mrSet);

		System.out.println((new JsonPretty()).format(JsonHelpers.toString(g)));
	}
}
