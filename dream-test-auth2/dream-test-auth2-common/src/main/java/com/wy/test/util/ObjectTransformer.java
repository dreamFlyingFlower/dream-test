package com.wy.test.util;

import java.io.Serializable;

import com.wy.test.crypto.HexUtils;

/**
 * ObjectTransformer<br>
 * serialize & deserialize<br>
 * object serialize to ByteArray,and ByteArray deserialize to object<br>
 * object serialize to HEX String,and HEX String deserialize to object<br>
 * 
 * @version 2.0
 * @since 1.6
 */
public class ObjectTransformer {

	/**
	 * serialize Serializable Object 2 HEX String
	 * 
	 * @param Serializable Object
	 * @return String
	 */
	public static final String serialize(Serializable s) {
		return HexUtils.hex2String(SerializationUtils.serialize(s));
	}

	/**
	 * deserialize 2 Object
	 * 
	 * @param HEX String
	 * @return Object
	 */
	public static final <T> T deserialize(String hex) {
		return SerializationUtils.deserialize(HexUtils.hex2Bytes(hex));
	}

}
