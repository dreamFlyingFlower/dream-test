package com.wy.test.crypto;

import org.junit.jupiter.api.Test;

import dream.flying.flower.binary.Base64Helper;
import dream.flying.flower.digest.DigestHelper;
import dream.flying.flower.digest.enums.MessageDigestType;
import dream.flying.flower.primitive.ByteHelper;

public class Base64UtilsTest {

	@Test
	public void test() {
		String encode = Base64Helper.encodeString("base64ToFile".getBytes());
		System.out.println(encode);
		String decode = ByteHelper.toString(Base64Helper.decode(encode));
		System.out.println(decode);

		String urlEncode = Base64Helper.encodeUrlString("{\"typ\":\"JWT\",\"alg\":\"HS256\"}".getBytes());
		System.out.println(urlEncode);
		String urlDecode = new String(Base64Helper.decodeUrl(urlEncode));
		System.out.println(urlDecode);

		System.out.println(Base64Helper.decode(
				"AAMkADU2OWY1MGQ3LWEyNWQtNDFmOC04MWFiLTI5YTE2NGM5YTZmNABGAAAAAABPKgpqnlfYQ7BVC/BfH2XIBwCS0xhUjzMYSLVky9bw7LddAAAAjov5AACS0xhUjzMYSLVky9bw7LddAAADzoyxAAA="));

		String b =
				"UsWdAIe4opTqcrX6~SrIMhBu5Gc9oZKEnnSDFRx9JwBINK8XTgnXUs2A3b7QmxDM9nRu8~mGsikVEoISLg.JTIHYRwv-Bp5ljIADLwUHv9iJAWo1delBOlW0Hd7nIVF0";

		System.out.println(Base64Helper.encodeUrlString(DigestHelper.digest(MessageDigestType.SHA_256, b)));
	}
}