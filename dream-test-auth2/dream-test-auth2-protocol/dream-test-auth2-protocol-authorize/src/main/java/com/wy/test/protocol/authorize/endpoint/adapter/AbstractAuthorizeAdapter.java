package com.wy.test.protocol.authorize.endpoint.adapter;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;

import com.wy.test.authentication.core.entity.SignPrincipal;
import com.wy.test.core.entity.AccountEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.vo.AppVO;
import com.wy.test.core.vo.UserVO;
import com.wy.test.core.web.AuthWebContext;

import dream.flying.flower.binary.Base64Helper;
import dream.flying.flower.framework.core.enums.BooleanEnum;
import dream.flying.flower.framework.crypto.cert.CertSigner;
import dream.flying.flower.framework.crypto.helper.ReciprocalHelpers;
import dream.flying.flower.framework.crypto.keystore.KeyStoreLoader;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public abstract class AbstractAuthorizeAdapter {

	protected AppVO app;

	protected UserVO userInfo;

	protected AccountEntity account;

	protected SignPrincipal principal;

	public abstract Object generateInfo();

	public ModelAndView authorize(ModelAndView modelAndView) {
		return modelAndView;
	}

	public Object sign(Object data, String signatureKey, String signature) {
		if (BooleanEnum.isTrue(app.getIsSignature())) {
			KeyStoreLoader keyStoreLoader = AuthWebContext.getBean("keyStoreLoader", KeyStoreLoader.class);
			try {
				byte[] signData = CertSigner.sign(data.toString().getBytes(), keyStoreLoader.getKeyStore(),
						keyStoreLoader.getEntityName(), keyStoreLoader.getKeystorePassword());
				log.debug("signed Token : " + data);
				log.debug("signature : " + signData.toString());

				return Base64Helper.encodeUrl(data.toString().getBytes("UTF-8")) + "."
						+ Base64Helper.encodeUrl(signData);
			} catch (UnsupportedEncodingException e) {
				log.error("UnsupportedEncodingException ", e);
			} catch (Exception e) {
				log.error("Exception ", e);
			}
			log.debug("Token {}", data);

		} else {
			log.debug("data not need sign .");
			return data;
		}

		return null;
	}

	public Object encrypt(Object data, String algorithmKey, String algorithm) {

		algorithmKey = PasswordReciprocal.getInstance().decoder(algorithmKey);
		log.debug("algorithm : " + algorithm);
		log.debug("algorithmKey : " + algorithmKey);
		// Chinese , encode data to HEX
		try {
			data = new String(Hex.encodeHex(data.toString().getBytes("UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] encodeData = ReciprocalHelpers.encode(data.toString(), algorithmKey, algorithm);
		String tokenString = Base64Helper.encodeUrlString(encodeData);
		log.trace("Reciprocal then HEX  Token : " + tokenString);

		return tokenString;
	}

	public static String getValueByUserAttr(UserVO userInfo, String userAttr) {
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
}