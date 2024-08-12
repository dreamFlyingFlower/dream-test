package com.wy.test.protocol.saml2.authz.saml20.provider.xml;

import java.util.HashMap;

import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.EncryptedAssertion;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.impl.ResponseBuilder;
import org.opensaml.saml2.encryption.Encrypter;
import org.opensaml.saml2.encryption.Encrypter.KeyPlacement;
import org.opensaml.xml.Configuration;
import org.opensaml.xml.encryption.EncryptionConstants;
import org.opensaml.xml.encryption.EncryptionParameters;
import org.opensaml.xml.encryption.KeyEncryptionParameters;
import org.opensaml.xml.security.keyinfo.KeyInfoGeneratorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.AppSamlDetailVO;
import com.wy.test.protocol.saml2.authz.saml.common.AuthnRequestInfo;
import com.wy.test.protocol.saml2.authz.saml.service.IDService;
import com.wy.test.protocol.saml2.authz.saml.service.TimeService;
import com.wy.test.protocol.saml2.authz.saml20.binding.BindingAdapter;
import com.wy.test.protocol.saml2.authz.saml20.xml.IssuerGenerator;

import dream.flying.flower.enums.YesNoEnum;

public class AuthnResponseGenerator {

	private final static Logger logger = LoggerFactory.getLogger(AuthnResponseGenerator.class);

	private String issuerName;

	private IDService idService;

	private TimeService timeService;

	private AssertionGenerator assertionGenerator;

	private IssuerGenerator issuerGenerator;

	private StatusGenerator statusGenerator;

	public AuthnResponseGenerator(String issuerName, TimeService timeService, IDService idService) {
		this.issuerName = issuerName;
		this.idService = idService;
		this.timeService = timeService;
		issuerGenerator = new IssuerGenerator(this.issuerName);
		assertionGenerator = new AssertionGenerator(issuerName, timeService, idService);
		statusGenerator = new StatusGenerator();
	}

	public Response generateAuthnResponse(AppSamlDetailVO saml20Details, AuthnRequestInfo authnRequestInfo,
			HashMap<String, String> attributeMap, BindingAdapter bindingAdapter, UserEntity currentUser) {

		Response authResponse = new ResponseBuilder().buildObject();
		// builder Assertion
		Assertion assertion = assertionGenerator.generateAssertion(saml20Details, bindingAdapter,
				saml20Details.getSpAcsUrl(), authnRequestInfo.getAuthnRequestID(), saml20Details.getAudience(),
				saml20Details.getValidityInterval(), attributeMap, currentUser);

		// Encrypt
		if (YesNoEnum.isYes(saml20Details.getEncrypted())) {
			logger.info("begin to encrypt assertion");
			try {
				// Assume this contains a recipient's RSA public
				EncryptionParameters encryptionParameters = new EncryptionParameters();
				encryptionParameters.setAlgorithm(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128);
				// encryptionParameters.setAlgorithm(EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128);
				logger.info("encryption assertion Algorithm : " + EncryptionConstants.ALGO_ID_BLOCKCIPHER_AES128);
				KeyEncryptionParameters keyEncryptionParameters = new KeyEncryptionParameters();
				keyEncryptionParameters.setEncryptionCredential(bindingAdapter.getSpSigningCredential());
				// kekParams.setAlgorithm(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSAOAEP);
				keyEncryptionParameters.setAlgorithm(EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15);
				logger.info("keyEncryption  Algorithm : " + EncryptionConstants.ALGO_ID_KEYTRANSPORT_RSA15);
				KeyInfoGeneratorFactory keyInfoGeneratorFactory =
						Configuration.getGlobalSecurityConfiguration().getKeyInfoGeneratorManager().getDefaultManager()
								.getFactory(bindingAdapter.getSpSigningCredential());
				keyEncryptionParameters.setKeyInfoGenerator(keyInfoGeneratorFactory.newInstance());
				Encrypter encrypter = new Encrypter(encryptionParameters, keyEncryptionParameters);
				encrypter.setKeyPlacement(KeyPlacement.PEER);
				EncryptedAssertion encryptedAssertion = encrypter.encrypt(assertion);
				authResponse.getEncryptedAssertions().add(encryptedAssertion);
			} catch (Exception e) {
				logger.info("Unable to encrypt assertion .", e);
			}
		} else {
			authResponse.getAssertions().add(assertion);
		}

		authResponse.setIssuer(issuerGenerator.generateIssuer());
		authResponse.setID(idService.generateID());
		authResponse.setIssueInstant(timeService.getCurrentDateTime());
		authResponse.setInResponseTo(authnRequestInfo.getAuthnRequestID());
		authResponse.setDestination(saml20Details.getSpAcsUrl());
		authResponse.setStatus(statusGenerator.generateStatus(StatusCode.SUCCESS_URI));
		logger.debug("authResponse.isSigned " + authResponse.isSigned());
		return authResponse;
	}
}