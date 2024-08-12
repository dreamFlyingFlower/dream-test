package com.wy.test.protocol.saml2.authz.saml.service;

import java.util.UUID;

public class IDService {

	public String generateID() {
		return "AUTH_" + UUID.randomUUID().toString();
	}
}
