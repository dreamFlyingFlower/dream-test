package com.wy.test.core.web;

/**
 * Web常量
 *
 * @author 飞花梦影
 * @date 2024-08-25 15:29:11
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class WebConstants {

	public final static String CURRENT_USER_PASSWORD_SET_TYPE = "current_user_password_set_type";

	public final static String CURRENT_MESSAGE = "current_message";

	public final static String CURRENT_INST = "current_inst";

	public final static String INST_COOKIE_NAME = "auth_inst";

	public final static String FIRST_SAVED_REQUEST_PARAMETER = "SPRING_SECURITY_SAVED_REQUEST";

	public final static String KAPTCHA_SESSION_KEY = "kaptcha_session_key";

	public final static String SINGLE_SIGN_ON_APP_ID = "single_sign_on_app_id";

	public final static String AUTHORIZE_SIGN_ON_APP = "authorize_sign_on_app";

	public final static String AUTHORIZE_SIGN_ON_APP_SAMLV20_ADAPTER = "authorize_sign_on_app_samlv20_adapter";

	public final static String KERBEROS_TOKEN_PARAMETER = "kerberosToken";

	public final static String CAS_SERVICE_PARAMETER = "service";

	public final static String KERBEROS_USERDOMAIN_PARAMETER = "kerberosUserDomain";

	public final static String REMEBER_ME_COOKIE = "sign_remeber_me";

	public final static String JWT_TOKEN_PARAMETER = "jwt";

	public final static String CURRENT_SINGLESIGNON_URI = "current_singlesignon_uri";

	public final static String AUTHENTICATION = "current_authentication";

	public final static String SESSION = "current_session";

	public final static String THEME_COOKIE_NAME = "auth_theme_value";

	public final static String LOGIN_ERROR_SESSION_MESSAGE = "login_error_session_message_key";

	public final static String ONLINE_TICKET_PREFIX = "OT";

	public final static String ONLINE_TICKET_NAME = "online_ticket";

	public final static String DREAM_METADATA_PREFIX = "auth_metadata_";

	public final static class LOGIN_RESULT {

		public final static String SUCCESS = "success";

		public final static String FAIL = "fail";

		public final static String PASSWORD_ERROE = "password error";

		public final static String USER_NOT_EXIST = "user not exist";

		public final static String USER_LOCKED = "locked";

		public final static String USER_INACTIVE = "inactive";
	}
}