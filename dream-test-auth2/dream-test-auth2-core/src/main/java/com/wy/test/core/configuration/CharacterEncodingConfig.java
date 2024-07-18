package com.wy.test.core.configuration;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * 字符集转换及转换配置.
 * 
 */
@Configuration
public class CharacterEncodingConfig {

	/**
	 * 源字符集.
	 */
	@Value("${server.servlet.encoding.charset.from:UTF-8}")
	String fromCharSet;

	/**
	 * 目标字符集.
	 */
	@Value("${server.servlet.encoding.charset:UTF-8}")
	String toCharSet;

	/**
	 * 转换标志.
	 */
	@Value("${server.servlet.encoding.enabled:false}")
	boolean encoding = false;

	public CharacterEncodingConfig() {

	}

	public String getFromCharSet() {
		return fromCharSet;
	}

	public void setFromCharSet(String fromCharSet) {
		this.fromCharSet = fromCharSet;
	}

	public String getToCharSet() {
		return toCharSet;
	}

	public void setToCharSet(String toCharSet) {
		this.toCharSet = toCharSet;
	}

	public boolean isEncoding() {
		return encoding;
	}

	public void setEncoding(boolean encoding) {
		this.encoding = encoding;
	}

	/**
	 * 字符集转换.
	 * 
	 * @param encodingString 源字符串
	 * @return encoded目标字符串
	 */
	public String encoding(String encodingString) {
		if (!this.encoding || encodingString == null) {
			return encodingString;
		}

		try {
			return new String(encodingString.getBytes(this.fromCharSet), this.toCharSet);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CharacterEncodingConfig [fromCharSet=");
		builder.append(fromCharSet);
		builder.append(", toCharSet=");
		builder.append(toCharSet);
		builder.append(", encoding=");
		builder.append(encoding);
		builder.append("]");
		return builder.toString();
	}

}
