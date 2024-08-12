package com.wy.test.web.core.contorller;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.authentication.core.authn.jwt.AuthTokenService;
import com.wy.test.authentication.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.authentication.sms.password.sms.SmsOtpAuthnService;
import com.wy.test.core.constant.ConstStatus;
import com.wy.test.core.entity.Message;
import com.wy.test.core.entity.UserEntity;
import com.wy.test.core.password.PasswordReciprocal;
import com.wy.test.core.web.WebContext;
import com.wy.test.persistence.service.UserService;

import dream.flying.flower.lang.StrHelper;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping(value = { "/signup" })
@Slf4j
public class RegisterController {

	Pattern mobileRegex = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");

	@Autowired
	AuthTokenService authTokenService;

	@Autowired
	private UserService userInfoService;

	@Autowired
	SmsOtpAuthnService smsOtpAuthnService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@ResponseBody
	@GetMapping(value = { "/produceOtp" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> produceOtp(@RequestParam String mobile) {
		log.debug("/signup/produceOtp Mobile {}: ", mobile);

		log.debug("Mobile Regex matches {}", mobileRegex.matcher(mobile).matches());
		if (StrHelper.isNotBlank(mobile) && mobileRegex.matcher(mobile).matches()) {
			UserEntity userInfo = new UserEntity();
			userInfo.setUsername(mobile);
			userInfo.setMobile(mobile);
			AbstractOtpAuthn smsOtpAuthn = smsOtpAuthnService.getByInstId(WebContext.getInst().getId());
			smsOtpAuthn.produce(userInfo);
			return new Message<UserEntity>(userInfo).buildResponse();
		}

		return new Message<UserEntity>(Message.FAIL).buildResponse();
	}

	// 直接注册
	@PostMapping(value = { "/register" })
	@ResponseBody
	public ResponseEntity<?> register(@ModelAttribute UserEntity userInfo, @RequestParam String captcha)
			throws ServletException, IOException {
		UserEntity validateUserInfo = new UserEntity();
		validateUserInfo.setUsername(userInfo.getMobile());
		validateUserInfo.setMobile(userInfo.getMobile());
		AbstractOtpAuthn smsOtpAuthn = smsOtpAuthnService.getByInstId(WebContext.getInst().getId());
		if (smsOtpAuthn != null && smsOtpAuthn.validate(validateUserInfo, captcha)) {
			UserEntity temp = userInfoService.findByEmailMobile(userInfo.getEmail());

			if (temp != null) {
				return new Message<UserEntity>(Message.FAIL).buildResponse();
			}

			temp = userInfoService.findByUsername(userInfo.getUsername());
			if (temp != null) {
				return new Message<UserEntity>(Message.FAIL).buildResponse();
			}

			// default InstId
			if (StrHelper.isEmpty(userInfo.getInstId())) {
				userInfo.setInstId("1");
			}
			String password = userInfo.getPassword();
			userInfo.setDecipherable(PasswordReciprocal.getInstance().encode(password));
			password = passwordEncoder.encode(password);
			userInfo.setPassword(password);
			userInfo.setStatus(ConstStatus.INACTIVE);

			if (userInfoService.insert(userInfo)) {
				return new Message<UserEntity>().buildResponse();
			}
		}
		return new Message<UserEntity>(Message.FAIL).buildResponse();
	}

}
