package com.wy.test.common.crypto.signature;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import dream.flying.flower.binary.Base64Helper;
import dream.flying.flower.digest.enums.KeyPairType;

/**
 * DSA Digital signature default signature algorithm is SHA1withDSA default key
 * size is 1024 DsaSigner support SHA1withDSA
 * 
 * @author 飞花梦影
 * @date 2024-07-14 21:37:49
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public final class DsaSigner implements ISigner {

	public static final KeyPairType KEY_ALGORITHM = KeyPairType.DSA;

	public static final String SIGNATURE_ALGORITHM = "SHA1withDSA";

	@Override
	public byte[] sign(byte[] dataBytes, byte[] privateKeyByte) throws Exception {
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyByte);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM.name());
		PrivateKey signPrivateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initSign(signPrivateKey);
		signature.update(dataBytes);
		return signature.sign();
	}

	@Override
	public String signB64(String data, String privateKey) throws Exception {
		byte[] privateKeyByte = Base64Helper.decode(privateKey);
		byte[] dataBytes = data.getBytes();
		byte[] signatureBytes = sign(dataBytes, privateKeyByte);
		return Base64Helper.encodeString(signatureBytes);
	}

	@Override
	public boolean verify(byte[] dataBytes, byte[] publicKeyBytes, byte[] signBytes) throws Exception {
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM.name());
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
		PublicKey verifyPublicKey = keyFactory.generatePublic(x509KeySpec);
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		signature.initVerify(verifyPublicKey);
		signature.update(dataBytes);
		return signature.verify(signBytes);
	}

	@Override
	public boolean verifyB64(String data, String publicKey, String sign) throws Exception {
		byte[] privateKeyByte = Base64Helper.decode(publicKey);
		byte[] dataBytes = data.getBytes();
		byte[] signBytes = Base64Helper.decode(sign);
		return verify(dataBytes, privateKeyByte, signBytes);
	}
}