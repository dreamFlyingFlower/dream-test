package com.wy.test.web.api.identity.scim.resources;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.wy.test.pretty.impl.JsonPretty;
import com.wy.test.util.JsonUtils;
import com.wy.test.web.apis.identity.scim.resources.ScimFormattedName;
import com.wy.test.web.apis.identity.scim.resources.ScimMeta;
import com.wy.test.web.apis.identity.scim.resources.ScimUser;
import com.wy.test.web.apis.identity.scim.resources.ScimUserEmail;
import com.wy.test.web.apis.identity.scim.resources.ScimUserPhoneNumber;

public class ScimUserJsonTest {

	public static void main(String[] args) {
		ScimUser u = new ScimUser();
		u.setUserName("UserName");
		u.setExternalId("UserName");
		u.setId("1111111111111");

		ScimMeta meta = new ScimMeta();
		meta.setVersion("W\\/\"f250dd84f0671c3\"");
		meta.setCreated(new Date());
		meta.setLocation("https://example.com/v2/Users/2819c223...");
		meta.setResourceType("User");
		meta.setLastModified(new Date());
		u.setMeta(meta);

		ScimFormattedName un = new ScimFormattedName();
		un.setFamilyName("Jensen");
		un.setFormatted("Ms. Barbara J Jensen, III");
		un.setGivenName("Barbara");
		un.setHonorificPrefix("Ms.");
		un.setHonorificSuffix("III");
		un.setMiddleName("Jane");
		u.setName(un);

		List<ScimUserPhoneNumber> UserPhoneNumberList = new ArrayList<ScimUserPhoneNumber>();
		ScimUserPhoneNumber pn = new ScimUserPhoneNumber();
		pn.setValue("555-555-8377");
		pn.setType(ScimUserPhoneNumber.UserPhoneNumberType.WORK);

		ScimUserPhoneNumber pnh = new ScimUserPhoneNumber();
		pnh.setValue("555-555-8377");
		pnh.setType(ScimUserPhoneNumber.UserPhoneNumberType.HOME);
		UserPhoneNumberList.add(pnh);

		UserPhoneNumberList.add(pn);

		u.setPhoneNumbers(UserPhoneNumberList);

		List<ScimUserEmail> ueList = new ArrayList<ScimUserEmail>();
		ScimUserEmail ue = new ScimUserEmail();
		ue.setValue("bjensen@example.com");
		ue.setType(ScimUserEmail.UserEmailType.WORK);
		ueList.add(ue);
		u.setEmails(ueList);

		System.out.println((new JsonPretty()).format(JsonUtils.toString(u)));
	}

}
