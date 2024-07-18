package com.wy.test.core.constants;

public class ConstsProperties {

	public static String classPathResource(String propertySource) {
		return propertySource.replaceAll("classpath:", "");
	}

	public static String classPathResource(String propertySource, String active) {
		if (active == null || active.equals("")) {
			return propertySource.replaceAll("classpath:", "");
		}
		return propertySource.replace(".", "-" + active + ".").replaceAll("classpath:", "");
	}

}
