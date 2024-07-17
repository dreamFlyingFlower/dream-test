package com.wy.test.util;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import dream.flying.flower.collection.MapHelper;

public final class StringUtils extends org.apache.commons.lang3.StringUtils {

	/*
	 * 获取指定UTF-8模式字节长度的字符串
	 */
	public static String limitLength(String strValue, int bytelen) {

		// 中文汉字占用三个字节
		int strlen = bytelen / 3;
		int real_bytelen = strlen * 3;

		// 如果为NULL或�?空，则直接返�?
		if (null == strValue || "".equalsIgnoreCase(strValue)) {
			return "";
		}

		try {
			byte[] utf8_bytes = strValue.getBytes("UTF-8");
			if (utf8_bytes.length <= bytelen)
				return strValue;

			byte[] cutoff_bytes = new byte[real_bytelen];
			System.arraycopy(utf8_bytes, 0, cutoff_bytes, 0, real_bytelen);

			String strResult = new String(cutoff_bytes, "UTF-8");

			return strResult;

		} catch (Exception e) {
			if (strValue.length() < strlen)
				return strValue;
			return strValue.substring(0, strlen);
		}
	}

	/**
	 * 密码不包含全部或部分的用户账户名 �?��str2中是否包含str中全部或部分的数�?
	 * 
	 * @param str
	 * @param str2
	 * @return
	 */
	public static Boolean containsPartOrAll(String string, String string2) {
		if (isNotEmpty(string) && isNotEmpty(string2)) {
			return Pattern.compile("[" + string + "]").matcher(string2).find();
		}
		return false;
	}

	/**
	 * 汉字转换位汉语拼音
	 * 
	 * @param hanYu Chinese
	 * @param first true is Convert first,else all
	 * @return 拼音
	 */
	public static Map<String, String> aduserName2Map(String activeDirectoryUserName) {
		if (isEmpty(activeDirectoryUserName)) {
			return null;
		}
		Map<String, String> map = new HashMap<String, String>();
		int index = 0;
		if ((index = activeDirectoryUserName.indexOf("\\")) > 0) {
			map.put("domain", activeDirectoryUserName.substring(0, index));
			map.put("userName", activeDirectoryUserName.substring(index + 1, activeDirectoryUserName.length()));
		} else if ((index = activeDirectoryUserName.indexOf("@")) > 0) {
			map.put("userName", activeDirectoryUserName.substring(0, index));
			map.put("domain", activeDirectoryUserName.substring(index + 1));
		} else {
			map.put("userName", activeDirectoryUserName);
		}
		return map;
	}

	/**
	 * 处理AD域中的用户名，将域名去掉
	 * 
	 * @param str
	 * @return
	 */
	public static String takeoffDomain(String activeDirectoryUserName) {
		Map<String, String> map = aduserName2Map(activeDirectoryUserName);
		if (MapHelper.isNotEmpty(map)) {
			return map.get("userName");
		}
		return null;
	}

	public static String getActiveDirectoryDomin(String activeDirectoryUserName) {
		Map<String, String> map = aduserName2Map(activeDirectoryUserName);
		if (MapHelper.isNotEmpty(map)) {
			return map.get("domain");
		}
		return null;
	}
}