package com.wy.test.authz.oauth2.jwt.crypto.sign;

import com.wy.test.authz.oauth2.jwt.AlgorithmMetadata;

/**
 * @author Luke Taylor
 */
public interface SignatureVerifier extends AlgorithmMetadata {

	void verify(byte[] content, byte[] signature);
}
