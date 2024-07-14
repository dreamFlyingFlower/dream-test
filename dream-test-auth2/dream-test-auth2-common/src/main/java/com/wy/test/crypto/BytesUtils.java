package com.wy.test.crypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public final class BytesUtils {

	public static String bytes2String(byte[] bytesArray) {
		String result = "";
		for (Byte bts : bytesArray) {
			result += (char) bts.intValue();
		}
		return result;
	}

	public static byte[] toByteArray(InputStream input) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 4];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}
}
