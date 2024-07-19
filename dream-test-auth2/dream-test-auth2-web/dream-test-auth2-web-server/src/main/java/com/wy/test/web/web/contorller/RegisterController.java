package com.wy.test.web.web.contorller;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.wy.test.core.authn.jwt.AuthTokenService;
import com.wy.test.core.configuration.ApplicationConfig;
import com.wy.test.core.constants.ConstsStatus;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.core.web.WebContext;
import com.wy.test.crypto.password.PasswordReciprocal;
import com.wy.test.entity.Message;
import com.wy.test.otp.password.onetimepwd.AbstractOtpAuthn;
import com.wy.test.persistence.service.UserInfoService;
import com.wy.test.sms.password.sms.SmsOtpAuthnService;

import dream.flying.flower.lang.StrHelper;

@Controller
@RequestMapping(value = { "/signup" })
public class RegisterController {

	private static Logger _logger = LoggerFactory.getLogger(RegisterController.class);

	Pattern mobileRegex = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");

	@Autowired
	AuthTokenService authTokenService;

	@Autowired
	protected ApplicationConfig applicationConfig;

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	SmsOtpAuthnService smsOtpAuthnService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@ResponseBody
	@GetMapping(value = { "/produceOtp" }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> produceOtp(@RequestParam String mobile) {
		_logger.debug("/signup/produceOtp Mobile {}: ", mobile);

		_logger.debug("Mobile Regex matches {}", mobileRegex.matcher(mobile).matches());
		if (StrHelper.isNotBlank(mobile) && mobileRegex.matcher(mobile).matches()) {
			UserInfo userInfo = new UserInfo();
			userInfo.setUsername(mobile);
			userInfo.setMobile(mobile);
			AbstractOtpAuthn smsOtpAuthn = smsOtpAuthnService.getByInstId(WebContext.getInst().getId());
			smsOtpAuthn.produce(userInfo);
			return new Message<UserInfo>(userInfo).buildResponse();
		}

		return new Message<UserInfo>(Message.FAIL).buildResponse();
	}

	// 直接注册
	@PostMapping(value = { "/register" })
	@ResponseBody
	public ResponseEntity<?> register(@ModelAttribute UserInfo userInfo, @RequestParam String captcha)
			throws ServletException, IOException {
		UserInfo validateUserInfo = new UserInfo();
		validateUserInfo.setUsername(userInfo.getMobile());
		validateUserInfo.setMobile(userInfo.getMobile());
		AbstractOtpAuthn smsOtpAuthn = smsOtpAuthnService.getByInstId(WebContext.getInst().getId());
		if (smsOtpAuthn != null && smsOtpAuthn.validate(validateUserInfo, captcha)) {
			UserInfo temp = userInfoService.findByEmailMobile(userInfo.getEmail());

			if (temp != null) {
				return new Message<UserInfo>(Message.FAIL).buildResponse();
			}

			temp = userInfoService.findByUsername(userInfo.getUsername());
			if (temp != null) {
				return new Message<UserInfo>(Message.FAIL).buildResponse();
			}

			// default InstId
			if (StrHelper.isEmpty(userInfo.getInstId())) {
				userInfo.setInstId("1");
			}
			String password = userInfo.getPassword();
			userInfo.setDecipherable(PasswordReciprocal.getInstance().encode(password));
			password = passwordEncoder.encode(password);
			userInfo.setPassword(password);
			userInfo.setStatus(ConstsStatus.INACTIVE);

			if (userInfoService.insert(userInfo)) {
				return new Message<UserInfo>().buildResponse();
			}
		}
		return new Message<UserInfo>(Message.FAIL).buildResponse();
	}

}
