package com.wy.test.crypto.keystore;

import java.security.KeyStore;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

public class KeyStoreLoader implements InitializingBean {

	private static final Logger _logger = LoggerFactory.getLogger(KeyStoreLoader.class);

	private KeyStore keyStore;

	private String entityName;

	private Resource keystoreFile;

	private String keystorePassword;

	private String keystoreType = "JKS";

	/**
	 * 
	 */
	public KeyStoreLoader() {
	}

	/**
	 * @return the keyStore
	 */
	public KeyStore getKeyStore() {
		return keyStore;
	}

	/**
	 * @param keystoreFile the keystoreFile to set
	 */
	public void setKeystoreFile(Resource keystoreFile) {
		this.keystoreFile = keystoreFile;
	}

	/**
	 * @param keystorePassword the keystorePassword to set
	 */
	public void setKeystorePassword(String keystorePassword) {
		this.keystorePassword = keystorePassword;
	}

	/**
	 * ��ȡKeyStore����
	 * 
	 * @return
	 */
	public String getKeystorePassword() {
		return keystorePassword;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		_logger.debug("Load KeyStore from file " + keystoreFile.getURL());
		keyStore = KeyStoreUtil.loadKeyStore(keystoreFile, keystorePassword.toCharArray(), KeyStoreType.JKS);
		_logger.debug("Load KeyStore success . ");

		Enumeration<String> temp = keyStore.aliases();
		int i = 0;
		while (temp.hasMoreElements()) {
			_logger.debug("KeyStore alias name " + (i++) + " : " + temp.nextElement());
		}
	}

	/**
	 * .
	 * 
	 * @return the entityName
	 */
	public String getEntityName() {
		return entityName;
	}

	/**
	 * @param entityName the entityName to set
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	/**
	 * @return the keystoreType
	 */
	public String getKeystoreType() {
		return keystoreType;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("KeyStoreLoader [keyStore=");
		builder.append(keyStore);
		builder.append(", entityName=");
		builder.append(entityName);
		builder.append(", keystoreFile=");
		builder.append(keystoreFile);
		builder.append(", keystorePassword=");
		builder.append(keystorePassword);
		builder.append(", keystoreType=");
		builder.append(keystoreType);
		builder.append("]");
		return builder.toString();
	}

}