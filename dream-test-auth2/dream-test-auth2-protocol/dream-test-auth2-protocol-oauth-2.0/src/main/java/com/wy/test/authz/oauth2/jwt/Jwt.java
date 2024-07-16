package com.wy.test.authz.oauth2.jwt;

import com.wy.test.authz.oauth2.jwt.crypto.sign.SignatureVerifier;

/**
 * @author Luke Taylor
 */
public interface Jwt extends BinaryFormat {
	String getClaims();

	String getEncoded();

	void verifySignature(SignatureVerifier verifier);
}
