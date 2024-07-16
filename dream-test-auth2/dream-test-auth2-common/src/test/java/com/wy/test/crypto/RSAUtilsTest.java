package com.wy.test.crypto;

import java.security.KeyPair;

import org.junit.jupiter.api.Test;

import dream.flying.flower.binary.Base64Helper;
import dream.flying.flower.binary.HexHelper;

public class RSAUtilsTest {

	@Test
	public void test() throws Exception {
		// RSA KeyPair
		KeyPair keyPair = RSAUtils.genRSAKeyPair();
		String privateKey = HexHelper.encodeHexString(keyPair.getPrivate().getEncoded());
		String publicKey = HexHelper.encodeHexString(keyPair.getPublic().getEncoded());
		System.out.println("privateKey:" + privateKey);
		System.out.println("publicKey:" + publicKey);
		String signString = "my name is shiming";
		System.out.println("privateKey:");
		System.out.println(Base64Helper.encodeUrl(keyPair.getPublic().getEncoded()));
		System.out.println("PublicKeyPEM:");
		System.out.println(RSAUtils.getPublicKeyPEM(keyPair.getPublic().getEncoded()));

		byte[] encodedData = RSAUtils.encryptByPrivateKey(signString.getBytes(), privateKey);
		System.out.println("encodedData \r\n" + new String(encodedData));
		System.out.println("encodedData HexString \r\n" + HexHelper.encodeHexString(encodedData));
		byte[] decodedData = RSAUtils.decryptByPublicKey(encodedData, publicKey);
		String target = new String(decodedData);
		System.out.println("target:" + target);
	}
}