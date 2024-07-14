package com.wy.test.crypto.signature;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.wy.test.crypto.KeyPairUtil;

public final class DsaSignerTest {

	@Test
	public void test() throws Exception {

		DsaSigner dsaSigner = new DsaSigner();
		// genKeyPair
		Map<String, Object> keyMap = KeyPairUtil.genKeyPairMap(DsaSigner.KEY_ALGORITHM);

		String publicKey = KeyPairUtil.getPublicKey(keyMap);
		String privateKey = KeyPairUtil.getPrivateKey(keyMap);
		System.out.println("privateKey:" + privateKey);
		System.out.println("privateKey:" + privateKey.length());
		System.out.println("publicKey:" + publicKey);
		System.out.println("publicKey:" + publicKey.length());

		String signStr = "my data need to sign use DSA Digital signature";
		System.out.println("signStr:" + signStr);

		String sign = dsaSigner.signB64(signStr, privateKey);
		System.out.println("sign��" + sign);
		// verify
		boolean status = dsaSigner.verifyB64(signStr, publicKey, sign);
		System.out.println("status��" + status);

	}

}
