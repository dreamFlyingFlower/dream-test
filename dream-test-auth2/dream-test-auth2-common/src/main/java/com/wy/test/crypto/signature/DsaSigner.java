package com.wy.test.crypto.signature;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import com.wy.test.crypto.Base64Utils;
import com.wy.test.crypto.KeyPairType;

/**
 * DSA Digital signature default signature algorithm is SHA1withDSA default key
 * size is 1024 DsaSigner support SHA1withDSA
 * 
 * @author 飞花梦影
 * @date 2024-07-14 21:37:49
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public final class DsaSigner implements ISigner {

	// ����ǩ����Կ�㷨
	public static final KeyPairType KEY_ALGORITHM = KeyPairType.DSA;

	/**
	 * ����ǩ�� ǩ��/��֤�㷨
	 */
	public static final String SIGNATURE_ALGORITHM = "SHA1withDSA";

	public byte[] sign(byte[] dataBytes, byte[] privateKeyByte) throws Exception {
		// ȡ��˽Կ
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(privateKeyByte);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM.name());
		// ���˽Կ
		PrivateKey signPrivateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		// ʵ��Signature
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		// ��ʼ��Signature
		signature.initSign(signPrivateKey);
		// ����
		signature.update(dataBytes);

		return signature.sign();
	}

	public String signB64(String data, String privateKey) throws Exception {

		byte[] privateKeyByte = Base64Utils.decoder(privateKey);
		byte[] dataBytes = data.getBytes();

		byte[] signatureBytes = sign(dataBytes, privateKeyByte);

		return Base64Utils.encoder(signatureBytes);
	}

	public boolean verify(byte[] dataBytes, byte[] publicKeyBytes, byte[] signBytes) throws Exception {

		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM.name());
		// ��ʼ����Կ
		// ��Կ����ת��
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(publicKeyBytes);
		// ����Կ
		PublicKey verifyPublicKey = keyFactory.generatePublic(x509KeySpec);
		// ʵ��Signature
		Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
		// ��ʼ��Signature
		signature.initVerify(verifyPublicKey);
		// ����
		signature.update(dataBytes);
		// ��֤
		return signature.verify(signBytes);
	}

	public boolean verifyB64(String data, String publicKey, String sign) throws Exception {

		byte[] privateKeyByte = Base64Utils.decoder(publicKey);
		byte[] dataBytes = data.getBytes();
		byte[] signBytes = Base64Utils.decoder(sign);

		// ��֤
		return verify(dataBytes, privateKeyByte, signBytes);
	}

}
