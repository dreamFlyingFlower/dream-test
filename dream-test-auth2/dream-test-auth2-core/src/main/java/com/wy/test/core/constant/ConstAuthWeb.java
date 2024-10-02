package com.wy.test.core.constant;

/**
 * Web常量
 *
 * @author 飞花梦影
 * @date 2024-08-25 15:29:11
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public interface ConstAuthWeb {

	String CURRENT_USER_PASSWORD_SET_TYPE = "current_user_password_set_type";

	String CURRENT_MESSAGE = "current_message";

	String CURRENT_INST = "current_inst";

	String INST_COOKIE_NAME = "auth_inst";

	String FIRST_SAVED_REQUEST_PARAMETER = "SPRING_SECURITY_SAVED_REQUEST";

	String KAPTCHA_SESSION_KEY = "kaptcha_session_key";

	String SINGLE_SIGN_ON_APP_ID = "single_sign_on_app_id";

	/**
	 * CAS登录时存储在Session中的APP信息Key,实际存储AppVO或其子类
	 */
	String AUTHORIZE_SIGN_ON_APP = "authorize_sign_on_app";

	String AUTHORIZE_SIGN_ON_APP_SAMLV20_ADAPTER = "authorize_sign_on_app_samlv20_adapter";

	String KERBEROS_TOKEN_PARAMETER = "kerberosToken";

	String CAS_SERVICE_PARAMETER = "service";

	String KERBEROS_USERDOMAIN_PARAMETER = "kerberosUserDomain";

	String REMEBER_ME_COOKIE = "sign_remeber_me";

	String JWT_TOKEN_PARAMETER = "jwt";

	String CURRENT_SINGLESIGNON_URI = "current_singlesignon_uri";

	String AUTHENTICATION = "current_authentication";

	String SESSION = "current_session";

	String THEME_COOKIE_NAME = "auth_theme_value";

	String LOGIN_ERROR_SESSION_MESSAGE = "login_error_session_message_key";

	String ONLINE_TICKET_PREFIX = "OT";

	String ONLINE_TICKET_NAME = "online_ticket";

	String DREAM_METADATA_PREFIX = "auth_metadata_";

	public interface LOGIN_RESULT {

		String SUCCESS = "success";

		String FAIL = "fail";

		String PASSWORD_ERROE = "password error";

		String USER_NOT_EXIST = "user not exist";

		String USER_LOCKED = "locked";

		String USER_INACTIVE = "inactive";
	}
}