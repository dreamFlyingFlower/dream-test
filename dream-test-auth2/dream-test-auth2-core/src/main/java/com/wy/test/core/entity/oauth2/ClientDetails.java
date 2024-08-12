package com.wy.test.core.entity.oauth2;

/**
 * Client details for OAuth 2
 * 
 * @author Ryan Heaton
 */
public interface ClientDetails extends org.springframework.security.oauth2.provider.ClientDetails {

	/*
	 * for OpenID Connect
	 */
	String getIssuer();

	String getAudience();

	String getAlgorithm();

	String getAlgorithmKey();

	String getEncryptionMethod();

	String getSignature();

	String getSignatureKey();

	String getApprovalPrompt();

	String getSubject();

	String getUserInfoResponse();

	String getPkce();

	String getProtocol();

	String getInstId();
}