package com.wy.test.provider.authn.support.wsfederation;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * This will remove the @example.org from the upn local accounts. Other IdP should have the upn un-altered to prevent
 * users collusions in CAS-based applications.
 * 
 */
@Slf4j
public class WsFedAttributeMutatorImpl implements WsFederationAttributeMutator {

	@Override
	public void modifyAttributes(Map<String, Object> attributes, String upnSuffix) {
		if (attributes.containsKey("upn")) {
			attributes.put("upn", attributes.get("upn").toString().replace("@" + upnSuffix, ""));
			log.debug(String.format("modifyAttributes: upn modified (%s)", attributes.get("upn").toString()));
		} else {
			log.warn("modifyAttributes: upn attribute not found");
		}

		attributeMapping(attributes, "surname", "LastName");
		attributeMapping(attributes, "givenname", "FirstName");
		attributeMapping(attributes, "Group", "Groups");
		attributeMapping(attributes, "employeeNumber", "UDC_IDENTIFIER");
	}

	private void attributeMapping(Map<String, Object> attributes, String oldName, String newName) {
		if (attributes.containsKey(oldName)) {
			log.debug(String.format("attributeRemapping: %s -> %s (%s)", oldName, newName, attributes.get(oldName)));
			attributes.put(newName, attributes.get(oldName));
			attributes.remove(oldName);
		} else {
			log.debug(String.format("attributeRemapping: attribute not found (%s)", oldName));
		}
	}
}