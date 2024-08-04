
package com.wy.test.web.web.contorller;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.PasswordPolicyEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.web.WebContext;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.otp.password.onetimepwd.MailOtpAuthnService;
import com.wy.test.persistence.service.PasswordPolicyService;
import com.wy.test.persistence.service.UserInfoService;
import com.wy.test.sms.password.sms.SmsOtpAuthnService;

@RestController
@RequestMapping(value = { "/forgotpassword" })
public class ForgotPasswordContorller {

	private static Logger _logger = LoggerFactory.getLogger(ForgotPasswordContorller.class);

	Pattern emailRegex =
			Pattern.compile("^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$");

	Pattern mobileRegex = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");

	public class ForgotType {

		public final static int NOTFOUND = 1;

		public final static int EMAIL = 2;

		public final static int MOBILE = 3;

		public final static int CAPTCHAERROR = 4;
	}

	public class PasswordResetResult {

		public final static int SUCCESS = 1;

		public final static int CAPTCHAERROR = 2;

		public final static int PASSWORDERROR = 3;
	}

	@Autowired
	AuthTokenService authTokenService;

	@Autowired
	UserInfoService userInfoService;

	@Autowired
	MailOtpAuthnService mailOtpAuthnService;

	@Autowired
	SmsOtpAuthnService smsOtpAuthnService;

	@Autowired
	private PasswordPolicyService passwordPolicyService;

	@GetMapping(value = { "/passwordpolicy" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> passwordpolicy() {
		PasswordPolicyEntity passwordPolicy = passwordPolicyService.get(WebContext.getInst().getId());
		// 构建密码强度说明
		passwordPolicy.buildMessage();
		return new Message<PasswordPolicyEntity>(passwordPolicy).buildResponse();
	}

	@GetMapping(value = { "/validateCaptcha" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> validateCaptcha(@RequestParam String userId, @RequestParam String state,
			@RequestParam String captcha, @RequestParam String otpCaptcha) {
		_logger.debug("forgotpassword  /forgotpassword/validateCaptcha.");
		_logger.debug(" userId {}: ", userId);
		UserEntity userInfo = userInfoService.get(userId);
		if (userInfo != null) {
			AbstractOtpAuthn smsOtpAuthn = smsOtpAuthnService.getByInstId(userInfo.getInstId());
			if (otpCaptcha == null || !smsOtpAuthn.validate(userInfo, otpCaptcha)) {
				return new Message<ChangePassword>(Message.FAIL).buildResponse();
			}
			return new Message<ChangePassword>(Message.SUCCESS).buildResponse();
		}
		return new Message<ChangePassword>(Message.FAIL).buildResponse();
	}

	@GetMapping(value = { "/produceOtp" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> produceOtp(@RequestParam String mobile, @RequestParam String state,
			@RequestParam String captcha) {
		_logger.debug("forgotpassword  /forgotpassword/produceOtp.");
		_logger.debug(" Mobile {}: ", mobile);
		if (!authTokenService.validateCaptcha(state, captcha)) {
			_logger.debug("login captcha valid error.");
			return new Message<ChangePassword>(Message.FAIL).buildResponse();
		}

		ChangePassword change = null;
		_logger.debug("Mobile Regex matches {}", mobileRegex.matcher(mobile).matches());
		if (StringUtils.isNotBlank(mobile) && mobileRegex.matcher(mobile).matches()) {
			UserEntity userInfo = userInfoService.findByEmailMobile(mobile);
			if (userInfo != null) {
				change = new ChangePassword(userInfo);
				change.clearPassword();
				AbstractOtpAuthn smsOtpAuthn = smsOtpAuthnService.getByInstId(userInfo.getInstId());
				smsOtpAuthn.produce(userInfo);
				return new Message<ChangePassword>(change).buildResponse();
			}
		}

		return new Message<ChangePassword>(Message.FAIL).buildResponse();
	}

	@GetMapping(value = { "/produceEmailOtp" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> produceEmailOtp(@RequestParam String email, @RequestParam String state,
			@RequestParam String captcha) {
		_logger.debug("/forgotpassword/produceEmailOtp Email {} : ", email);
		if (!authTokenService.validateCaptcha(state, captcha)) {
			_logger.debug("captcha valid error.");
			return new Message<ChangePassword>(Message.FAIL).buildResponse();
		}

		ChangePassword change = null;
		if (StringUtils.isNotBlank(email) && emailRegex.matcher(email).matches()) {
			UserEntity userInfo = userInfoService.findByEmailMobile(email);
			if (userInfo != null) {
				change = new ChangePassword(userInfo);
				change.clearPassword();
				AbstractOtpAuthn mailOtpAuthn = mailOtpAuthnService.getMailOtpAuthn(userInfo.getInstId());
				mailOtpAuthn.produce(userInfo);
				return new Message<ChangePassword>(change).buildResponse();
			}
		}
		return new Message<ChangePassword>(Message.FAIL).buildResponse();
	}

	@PostMapping(value = { "/setpassword" })
	public ResponseEntity<?> setPassWord(@ModelAttribute ChangePassword changePassword, @RequestParam String forgotType,
			@RequestParam String otpCaptcha, @RequestParam String state) {
		_logger.debug("forgotPassword  /forgotpassword/setpassword.");
		if (StringUtils.isNotBlank(changePassword.getPassword())
				&& changePassword.getPassword().equals(changePassword.getConfirmPassword())) {
			UserEntity loadedUserInfo = userInfoService.get(changePassword.getUserId());
			if (loadedUserInfo != null) {
				AbstractOtpAuthn smsOtpAuthn = smsOtpAuthnService.getByInstId(loadedUserInfo.getInstId());
				AbstractOtpAuthn mailOtpAuthn = mailOtpAuthnService.getMailOtpAuthn(loadedUserInfo.getInstId());
				if ((forgotType.equalsIgnoreCase("email") && mailOtpAuthn != null
						&& mailOtpAuthn.validate(loadedUserInfo, otpCaptcha))
						|| (forgotType.equalsIgnoreCase("mobile") && smsOtpAuthn != null
								&& smsOtpAuthn.validate(loadedUserInfo, otpCaptcha))) {

					if (userInfoService.changePassword(changePassword, true)) {
						return new Message<ChangePassword>(Message.SUCCESS).buildResponse();
					} else {
						return new Message<ChangePassword>(Message.FAIL).buildResponse();
					}
				} else {
					return new Message<ChangePassword>(Message.FAIL).buildResponse();
				}
			}
		}
		return new Message<ChangePassword>(Message.FAIL).buildResponse();
	}
}