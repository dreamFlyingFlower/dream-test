
package com.wy.test.web.core.contorller;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.authentication.core.authn.jwt.AuthTokenService;
import com.wy.test.authentication.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.authentication.otp.password.onetimepwd.MailOtpAuthnService;
import com.wy.test.authentication.sms.password.sms.SmsOtpAuthnService;
import com.wy.test.core.convert.PasswordPolicyConvert;
import com.wy.test.core.convert.UserConvert;
import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.PasswordPolicyEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.PasswordPolicyVO;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.PasswordPolicyService;
import com.wy.test.persistence.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = { "/forgotpassword" })
@Slf4j
public class ForgotPasswordContorller {

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
	UserService userService;

	@Autowired
	UserConvert userConvert;

	@Autowired
	MailOtpAuthnService mailOtpAuthnService;

	@Autowired
	SmsOtpAuthnService smsOtpAuthnService;

	@Autowired
	private PasswordPolicyService passwordPolicyService;

	@GetMapping(value = { "/passwordpolicy" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> passwordpolicy() {
		PasswordPolicyEntity passwordPolicy = passwordPolicyService
				.listOne(PasswordPolicyEntity.builder().instId(WebContext.getInst().getId()).build());
		PasswordPolicyVO passwordPolicyVO = PasswordPolicyConvert.INSTANCE.convertt(passwordPolicy);
		// 构建密码强度说明
		passwordPolicyVO.buildMessage();
		return new Message<>(passwordPolicyVO).buildResponse();
	}

	@GetMapping(value = { "/validateCaptcha" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> validateCaptcha(@RequestParam String userId, @RequestParam String state,
			@RequestParam String captcha, @RequestParam String otpCaptcha) {
		log.debug("forgotpassword  /forgotpassword/validateCaptcha.");
		log.debug(" userId {}: ", userId);
		UserEntity userInfo = userService.getById(userId);
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
		log.debug("forgotpassword  /forgotpassword/produceOtp.");
		log.debug(" Mobile {}: ", mobile);
		if (!authTokenService.validateCaptcha(state, captcha)) {
			log.debug("login captcha valid error.");
			return new Message<ChangePassword>(Message.FAIL).buildResponse();
		}

		ChangePassword change = null;
		log.debug("Mobile Regex matches {}", mobileRegex.matcher(mobile).matches());
		if (StringUtils.isNotBlank(mobile) && mobileRegex.matcher(mobile).matches()) {
			UserEntity userInfo = userService.findByEmailMobile(mobile);
			if (userInfo != null) {
				change = new ChangePassword(userConvert.convertt(userInfo));
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
		log.debug("/forgotpassword/produceEmailOtp Email {} : ", email);
		if (!authTokenService.validateCaptcha(state, captcha)) {
			log.debug("captcha valid error.");
			return new Message<ChangePassword>(Message.FAIL).buildResponse();
		}

		ChangePassword change = null;
		if (StringUtils.isNotBlank(email) && emailRegex.matcher(email).matches()) {
			UserEntity userInfo = userService.findByEmailMobile(email);
			if (userInfo != null) {
				change = new ChangePassword(userConvert.convertt(userInfo));
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
		log.debug("forgotPassword  /forgotpassword/setpassword.");
		if (StringUtils.isNotBlank(changePassword.getPassword())
				&& changePassword.getPassword().equals(changePassword.getConfirmPassword())) {
			UserEntity loadedUserInfo = userService.getById(changePassword.getUserId());
			if (loadedUserInfo != null) {
				AbstractOtpAuthn smsOtpAuthn = smsOtpAuthnService.getByInstId(loadedUserInfo.getInstId());
				AbstractOtpAuthn mailOtpAuthn = mailOtpAuthnService.getMailOtpAuthn(loadedUserInfo.getInstId());
				if ((forgotType.equalsIgnoreCase("email") && mailOtpAuthn != null
						&& mailOtpAuthn.validate(loadedUserInfo, otpCaptcha))
						|| (forgotType.equalsIgnoreCase("mobile") && smsOtpAuthn != null
								&& smsOtpAuthn.validate(loadedUserInfo, otpCaptcha))) {

					if (userService.changePassword(changePassword, true)) {
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