package com.wy.test.crypto;

import org.junit.jupiter.api.Test;

public class Base64UtilsTest {

	/**
	 * @param args
	 */
	@Test
	public void test() {
		String encode = Base64Utils.encoder("base64ToFile".getBytes());
		System.out.println(encode);
		String decode = Base64Utils.decode(encode);
		System.out.println(decode);

		String urlEncode = Base64Utils.base64UrlEncode("{\"typ\":\"JWT\",\"alg\":\"HS256\"}".getBytes());
		System.out.println(urlEncode);
		String urlDecode = new String(Base64Utils.base64UrlDecode(urlEncode));
		System.out.println(urlDecode);

		System.out.println(Base64Utils.decode(
				"AAMkADU2OWY1MGQ3LWEyNWQtNDFmOC04MWFiLTI5YTE2NGM5YTZmNABGAAAAAABPKgpqnlfYQ7BVC/BfH2XIBwCS0xhUjzMYSLVky9bw7LddAAAAjov5AACS0xhUjzMYSLVky9bw7LddAAADzoyxAAA="));

		String b =
				"UsWdAIe4opTqcrX6~SrIMhBu5Gc9oZKEnnSDFRx9JwBINK8XTgnXUs2A3b7QmxDM9nRu8~mGsikVEoISLg.JTIHYRwv-Bp5ljIADLwUHv9iJAWo1delBOlW0Hd7nIVF0";

		System.out.println(DigestUtils.digestBase64Url(b, DigestUtils.Algorithm.SHA256));
	}

}
