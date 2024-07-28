package com.wy.test.authorize.endpoint.adapter;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.common.crypto.ReciprocalUtils;
import com.wy.test.common.crypto.cert.CertSigner;
import com.wy.test.common.crypto.password.PasswordReciprocal;
import com.wy.test.core.authn.SignPrincipal;
import com.wy.test.core.constants.ConstsBoolean;
import com.wy.test.core.entity.Accounts;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.entity.apps.Apps;
import com.wy.test.core.web.WebContext;

import dream.flying.flower.binary.Base64Helper;
import dream.flying.flower.framework.core.crypto.keystore.KeyStoreLoader;

public abstract class AbstractAuthorizeAdapter {

	final static Logger _logger = LoggerFactory.getLogger(AbstractAuthorizeAdapter.class);

	protected Apps app;

	protected UserInfo userInfo;

	protected Accounts account;

	protected SignPrincipal principal;

	public abstract Object generateInfo();

	public ModelAndView authorize(ModelAndView modelAndView) {
		return modelAndView;
	}

	public Object sign(Object data, String signatureKey, String signature) {
		if (ConstsBoolean.isTrue(app.getIsSignature())) {
			KeyStoreLoader keyStoreLoader = WebContext.getBean("keyStoreLoader", KeyStoreLoader.class);
			try {
				byte[] signData = CertSigner.sign(data.toString().getBytes(), keyStoreLoader.getKeyStore(),
						keyStoreLoader.getEntityName(), keyStoreLoader.getKeystorePassword());
				_logger.debug("signed Token : " + data);
				_logger.debug("signature : " + signData.toString());

				return Base64Helper.encodeUrl(data.toString().getBytes("UTF-8")) + "."
						+ Base64Helper.encodeUrl(signData);
			} catch (UnsupportedEncodingException e) {
				_logger.error("UnsupportedEncodingException ", e);
			} catch (Exception e) {
				_logger.error("Exception ", e);
			}
			_logger.debug("Token {}", data);

		} else {
			_logger.debug("data not need sign .");
			return data;
		}

		return null;
	}

	public Object encrypt(Object data, String algorithmKey, String algorithm) {

		algorithmKey = PasswordReciprocal.getInstance().decoder(algorithmKey);
		_logger.debug("algorithm : " + algorithm);
		_logger.debug("algorithmKey : " + algorithmKey);
		// Chinese , encode data to HEX
		try {
			data = new String(Hex.encodeHex(data.toString().getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] encodeData = ReciprocalUtils.encode(data.toString(), algorithmKey, algorithm);
		String tokenString = Base64Helper.encodeUrlString(encodeData);
		_logger.trace("Reciprocal then HEX  Token : " + tokenString);

		return tokenString;
	}

	public static String getValueByUserAttr(UserInfo userInfo, String userAttr) {
		String value = "";
		if (StringUtils.isBlank(userAttr)) {
			value = userInfo.getUsername();
		} else if (userAttr.equalsIgnoreCase("username")) {
			value = userInfo.getUsername();
		} else if (userAttr.equalsIgnoreCase("userId")) {
			value = userInfo.getId();
		} else if (userAttr.equalsIgnoreCase("email")) {
			value = userInfo.getEmail();
		} else if (userAttr.equalsIgnoreCase("mobile")) {
			value = userInfo.getMobile();
		} else if (userAttr.equalsIgnoreCase("workEmail")) {
			value = userInfo.getWorkEmail();
		} else if (userAttr.equalsIgnoreCase("windowsAccount")) {
			value = userInfo.getWindowsAccount();
		} else if (userAttr.equalsIgnoreCase("employeeNumber")) {
			value = userInfo.getEmployeeNumber();
		} else {
			value = userInfo.getId();
		}

		if (StringUtils.isBlank(value)) {
			value = userInfo.getUsername();
		}

		return value;
	}

	public String serialize() {
		return "";
	};

	public void setPrincipal(SignPrincipal principal) {
		this.principal = principal;
		this.userInfo = principal.getUserInfo();
	}

	public void setApp(Apps app) {
		this.app = app;
	}

	public void setAccount(Accounts account) {
		this.account = account;
	}

}
