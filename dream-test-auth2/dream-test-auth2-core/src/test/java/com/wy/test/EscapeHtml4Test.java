package com.wy.test;

import java.sql.SQLException;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.boot.actuate.endpoint.ApiVersion;

public class EscapeHtml4Test {
	public static void main(String[] args) throws SQLException {
		String value="<IMG SRC=javascript:alert('XSS')<javascript>>";
		System.out.println(StringEscapeUtils.escapeHtml4(value));
		System.out.println(StringEscapeUtils.escapeEcmaScript(value));
		System.out.println(ApiVersion.V2.getProducedMimeType().toString());
	}
}
