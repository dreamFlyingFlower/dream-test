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
 * RSA Digital signature default signature algorithm is SHA1withRSA default key
 * size is 1024 RsaSigner support MD5withRSA and MD5withRSA
 *
 * @author 飞花梦影
 * @date 2024-07-20 12:21:32
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public final class RsaSigner implements ISigner {

	public static final KeyPairType KEY_ALGORTHM = KeyPairType.RSA;

	public final class RsaAlgorithm {

		public static final String MD5withRSA = "MD5withRSA";

		public static final String SHA1withRSA = "SHA1withRSA";
	}

	public static final String SIGNATURE_ALGORITHM = RsaAlgorithm.SHA1withRSA;

	public byte[] sign(byte[] dataBytes, byte[] privateKeyBytes, String algorithm) throws Exception {
		PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM.name());
		PrivateKey signPrivateKey = keyFactory.generatePrivate(pkcs8EncodedKeySpec);
		Signature signature = Signature.getInstance(algorithm);
		signature.initSign(signPrivateKey);
		signature.update(dataBytes);
		return signature.sign();
	}

	@Override
	public byte[] sign(byte[] dataBytes, byte[] privateKeyBytes) throws Exception {
		return sign(dataBytes, privateKeyBytes, SIGNATURE_ALGORITHM);
	}

	/**
	 * sign with BASE64 privateKey use SHA1withRSA Algorithm
	 */
	@Override
	public String signB64(String data, String privateKey) throws Exception {
		byte[] keyBytes = Base64Helper.decode(privateKey);
		byte[] dataBytes = data.getBytes();
		byte[] signature = sign(dataBytes, keyBytes);

		return Base64Helper.encodeString(signature);
	}

	public boolean verify(byte[] dataBytes, byte[] publicKeyBytes, byte[] signBytes, String algorithm)
			throws Exception {
		X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKeyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORTHM.name());
		PublicKey verifyPublicKey = keyFactory.generatePublic(x509EncodedKeySpec);
		Signature signature = Signature.getInstance(algorithm);
		signature.initVerify(verifyPublicKey);
		signature.update(dataBytes);
		return signature.verify(signBytes);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.connsec.crypto.signature.Signer#verify(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean verify(byte[] dataBytes, byte[] publicKeyBytes, byte[] signBytes) throws Exception {
		return verify(dataBytes, publicKeyBytes, signBytes, SIGNATURE_ALGORITHM);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @param publicKey is base64
	 * 
	 * @param sign is base64
	 * 
	 * @see com.connsec.crypto.signature.Signer#verify(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public boolean verifyB64(String data, String publicKey, String sign) throws Exception {
		byte[] keyBytes = Base64Helper.decode(publicKey);
		byte[] dataBytes = data.getBytes();
		byte[] signBytes = Base64Helper.decode(sign);
		return verify(dataBytes, keyBytes, signBytes);
	}
}