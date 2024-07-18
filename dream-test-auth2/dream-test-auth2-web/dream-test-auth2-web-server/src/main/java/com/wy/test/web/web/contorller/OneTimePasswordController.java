
package com.wy.test.web.web.contorller;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wy.test.core.authn.annotation.CurrentUser;
import com.wy.test.core.entity.UserInfo;
import com.wy.test.crypto.password.PasswordReciprocal;
import com.wy.test.entity.Message;
import com.wy.test.otp.password.onetimepwd.algorithm.OtpKeyUriFormat;
import com.wy.test.otp.password.onetimepwd.algorithm.OtpSecret;
import com.wy.test.otp.password.onetimepwd.impl.TimeBasedOtpAuthn;
import com.wy.test.persistence.service.UserInfoService;
import com.wy.test.util.RQCodeUtils;

import dream.flying.flower.framework.core.crypto.Base32Helpers;
import dream.flying.flower.helper.ImageHelper;

@Controller
@RequestMapping(value = { "/config" })
public class OneTimePasswordController {

	static final Logger _logger = LoggerFactory.getLogger(OneTimePasswordController.class);

	@Autowired
	private UserInfoService userInfoService;

	@Autowired
	OtpKeyUriFormat otpKeyUriFormat;

	@Autowired
	private TimeBasedOtpAuthn timeBasedOtpAuthn;

	@GetMapping(value = { "/timebased" })
	@ResponseBody
	public ResponseEntity<?> timebased(@RequestParam String generate, @CurrentUser UserInfo currentUser) {
		HashMap<String, Object> timebased = new HashMap<String, Object>();

		generate(generate, currentUser);

		String sharedSecret = PasswordReciprocal.getInstance().decoder(currentUser.getSharedSecret());

		otpKeyUriFormat.setSecret(sharedSecret);
		String otpauth = otpKeyUriFormat.format(currentUser.getUsername());
		byte[] byteSharedSecret = Base32Helpers.decode(sharedSecret);
		String hexSharedSecret = Hex.encodeHexString(byteSharedSecret);
		BufferedImage bufferedImage = RQCodeUtils.write2BufferedImage(otpauth, "gif", 300, 300);
		String rqCode = ImageHelper.encodeImage(bufferedImage);

		timebased.put("displayName", currentUser.getDisplayName());
		timebased.put("username", currentUser.getUsername());
		timebased.put("digits", otpKeyUriFormat.getDigits());
		timebased.put("period", otpKeyUriFormat.getPeriod());
		timebased.put("sharedSecret", sharedSecret);
		timebased.put("hexSharedSecret", hexSharedSecret);
		timebased.put("rqCode", rqCode);
		return new Message<HashMap<String, Object>>(timebased).buildResponse();
	}

	public void generate(String generate, @CurrentUser UserInfo currentUser) {
		if ((StringUtils.isNotBlank(generate) && generate.equalsIgnoreCase("YES"))
				|| StringUtils.isBlank(currentUser.getSharedSecret())) {

			byte[] byteSharedSecret = OtpSecret.generate(otpKeyUriFormat.getCrypto());
			String sharedSecret = Base32Helpers.encode(byteSharedSecret);
			sharedSecret = PasswordReciprocal.getInstance().encode(sharedSecret);
			currentUser.setSharedSecret(sharedSecret);
			userInfoService.updateSharedSecret(currentUser);
		}
	}

	@SuppressWarnings("unused")
	@GetMapping("/verify")
	public ResponseEntity<?> verify(@RequestParam("otp") String otp, @CurrentUser UserInfo currentUser) {
		// 从当前用户信息中获取共享密钥
		String sharedSecret = PasswordReciprocal.getInstance().decoder(currentUser.getSharedSecret());
		// 计算当前时间对应的动态密码
		boolean validate = timeBasedOtpAuthn.validate(currentUser, otp);
		if (validate) {
			return new Message<>(0, "One-Time Password verification succeeded").buildResponse();
		} else {
			return new Message<>(2, "One-Time Password verification failed").buildResponse();
		}
	}
}