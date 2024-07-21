package com.wy.test.common.crypto.signature;

import java.security.Signature;
import java.security.Signer;

/**
 * Digital signature interface for RSA & DSA sign with private key ,then verify
 * use public key scenario 1. A genKeyPair include private key & public key 2. A
 * give public key to B 3. A sign a data with private key to signedData 4. A
 * send data and signedData to B 5. B received data & signedData from A 6. B
 * verify data & signedData use public key
 * 
 * {@link Signer} , {@link Signature}
 * 
 */
@SuppressWarnings("deprecation")
public interface ISigner {

	byte[] sign(byte[] data, byte[] privateKey) throws Exception;

	String signB64(String data, String privateKey) throws Exception;

	boolean verify(byte[] data, byte[] publicKey, byte[] sign) throws Exception;

	boolean verifyB64(String data, String publicKey, String sign) throws Exception;
}