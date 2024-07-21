package com.wy.test.crypto;

import java.security.KeyPair;

import org.junit.jupiter.api.Test;

import dream.flying.flower.binary.Base64Helper;
import dream.flying.flower.binary.HexHelper;
import dream.flying.flower.digest.DigestHelper;

public class RSAUtilsTest {

	@Test
	public void test() throws Exception {
		KeyPair keyPair = DigestHelper.rsaGenerateKeyPair();
		String privateKey = HexHelper.encodeHexString(keyPair.getPrivate().getEncoded());
		String publicKey = HexHelper.encodeHexString(keyPair.getPublic().getEncoded());
		System.out.println("privateKey:" + privateKey);
		System.out.println("publicKey:" + publicKey);
		String signString = "my name is shiming";
		System.out.println("privateKey:");
		System.out.println(Base64Helper.encodeUrl(keyPair.getPublic().getEncoded()));
		System.out.println("PublicKeyPEM:");
		System.out.println(DigestHelper.getPemPublicKey(keyPair.getPublic().getEncoded()));

		String encodedData = DigestHelper.rsaEncrypt(publicKey, signString.getBytes());
		System.out.println("encodedData \r\n" + encodedData);
		String decodedData = DigestHelper.rsaDecrypt(privateKey, encodedData);
		System.out.println("target:" + decodedData);
	}
}