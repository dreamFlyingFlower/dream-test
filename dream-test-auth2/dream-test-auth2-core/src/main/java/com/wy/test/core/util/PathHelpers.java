package com.wy.test.core.util;

import java.io.IOException;
import java.net.URLDecoder;

import org.springframework.core.io.ClassPathResource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PathHelpers {

	private static PathHelpers instance = null;

	private String classPath;

	private String appPath;

	public static String WEB_INFO = "/WEB-INF/";

	private static final String PATH_FILE_NAME = "PathUtils.properties";

	/**
	 * getInstance .
	 * 
	 * @return
	 */
	public static synchronized PathHelpers getInstance() {
		if (instance == null) {
			instance = new PathHelpers();
		}
		return instance;
	}

	/**
	 * PathUtils.
	 */
	public PathHelpers() {
		try {
			// 绝对路径
			classPath = URLDecoder.decode(new ClassPathResource(PATH_FILE_NAME).getURL().getFile(), "UTF-8");
			log.trace("PathUtils() PathUtils " + classPath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			String fileProtocol = new ClassPathResource(PATH_FILE_NAME).getURL().getProtocol();
			if (fileProtocol.equalsIgnoreCase("file") && classPath.indexOf("file:") == 0) {
				classPath = classPath.substring(5, classPath.length());
			} else if (fileProtocol.equalsIgnoreCase("jar") && classPath.indexOf("file:") == 0) {
				// file:/Server/webapps/app
				classPath = classPath.substring(5, classPath.length());
			} else if (fileProtocol.equalsIgnoreCase("wsjar") && classPath.indexOf("file:") == 0) {
				classPath = classPath.substring(5, classPath.length());
			} else if (classPath.equalsIgnoreCase("file:")) {
				classPath = classPath.substring(5, classPath.length());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		classPath = classPath.substring(0, classPath.indexOf("/com/wy/test/util/" + PATH_FILE_NAME));
		if (classPath.indexOf(WEB_INFO) == -1) {
			appPath = classPath.substring(0, classPath.lastIndexOf("/"));
		} else {
			appPath = classPath.substring(0, classPath.lastIndexOf(WEB_INFO));
		}

		System.setProperty("APP_PATH", appPath);
		System.setProperty("CLASSES_PATH", classPath);
	}

	public String getAppPath() {
		return appPath + "/";
	}

	public String getClassPath() {
		return classPath + "/";
	}

	public String getWebInf() {
		return (classPath.lastIndexOf(WEB_INFO) > -1) ? (appPath + WEB_INFO) : "";
	}
}