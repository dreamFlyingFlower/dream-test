package com.wy.test.protocol.saml2.authz.saml20.consumer;

import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.impl.AuthnRequestBuilder;

import com.wy.test.protocol.saml2.authz.saml.service.IDService;
import com.wy.test.protocol.saml2.authz.saml.service.TimeService;
import com.wy.test.protocol.saml2.authz.saml20.xml.IssuerGenerator;

public class AuthnRequestGenerator {

	private final String issuingEntityName;

	private final TimeService timeService;

	private final IDService idService;

	private IssuerGenerator issuerGenerator;

	public AuthnRequestGenerator(String issuingEntityName, TimeService timeService, IDService idService) {
		super();
		this.issuingEntityName = issuingEntityName;
		this.timeService = timeService;
		this.idService = idService;

		issuerGenerator = new IssuerGenerator(this.issuingEntityName);
	}

	public AuthnRequest generateAuthnRequest(String destination, String responseLocation) {
		AuthnRequest authnRequest = new AuthnRequestBuilder().buildObject();

		authnRequest.setAssertionConsumerServiceURL(responseLocation);
		authnRequest.setID(idService.generateID());
		authnRequest.setIssueInstant(timeService.getCurrentDateTime());
		authnRequest.setDestination(destination);

		authnRequest.setIssuer(issuerGenerator.generateIssuer());
		return authnRequest;
	}
}