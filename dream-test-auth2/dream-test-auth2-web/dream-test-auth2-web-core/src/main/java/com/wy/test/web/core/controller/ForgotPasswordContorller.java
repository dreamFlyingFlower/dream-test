
package com.wy.test.web.core.controller;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wy.test.authentication.core.jwt.AuthTokenService;
import com.wy.test.authentication.otp.onetimepwd.AbstractOtpAuthn;
import com.wy.test.authentication.otp.onetimepwd.MailOtpAuthnService;
import com.wy.test.authentication.sms.password.sms.SmsOtpAuthnService;
import com.wy.test.core.base.ResultResponse;
import com.wy.test.core.convert.PasswordPolicyConvert;
import com.wy.test.core.convert.UserConvert;
import com.wy.test.core.entity.ChangePassword;
import com.wy.test.core.entity.PasswordPolicyEntity;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.vo.PasswordPolicyVO;
import com.wy.test.core.web.AuthWebContext;
import com.wy.test.persistence.service.PasswordPolicyService;
import com.wy.test.persistence.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 将接口调整到{@link ProfileController}中
 *
 * @author 飞花梦影
 * @date 2024-10-10 17:24:27
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Tag(name = "1-5 忘记密码API")
@RestController
@RequestMapping(value = { "/forgotpassword" })
@Slf4j
@Deprecated
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

	@Operation(summary = "获得密码策略", description = "获得密码策略", method = "GET")
	@GetMapping(value = { "/passwordpolicy" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> passwordpolicy() {
		PasswordPolicyEntity passwordPolicy = passwordPolicyService
				.listOne(PasswordPolicyEntity.builder().instId(AuthWebContext.getInst().getId()).build());
		PasswordPolicyVO passwordPolicyVO = PasswordPolicyConvert.INSTANCE.convertt(passwordPolicy);
		// 构建密码强度说明
		passwordPolicyVO.buildMessage();
		return new ResultResponse<>(passwordPolicyVO).buildResponse();
	}

	@Operation(summary = "校验验证码", description = "校验验证码", method = "GET")
	@GetMapping(value = { "/validateCaptcha" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> validateCaptcha(@RequestParam String userId, @RequestParam String state,
			@RequestParam String captcha, @RequestParam String otpCaptcha) {
		log.debug("forgotpassword  /forgotpassword/validateCaptcha.");
		log.debug(" userId {}: ", userId);
		UserEntity userInfo = userService.getById(userId);
		if (userInfo != null) {
			AbstractOtpAuthn smsOtpAuthn = smsOtpAuthnService.getByInstId(userInfo.getInstId());
			if (otpCaptcha == null || !smsOtpAuthn.validate(userInfo, otpCaptcha)) {
				return new ResultResponse<ChangePassword>(ResultResponse.FAIL).buildResponse();
			}
			return new ResultResponse<ChangePassword>(ResultResponse.SUCCESS).buildResponse();
		}
		return new ResultResponse<ChangePassword>(ResultResponse.FAIL).buildResponse();
	}

	@Operation(summary = "发送验证码", description = "发送验证码", method = "GET")
	@GetMapping(value = { "/produceOtp" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> produceOtp(@RequestParam String mobile, @RequestParam String state,
			@RequestParam String captcha) {
		log.debug("forgotpassword  /forgotpassword/produceOtp.");
		log.debug(" Mobile {}: ", mobile);
		if (!authTokenService.validateCaptcha(state, captcha)) {
			log.debug("login captcha valid error.");
			return new ResultResponse<ChangePassword>(ResultResponse.FAIL).buildResponse();
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
				return new ResultResponse<ChangePassword>(change).buildResponse();
			}
		}

		return new ResultResponse<ChangePassword>(ResultResponse.FAIL).buildResponse();
	}

	@Operation(summary = "发送邮件", description = "发送邮件", method = "GET")
	@GetMapping(value = { "/produceEmailOtp" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> produceEmailOtp(@RequestParam String email, @RequestParam String state,
			@RequestParam String captcha) {
		log.debug("/forgotpassword/produceEmailOtp Email {} : ", email);
		if (!authTokenService.validateCaptcha(state, captcha)) {
			log.debug("captcha valid error.");
			return new ResultResponse<ChangePassword>(ResultResponse.FAIL).buildResponse();
		}

		ChangePassword change = null;
		if (StringUtils.isNotBlank(email) && emailRegex.matcher(email).matches()) {
			UserEntity userInfo = userService.findByEmailMobile(email);
			if (userInfo != null) {
				change = new ChangePassword(userConvert.convertt(userInfo));
				change.clearPassword();
				AbstractOtpAuthn mailOtpAuthn = mailOtpAuthnService.getMailOtpAuthn(userInfo.getInstId());
				mailOtpAuthn.produce(userInfo);
				return new ResultResponse<ChangePassword>(change).buildResponse();
			}
		}
		return new ResultResponse<ChangePassword>(ResultResponse.FAIL).buildResponse();
	}

	@Operation(summary = "重置密码", description = "重置密码", method = "POST")
	@PostMapping(value = { "/setpassword" })
	public ResponseEntity<?> setPassWord(@RequestBody ChangePassword changePassword, @RequestParam String forgotType,
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
						return new ResultResponse<ChangePassword>(ResultResponse.SUCCESS).buildResponse();
					} else {
						return new ResultResponse<ChangePassword>(ResultResponse.FAIL).buildResponse();
					}
				} else {
					return new ResultResponse<ChangePassword>(ResultResponse.FAIL).buildResponse();
				}
			}
		}
		return new ResultResponse<ChangePassword>(ResultResponse.FAIL).buildResponse();
	}
}