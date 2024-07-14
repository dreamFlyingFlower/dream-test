package com.wy.test.util;

import javax.servlet.http.HttpServletRequest;

import com.wy.test.crypto.Base64Utils;

public class AuthorizationHeaderUtils {

	/**
	 * first UpperCase
	 */
	public static final String HEADER_Authorization = "Authorization";

	/**
	 * first LowerCase
	 */
	public static final String HEADER_authorization = "authorization";

	public static String createBasic(String username, String password) {
		String authUserPass = username + ":" + password;
		String encodedAuthUserPass = Base64Utils.encode(authUserPass);
		return AuthorizationHeader.Credential.BASIC + encodedAuthUserPass;
	}

	public static String createBearer(String bearer) {
		return AuthorizationHeader.Credential.BEARER + bearer;
	}

	public static AuthorizationHeader resolve(HttpServletRequest request) {
		String authorization = resolveBearer(request);
		return resolve(authorization);
	}

	public static AuthorizationHeader resolve(String authorization) {
		if (StringUtils.isNotBlank(authorization) && isBasic(authorization)) {
			String decodeUserPass = Base64Utils.decode(authorization.split(" ")[1]);
			String[] userPass = decodeUserPass.split(":");
			return new AuthorizationHeader(userPass[0], userPass[1]);
		} else {
			return new AuthorizationHeader(resolveBearer(authorization));
		}
	}

	public static String resolveBearer(HttpServletRequest request) {
		String authorization = StringUtils.isNotBlank(request.getHeader(HEADER_Authorization))
				? request.getHeader(HEADER_Authorization)
				: request.getHeader(HEADER_authorization);
		if (StringUtils.isNotBlank(authorization)) {
			return resolveBearer(authorization);
		}
		return null;
	}

	public static boolean isBasic(String basic) {
		if (basic.startsWith(AuthorizationHeader.Credential.BASIC)) {
			return true;
		} else {
			return false;
		}
	}

	static String resolveBearer(String bearer) {
		if (StringUtils.isNotBlank(bearer) && isBearer(bearer)) {
			return bearer.split(" ")[1];
		} else {
			return bearer;
		}
	}

	static boolean isBearer(String bearer) {
		if (bearer.toLowerCase().startsWith(AuthorizationHeader.Credential.BEARER.toLowerCase())) {
			return true;
		} else {
			return false;
		}
	}
}