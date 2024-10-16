package com.wy.test.protocol.oauth2.common;

import com.wy.test.protocol.oauth2.endpoint.ApprovalConfirmEndpoint;

public class OAuth2Constants {

	public static final class PARAMETER {

		public static final String CLIENT_SECRET = "client_secret";

		public static final String CODE = "code";

		public static final String TOKEN = "token";

		public static final String TOKEN_TYPE = "token_type";

		public static final String EXPIRES_IN = "expires_in";

		public static final String GRANT_TYPE_CODE = "code";

		public static final String GRANT_TYPE_PASSWORD = "password";

		public static final String GRANT_TYPE_IMPLICIT = "implicit";

		public static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";

		public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";

		public static final String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";

		public static final String ACCESS_TOKEN = "access_token";

		public static final String APPROVAL_PROMPT = "approval_prompt";

		// https://datatracker.ietf.org/doc/html/rfc7636 PKCE
		// Proof Key for Code Exchange by OAuth Public Clients
		public static final String CODE_CHALLENGE = "code_challenge";

		public static final String CODE_CHALLENGE_METHOD = "code_challenge_method";

		public static final String CODE_VERIFIER = "code_verifier";

	}

	public static class PKCE_TYPE {

		public static final String PKCE_TYPE_YES = "YES";

		public static final String PKCE_TYPE_NO = "NO";
	}

	public static class CODE_CHALLENGE_METHOD_TYPE {

		public static final String PLAIN = "plain";

		public static final String S256 = "S256";
	}

	public static class ENDPOINT {

		public final static String ENDPOINT_BASE = "/authz/oauth/v20";

		/**
		 * OAuth2自定义认证接口
		 */
		public final static String ENDPOINT_AUTHORIZE = ENDPOINT_BASE + "/authorize";

		/**
		 * OAuth2原生认证接口
		 */
		public final static String ENDPOINT_TENCENT_IOA_AUTHORIZE = "/oauth2/authorize";

		/**
		 * OAuth2自定义获取Token接口
		 */
		public final static String ENDPOINT_TOKEN = ENDPOINT_BASE + "/token";

		/**
		 * OAuth2原生获取Token接口
		 */
		public final static String ENDPOINT_TENCENT_IOA_TOKEN = "/oauth2/token";

		public final static String ENDPOINT_CHECK_TOKEN = ENDPOINT_BASE + "/check_token";

		public final static String ENDPOINT_TOKEN_KEY = ENDPOINT_BASE + "/token_key";

		/**
		 * OAuth2获取权限确认API,跳转{@link ApprovalConfirmEndpoint#approvalConfirm}
		 */
		public final static String ENDPOINT_APPROVAL_CONFIRM = ENDPOINT_BASE + "/approval_confirm";

		/**
		 * OAuth2授权错误API,跳转{@link ApprovalConfirmEndpoint#handleError}
		 */
		public final static String ENDPOINT_ERROR = ENDPOINT_BASE + "/error";

		public final static String ENDPOINT_USERINFO = "/api/oauth/v20/me";

		public final static String ENDPOINT_OPENID_CONNECT_USERINFO = "/api/connect/v10/userinfo";
	}
}