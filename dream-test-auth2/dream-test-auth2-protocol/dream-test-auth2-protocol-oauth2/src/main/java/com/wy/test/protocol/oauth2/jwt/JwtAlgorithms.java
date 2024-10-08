package com.wy.test.protocol.oauth2.jwt;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.jwt.crypto.cipher.CipherMetadata;

/**
 * 
 *
 * @author 飞花梦影
 * @date 2024-07-21 22:31:00
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class JwtAlgorithms {

	private static final Map<String,String> sigAlgs = new HashMap<String,String>();
	private static final Map<String,String> javaToSigAlgs = new HashMap<String,String>();
	private static final Map<String,String> keyAlgs = new HashMap<String,String>();
	private static final Map<String,String> javaToKeyAlgs = new HashMap<String,String>();

	static {
		sigAlgs.put("HS256", "HMACSHA256");
		sigAlgs.put("HS384" , "HMACSHA384");
		sigAlgs.put("HS512" , "HMACSHA512");
		sigAlgs.put("RS256" , "SHA256withRSA");
		sigAlgs.put("RS512" , "SHA512withRSA");

		keyAlgs.put("RSA1_5" , "RSA/ECB/PKCS1Padding");

		for(Map.Entry<String,String> e: sigAlgs.entrySet()) {
			javaToSigAlgs.put(e.getValue(), e.getKey());
		}
		for(Map.Entry<String,String> e: keyAlgs.entrySet()) {
			javaToKeyAlgs.put(e.getValue(), e.getKey());
		}

	}

	static String sigAlg(String javaName){
		String alg = javaToSigAlgs.get(javaName);

		if (alg == null) {
			throw new IllegalArgumentException("Invalid or unsupported signature algorithm: " + javaName);
		}

		return alg;
	}

	static String keyEncryptionAlg(String javaName) {
		String alg = javaToKeyAlgs.get(javaName);

		if (alg == null) {
			throw new IllegalArgumentException("Invalid or unsupported key encryption algorithm: " + javaName);
		}

		return alg;
	}

	static String enc(CipherMetadata cipher) {
		if (!cipher.algorithm().equalsIgnoreCase("AES/CBC/PKCS5Padding")) {
			throw new IllegalArgumentException("Unknown or unsupported algorithm");
		}
		if (cipher.keySize() == 128) {
			return "A128CBC";
		} else if (cipher.keySize() == 256) {
			return "A256CBC";
		} else {
			throw new IllegalArgumentException("Unsupported key size");
		}
	}
}
