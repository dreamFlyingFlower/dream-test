package com.wy.test.crypto.signature;

import java.security.SecureRandom;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;

import dream.flying.flower.binary.Base64Helper;

public class RSAKeyTest {

	public static void main(String[] args) throws JOSEException {
		// RSA signatures require a public and private RSA key pair, the public key
		// must be made known to the JWS recipient in order to verify the signatures
		RSAKey rsaJWK = new RSAKeyGenerator(2048).keyID("123").keyUse(KeyUse.SIGNATURE).algorithm(JWSAlgorithm.RS256)
				.generate();
		RSAKey rsaPublicJWK = rsaJWK.toPublicJWK();
		System.out.println(rsaPublicJWK.toJSONString());

		System.out.println(rsaJWK.toJSONString());

		byte[] sharedKey = new byte[32];
		new SecureRandom().nextBytes(sharedKey);
		System.out.println(Base64Helper.encodeString(sharedKey));

		OctetSequenceKey octKey = new OctetSequenceKeyGenerator(2048).keyID("123").keyUse(KeyUse.SIGNATURE)
				.algorithm(JWSAlgorithm.HS256).generate();
		System.out.println(octKey.toJSONString());

		// Create HMAC signer
		JWSSigner signer = new MACSigner(octKey);

		// Prepare JWS object with "Hello, world!" payload
		JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload("Hello, world!"));

		// Apply the HMAC
		jwsObject.sign(signer);
		String s = jwsObject.serialize();
		System.out.println(s);

		System.out.print("A128KW".substring(1, 4));
	}
}