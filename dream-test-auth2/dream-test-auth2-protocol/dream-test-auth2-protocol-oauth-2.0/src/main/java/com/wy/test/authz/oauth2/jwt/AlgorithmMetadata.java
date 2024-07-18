package com.wy.test.authz.oauth2.jwt;

/**
 * @author Luke Taylor
 */
public interface AlgorithmMetadata {

	/**
	 * @return the JCA/JCE algorithm name.
	 */
	String algorithm();
}
